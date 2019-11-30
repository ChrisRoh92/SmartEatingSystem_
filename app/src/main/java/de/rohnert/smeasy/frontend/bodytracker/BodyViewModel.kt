package de.rohnert.smeasy.frontend.bodytracker

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import backend.helper.Helper
import com.example.roomdatabaseexample.backend.databases.body_database.Body
import de.rohnert.smeasy.backend.repository.MainRepository2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class BodyViewModel(application: Application) : AndroidViewModel(application)
{

    // Allgemeines:
    //Allgemeines:
    private var helper = Helper()
    private var repository = MainRepository2(application)

    // BodyList:
    private lateinit var localBodyList:ArrayList<Body>
    private var date:String = helper.getStringFromDate(helper.getCurrentDate())
    private lateinit var localCurrentBody:Body

    // LiveData
    private var liveBodyList:MutableLiveData<ArrayList<Body>> = MutableLiveData()


    init
    {
        CoroutineScope(IO).launch {
            localBodyList = repository.getBodyList()
            localCurrentBody = repository.getBodyByDate(date)
            liveBodyList.postValue(localBodyList)
        }

    }




    // Methoden mit Zugriff:
    fun addNewBody(weight:Float,kfa:Float,bauch:Float,brust:Float,hals:Float, huefte:Float, dir:String)
{
    CoroutineScope(IO).launch {
        if(repository.getBodyByDate(date) == null)
        {
            var export = Body(date,weight,kfa,bauch,brust,hals,huefte,dir)
            repository.addNewBody(export)
            localCurrentBody = export
            localBodyList.add(export)
            liveBodyList.postValue(localBodyList)
        }
    }
}

    fun deleteBody(body:Body)
    {
        CoroutineScope(IO).launch {


            localBodyList.remove(body)
            Log.d("Smeasy","BodyViewModel - deleteBody Size of localBodyList = ${localBodyList.size} after delete")
            if(localCurrentBody == body && !localBodyList.isNullOrEmpty())
            {
                localCurrentBody = localBodyList.last()
            }
            runBlocking {
                liveBodyList.postValue(localBodyList)
                repository.deleteBody(body)
            }



        }
    }

    fun updateBody(newBody:Body)
    {
        CoroutineScope(IO).launch {
            var pos = -1
            for((index,i) in localBodyList.withIndex())
            {
                if(i.date == newBody.date)
                {
                    pos = index
                    break
                }
            }
            if(pos != -1)
            {
                localBodyList[pos] = newBody
                repository.updateBody(newBody)
                if(localCurrentBody.date == newBody.date)
                {
                    localCurrentBody = newBody
                }
                liveBodyList.postValue(localBodyList)
            }



        }
    }

    fun checkIfBodyExist():Boolean
    {
        var export = false
        for(i in localBodyList)
        {
            if(i.date == date)
            {
                export = true
                break
            }
        }

        return export
    }







    // Getter Methoden:
    fun getLiveBodyList():LiveData<ArrayList<Body>>
    {
        return liveBodyList
    }

    fun getBodyList():ArrayList<Body>
    {
        var export:ArrayList<Body> = ArrayList()
        for(i in 1..10)
        {
            var body = Body("${i*2}.11.2019",100f*((100-2*i)/100f),25f,90f,90f,90f,90f,"")
            export.add(body)
        }
        return ArrayList(export.asReversed())
    }

    fun getProgressValues():ArrayList<Float>
    {
        return arrayListOf(0f,0f,0f,0f)
    }
}