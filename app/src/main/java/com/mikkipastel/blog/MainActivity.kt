package com.mikkipastel.blog

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.mikkipastel.blog.fragment.AboutAppFragment
import com.mikkipastel.blog.fragment.MainFragment
import com.mikkipastel.blog.utils.CustomChromeUtils

const val MY_REQUEST_CODE = 101

class MainActivity: AppCompatActivity(), InstallStateUpdatedListener {

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
                            MY_REQUEST_CODE)
                } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            this,
                            MY_REQUEST_CODE)
                }
            }

            if (appUpdateInfo.installStatus() == InstallStatus.INSTALLED) {
                popupSnackbarForState("An update has just been downloaded.", Snackbar.LENGTH_LONG)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_aboutapp) {
            showDialog()
        } else if (item.itemId == R.id.action_aboutme) {
            aboutMe()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showDialog() {
        // Create and show the dialog.
        val newFragment = AboutAppFragment.newInstance()
        newFragment.show(supportFragmentManager.beginTransaction(), "dialog")
    }

    private fun aboutMe() {
        CustomChromeUtils().setBlogWebpage(
                this,
                "https://mikkipastel.firebaseapp.com/",
                "About Mikkipastel"
        )
    }

    private fun addShortcut() {
        //Adding shortcut for MainActivity
        //on Home screen

        val shortcutIntent = Intent(applicationContext, MainActivity::class.java)

        shortcutIntent.action = Intent.ACTION_MAIN

        val addIntent = Intent()
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent)
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "MikkiPastel")
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(applicationContext, R.mipmap.ic_launcher))

        addIntent.action = "com.android.launcher.action.INSTALL_SHORTCUT"
        applicationContext.sendBroadcast(addIntent)
    }
}