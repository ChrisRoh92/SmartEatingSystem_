package de.rohnert.smeasy.frontend.bodytracker

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import backend.helper.Helper
import com.example.roomdatabaseexample.backend.databases.body_database.Body
import com.example.roomdatabaseexample.backend.repository.subrepositories.body.BodyProcessor
import de.rohnert.smeasy.backend.repository.MainRepository2
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smeasy.backend.sharedpreferences.SharedPreferencesSmeasyValues
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
    private var prefs = SharedAppPreferences(application)
    private var smeasyPrefs = SharedPreferencesSmeasyValues(application)
    private var bodyProcessor = BodyProcessor()

    // BodyList:
    private lateinit var localBodyList:ArrayList<Body>
    private var date:String = helper.getStringFromDate(helper.getCurrentDate())
    private var localCurrentBody:Body? = null
    private var bodyAimList:ArrayList<Float> = ArrayList()
    private var progressList:ArrayList<Float> = ArrayList()
    private var startBodyValues:ArrayList<Float> = ArrayList()

    // LiveData
    private var liveBodyList:MutableLiveData<ArrayList<Body>> = MutableLiveData()
    private var liveProgressValue:MutableLiveData<Int> = MutableLiveData()


    init
    {
        CoroutineScope(IO).launch {
            localBodyList = repository.getBodyList()
            localCurrentBody = repository.getBodyByDate(date)
            if(repository.getBodyByDate(date) == null)
            {
                if(localBodyList.isNullOrEmpty())
                {

                    localCurrentBody = null
                }
                else
                {
                    localCurrentBody = localBodyList.last()
                    saveCurrentBodyToPref()
                }

            }
            else
            {
                localCurrentBody = repository.getBodyByDate(date)
                saveCurrentBodyToPref()
            }
            setBodyProgress()
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
            setBodyProgress()
            saveCurrentBodyToPref()
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
            setBodyProgress()
            saveCurrentBodyToPref()



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
                if(localCurrentBody!!.date == newBody.date)
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

    fun setBodyProgress()
    {

        if(localCurrentBody!=null) {

            bodyAimList = ArrayList()
            progressList = ArrayList()
            startBodyValues = ArrayList()


            startBodyValues.add(localBodyList[0].weight)
            startBodyValues.add(localBodyList[0].kfa)
            startBodyValues.add(bodyProcessor.calcBMI(prefs.userHeight, localBodyList[0].weight))
            startBodyValues.add(localBodyList[0].bauch)
            startBodyValues.add(localBodyList[0].brust)
            startBodyValues.add(localBodyList[0].hals)
            startBodyValues.add(localBodyList[0].huefte)


            bodyAimList.add(prefs.weightAim)
            bodyAimList.add(prefs.kfaAim)
            bodyAimList.add(prefs.bmiAim)
            bodyAimList.add(prefs.bauchAim)
            bodyAimList.add(prefs.brustAim)
            bodyAimList.add(prefs.halsAim)
            bodyAimList.add(prefs.huefteAim)

            var currentBodyValues: ArrayList<Float> = ArrayList()
            currentBodyValues.add(localCurrentBody!!.weight)
            currentBodyValues.add(localCurrentBody!!.kfa)
            currentBodyValues.add(
                bodyProcessor.calcBMI(
                    prefs.userHeight,
                    localCurrentBody!!.weight
                )
            )
            currentBodyValues.add(localCurrentBody!!.bauch)
            currentBodyValues.add(localCurrentBody!!.brust)
            currentBodyValues.add(localCurrentBody!!.hals)
            currentBodyValues.add(localCurrentBody!!.huefte)

            Log.d("Smeasy","BodyViewModel - setBodyProgress startBodyValues: $startBodyValues")
            Log.d("Smeasy","BodyViewModel - setBodyProgress bodyAimList: $bodyAimList")
            Log.d("Smeasy","BodyViewModel - setBodyProgress localCurrentBody: $localCurrentBody")
            Log.d("Smeasy","BodyViewModel - setBodyProgress currentBodyValues: $currentBodyValues")


            for ((index, i) in bodyAimList.withIndex()) {
                if (i != 0f && startBodyValues[index] != i && startBodyValues[index] > 0) {
                    var value =
                        (startBodyValues[index] - currentBodyValues[index]) / (startBodyValues[index] - i)*100f
                    progressList.add(value)
                } else if (startBodyValues[index] != i) {
                    progressList.add(0f)
                } else {
                    progressList.add(0f)
                }
            }

            liveProgressValue.postValue(liveProgressValue.value?.plus(1))
        }
        else
        {
            for(i in 0..6)
            {
                progressList.add(0f)
            }
        }
        saveProgressToPref()
        Log.d("Smeasy","BodyViewModel - setBodyProgress progressList: $progressList")



    }


    // To Smeasy_Preferences save
    private fun saveCurrentBodyToPref()
    {
        smeasyPrefs.setNewWeight(localCurrentBody!!.weight)
        smeasyPrefs.setNewKfa(localCurrentBody!!.kfa)
        smeasyPrefs.setNewBmi(bodyProcessor.calcBMI(prefs.userHeight,localCurrentBody!!.weight))
        smeasyPrefs.setNewBauch(localCurrentBody!!.bauch)
        smeasyPrefs.setNewBrust(localCurrentBody!!.brust)
        smeasyPrefs.setNewHals(localCurrentBody!!.hals)
        smeasyPrefs.setNewHuefte(localCurrentBody!!.huefte)
    }

    private fun saveProgressToPref()
    {
        smeasyPrefs.setNewWeightProgress(progressList[0])
        smeasyPrefs.setNewKfaProgress(progressList[1])
        smeasyPrefs.setNewBmiProgress(progressList[2])
        smeasyPrefs.setNewBauchProgress(progressList[3])
        smeasyPrefs.setNewBrustProgress(progressList[4])
        smeasyPrefs.setNewHalsProgress(progressList[5])
        smeasyPrefs.setNewHuefteProgress(progressList[6])
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

    fun getLocalBody():Body?
    {
        return localCurrentBody
    }

    fun getProgressWasSetValue():LiveData<Int>
    {
        return liveProgressValue
    }

    fun getProgressList():ArrayList<Float>
    {
        return progressList
    }

    fun getStartBodyList():ArrayList<Float>
    {
        return startBodyValues
    }

    fun getAimBodyList():ArrayList<Float>
    {
        return bodyAimList
    }
}