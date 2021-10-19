package de.rohnert.smarteatingsystem.frontend.statistics.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.frontend.statistics.StatisticViewModel
import de.rohnert.smarteatingsystem.frontend.statistics.adapter.StatisticFoodTrackedFoodRecyclerAdapter
import de.rohnert.smarteatingsystem.utils.others.CustomDividerItemDecoration
import de.rohnert.smarteatingsystem.utils.standard_charts.StandardBarChart
import de.rohnert.smarteatingsystem.utils.standard_charts.StandardLineChart
import de.rohnert.smarteatingsystem.utils.standard_charts.StandardPieChart

class StatisticFoodFragment:Fragment()
{
    // Allgmeine Variablen:
    private lateinit var rootView: View

    // Content:
    private lateinit var statisticViewModel: StatisticViewModel

    // View Elemente, geordnet nach den Cards:
    // TrackedFood Cars:
    private lateinit var rvTrackedFood:RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapterTrackedFood:StatisticFoodTrackedFoodRecyclerAdapter
    // Values of Meals in PieChart:
    private lateinit var mealPieChart:StandardPieChart
    // CardView for Ø nutrition values in PieChart
    private lateinit var nutritionPieChart:StandardPieChart
    // CardView für den Verlauf der Nährstoffe
    private lateinit var nutritionCourseLineChart:StandardLineChart
    // CardView für den Verlauf der Kcal
    private lateinit var kcalBarChart:StandardBarChart




