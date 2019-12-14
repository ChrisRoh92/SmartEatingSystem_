package de.rohnert.smeasy.frontend.statistics

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import backend.helper.Helper
import com.example.roomdatabaseexample.backend.databases.daily_database.helper.CalcedFood
import com.example.roomdatabaseexample.backend.databases.food_database.Food
import com.example.roomdatabaseexample.backend.repository.subrepositories.daily.DailyProcessor
import com.example.roomdatabaseexample.backend.repository.subrepositories.food.FoodProcessor
import de.rohnert.smeasy.backend.repository.MainRepository2
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smeasy.backend.statistic.CsvDataExport
import de.rohnert.smeasy.backend.statistic.StatisticProcessor
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlin.math.exp

class StatisticViewModel(application: Application) : AndroidViewModel(application)
{
    //Allgemeines:
    private var helper = Helper()
    private var repository = MainRepository2(application)
    // Prozessoren...
    private var dailyProcess = DailyProcessor(application)
    private lateinit var foodProcessor:FoodProcessor
    private lateinit var statisticProcessor:StatisticProcessor
    private lateinit var csvExport:CsvDataExport
    private var csvExportDir:String = ""

    // Daten für die Statistics-Stuff:
    private var dateList:ArrayList<String> = ArrayList()
    private var kcalList:ArrayList<Float> = ArrayList()
    private var allKcal:Float = 0f
    private var allFood:Int = 0
    private var nutritionValues:ArrayList<Float> = ArrayList()
    private var nutritionValueList:ArrayList<ArrayList<Float>> = ArrayList()
    private var mealValues:ArrayList<Float> = ArrayList()
    private var top20FoodList:ArrayList<CalcedFood> = ArrayList()


    // Zentraler Zugriff auf Einstellungen...
    private var sharePrefs = SharedAppPreferences(application)

    // Lokale Daten:
    private var foodList:ArrayList<Food> = ArrayList()

    // LiveDaten:
    private var foodListAvailable:MutableLiveData<Int> = MutableLiveData()
    private var statisticDataAvailable:MutableLiveData<Int> = MutableLiveData()
    private var exportDataFinish:MutableLiveData<Int> = MutableLiveData()


    var date:String = helper.getStringFromDate(helper.getCurrentDate())


    init {

        CoroutineScope(IO).launch{

            // FoodListe abrufen:
            foodList = repository.getFoodList()
            foodListAvailable.postValue(1)

            foodProcessor = FoodProcessor(application,foodList)
            statisticProcessor = StatisticProcessor(application,repository,foodProcessor)

            dateList = helper.convertDateListToStringList(helper.getDateListWithValue(helper.getCurrentDate(),
                (7).toLong()
            ))

            csvExport = CsvDataExport(application,repository,foodProcessor)

            runBlocking {
                getAllStatisticsDatas()
                statisticDataAvailable.postValue(1)
            }


        }



        }


    private suspend fun getAllStatisticsDatas()
    {
        statisticProcessor.getDailyList(dateList)
        kcalList = statisticProcessor.getAlLKcalValues()
        allKcal = statisticProcessor.allKcal
        allFood = statisticProcessor.getNumberOfFoods()

        nutritionValueList = statisticProcessor.getCalcedNutritionValueList()
        nutritionValues = statisticProcessor.getAllNutritionValues()
        mealValues = statisticProcessor.getKcalFromMeal()
        top20FoodList = statisticProcessor.getTop20Foods()



    }

    fun startCSVExport()
    {
        csvExport.startExportProcess()
        csvExport.setOnCSVExportFinishListener(object :CsvDataExport.OnCSVExportFinishListener{
            override fun setOnCSVExportFinishListener(filename: String) {
                exportDataFinish.postValue(exportDataFinish.value?.plus(1))
                csvExportDir = filename
            }

        })
    }





    // Getters:



    // Live Data
    // Soll aufgerufen werden, sobald die FoodListe vollständig geladen wurde....
    fun getFoodListAvailable():LiveData<Int>
    {
        return foodListAvailable
    }

    fun getStatisticDataAvailable():LiveData<Int>
    {
        return statisticDataAvailable
    }

    fun getCSVExportFinished():LiveData<Int>
    {
        return exportDataFinish
    }




    //////////////////////////////////////////////////////////////////////////////////////////////
    // Abrufen der Daten für die Liste von
    fun getKcalValues():ArrayList<Float>
    {
        return kcalList
    }

    fun getKcalSum():Float
    {
        return allKcal
    }

    fun getNumberOfFoods():Int
    {
        return allFood
    }

    // Über diese Methode, werden die Daten für den PieChart zur Darstellung für die
    // durchschnittliche Verteilung der Nährstoffe bereitgestellt
    fun getNutritionValues():ArrayList<Float>
    {
        return nutritionValues
    }

    // Hier wird der Verlauf der Werte dargestellt:
    fun getNutritionCourseValues():ArrayList<ArrayList<Float>>
    {
        return nutritionValueList
    }


    // Über diese Methode, werden die Daten für den PieChart zur Darstellung für die
    // durchschnittliche Verteilung der Nährstoffe nach Mahlzeit bereitgestellt
    fun getMealValues():ArrayList<Float>
    {
        return mealValues
    }

    fun getTop20Foods():ArrayList<CalcedFood>
    {
        return top20FoodList
    }














    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Other Stuff
    fun getFoodList():ArrayList<Food>
    {
        return foodList
    }

    fun getFoodAtPosition(pos:Int):Food
    {
        return foodList[pos]
    }

    fun getCSVExportFileName():String
    {
        return csvExportDir
    }






    // Über diese Methode, werden die Daten für den LineChart zur Darstellung vom Verlauf der
    // Nährstoffe über einen gegebenen Zeitraum....
    fun getNutritionCourseData():ArrayList<ArrayList<Float>>
    {
        // Wird später natürlich korrekt berechnet...
        var export:ArrayList<ArrayList<Float>> = ArrayList()
        for(i in 1..4) {
            export.add(ArrayList())

            for (j in 1..7) {
                export[i-1].add(0f)

            }
        }

        return export
    }





    }
