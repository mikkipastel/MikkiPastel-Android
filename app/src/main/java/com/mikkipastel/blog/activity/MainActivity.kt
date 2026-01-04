package com.mikkipastel.blog.activity

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.mikkipastel.blog.R
import com.mikkipastel.blog.databinding.ActivityMainBinding
import com.mikkipastel.blog.fragment.MainFragment

const val MY_REQUEST_CODE = 101
private const val PREF_KEY_SHORTCUT_ADDED = "shortcut_added_flag"

class MainActivity : AppCompatActivity(), InstallStateUpdatedListener {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val appUpdateManager by lazy {
        AppUpdateManagerFactory.create(this)
    }

    private var mCustomTabsServiceConnection: CustomTabsServiceConnection? = null
    private var mCustomTabsClient: CustomTabsClient? = null
    private var mCustomTabsSession: CustomTabsSession? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        setContentView(R.layout.activity_main)

        // Update status bar background view height dynamically
        ViewCompat.setOnApplyWindowInsetsListener(binding.statusBarBackgroundView) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                height = insets.top
            }
            WindowInsetsCompat.CONSUMED
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.contentContainer, MainFragment.newInstance())
                .commit()
        }

        val prefs = getSharedPreferences("data_install", MODE_PRIVATE)
        val insertBranch = prefs.getBoolean("install_status", false)
        if (insertBranch) {
            addShortcutWithDuplicateCheck(this)
        }

        getInAppUpdateWithPlayStore()
        initChromeCustomTabService()
        setStatusBar()
    }

    private fun setStatusBar() {
        val isLightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = isLightMode
    }

    private fun getInAppUpdateWithPlayStore() {
        appUpdateManager.registerListener(this)

        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.FLEXIBLE,
                        this,
                        MY_REQUEST_CODE
                    )
                } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this,
                        MY_REQUEST_CODE
                    )
                }
            }

            if (appUpdateInfo.installStatus() == InstallStatus.INSTALLED) {
                popupSnackbarForState("An update has just been downloaded.", Snackbar.LENGTH_LONG)
            }
        }
    }

    private fun initChromeCustomTabService() {
        mCustomTabsServiceConnection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(componentName: ComponentName, customTabsClient: CustomTabsClient) {
                mCustomTabsClient = customTabsClient
                mCustomTabsClient?.warmup(0L)
                mCustomTabsSession = mCustomTabsClient?.newSession(null)
            }

            override fun onServiceDisconnected(name: ComponentName) {
                mCustomTabsClient = null
                mCustomTabsSession = null
            }
        }

        val packageName = CustomTabsClient.getPackageName(this, null)
        if (packageName != null && mCustomTabsServiceConnection != null) {
            mCustomTabsServiceConnection?.let {
                CustomTabsClient.bindCustomTabsService(this, packageName, it)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    popupSnackbarForState("App is uploading", Snackbar.LENGTH_INDEFINITE)
                }
                Activity.RESULT_CANCELED -> {
                    popupSnackbarForState("You cancel for update new version.", Snackbar.LENGTH_SHORT)
                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                    popupSnackbarForState("App download failed.", Snackbar.LENGTH_SHORT)
                }
            }
        }
    }

    override fun onStateUpdate(state: InstallState) {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            popupSnackbarForCompleteUpdate()
        } else if (state.installStatus() == InstallStatus.INSTALLED) {
            popupSnackbarForState("An update has just been downloaded.", Snackbar.LENGTH_LONG)
            appUpdateManager.unregisterListener(this@MainActivity)
        }
    }

    private fun popupSnackbarForState(text: String, length: Int) {
        Snackbar.make(
            findViewById(R.id.rootview),
            text,
            length
        ).show()
    }

    private fun popupSnackbarForCompleteUpdate() {
        Snackbar.make(
            findViewById(R.id.rootview),
            "An update has just been downloaded from Play Store.",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction("RESTART") {
                appUpdateManager.completeUpdate()
                appUpdateManager.unregisterListener(this@MainActivity)
            }
            show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        appUpdateManager.unregisterListener(this)
        // Unbind from the service to prevent leaks
        mCustomTabsServiceConnection?.let {
            unbindService(it)
        }
        mCustomTabsServiceConnection = null
        mCustomTabsClient = null
    }

    private fun addShortcutWithDuplicateCheck(context: Context) {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        // Check a flag in SharedPreferences to see if we've added it before.
        if (prefs.getBoolean(PREF_KEY_SHORTCUT_ADDED, false)) {
            return // Shortcut already added, do nothing.
        }

        // ... (rest of the code to add the shortcut, using either ShortcutManager or legacy)

        // After successfully requesting the shortcut, save the flag.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            prefs.edit().putBoolean(PREF_KEY_SHORTCUT_ADDED, true).apply()
        } else {
            // For the legacy method, we broadcast and hope for the best.
            val shortcutIntent = Intent(applicationContext, MainActivity::class.java).apply {
                action = Intent.ACTION_MAIN
            }

            val addIntent = Intent().apply {
                putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent)
                putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name))
                putExtra(
                    Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(applicationContext, R.mipmap.ic_launcher)
                )
            }
            addIntent.action = "com.android.launcher.action.INSTALL_SHORTCUT"
            applicationContext.sendBroadcast(addIntent)
            prefs.edit().putBoolean(PREF_KEY_SHORTCUT_ADDED, true).apply()
        }
    }
}
