package de.rohnert.smarteatingsystem.utils.others

import android.app.Activity
import android.content.Context
import androidx.core.app.ShareCompat


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