package de.rohnert.smarteatingsystem.data.statistic

import android.content.Context
import android.util.Log
import de.rohnert.smarteatingsystem.data.helper.Helper

import de.rohnert.smarteatingsystem.data.databases.body_database.Body
import de.rohnert.smarteatingsystem.data.databases.daily_database.Daily
import de.rohnert.smarteatingsystem.data.databases.daily_database.MealEntry
import de.rohnert.smarteatingsystem.data.repository.subrepositories.food.FoodProcessor
import de.rohnert.smarteatingsystem.data.databases.food_database.extend_database.ExtendedFood
import de.rohnert.smarteatingsystem.data.repository.MainRepository2
import de.rohnert.smarteatingsystem.data.sharedpreferences.SharedAppPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.File

class CsvDataExport(var context: Context, var repository: MainRepository2, var foodProcessor: FoodProcessor)
{


    // Allgemeine Sachen:
    private var helper = Helper()
    private var csvDataHandler = CSVExportDataHandler(context)
    private var prefs = SharedAppPreferences(context)
    private var mDate = helper.getCurrentDate()
    private var dateList = helper.getDateListWithValue(mDate,7)
    private var dateStringList = helper.convertDateListToStringList(dateList)

    // Interface:
    private lateinit var mListener:OnCSVExportFinishListener

    // Daten aus der Datenbank
    private lateinit var latestBody: Body
    private lateinit var foodList:ArrayList<ExtendedFood>
    private var dailyList:ArrayList<Daily> = ArrayList()

    //
    private var basicData:ArrayList<String> = ArrayList()
    private var lastBodyData:ArrayList<String> = ArrayList()
    private var dailyDatasList:ArrayList<ArrayList<String>> = ArrayList()




    // Diese Methode wird gestartet, wenn ein CSV Export initiert werden soll
    fun startExportProcess()
    {
        CoroutineScope(IO).launch {
            getFoodList()
            getDailyList(dateStringList)
            getLatestBody()
            setBasicDatas()
            setBodyData()
            setDailyData()
            writeToFile()
        }
    }

    private suspend fun getFoodList()
    {
        foodList = repository.getFoodList()
    }

    private suspend fun getDailyList(days:ArrayList<String>):ArrayList<Daily>
    {

        var export:ArrayList<Daily> = ArrayList()
        for(i in days)
        {
            export.add(repository.getDailyByDate(i))
        }

        dailyList = export


        return export

    }

    private suspend fun getLatestBody()
    {
        latestBody = repository.getBodyList().last()
    }

    // Stringlisten zusammenbauen:
    private fun setBasicDatas()
    {
        basicData.add("Smeasy Kalorien- und Körpertracker")
        basicData.add("Ausgabedatum;${helper.getStringFromDate(mDate)}")
        basicData.add("Name;${prefs.userName}")
        basicData.add("Alter;${helper.getYearsBetweenDates(mDate,helper.getDateFromString(prefs.bday))}")
        basicData.add("Körpergröße;${prefs.userHeight}")
        basicData.add("")
    }

    private fun setBodyData()
    {
        lastBodyData.add("Letzte Körperdaten")
        if(lastBodyData!=null)
        {
            lastBodyData.add("Bodyeintrag vom: ${latestBody.date}")


            lastBodyData.add("Gewicht;${latestBody.weight}")
            lastBodyData.add("Körperfettanteil (Kfa);${latestBody.kfa}")
            lastBodyData.add("BodyMassIndex (BMI);${getBodyBMI()}")
            lastBodyData.add("Bauchumfang in cm;${latestBody.bauch}")
            lastBodyData.add("Brustumfang in cm;${latestBody.brust}")
            lastBodyData.add("Halsumfang in cm;${latestBody.hals}")
            lastBodyData.add("Hüftumfang in cm;${latestBody.huefte}")
            

        }
        else
        {
            lastBodyData.add("Bodyeintrag vom: ${latestBody.date}")


            lastBodyData.add("Gewicht;k.A.")
            lastBodyData.add("Körperfettanteil (Kfa);k.A.")
            lastBodyData.add("BodyMassIndex (BMI);k.A.")
            lastBodyData.add("Bauchumfang in cm;k.A.")
            lastBodyData.add("Brustumfang in cm;k.A.")
            lastBodyData.add("Halsumfang in cm;k.A.")
            lastBodyData.add("Hüftumfang in cm;k.A.")
        }
        lastBodyData.add("")

    }


