package com.example.roomdatabaseexample.backend.repository.subrepositories.daily

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.roomdatabaseexample.backend.databases.daily_database.Daily
import de.rohnert.smeasy.backend.databases.daily_database.DailyDao
import com.example.roomdatabaseexample.backend.databases.daily_database.DailyDataBaseProvider

class DailyRepository(var application: Application)
{

    private var dailyDao: DailyDao
    private var dailyProcessor:DailyProcessor

    init {
        var db = DailyDataBaseProvider.getDatabase(application)
        dailyDao = db.dailyDao()
        dailyProcessor = DailyProcessor(application)
    }


    // Methoden für das Daily
    fun getDailyByDate(date:String):LiveData<Daily>
    {
        //var m:MutableLiveData<Daily> =
        return dailyDao.getDailyByDate(date)


    }

    fun getOfflineDailyByDate(date:String):Daily
    {return dailyDao.getOffLineDailyByDate(date)}

    // Prüfen ob Daily vorhanden ist....
    fun checkIfDailyExist(date:String):Boolean
    {
        var m = dailyDao.getNumberOfEntries(date)
        return (m != 0)
    }

    fun updateDaily(daily:Daily)
    {
        // AsyncTask einrichten dafür..
        UpdateDailyAsyncTask(dailyDao).execute(daily)
    }

    // Neues Daily erstellen, falls noch nicht vorhanden...
    fun createNewDaily(daily:Daily)
    {
        InsertDailyAsyncTask(dailyDao).execute(daily)


    }

    /////////////////////////////////////////////////////
    // AsyncTasks...
    // Insert new Daily...
    class InsertDailyAsyncTask(private val dailyDao: DailyDao) : AsyncTask<Daily, Void, Void>()
    {

        override fun doInBackground(vararg daily: Daily): Void? {
            dailyDao.insert(daily[0])
            return null
        }
    }
    // Update Daily
    class UpdateDailyAsyncTask(private val dailyDao: DailyDao) : AsyncTask<Daily, Void, Void>()
    {

        override fun doInBackground(vararg daily: Daily): Void? {
            dailyDao.update(daily[0])
            return null
        }
    }







}