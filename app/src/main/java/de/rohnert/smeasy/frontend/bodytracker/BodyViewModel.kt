package de.rohnert.smeasy.frontend.bodytracker

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.rohnert.smeasy.backend.helper.Helper
import de.rohnert.smeasy.backend.databases.body_database.Body
import de.rohnert.smeasy.backend.repository.subrepositories.body.BodyProcessor
import de.rohnert.smeasy.backend.repository.MainRepository2
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smeasy.backend.sharedpreferences.SharedPreferencesSmeasyValues
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class BodyViewModel(application: Application) : AndroidViewModel(application)
{

    // Allgemeines:
    //Allgemeines:
    private var helper = Helper()
    private var repository = MainRepository2(application)
    private var prefs = SharedAppPreferences(application)
    private var smeasyPrefs = SharedPreferencesSmeasyValues(application)
    private var bodyProcessor = BodyProcessor()
    private var context = application

    // BodyList:
    private lateinit var localBodyList:ArrayList<Body>
    private var date:String = helper.getStringFromDate(helper.getCurrentDate())
    private var localCurrentBody: Body? = null
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
            sortLocalBodyList()
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

    fun undoBodyDelete(body: Body)
    {
        CoroutineScope(IO).launch {
            repository.addNewBody(body)
            localBodyList.add(body)
            sortLocalBodyList()
            localCurrentBody = localBodyList.last()
            liveBodyList.postValue(localBodyList)
            setBodyProgress()
            saveCurrentBodyToPref()
        }
    }

    fun deleteBody(body: Body)
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

    fun updateBody(newBody: Body)
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
                updateBodyProgress()
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

        if(localCurrentBody!=null && !localBodyList.isNullOrEmpty()) {

            bodyAimList = ArrayList()
            progressList = ArrayList()
            startBodyValues = ArrayList()

            // Dieses Liste enthält die Werte von den ersten eingetragenen Körperwerten
            startBodyValues.add(localBodyList[0].weight)
            startBodyValues.add(localBodyList[0].kfa)
            startBodyValues.add(bodyProcessor.calcBMI(prefs.userHeight, localBodyList[0].weight))
            startBodyValues.add(localBodyList[0].bauch)
            startBodyValues.add(localBodyList[0].brust)
            startBodyValues.add(localBodyList[0].hals)
            startBodyValues.add(localBodyList[0].huefte)

            // Liste enthält die aktuellen Ziele vom User
            bodyAimList.add(prefs.weightAim)
            bodyAimList.add(prefs.kfaAim)
            bodyAimList.add(prefs.bmiAim)
            bodyAimList.add(prefs.bauchAim)
            bodyAimList.add(prefs.brustAim)
            bodyAimList.add(prefs.halsAim)
            bodyAimList.add(prefs.huefteAim)

            // Liste enthält die aktuellen Körperwerte vom User (bzw. die vom letzten Eintrag)
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

            // Hier werden die Progress eingestellt...
            for ((index, i) in bodyAimList.withIndex()) {
                if (i > 0f && startBodyValues[index] != i && startBodyValues[index] > 0) {
                    var value = (startBodyValues[index] - currentBodyValues[index]) / (startBodyValues[index] - i)*100f
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

    fun updateBodyProgress()
    {
        CoroutineScope(IO).launch {
            prefs = SharedAppPreferences(context)
            setBodyProgress()
            saveCurrentBodyToPref()
            liveBodyList.postValue(localBodyList)
        }


    }




    // LocalBodyListe sortieren:
    private fun sortLocalBodyList()
    {
        var values:ArrayList<Body> = ArrayList()
        values = ArrayList(localBodyList.sortedWith(compareBy { it.date }))
        localBodyList = values
    }





    // Getter Methoden:
    fun getLiveBodyList():LiveData<ArrayList<Body>>
    {
        return liveBodyList
    }

    fun getLocalBody(): Body?
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