package de.rohnert.smeasy.frontend.foodtracker.helper

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import backend.helper.Helper
import com.example.roomdatabaseexample.backend.databases.daily_database.Daily
import com.example.roomdatabaseexample.backend.databases.daily_database.MealEntry
import com.example.roomdatabaseexample.backend.databases.daily_database.helper.CalcedFood
import com.example.roomdatabaseexample.backend.databases.food_database.Food
import com.example.roomdatabaseexample.backend.repository.subrepositories.daily.DailyProcessor
import de.rohnert.smeasy.frontend.foodtracker.FoodViewModel2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class WeekReportCreator(date:String, var foodViewModel:FoodViewModel2)
{
    // Allgemeiner Stuff
    private var helper = Helper()
    private var mDate:String = date
    private var dayList:ArrayList<String> = ArrayList()
    private var dailyList:ArrayList<Daily> = ArrayList()
    private var dailyProcess = DailyProcessor(foodViewModel.getApplication())
    private var created:MutableLiveData<Int> = MutableLiveData()
    // Interface:
    private lateinit var mListener:OnWeekReportChangeListener


    init {

        // Woche herausfunden...
        runBlocking {
            CoroutineScope(IO).launch {
                createDayList()
                createDailyList()
                created.postValue(0)
            }



            /*if(mListener!=null)
            {
                mListener.setOnWeekReportChangeListener()
            }*/
        }







    }

    // Init Funktionen....
    private suspend fun createDayList()
    {


        var dayOfWeek = helper.getLocalDateFromDate(helper.getDateFromString(mDate))
        var dayOfWeekValue = dayOfWeek.dayOfWeek
        Log.d("Smeasy","WeekReportCreator: createDayList dayOfWeek.dayOfWeek = $dayOfWeekValue")
        Log.d("Smeasy","WeekReportCreator: createDayList dayOfWeek.dayOfWeek.value = ${dayOfWeekValue.value}")
        var diff = (dayOfWeekValue.value).toLong()
        Log.d("Smeasy","WeekReportCreator: createDayList diff = $diff")
        var startDay = dayOfWeek.minusDays(diff-1)
        Log.d("Smeasy","WeekReportCreator: createDayList startDay = $startDay")
        dayList.clear()
        for(i in 0..6)
        {

            dayList.add(helper.getStringFromDate(helper.getDateFromLocalDate(startDay.plusDays(i.toLong()))))
        }

    }

    private suspend fun createDailyList()
    {
        dailyList.clear()
        for((index,i) in dayList.withIndex())
        {
            dailyList.add(foodViewModel.getDailyByDate(i)!!)
            Log.d("Smeasy","${dailyList[index]}")
        }
    }


    fun getCreated():LiveData<Int>
    {
        return created
    }


    // TagesWerte Komplett abrufen..
    fun getCalcedValues():ArrayList<Float>
    {
        var export:ArrayList<Float> = arrayListOf(0f,0f,0f,0f)
        for(i in dailyList)
        {
            var localList = getDailyValues(i)
            for((index2,j) in localList.withIndex())
            {
                export[index2] += j
            }
        }
        return export
    }

    fun getWeeklyFoodList():ArrayList<CalcedFood>
    {
        var export:ArrayList<CalcedFood> = ArrayList()
        for(i in dailyList)
        {
            export.addAll(getCalcedFoodsByDaily(i))
        }



        return export
    }

    // Liste mit den Werten....
    fun getChartValues():ArrayList<ArrayList<Float>>
    {
        //var export:ArrayList<Map<Float,Float>> = ArrayList()
        var export:ArrayList<ArrayList<Float>> = ArrayList()
        export.add(ArrayList())
        export.add(ArrayList())
        export.add(ArrayList())
        export.add(ArrayList())
        for((index,i) in dailyList.withIndex())
        {
            var values = getDailyValues(i)
            for(j in 0..3)
            {
                export[j].add(values[j])
            }
           //export.add(mapOf(values[0] to index.toFloat(),values[1] to index.toFloat(),values[2] to index.toFloat(),values[3] to index.toFloat()))
            /*export.add(mapOf(index.toFloat() to values[0],index.toFloat() to values[1],index.toFloat() to values[2],index.toFloat() to values[3]))*/

        }

        /*var meals:ArrayList<String> = arrayListOf("breakfast","lunch","dinner","snack")
        for((index,i) in meals.withIndex())
        {
            var map:MutableMap<Float,Float> = mutableMapOf<Float,Float>()
            for((indexJ,j) in dailyList.withIndex())
            {
                var value =
                map.set(indexJ.toFloat(), getMealValues(j,i)[index])

            }
            export.add(map)

        }*/

        return export
    }


    fun setNewDate(newDate:String)
    {
        mDate = newDate
        if(dayList.contains(mDate))
        {
            // Do Nothing
        }
        else
        {
            runBlocking {
                CoroutineScope(IO).launch {
                    createDayList()
                    createDailyList()
                    var m = created.value!! + 1
                    created.postValue(m)
                }
            }
        }
    }



    // Interface:
    interface OnWeekReportChangeListener
    {
        fun setOnWeekReportChangeListener()
    }

    fun setOnWeekReportChangeListener(mListener:OnWeekReportChangeListener)
    {
        this.mListener = mListener
    }

    fun getFirstDay():String
    {
        return dayList[0]
    }

    fun getLastDay():String
    {
        return dayList.last()
    }











    private fun getDailyValues(daily:Daily):ArrayList<Float>
    {
        // Steht noch aus
        var export:ArrayList<Float> = arrayListOf(0f,0f,0f,0f)
        runBlocking {
            var values:ArrayList<ArrayList<Float>> = ArrayList()
            var breakfast:ArrayList<Float> = getMealValues(daily,"breakfast")
            var lunch:ArrayList<Float> = getMealValues(daily,"lunch")
            var dinner:ArrayList<Float> = getMealValues(daily,"dinner")
            var snack:ArrayList<Float> = getMealValues(daily,"snack")
            values.add(breakfast)
            values.add(lunch)
            values.add(dinner)
            values.add(snack)
            for(i in values)
            {
                export[0] += i[0]
                export[1] += i[1]
                export[2] += i[2]
                export[3] += i[3]
            }
        }

        Log.d("Smeasy","WeekReportCreator getDailyValues export=$export")
        return export
    }

    private fun getMealValues(daily:Daily,meal:String):ArrayList<Float>
    {
        var export:ArrayList<Float> = arrayListOf(0f,0f,0f,0f)
        if(getCalcedFoodsByMeal(daily,meal).isNotEmpty())
        {
            export = dailyProcess.getMealValues(getCalcedFoodsByMeal(daily,meal))
        }
        //Log.d("Smeasy","FoodViewModel - getMealValues - export: $export")
        return export
    }

    private fun getCalcedFoodsByMeal(daily:Daily,meal:String):ArrayList<CalcedFood>
    {



        var calcedFoodList:ArrayList<CalcedFood> = ArrayList()
        runBlocking {
            suspend fun createCalcedFoodList(list:ArrayList<MealEntry>)
            {
                if(!list.isNullOrEmpty())
                {
                    //Log.d("Smeas")

                    for(i in list)
                    {
                        calcedFoodList.add(dailyProcess.getCalcedFood(i.mealID,foodViewModel.getAppFoodById(i.id)!!,i.menge))
                    }

                }




            }

            when(meal)
            {
                "breakfast" -> createCalcedFoodList(daily.breakfastEntry!!)
                "lunch" -> createCalcedFoodList(daily.lunchEntry!!)
                "dinner" -> createCalcedFoodList(daily.dinnerEntry!!)
                "snack" -> createCalcedFoodList(daily.snackEntry!!)
            }
        }



        /*CoroutineScope(Main).launch {

            fun createCalcedFoodList(list:ArrayList<MealEntry>)
            {
                if(!list.isNullOrEmpty())
                {
                    //Log.d("Smeas")
                    for(i in list)
                    {
                        calcedFoodList.add(dailyProcess.getCalcedFood(i.mealID,getAppFoodById(i.id)!!,i.menge))
                    }

                }




            }

            when(meal)
            {
                "breakfast" -> createCalcedFoodList(breakfastList!!.value!!)
                "lunch" -> createCalcedFoodList(lunchList!!.value!!)
                "dinner" -> createCalcedFoodList(dinnerList!!.value!!)
                "snack" -> createCalcedFoodList(snackList!!.value!!)
            }


            // Steht noch aus..

        }*/
        return calcedFoodList


    }


    private fun getCalcedFoodsByDaily(daily:Daily):ArrayList<CalcedFood>
    {

        // CalcedFoodList für die einzelnen meals zurückgeben...
        fun createCalcedFoodList(list:ArrayList<MealEntry>):ArrayList<CalcedFood>
        {
            var internalExport:ArrayList<CalcedFood> = ArrayList()
            if(!list.isNullOrEmpty())
            {
                //Log.d("Smeas")

                for(i in list)
                {
                    internalExport.add(dailyProcess.getCalcedFood(i.mealID,foodViewModel.getFoodById(i.id)!!,i.menge))
                }

            }
            return internalExport

        }

        var export:ArrayList<CalcedFood> = ArrayList()
        export.addAll(createCalcedFoodList(daily.breakfastEntry!!))
        export.addAll(createCalcedFoodList(daily.lunchEntry!!))
        export.addAll(createCalcedFoodList(daily.dinnerEntry!!))
        export.addAll(createCalcedFoodList(daily.snackEntry!!))

        return export



    }

}