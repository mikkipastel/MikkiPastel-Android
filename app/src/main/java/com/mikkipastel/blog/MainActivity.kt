package com.mikkipastel.blog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.mikkipastel.blog.fragment.AboutAppFragment
import com.mikkipastel.blog.fragment.MainFragment
import com.mikkipastel.blog.utils.CustomChromeUtils

class MainActivity: AppCompatActivity() {

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