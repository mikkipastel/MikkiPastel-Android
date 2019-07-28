package com.mikkipastel.blog.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import com.mikkipastel.blog.R

class CustomChromeUtils {

    fun setBlogWebpage(context: Context, url: String, shareMessage: String) {

        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))

        builder.setShowTitle(true)
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(
                context.resources, R.drawable.ic_action_arrow_left))

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "$shareMessage $url")

        val pi = PendingIntent.getActivity(context, 0, shareIntent, 0)
        builder.addMenuItem("Share", pi)

        builder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
        builder.setExitAnimations(context, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right)

        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, Uri.parse(url))
    }

}