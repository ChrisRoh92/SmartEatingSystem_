package de.rohnert.smarteatingsystem.ui.statistics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import de.rohnert.smarteatingsystem.data.databases.body_database.Body
import de.rohnert.smarteatingsystem.data.databases.daily_database.Daily
import de.rohnert.smarteatingsystem.data.databases.daily_database.MealEntry
import de.rohnert.smarteatingsystem.data.databases.daily_database.helper.CalcedFood
import de.rohnert.smarteatingsystem.data.databases.food_database.extend_database.ExtendedFood
import de.rohnert.smarteatingsystem.data.helper.Helper
import de.rohnert.smarteatingsystem.data.repository.MainRepository2
import de.rohnert.smarteatingsystem.data.repository.subrepositories.food.FoodProcessor
import de.rohnert.smarteatingsystem.data.statistic.CsvDataExport
import de.rohnert.smarteatingsystem.data.statistic.StatisticProcessor
import de.rohnert.smarteatingsystem.utils.getDateListFromTodayToDate
import de.rohnert.smarteatingsystem.utils.getDateNDaysAgo
import de.rohnert.smarteatingsystem.utils.toStringDate
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class StatisticViewModel(application: Application) : AndroidViewModel(application)
{
    //Allgemeines:
    private var helper = Helper()
    private var repository = MainRepository2(application)
    private lateinit var foodProcessor: FoodProcessor
    private lateinit var statisticProcessor:StatisticProcessor
    private lateinit var csvExport:CsvDataExport
    private var csvExportDir:String = ""
    private var csvFile:File? = null

    // Daten für die Statistics-Stuff:
    private var dateList:ArrayList<String> = ArrayList()
    private var kcalList:ArrayList<Float> = ArrayList()
    private var allKcal:Float = 0f
    private var allFood:Int = 0
    private var nutritionValues:ArrayList<Float> = ArrayList()
    private var nutritionValueList:ArrayList<ArrayList<Float>> = ArrayList()
    private var mealValues:ArrayList<Float> = ArrayList()
    private var top20FoodList:ArrayList<CalcedFood> = ArrayList()


    // Lokale Daten:
    private var foodList:ArrayList<ExtendedFood> = ArrayList()

    // LiveData - Food:
    private var foodListAvailable:MutableLiveData<Int> = MutableLiveData()
    private var statisticDataAvailable:MutableLiveData<Int> = MutableLiveData()
    private var exportDataFinish:MutableLiveData<Int> = MutableLiveData()

    // LiveData - Body:
    private var bodyDataList:MutableLiveData<ArrayList<Body>> = MutableLiveData()


    var date:String = helper.getStringFromDate(helper.getCurrentDate())


    init {

        CoroutineScope(IO).launch{

            // FoodListe abrufen:
            foodList = repository.getFoodList()
            foodListAvailable.postValue(1)

            foodProcessor = FoodProcessor(application,foodList)
            statisticProcessor = StatisticProcessor(application,repository,foodProcessor)
//
//            dateList = helper.convertDateListToStringList(helper.getDateListWithValue(helper.getCurrentDate(),(7).toLong()
//            ))

            dateList = helper.getWeekListAsString(Date())

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
//        top20FoodList = statisticProcessor.getTop20Foods()



    }

    fun startCSVExport()
    {
        csvExport.startExportProcess()
        csvExport.setOnCSVExportFinishListener(object :CsvDataExport.OnCSVExportFinishListener{
            override fun setOnCSVExportFinishListener(file: File) {
                exportDataFinish.postValue(exportDataFinish.value?.plus(1))
                //csvExportDir = filename
                csvFile = file
            }

        })
    }





    // Getters:


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



    // BodyDatas:
    fun loadBodyData()
    {
        viewModelScope.launch {
            bodyDataList.value = ArrayList(repository.getBodyList().asReversed())
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Other Stuff
    fun getFoodList():ArrayList<ExtendedFood>
    {
        return foodList
    }

    fun returnNewCSVFile():File?
    {
        return csvFile
    }


    fun getDailysFromLastNDays(n:Int): ArrayList<Daily>
    {
        // Get Daily by Date:
        val fromDay:Date = getDateNDaysAgo(n)

        // Create a List of Dates from current date to current data - n
        val dates = getDateListFromTodayToDate(fromDay)
        var dailies:ArrayList<Daily> = ArrayList()

            // Try to get the
            CoroutineScope(IO).launch{
                for(date in dates) {
                    val daily: Daily? = repository.getDailyByDate(date.toStringDate())
                    if (daily != null) {
                        dailies.add(daily)
                    }
                }

        }
        return dailies
    }






    // Über diese Methode, werden die Daten für den LineChart zur Darstellung vom Verlauf der
    // Nährstoffe über einen gegebenen Zeitraum....
    /**
     * @brief
     *
     * @return
     */
    fun getNutritionCourseData(includesDayNumber:Int = 7):ArrayList<Map<String, Float>>
    {
        // For each Day
        var values:MutableMap<String, Float> = mutableMapOf(
            Pair("kcal", 0.0F),
            Pair("carb", 0.0F),
            Pair("protein", 0.0F),
            Pair("fat", 0.0F)
        )
        // Get Daily Data from all days within includesDayNumbers
        val dailyData = getDailysFromLastNDays(includesDayNumber)

        // Wird später natürlich korrekt berechnet...
        var export:ArrayList<Map<String, Float>> = ArrayList()

        for (daily in dailyData) {
            val data = getNutritionValuesFromDaily(daily)
            var values:MutableMap<String, Float> = mutableMapOf(
                Pair("kcal",        data["kcal"] ?: 0.0F),
                Pair("carb",        data["carb"] ?: 0.0F),
                Pair("protein",     data["protein"] ?: 0.0F),
                Pair("fat",         data["fat"] ?: 0.0F)
            )
            export.add(values)
        }

        return export
    }

    private fun getNutritionValuesFromDaily(daily:Daily): Map<String, Float>
    {
        val values:MutableMap<String, Float> = mutableMapOf(
            Pair("kcal", 0.0F),
            Pair("carb", 0.0F),
            Pair("protein", 0.0F),
            Pair("fat", 0.0F)
        )

        // BreakFast:
        if(!daily.breakfastEntry.isNullOrEmpty())
        {
            for(entry in daily.breakfastEntry!!)
            {
                val nutritionValues = getNutritionValuesFromMealEntry(entry)
                values["kcal"] = (values["kcal"] ?: 0.0F)+(nutritionValues["kcal"])!!
                values["carb"] = (values["carb"] ?: 0.0F)+(nutritionValues["carb"])!!
                values["protein"] = (values["protein"] ?: 0.0F)+(nutritionValues["protein"])!!
                values["fat"] = (values["fat"] ?: 0.0F)+(nutritionValues["fat"])!!
            }
        }

        if(!daily.lunchEntry.isNullOrEmpty())
        {
            for(entry in daily.lunchEntry!!)
            {
                val nutritionValues = getNutritionValuesFromMealEntry(entry)
                values["kcal"] = (values["kcal"] ?: 0.0F)+(nutritionValues["kcal"])!!
                values["carb"] = (values["carb"] ?: 0.0F)+(nutritionValues["carb"])!!
                values["protein"] = (values["protein"] ?: 0.0F)+(nutritionValues["protein"])!!
                values["fat"] = (values["fat"] ?: 0.0F)+(nutritionValues["fat"])!!
            }
        }


        if(!daily.dinnerEntry.isNullOrEmpty())
        {
            for(entry in daily.dinnerEntry!!)
            {
                val nutritionValues = getNutritionValuesFromMealEntry(entry)
                values["kcal"] = (values["kcal"] ?: 0.0F)+(nutritionValues["kcal"])!!
                values["carb"] = (values["carb"] ?: 0.0F)+(nutritionValues["carb"])!!
                values["protein"] = (values["protein"] ?: 0.0F)+(nutritionValues["protein"])!!
                values["fat"] = (values["fat"] ?: 0.0F)+(nutritionValues["fat"])!!
            }
        }

        if(!daily.snackEntry.isNullOrEmpty())
        {
            for(entry in daily.snackEntry!!)
            {
                val nutritionValues = getNutritionValuesFromMealEntry(entry)
                values["kcal"] = (values["kcal"] ?: 0.0F)+(nutritionValues["kcal"])!!
                values["carb"] = (values["carb"] ?: 0.0F)+(nutritionValues["carb"])!!
                values["protein"] = (values["protein"] ?: 0.0F)+(nutritionValues["protein"])!!
                values["fat"] = (values["fat"] ?: 0.0F)+(nutritionValues["fat"])!!
            }
        }



        return values
    }

    private fun getNutritionValuesFromMealEntry(entry: MealEntry): Map<String, Float> {

        return mapOf(
            Pair("kcal", entry.kcal),
            Pair("carb", entry.carb),
            Pair("protein", entry.protein),
            Pair("fat", entry.fat)
        )
    }

    fun getBodyList():LiveData<ArrayList<Body>> = bodyDataList





    }
