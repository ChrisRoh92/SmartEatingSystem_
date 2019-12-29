package de.rohnert.smarteatingsystem.backend.share

import android.app.Activity
import android.content.Context
import android.net.Uri
import com.facebook.CallbackManager
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog

class FacebookShare(var context: Context,var activity:Activity)
{
    private lateinit var callBackManager:CallbackManager
    private lateinit var shareDialog:ShareDialog

    init {
        startSharing()
    }

    private fun startSharing()
    {
        callBackManager = CallbackManager.Factory.create()
        shareDialog = ShareDialog(activity)
        if (ShareDialog.canShow(ShareLinkContent::class.java)) {
            val linkContent = ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                .build()
            shareDialog.show(linkContent)
        }
    }
}