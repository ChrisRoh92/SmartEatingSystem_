package de.rohnert.smarteatingsystem.backend.repository.subrepositories.daily

import android.app.Application
import androidx.lifecycle.LiveData
import de.rohnert.smarteatingsystem.backend.databases.daily_database.Daily
import de.rohnert.smarteatingsystem.backend.databases.daily_database.DailyDao2
import de.rohnert.smarteatingsystem.backend.databases.daily_database.DailyDataBaseProvider2
import de.rohnert.smarteatingsystem.backend.sharedpreferences.SharedAppPreferences

class DailyRepository2(var application: Application)
{
    private var dailyDao2: DailyDao2
    private var dailyProcessor: DailyProcessor
    private var prefs = SharedAppPreferences(application)

    init {
        var db = DailyDataBaseProvider2.getDatabase(application)
        dailyDao2 = db.dailyDao2()
        dailyProcessor = DailyProcessor(application)
    }

    suspend fun getDailyByDate(date:String): Daily
    {

        if(checkIfDailyExist(date))
        {
            return dailyDao2.getOffLineDailyByDate(date)
        }
        else
        {
            var daily = Daily(date, ArrayList(), ArrayList(), ArrayList(),
                ArrayList(),prefs.maxKcal,prefs.maxCarb,prefs.maxProtein,prefs.maxFett
            )
            addNewDaily(daily)
            return dailyDao2.getOffLineDailyByDate(date)
        }

    }

    suspend fun getDailyList():ArrayList<Daily>
    {
        return ArrayList(dailyDao2.getAll())
    }

    suspend fun getLiveDailyByDate(date:String):LiveData<Daily>
    {
        if(checkIfDailyExist(date))
        {
            return dailyDao2.getDailyByDate(date)
        }
        else
        {
            var daily = Daily(date, ArrayList(), ArrayList(), ArrayList(),
                ArrayList(),2500f,150f,200f,50f
            )
            addNewDaily(daily)
            return dailyDao2.getDailyByDate(date)
        }

    }

    // Prüfen ob Daily vorhanden ist....
    suspend fun checkIfDailyExist(date:String):Boolean
    {
        var m = dailyDao2.getNumberOfEntries(date)
        return (m != 0)
    }

    suspend fun updateDaily(daily: Daily)
    {
        dailyDao2.update(daily)
    }

    suspend fun deleteDaily(daily: Daily)
    {
        dailyDao2.delete(daily)
    }

    suspend fun addNewDaily(daily: Daily)
    {
        dailyDao2.insert(daily)
    }


}