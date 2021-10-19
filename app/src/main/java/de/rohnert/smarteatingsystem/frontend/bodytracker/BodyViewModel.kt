package de.rohnert.smarteatingsystem.frontend.bodytracker

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import de.rohnert.smarteatingsystem.utils.Constants.LOGGING_TAG
import de.rohnert.smarteatingsystem.backend.helper.Helper
import de.rohnert.smarteatingsystem.backend.databases.body_database.Body
import de.rohnert.smarteatingsystem.backend.repository.subrepositories.body.BodyProcessor
import de.rohnert.smarteatingsystem.backend.repository.MainRepository2
import de.rohnert.smarteatingsystem.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smarteatingsystem.backend.sharedpreferences.SharedPreferencesSmeasyValues
import de.rohnert.smarteatingsystem.utils.getDateFromString
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
    private var currentBodyValues: ArrayList<Float> = ArrayList()

    // LiveData
    private var liveBodyList:MutableLiveData<ArrayList<Body>> = MutableLiveData()
    private var liveProgressValue:MutableLiveData<Int> = MutableLiveData()


    init
    {
        viewModelScope.launch(IO) {
            localBodyList = repository.getBodyList()
            sortLocalBodyList()
            localCurrentBody = repository.getBodyByDate(getDateFromString(date,"dd.MM.yyyy").time)
            if(repository.getBodyByDate(getDateFromString(date,"dd.MM.yyyy").time) == null)
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
                localCurrentBody = repository.getBodyByDate(getDateFromString(date).time)
                saveCurrentBodyToPref()
            }
            setBodyProgress()
            liveBodyList.postValue(repository.getBodyList())
        }

    }




    // Methoden mit Zugriff:
    fun addNewBody(weight:Float,kfa:Float,bauch:Float,brust:Float,hals:Float, huefte:Float, dir:String,entryDate:Long)
{
    CoroutineScope(IO).launch {
        if(repository.getBodyByDate(entryDate) == null)
        {
            Log.d(LOGGING_TAG,"EntryDate = $entryDate")
            var export = Body(entryDate,weight,kfa,bauch,brust,hals,huefte,dir)
            repository.addNewBody(export)
            localCurrentBody = export
            localBodyList.add(export)
            liveBodyList.postValue(repository.getBodyList())
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
            liveBodyList.postValue(repository.getBodyList())
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
                liveBodyList.postValue(repository.getBodyList())
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
            if(i.date == getDateFromString(date,"dd.MM.yyyy").time)
            {
                export = true
                break
            }
        }

        return export
    }

    private fun setBodyProgress()
    {

        CoroutineScope(IO).launch {

            if(repository.getNumberOfBodyEntries() > 0)
            {
                bodyAimList = ArrayList()
                progressList = ArrayList()

                startBodyValues = ArrayList()

                val oldestBody = repository.getOldestBody()
                val latestBody = repository.getLatestBody()

                // Werte vom ersten Eintrag:
                startBodyValues.add(oldestBody.weight)
                startBodyValues.add(oldestBody.kfa)
                startBodyValues.add(bodyProcessor.calcBMI(prefs.userHeight, oldestBody.weight))
                startBodyValues.add(oldestBody.bauch)
                startBodyValues.add(oldestBody.brust)
                startBodyValues.add(oldestBody.hals)
                startBodyValues.add(oldestBody.huefte)


                // Werte vom letzten Eintrag:
                currentBodyValues =  ArrayList()
                currentBodyValues.add(latestBody.weight)
                currentBodyValues.add(latestBody.kfa)
                currentBodyValues.add(bodyProcessor.calcBMI(prefs.userHeight,latestBody.weight))
                currentBodyValues.add(latestBody.bauch)
                currentBodyValues.add(latestBody.brust)
                currentBodyValues.add(latestBody.hals)
                currentBodyValues.add(latestBody.huefte)



                // Liste enthält die aktuellen Ziele vom User
                bodyAimList.add(prefs.weightAim)
                bodyAimList.add(prefs.kfaAim)
                bodyAimList.add(prefs.bmiAim)
                bodyAimList.add(prefs.bauchAim)
                bodyAimList.add(prefs.brustAim)
                bodyAimList.add(prefs.halsAim)
                bodyAimList.add(prefs.huefteAim)

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


    }

    fun updateBodyProgress()
    {
        CoroutineScope(IO).launch {
            prefs = SharedAppPreferences(context)
            setBodyProgress()
            saveCurrentBodyToPref()
            liveBodyList.postValue(repository.getBodyList())
        }


    }


    // To Smeasy_Preferences save
    private fun saveCurrentBodyToPref()
    {
        // TODO: Überlegen ob das so noch notwendig ist und anpassen!
        if(localCurrentBody!= null)
        {
            smeasyPrefs.setNewWeight(localCurrentBody!!.weight)
            smeasyPrefs.setNewKfa(localCurrentBody!!.kfa)
            smeasyPrefs.setNewBmi(bodyProcessor.calcBMI(prefs.userHeight,localCurrentBody!!.weight))
            smeasyPrefs.setNewBauch(localCurrentBody!!.bauch)
            smeasyPrefs.setNewBrust(localCurrentBody!!.brust)
            smeasyPrefs.setNewHals(localCurrentBody!!.hals)
            smeasyPrefs.setNewHuefte(localCurrentBody!!.huefte)
        }

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






    // LocalBodyListe sortieren:
    private fun sortLocalBodyList()
    {
        // TODO: Wird die LocalBodyListe überhaupt noch benötigt?
        localBodyList = ArrayList(localBodyList.sortedWith(compareBy { it.date }))

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