    private fun setDailyData()
    {
        for((indexK,k) in dailyList.withIndex())
        {
            var inputList:ArrayList<String> = ArrayList()
            if(indexK == 0)
            {
                inputList.add("Ausgabe Getrackter Lebensmittel der letzten 7 Tage")
            }
            inputList.add("Getrackter Tag;${k.date}")
            var dailyValues = getDailyValues(k)
            inputList.add("Getrackte Anzahl Kalorien [kcal];${dailyValues[0]}")
            inputList.add("Getrackte Menge Kohlenhydrate [g];${dailyValues[1]}")
            inputList.add("Getrackte Menge Protein [g];${dailyValues[2]}")
            inputList.add("Getrackte Menge Fett [g];${dailyValues[3]}")

            inputList.add("Erlaubte Anzahl Kalorien [kcal];${k.maxKcal}")
            inputList.add("Erlaubte Menge Kohlenhydrate [g];${k.maxCarb}")
            inputList.add("Erlaubte Menge Protein [g];${k.maxProtein}")
            inputList.add("Erlaubte Menge Fett [g];${k.maxFett}")

            // Ab hier die Lebensmittel eintragen:
            inputList.add("Mahlzeit;Lebensmittel;Menge in g;Kalorien in kcal;Kohlenhydrate in g;Protein in g;Fett in g")
            var mealList:ArrayList<ArrayList<MealEntry>> = arrayListOf(k.breakfastEntry!!,k.lunchEntry!!,k.dinnerEntry!!,k.snackEntry!!)
            var meals:ArrayList<String> = arrayListOf("Frühstück","Mittagessen","Abendessen","Snacks")
            for((index,i) in mealList.withIndex())
                if(!i.isNullOrEmpty())
                {
                    for(j in i)
                    {
                        var cf = foodProcessor.getCalcedFood(j)
                        var mString = "${meals[index]};${cf.f.name};${cf.menge};${cf.values[0]};${cf.values[1]};${cf.values[2]};${cf.values[3]}"
                        inputList.add(mString)
                    }


                }

            inputList.add("")
            dailyDatasList.add(inputList)




        }
    }

    private fun writeToFile()
    {
        var export:ArrayList<String> = ArrayList()
        export.addAll(basicData)
        export.addAll(lastBodyData)
        for(i in dailyDatasList)
        {
            export.addAll(i)
        }

        var dir = context.getExternalFilesDir(null)!!.absolutePath
        var dir2 = context.filesDir.absolutePath+"/"
        var file = "export_${helper.getStringFromDateWithPattern(mDate,"ddMMyyyy")}.csv"
        var fileName = dir+file

        var filePath = File(context.filesDir,"data")
        filePath.mkdir()
        var newCSVFile = File(dir,file)

        Log.d("Smeasy","CsvDataExport - writeToFile() - dir: $dir")
        Log.d("Smeasy","CsvDataExport - writeToFile() - file: $file")
        Log.d("Smeasy","CsvDataExport - writeToFile() - fileName: $fileName")

        csvDataHandler.writeArrayToFile(export,dir,file,newCSVFile)

        if (mListener!=null)
        {
            mListener.setOnCSVExportFinishListener(newCSVFile)
        }


    }




    // Methoden die eigentlich woanders hingehören...
    private fun getBodyBMI():Float
    {
        if(prefs.userHeight>0)
        {
            var height = prefs.userHeight
            return latestBody.weight / ((height/100f)*(height/100f))
        }
        else
        {
            return -1f
        }

    }



    // Methoden, die wohl auch woanders ausgelagert werden können:
    private fun getValuesFromMeal(values:ArrayList<MealEntry>):ArrayList<Float>
    {
        var export:ArrayList<ArrayList<Float>> = ArrayList()
        for(i in values)
        {
            export.add(foodProcessor.getCalcecFoodValues(i))
        }
        var exportValues:ArrayList<Float> = ArrayList()
        exportValues.add(0f)
        exportValues.add(0f)
        exportValues.add(0f)
        exportValues.add(0f)
        for(i in export)
        {
            exportValues[0] += i[0]
            exportValues[1] += i[1]
            exportValues[2] += i[2]
            exportValues[3] += i[3]
        }

        return exportValues




    }

    private fun getDailyValues(daily: Daily):ArrayList<Float>
    {
        var export:ArrayList<Float> = ArrayList()
        export.add(0f)
        export.add(0f)
        export.add(0f)
        export.add(0f)
        var entryList:ArrayList<ArrayList<MealEntry>> =
            arrayListOf(daily.breakfastEntry!!,daily.lunchEntry!!,daily.dinnerEntry!!,daily.snackEntry!!)

        for(i in entryList)
        {
            var values = getValuesFromMeal(i)
            export[0] += values[0]
            export[1] += values[1]
            export[2] += values[2]
            export[3] += values[3]
        }

        return export
    }


    // Interface
    interface OnCSVExportFinishListener
    {
        fun setOnCSVExportFinishListener(file:File)
    }

    fun setOnCSVExportFinishListener(mListener:OnCSVExportFinishListener)
    {
        this.mListener = mListener
    }




}