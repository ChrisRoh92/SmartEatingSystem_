package de.rohnert.smeasy.helper.others

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat.startActivity



class SimpleShare(var context:Context,var activity: Activity)
{
    init {
        initSharingProcress()
    }

    private fun initSharingProcress()
    {

        var content ="SmartEatingSystem - Kalorienzähler & Körpertracker \n\n"+
                "Lade dir über folgenden Link SmartEatingSystem im Google Play Store herunter:\n\n\n"+
                ("http://play.google.com/store/apps/details?id=" + activity.packageName)
        ShareCompat.IntentBuilder.from(activity)
            .setType("text/plain")
            .setChooserTitle("SmartEatingSystem - Kalorienzähler & Körpertracker")

            .setText(content)
            .startChooser()
    }
}