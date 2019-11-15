package de.rohnert.smeasy.frontend.foodtracker.fragment

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import backend.helper.Helper
import com.example.roomdatabaseexample.backend.databases.daily_database.Daily
import com.example.roomdatabaseexample.backend.databases.daily_database.MealEntry
import com.example.roomdatabaseexample.backend.databases.daily_database.helper.CalcedFood
import com.google.android.material.snackbar.Snackbar
import de.rohnert.smeasy.R
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smeasy.frontend.foodtracker.FoodViewModel
import de.rohnert.smeasy.frontend.foodtracker.FoodViewModel2
import de.rohnert.smeasy.frontend.foodtracker.adapter.MealCardItemAdapter
import de.rohnert.smeasy.frontend.foodtracker.animations.AnimationStatusView
import de.rohnert.smeasy.frontend.foodtracker.dialogs.DialogDatePicker
import de.rohnert.smeasy.frontend.foodtracker.dialogs.DialogFragmentFoodList
import de.rohnert.smeasy.helper.others.WrapContentLinearLayoutManager
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class FoodTrackerFragment: Fragment(), View.OnClickListener{

    // Allgemeine Variablen:
    private lateinit var rootView:View
    private lateinit var foodViewModel: FoodViewModel2
    private lateinit var sharePrefs:SharedAppPreferences
    private var statusAnim:AnimationStatusView? = null
    private var helper = Helper()
    private var started = false

    // StatusView:
    // ProgressViews:
    private lateinit var pbKcal: ProgressBar
    private lateinit var pbCarb: ProgressBar
    private lateinit var pbProtein: ProgressBar
    private lateinit var pbFett: ProgressBar
    // TextViews:
    private lateinit var tvCarbs: TextView
    private lateinit var tvProtein: TextView
    private lateinit var tvFett: TextView
    private lateinit var tvKcalAdded: TextView
    private lateinit var tvKcalRest: TextView
    private lateinit var tvKcalActive: TextView
    private lateinit var tvAim: TextView
    private lateinit var tvAimProgress: TextView

    // MealCards:
    // Breakfast
    private lateinit var cardBreakfast: CardView
    private lateinit var tvTitleBreakfast:TextView
    private lateinit var tvKcalBreakfast:TextView
    private lateinit var rvBreakfast:RecyclerView
    private lateinit var adapterBreakfast:MealCardItemAdapter
    private lateinit var layoutManagerBreakfast: LinearLayoutManager
    private lateinit var btnAddBreakfast:Button
    // Lunch
    private lateinit var cardLunch: CardView
    private lateinit var tvTitleLunch:TextView
    private lateinit var tvKcalLunch:TextView
    private lateinit var rvLunch:RecyclerView
    private lateinit var adapterLunch:MealCardItemAdapter
    private lateinit var layoutManagerLunch: LinearLayoutManager
    private lateinit var btnAddLunch:Button
    // Dinner
    private lateinit var cardDinner: CardView
    private lateinit var tvTitleDinner:TextView
    private lateinit var tvKcalDinner:TextView
    private lateinit var rvDinner:RecyclerView
    private lateinit var adapterDinner:MealCardItemAdapter
    private lateinit var layoutManagerDinner: LinearLayoutManager
    private lateinit var btnAddDinner:Button
    // Snacks
    private lateinit var cardSnacks: CardView
    private lateinit var tvTitleSnacks:TextView
    private lateinit var tvKcalSnacks:TextView
    private lateinit var rvSnacks:RecyclerView
    private lateinit var adapterSnacks: MealCardItemAdapter
    private lateinit var layoutManagerSnacks: LinearLayoutManager
    private lateinit var btnAddSnacks:Button

    ////////////////////////////////////////
    // Calendar Button:
    private lateinit var btnCalendar: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel2::class.java)
        rootView = inflater.inflate(R.layout.fragment_foodtracker, container, false)


        sharePrefs = SharedAppPreferences(rootView.context)
        if(!sharePrefs.appInitalStart)
        {
            // create Snackbar
            Handler().postDelayed({
                var snackbar = Snackbar.make(activity!!.findViewById(R.id.nav_host_fragment),"Willkommen zurück ${sharePrefs.userName}",Snackbar.LENGTH_LONG)
                var snackView = snackbar.view
                var snackTv:TextView = snackView!!.findViewById(com.google.android.material.R.id.snackbar_text)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    snackTv.textAlignment = View.TEXT_ALIGNMENT_CENTER
                } else {
                    snackTv.gravity = Gravity.CENTER_HORIZONTAL
                }
                snackbar.show()
                sharePrefs.setNewAppInitialStart(true)
            },1000)

        }

        Handler().postDelayed({
            initCalendar()
            initMealCards()
            initViewModelObserver()
            initStatusViewObjects()

        },150)


        return rootView
    }

    // MealCards initialisieren:
    private fun initMealCards()
    {
        // Hier werden die Views der MealCards initialisiert.....
        fun initViews()
        {
            layoutManagerBreakfast = WrapContentLinearLayoutManager(rootView.context,
                RecyclerView.VERTICAL,false)
            layoutManagerLunch = WrapContentLinearLayoutManager(rootView.context,
                RecyclerView.VERTICAL,false)
            layoutManagerDinner = WrapContentLinearLayoutManager(rootView.context,
                RecyclerView.VERTICAL,false)
            layoutManagerSnacks = WrapContentLinearLayoutManager(rootView.context,
                RecyclerView.VERTICAL,false)


            // Init all MealCards...
            // Breakfast:
            tvTitleBreakfast = rootView.findViewById(R.id.foodtracker_mealcard_breakfast_tv_title)
            tvKcalBreakfast = rootView.findViewById(R.id.foodtracker_mealcard_breakfast_tv_kcal)
            rvBreakfast = rootView.findViewById(R.id.foodtracker_mealcard_breakfast_rv)
            adapterBreakfast = MealCardItemAdapter()
            btnAddBreakfast = rootView.findViewById(R.id.foodtracker_mealcard_breakfast_btn)
            rvBreakfast.layoutManager = layoutManagerBreakfast
            rvBreakfast.adapter = adapterBreakfast

            // Lunch:
            tvTitleLunch = rootView.findViewById(R.id.foodtracker_mealcard_lunch_tv_title)
            tvKcalLunch = rootView.findViewById(R.id.foodtracker_mealcard_lunch_tv_kcal)
            rvLunch = rootView.findViewById(R.id.foodtracker_mealcard_lunch_rv)
            adapterLunch = MealCardItemAdapter()
            btnAddLunch = rootView.findViewById(R.id.foodtracker_mealcard_lunch_btn)
            rvLunch.layoutManager = layoutManagerLunch
            rvLunch.adapter = adapterLunch

            // Dinner:
            tvTitleDinner = rootView.findViewById(R.id.foodtracker_mealcard_dinner_tv_title)
            tvKcalDinner = rootView.findViewById(R.id.foodtracker_mealcard_dinner_tv_kcal)
            rvDinner = rootView.findViewById(R.id.foodtracker_mealcard_dinner_rv)
            adapterDinner = MealCardItemAdapter()
            btnAddDinner = rootView.findViewById(R.id.foodtracker_mealcard_dinner_btn)
            rvDinner.layoutManager = layoutManagerDinner
            rvDinner.adapter = adapterDinner

            // Snacks:
            tvTitleSnacks = rootView.findViewById(R.id.foodtracker_mealcard_snacks_tv_title)
            tvKcalSnacks = rootView.findViewById(R.id.foodtracker_mealcard_snacks_tv_kcal)
            rvSnacks = rootView.findViewById(R.id.foodtracker_mealcard_snacks_rv)
            adapterSnacks = MealCardItemAdapter()
            btnAddSnacks = rootView.findViewById(R.id.foodtracker_mealcard_snacks_btn)
            rvSnacks.layoutManager = layoutManagerSnacks
            rvSnacks.adapter = adapterSnacks
        }

        initViews()

        btnAddBreakfast.setOnClickListener(this)
        btnAddLunch.setOnClickListener(this)
        btnAddDinner.setOnClickListener(this)
        btnAddSnacks.setOnClickListener(this)

        var adapterList:ArrayList<MealCardItemAdapter> = arrayListOf(adapterBreakfast,adapterLunch,adapterDinner,adapterSnacks)
        var mealList:ArrayList<String> = arrayListOf("breakfast","lunch","dinner","snack")

        for((index,i) in adapterList.withIndex())
        {


            i.setOnLongClickListener(object : MealCardItemAdapter.OnLongClickListener
            {
                override fun setOnLongClickListener(calcedFood: CalcedFood, position: Int) {
                    foodViewModel.removeMealEntry(MealEntry(calcedFood.id,calcedFood.f.id,calcedFood.menge),mealList[index])
                }

            })
        }


        // Animationen....
        var animator = AnimationUtils.loadLayoutAnimation(rootView.context, R.anim.layout_animation_fall_down)
        var rvList:ArrayList<RecyclerView> = arrayListOf(rvBreakfast,rvLunch,rvDinner,rvSnacks)
        for(i in rvList)
        {
            i.layoutAnimation = animator
        }

    }

    // Calendar Logik + Objekte initialisieren...
    private fun initCalendar()
    {
        btnCalendar = rootView.findViewById(R.id.foodtracker_btn_calendar)
        btnCalendar.text = foodViewModel.date
        btnCalendar.setOnClickListener {
            var dialog = DialogDatePicker(rootView.context,rootView)
            dialog.setOnDialogClickListener(object: DialogDatePicker.OnDialogClickListener
            {
                override fun setOnDialogClickListener(date: Date) {
                    if(foodViewModel.date != helper.getStringFromDate(date))
                    {
                        foodViewModel.setNewDate(helper.getStringFromDate(date))
                        btnCalendar.text = helper.getStringFromDate(date)
                    }

                }

            })
        }



    }

    // View initialisieren:
    private fun initStatusViewObjects()
    {
        pbKcal = rootView.findViewById(R.id.fragment_foodtracker_pb_kcal)
        pbCarb = rootView.findViewById(R.id.fragment_foodtracker_pb_Kohlenhydrate)
        pbProtein = rootView.findViewById(R.id.fragment_foodtracker_pb_protein)
        pbFett = rootView.findViewById(R.id.fragment_foodtracker_pb_fett)


        tvCarbs = rootView.findViewById(R.id.fragment_foodtracker_tv_carb)
        tvProtein = rootView.findViewById(R.id.fragment_foodtracker_tv_protein)
        tvFett = rootView.findViewById(R.id.fragment_foodtracker_tv_fett)

        tvKcalAdded = rootView.findViewById(R.id.fragment_foodtracker_tv_kcal_added)
        tvKcalActive = rootView.findViewById(R.id.fragment_foodtracker_tv_kcal_active)
        tvKcalRest = rootView.findViewById(R.id.fragment_foodtracker_tv_kcal_rest)

        tvAim = rootView.findViewById(R.id.fragment_foodtracker_tv_aim)
        tvAimProgress = rootView.findViewById(R.id.fragment_foodtracker_tv_aim_progress)
    }

    // Observer:
    private fun initViewModelObserver2()
    {
        foodViewModel.getDaily().observe(this,androidx.lifecycle.Observer {
            statusViewAnimation()
            mealCardAnimation("breakfast", 500)
            mealCardAnimation("lunch",500)
            mealCardAnimation("dinner",500)
            mealCardAnimation("snacks",500)
        })
    }

    private fun initViewModelObserver()
    {


        /*foodViewModel.getDaily().observe( this, androidx.lifecycle.Observer
        {

            //Wenn App neu gestartet wird...
            // Sobald das Daily verfügbar ist...
            try{
                foodViewModel.createEntryLists()
                started = true
                statusViewAnimation()
                mealCardAnimation("breakfast", 500)
                mealCardAnimation("lunch",500)
                mealCardAnimation("dinner",500)
                mealCardAnimation("snacks",500)
            } catch (e:Exception)
            {
                Log.d("Smeasy","FoodTrackerFragment - initViewModelObserver - dailyObserver.getDaily():  foodViewModel.createEntryLists()- $e")
            }


            *//*try {
                foodViewModel.createEntryLists()
                if(!started)
                {

                    statusViewAnimation()
                    mealCardAnimation("breakfast", 500)
                    mealCardAnimation("lunch",500)
                    mealCardAnimation("dinner",500)
                    mealCardAnimation("snacks",500)
                    started = true
                }
            }
            catch (e:Exception)
            {
                Thread(Runnable {
                    Thread.sleep(1000)
                    foodViewModel.createEntryLists()
                    if(!started)
                    {

                        statusViewAnimation()
                        mealCardAnimation("breakfast", 500)
                        mealCardAnimation("lunch",500)
                        mealCardAnimation("dinner",500)
                        mealCardAnimation("snacks",500)
                        started = true
                    }
                }).start()


            }*//*

            *//*foodViewModel.createEntryLists()
            if(!started) started = true*//*

        })*/

        foodViewModel.getBreakfastEntries().observe( this, androidx.lifecycle.Observer
        {
            if(!started)
            {
                statusViewAnimation()
                mealCardAnimation("breakfast")
                mealCardAnimation("lunch")
                mealCardAnimation("dinner")
                mealCardAnimation("snack")
                started = true
            }
            else
            {
                statusViewAnimation()
                mealCardAnimation("breakfast")
            }
            adapterBreakfast.submitList(foodViewModel.getCalcedFoodsByMeal("breakfast"))


        })

        foodViewModel.getLunchEntries().observe( this, androidx.lifecycle.Observer
        {

            adapterLunch.submitList(foodViewModel.getCalcedFoodsByMeal("lunch"))
            if(started)
            {
                statusViewAnimation()
                mealCardAnimation("lunch")
            }

        })

        foodViewModel.getDinnerEntries().observe( this, androidx.lifecycle.Observer
        {

            adapterDinner.submitList(foodViewModel.getCalcedFoodsByMeal("dinner"))
            if(started)
            {
                statusViewAnimation()
                mealCardAnimation("dinner")
            }

        })

        foodViewModel.getSnackEntries().observe( this, androidx.lifecycle.Observer
        {

            adapterSnacks.submitList(foodViewModel.getCalcedFoodsByMeal("snack"))
            if(started)
            {
                statusViewAnimation()
                mealCardAnimation("snack")
            }


        })

        /*
        foodViewModel.getLunchEntries().observe( this, androidx.lifecycle.Observer
        {
            adapterLunch.submitList(foodViewModel.getCalcedFoodsByMeal("lunch"))


            if(started)
            {
                statusViewAnimation()
                mealCardAnimation("lunch")
            }

        })

        foodViewModel.getDinnerEntries().observe( this, androidx.lifecycle.Observer
        {
            adapterDinner.submitList(foodViewModel.getCalcedFoodsByMeal("dinner"))


            if(started)
            {
                statusViewAnimation()
                mealCardAnimation("dinner")
            }

        })

        foodViewModel.getSnackEntries().observe( this, androidx.lifecycle.Observer
        {
            adapterSnacks.submitList(foodViewModel.getCalcedFoodsByMeal("snack"))
            if(started)
                {
                    statusViewAnimation()
                    mealCardAnimation("snacks")
                }


        })*/

        /*foodViewModel.getDate().observe( this, androidx.lifecycle.Observer
        {


                *//*try{
                    foodViewModel.createEntryLists()


                    mealCardAnimation("breakfast", 500)
                    mealCardAnimation("lunch",500)
                    mealCardAnimation("dinner",500)
                    mealCardAnimation("snacks",500)
                    started = true
                } catch (e:Exception)
                {
                    Log.d("Smeasy","FoodTrackerFragment - initViewModelObserver - dailyObserver.getDate(): foodViewModel.createEntryLists() - $e")
                }

                try
                {
                    statusViewAnimation()
                }catch (e:Exception)
                {
                    Log.d("Smeasy","FoodTrackerFragment - initViewModelObserver - dailyObserver.getDate(): statusViewAnimation() - $e")
                }*//*






        })*/



    }

    private fun statusViewAnimation()
    {
        var progressValues:ArrayList<Float> = arrayListOf(0f,0f,0f,0f)
        try{
            progressValues = foodViewModel.getDailyValues()
        }catch(e:Exception)
        {
            Log.d("Smeasy","FoodTrackerFragment - statusViewAnimation - foodViewModel.getDailyValues() - $e")
        }



        if(statusAnim == null)
        {

            var maxValues = foodViewModel.getDailyMaxValues()
            var pbList:ArrayList<ProgressBar> = arrayListOf(pbKcal,pbCarb,pbProtein,pbFett)
            var tvList:ArrayList<TextView> = arrayListOf(tvKcalAdded,tvKcalRest,tvCarbs,tvProtein,tvFett)
            for((index,i) in pbList.withIndex())
            {
                if(index >0) i.max = 1000
                else
                {
                    i.max = 3600
                }
            }


            statusAnim = AnimationStatusView(rootView.context,pbList,tvList,progressValues,maxValues)
        }
        else
        {
            // hier werden updates gestartet.
            statusAnim!!.animateNewValues(progressValues)
        }
    }

    private fun mealCardAnimation(meal:String, delay:Int = 100)
    {

        fun animate(tv:TextView, content:String)
        {
            tv.text = content
            tv.alpha = 0f


            var alpha = PropertyValuesHolder.ofFloat(View.ALPHA,0f,1f)
            var scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X,0f,1f)
            var scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y,0f,1f)

            ObjectAnimator.ofPropertyValuesHolder(tv,alpha,scaleX,scaleY).apply {
                startDelay = delay.toLong()
                duration = 500
                interpolator = FastOutSlowInInterpolator()

            }.start()
        }

        when(meal)
        {
            "breakfast" -> animate(tvKcalBreakfast,"${helper.getFloatAsFormattedString(foodViewModel.getMealValues("breakfast")[0],"#")} Kcal")
            "lunch" -> animate(tvKcalLunch,"${helper.getFloatAsFormattedString(foodViewModel.getMealValues("lunch")[0],"#")} Kcal")
            "dinner" -> animate(tvKcalDinner,"${helper.getFloatAsFormattedString(foodViewModel.getMealValues("dinner")[0],"#")} Kcal")
            "snack" -> animate(tvKcalSnacks,"${helper.getFloatAsFormattedString(foodViewModel.getMealValues("snack")[0],"#")} Kcal")
        }
    }

    // Implementierte Methoden:
    override fun onClick(p0: View?) {




        var handler = Handler()
        handler.postDelayed(Runnable {
            when(p0)
            {
                btnAddBreakfast -> DialogFragmentFoodList("breakfast",foodViewModel).show(fragmentManager!!,"breakfast")
                btnAddLunch ->  DialogFragmentFoodList("lunch",foodViewModel).show(fragmentManager!!,"lunch")
                btnAddDinner -> DialogFragmentFoodList("dinner",foodViewModel).show(fragmentManager!!,"dinner")
                btnAddSnacks -> DialogFragmentFoodList("snack",foodViewModel).show(fragmentManager!!,"snack")
            }
        },125)

    }







}