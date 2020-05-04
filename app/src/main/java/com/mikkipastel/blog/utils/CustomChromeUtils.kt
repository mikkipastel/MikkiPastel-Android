package com.mikkipastel.blog.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.mikkipastel.blog.R

class CustomChromeUtils {

    fun setBlogWebpage(context: Context, url: String, shareMessage: String) {

        CustomTabsIntent.Builder().apply {
            //Setting a custom toolbar color, show title, back button
            setCloseButtonIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_action_arrow_left))
            setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
            setShowTitle(true)

            //Sharing content from CustomTabs
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "$shareMessage $url")
                putExtra(Intent.EXTRA_REFERRER, Uri.parse("android-app://" + context.packageName))
            }
            val pendingIntent = PendingIntent.getActivity(context, 0, shareIntent, 0)
            addMenuItem("Share", pendingIntent)

            //Setting custom enter/exit animations
            setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
            setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right)

            //Open the Custom Tab
            build().launchUrl(context, Uri.parse(url))
        }
    }

}