package com.mikkipastel.blog

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.mikkipastel.blog.fragment.MainFragment

const val MY_REQUEST_CODE = 101

class MainActivity : AppCompatActivity(), InstallStateUpdatedListener {

    private val appUpdateManager by lazy {
        AppUpdateManagerFactory.create(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.contentContainer, MainFragment.newInstance())
                .commit()
        }

        val prefs = getSharedPreferences("data_install", Context.MODE_PRIVATE)
        val insertBranch = prefs.getBoolean("install_status", false)
        if (insertBranch) {
            addShortcut()
        }

        getInAppUpdateWithPlayStore()
        initChromeCustomTabService()
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
        var mCustomTabsClient: CustomTabsClient?
        var mCustomTabsSession: CustomTabsSession

        object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(componentName: ComponentName, customTabsClient: CustomTabsClient) {
                mCustomTabsClient = customTabsClient
                mCustomTabsClient?.warmup(0L)
                mCustomTabsSession = mCustomTabsClient?.newSession(null)!!
            }

            override fun onServiceDisconnected(name: ComponentName) {
                mCustomTabsClient = null
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
    }

    private fun addShortcut() {
        // Adding shortcut for MainActivity
        // on Home screen

        val shortcutIntent = Intent(applicationContext, MainActivity::class.java)

        shortcutIntent.action = Intent.ACTION_MAIN

        val addIntent = Intent()
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent)
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "MikkiPastel")
        addIntent.putExtra(
            Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
            Intent.ShortcutIconResource.fromContext(applicationContext, R.mipmap.ic_launcher)
        )

        addIntent.action = "com.android.launcher.action.INSTALL_SHORTCUT"
        applicationContext.sendBroadcast(addIntent)
    }
}