    // Views:

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_statistic_food, container, false)

        statisticViewModel = ViewModelProviders.of(this).get(StatisticViewModel::class.java)

        // Views initialisieren:

        initKcalBarChart()
        initTrackedFoodRecyclerView()
        initMealChart()
        initNutrititionPieChart()
        initNutritionCourseLineChart()

        // Observer starten:
        startObserver()






        return rootView
    }

    // Views initialisieren:
    private fun initTrackedFoodRecyclerView()
    {
        rvTrackedFood = rootView.findViewById(R.id.statistic_food_rv)
        layoutManager = LinearLayoutManager(rootView.context,RecyclerView.VERTICAL,false)
        rvTrackedFood.layoutManager = layoutManager
        adapterTrackedFood = StatisticFoodTrackedFoodRecyclerAdapter(ArrayList())
        rvTrackedFood.adapter = adapterTrackedFood
        // DecorItem nehmen
        rvTrackedFood.addItemDecoration(
            CustomDividerItemDecoration(RecyclerView.VERTICAL, rootView.context, 0)
        )

        adapterTrackedFood.setOnRecyclerViewClickListener(object:StatisticFoodTrackedFoodRecyclerAdapter.OnRecyclerViewClickListener{
            override fun setOnRecyclerViewClickListener(pos: Int) {
                // Do Nothing in the Moment....
            }

        })
    }

    private fun initMealChart()
    {
        var id = R.id.statistic_food_chart_pie_meal
        var pieValues:ArrayList<PieEntry> = ArrayList()
        var values = arrayListOf(0f,0f,0f,0f)
        pieValues.add(PieEntry(values[0],"Frühstück"))
        pieValues.add(PieEntry(values[1],"Mittagessen"))
        pieValues.add(PieEntry(values[2],"Abendessen"))
        pieValues.add(PieEntry(values[3],"Snacks"))
        mealPieChart = StandardPieChart(id,rootView,pieValues)


    }

    private fun initNutrititionPieChart()
    {
        var id = R.id.statistic_food_chart_pie_nutrition
        var pieValues:ArrayList<PieEntry> = ArrayList()
        var values = arrayListOf(0f,0f,0f)
        pieValues.add(PieEntry(values[0],"Kohlenhydrate"))
        pieValues.add(PieEntry(values[1],"Protein"))
        pieValues.add(PieEntry(values[2],"Fett"))
        nutritionPieChart = StandardPieChart(id,rootView,pieValues)

    }

    private fun initNutritionCourseLineChart()
    {
        var id = R.id.statistic_food_chart_line_nutrition
        var values = statisticViewModel.getNutritionCourseData()
        var chartData:ArrayList<ArrayList<Entry>> = ArrayList()
        for((index,i) in values.withIndex())
        {
            chartData.add(ArrayList())
            for((indexJ,j) in i.withIndex())
            {
                chartData[index].add(Entry(indexJ.toFloat(),j))
            }
        }
        fun getLabels():ArrayList<String>
        {
            var export:ArrayList<String> = ArrayList()
            export.add("Kohlenhydrate")
            export.add("Protein")
            export.add("Fett")
            export.add("100 %")

            var values:ArrayList<String> = ArrayList()
            for((index,i) in chartData.withIndex())
            {
                values.add(export[index])
            }

            return values


        }

        nutritionCourseLineChart = StandardLineChart(id,rootView,chartData,getLabels())
    }

    private fun initKcalBarChart()
    {
        var id = R.id.statistic_food_chart_kcal
        var values = statisticViewModel.getKcalValues()
        var chartValues:ArrayList<BarEntry> = ArrayList()
        for(i in 1..7)
        {
            chartValues.add(BarEntry(i.toFloat(),0f))
        }
        for((index,i) in values.withIndex())
        {
            chartValues.add(BarEntry(index.toFloat(),i))
        }
        kcalBarChart = StandardBarChart(id,rootView,chartValues)


    }



    // Observer

    private fun startObserver()
    {
        /*statisticViewModel.getFoodListAvailable().observe(viewLifecycleOwner, Observer {
            fun createFantasyTrackedFoodList()
            {
                var testList:ArrayList<TrackedFood> = ArrayList()
                for(i in 0..19)
                {
                    testList.add(TrackedFood(statisticViewModel.getFoodAtPosition(i),1450*(35-i).toFloat(),ArrayList()))
                }
                adapterTrackedFood.updateContent(testList)
            }

            createFantasyTrackedFoodList()
            Log.d("Smeasy","StatisticFoodFragment - startObserver - FoodListAvailable - size of foodList: ${statisticViewModel.getFoodList().size}")
        })*/

        statisticViewModel.getStatisticDataAvailable().observe(viewLifecycleOwner, Observer {
            updateTrackedFoodRecyclerView()
            updateKcalBarChart()
            updateMealChart()
            updateNutritionPieChart()
            updateNutritionCourseLineChart()




        })
    }


    // Update Funktionen:
    private fun updateKcalBarChart()
    {
        var values = statisticViewModel.getKcalValues()
        var chartValues:ArrayList<BarEntry> = ArrayList()
        for((index,i) in values.withIndex())
        {
            chartValues.add(BarEntry(index.toFloat(),i))
        }
        kcalBarChart.updateBarData(chartValues)
    }

    private fun updateTrackedFoodRecyclerView()
    {
        adapterTrackedFood.updateContent(statisticViewModel.getTop20Foods())
    }

    private fun updateMealChart()
    {
        var pieValues:ArrayList<PieEntry> = ArrayList()
        var values = statisticViewModel.getMealValues()
        pieValues.add(PieEntry(values[0],"Frühstück"))
        pieValues.add(PieEntry(values[1],"Mittagessen"))
        pieValues.add(PieEntry(values[2],"Abendessen"))
        pieValues.add(PieEntry(values[3],"Snacks"))
        mealPieChart.updatePieChart(pieValues)
    }

    private fun updateNutritionPieChart()
    {
        var pieValues:ArrayList<PieEntry> = ArrayList()
        var values = statisticViewModel.getNutritionValues()
        pieValues.add(PieEntry(values[0],"Kohlenhydrate"))
        pieValues.add(PieEntry(values[1],"Protein"))
        pieValues.add(PieEntry(values[2],"Fett"))
        nutritionPieChart.updatePieChart(pieValues)
    }

    private fun updateNutritionCourseLineChart()
    {
        // Muss noch implementiert werden...
        var values = statisticViewModel.getNutritionCourseValues()
        Log.d("Smeasy","StatisticFoodFragment - updateNutritionCourseLineChart values: $values")
        var chartData:ArrayList<ArrayList<Entry>> = ArrayList()
        for((index,i) in values.withIndex())
        {
            chartData.add(ArrayList())
            for((indexJ,j) in i.withIndex())
            {
                chartData[index].add(Entry(indexJ.toFloat(),j))
            }
        }

//        chartData = ArrayList(chartData.asReversed())

        nutritionCourseLineChart.updateLineChart(chartData)


    }
}