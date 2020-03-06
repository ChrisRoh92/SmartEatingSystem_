package de.rohnert.smarteatingsystem.frontend.foodtracker.fragment

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smarteatingsystem.backend.helper.Helper
import de.rohnert.smarteatingsystem.backend.databases.daily_database.MealEntry
import de.rohnert.smarteatingsystem.backend.databases.daily_database.helper.CalcedFood
import com.google.android.material.snackbar.Snackbar
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smarteatingsystem.backend.sharedpreferences.SharedPreferencesSmeasyValues
import de.rohnert.smarteatingsystem.frontend.foodtracker.FoodViewModel2
import de.rohnert.smarteatingsystem.frontend.foodtracker.adapter.MealCardItemAdapter
import de.rohnert.smarteatingsystem.frontend.foodtracker.animations.AnimationStatusView
import de.rohnert.smarteatingsystem.frontend.foodtracker.dialogs.*
import de.rohnert.smarteatingsystem.frontend.foodtracker.foodchooser.DialogFragmentChooseHistoryMeals
import de.rohnert.smarteatingsystem.frontend.premium.dialogs.DialogFragmentPremium
import de.rohnert.smarteatingsystem.helper.dialogs.DialogAppRating
import de.rohnert.smarteatingsystem.helper.dialogs.DialogLoading
import de.rohnert.smarteatingsystem.helper.dialogs.DialogSingleChoiceList
import de.rohnert.smarteatingsystem.helper.others.WrapContentLinearLayoutManager
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.*
import kotlin.collections.ArrayList

class FoodTrackerFragment: Fragment(), View.OnClickListener{

    // Allgemeine Variablen:
    private lateinit var rootView:View
    private lateinit var foodViewModel: FoodViewModel2
    private lateinit var sharePrefs:SharedAppPreferences
    private lateinit var smeasyPrefs:SharedPreferencesSmeasyValues
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

    // TextViews of the StatusView:
    private lateinit var tvAimWeight:TextView
    private lateinit var tvAimKfa:TextView

    private lateinit var tvTitleBreakfast:TextView
    private lateinit var tvKcalBreakfast:TextView
    private lateinit var rvBreakfast:RecyclerView
    private lateinit var adapterBreakfast:MealCardItemAdapter
    private lateinit var layoutManagerBreakfast: LinearLayoutManager
    private lateinit var btnAddBreakfast:Button
    private lateinit var tvTitleLunch:TextView
    private lateinit var tvKcalLunch:TextView
    private lateinit var rvLunch:RecyclerView
    private lateinit var adapterLunch:MealCardItemAdapter
    private lateinit var layoutManagerLunch: LinearLayoutManager
    private lateinit var btnAddLunch:Button
    private lateinit var tvTitleDinner:TextView
    private lateinit var tvKcalDinner:TextView
    private lateinit var rvDinner:RecyclerView
    private lateinit var adapterDinner:MealCardItemAdapter
    private lateinit var layoutManagerDinner: LinearLayoutManager
    private lateinit var btnAddDinner:Button
    private lateinit var tvTitleSnacks:TextView
    private lateinit var tvKcalSnacks:TextView
    private lateinit var rvSnacks:RecyclerView
    private lateinit var adapterSnacks: MealCardItemAdapter
    private lateinit var layoutManagerSnacks: LinearLayoutManager
    private lateinit var btnAddSnacks:Button

    // PremiumCard
    private lateinit var cardPremium:CardView
    private lateinit var btnPremium:Button

    ////////////////////////////////////////
    // Calendar Button:
    private lateinit var btnCalendar: Button

    // View Listen für StatusView
    private lateinit var pbList:ArrayList<ProgressBar>
    private lateinit var tvList:ArrayList<TextView>

    // Loading Dialog
    private lateinit var dialogLoader:DialogLoading

    // Rating
    private lateinit var dialogRating:DialogAppRating
    private var rateCountLimit = 60
    private var dayCountLimit = 60

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        started = false
        Log.d("Smeasy","FoodTrackerFragment - onCreate - time: ${System.currentTimeMillis()}")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel2::class.java)
        rootView = inflater.inflate(R.layout.fragment_foodtracker, container, false)

        dialogLoader = DialogLoading(rootView.context)







        sharePrefs = SharedAppPreferences(rootView.context)
        smeasyPrefs = SharedPreferencesSmeasyValues(rootView.context)
        initPremiumCardView()
        if(!sharePrefs.appInitalStart)
        {
            // create Snackbar
            Handler().postDelayed({
                var snackbar = Snackbar.make(activity!!.findViewById(R.id.nav_host_fragment),"Willkommen zurück ${sharePrefs.userName}",Snackbar.LENGTH_LONG)
                var snackView = snackbar.view
                var snackTv:TextView = snackView.findViewById(com.google.android.material.R.id.snackbar_text)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    snackTv.textAlignment = View.TEXT_ALIGNMENT_CENTER
                } else {
                    snackTv.gravity = Gravity.CENTER_HORIZONTAL
                }
                snackbar.show()
                sharePrefs.setNewAppInitialStart(true)
            },1000)
            Handler().postDelayed({
                initRatingDialog()
            },1000)

        }



        Handler().postDelayed({
            initCalendar()
            initMealCards()
            initStatusViewObjects()
            initViewModelObserver()
            initToolbar()



        },0)


        return rootView
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Views initialisieren...
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

        /*var adapterList:ArrayList<MealCardItemAdapter> = arrayListOf(adapterBreakfast,adapterLunch,adapterDinner,adapterSnacks)
        var mealList:ArrayList<String> = arrayListOf("breakfast","lunch","dinner","snack")

        for((index,i) in adapterList.withIndex())
        {


            i.setOnLongClickListener(object : MealCardItemAdapter.OnLongClickListener
            {
                override fun setOnLongClickListener(calcedFood: CalcedFood, position: Int) {

                    var dialog = DialogMealEntry(rootView.context)
                    dialog.setOnDialogClickListener(object: DialogMealEntry.OnDialogMealEntryClickListener{
                        override fun setOnDialogClickListener(delete: Boolean) {
                            if(delete)
                            {
                                foodViewModel.removeMealEntry(MealEntry(calcedFood.id,calcedFood.f.id,calcedFood.menge),mealList[index])
                            }
                            else
                            {
                                // Neues Meal aussuchen
                                // ListDialog auswählen
                                var listDialog = DialogMealList(rootView.context)
                                listDialog.setOnDialogMealListClickListener(object:DialogMealList.OnDialogMealListClick{
                                    override fun setOnDialogMealListClickListener(value: String)
                                    {
                                        if(mealList[index] != value)
                                        {
                                            foodViewModel.changeMealEntry(MealEntry(calcedFood.id,calcedFood.f.id,calcedFood.menge),mealList[index],value)
                                            *//*viewmodel.removeMailFromDaily(MealEntry(calcedFood.food.id,calcedFood.menge),meal = mealList[index])
                                            addFood = false
                                            it.deleteItem(viewmodel.getCalcedFoodList().value!![index],pos)
                                            var pos = mealList.indexOf(value)
                                            adapterList[pos]
                                            viewmodel.addNewMealToDaily(MealEntry(calcedFood.food.id,calcedFood.menge),meal = value)
                                            addFood = false

                                            adapterList[pos].addNewItem(viewmodel.getCalcedFoodList().value!![pos])
                                            textList[index].text = "${viewmodel.getCalcedValueList().value!![pos][0]} Kcal"
                                            updateStatusView()*//*

                                        }


                                    }

                                })
                            }
                        }

                    })
                }

            })

            i.setOnClickListener(object: MealCardItemAdapter.OnClickListener{
                override fun setOnClickListener(calcedFood: CalcedFood, position: Int) {
                    //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    Toast.makeText(rootView.context,"${calcedFood.f.name} mit ${calcedFood.values[2]} g Protein",Toast.LENGTH_SHORT).show()
                }

            })
        }*/
        initMealCardItemClickListener()

        // Animationen....
        var animator = AnimationUtils.loadLayoutAnimation(rootView.context, R.anim.layout_animation_fall_down)
        var rvList:ArrayList<RecyclerView> = arrayListOf(rvBreakfast,rvLunch,rvDinner,rvSnacks)
        for(i in rvList)
        {
            i.layoutAnimation = animator
        }

    }

    private fun initMealCardItemClickListener()
    {
        var adapterList:ArrayList<MealCardItemAdapter> = arrayListOf(adapterBreakfast,adapterLunch,adapterDinner,adapterSnacks)
        var mealList:ArrayList<String> = arrayListOf("breakfast","lunch","dinner","snack")

        for((index,i) in adapterList.withIndex())
        {


            i.setOnLongClickListener(object : MealCardItemAdapter.OnLongClickListener
            {
                override fun setOnLongClickListener(calcedFood: CalcedFood, position: Int) {

                    var dialog = DialogMealEntry(rootView.context)
                    dialog.setOnDialogClickListener(object: DialogMealEntry.OnDialogMealEntryClickListener{
                        override fun setOnDialogClickListener(delete: Boolean) {
                            if(delete)
                            {
                                foodViewModel.removeMealEntry(MealEntry(calcedFood.id,calcedFood.f.id,calcedFood.menge),mealList[index])
                                undoMealEntryDelete(calcedFood.f.id,calcedFood.menge,mealList[index])
                            }
                            else
                            {
                                // Neues Meal aussuchen
                                // ListDialog auswählen
                                var listDialog = DialogMealList(rootView.context)
                                listDialog.setOnDialogMealListClickListener(object:DialogMealList.OnDialogMealListClick{
                                    override fun setOnDialogMealListClickListener(value: String)
                                    {
                                        if(mealList[index] != value)
                                        {
                                            foodViewModel.changeMealEntry(MealEntry(calcedFood.id,calcedFood.f.id,calcedFood.menge),mealList[index],value)
                                            //undoMealChange(MealEntry(calcedFood.id,calcedFood.f.id,calcedFood.menge),value,mealList[index])
                                            /*viewmodel.removeMailFromDaily(MealEntry(calcedFood.food.id,calcedFood.menge),meal = mealList[index])
                                            addFood = false
                                            it.deleteItem(viewmodel.getCalcedFoodList().value!![index],pos)
                                            var pos = mealList.indexOf(value)
                                            adapterList[pos]
                                            viewmodel.addNewMealToDaily(MealEntry(calcedFood.food.id,calcedFood.menge),meal = value)
                                            addFood = false

                                            adapterList[pos].addNewItem(viewmodel.getCalcedFoodList().value!![pos])
                                            textList[index].text = "${viewmodel.getCalcedValueList().value!![pos][0]} Kcal"
                                            updateStatusView()*/

                                        }


                                    }

                                })
                            }
                        }

                    })
                }

            })

            i.setOnClickListener(object: MealCardItemAdapter.OnClickListener{
                override fun setOnClickListener(calcedFood: CalcedFood, position: Int) {
                    //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    //Toast.makeText(rootView.context,"${calcedFood.f.name} mit ${calcedFood.values[2]} g Protein",Toast.LENGTH_SHORT).show()
                    Handler().postDelayed({
                        var dialog = DialogMealEntryChanger(foodViewModel,rootView.context,calcedFood,mealList[index])
                    },100)


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

        pbKcal.max = 3600
        pbCarb.max = 1000
        pbProtein.max = 1000
        pbFett.max = 1000




        // TextViews of the StatusView...
        tvKcalAdded = rootView.findViewById(R.id.fragment_foodtracker_tv_kcal_added)
        tvKcalActive = rootView.findViewById(R.id.fragment_foodtracker_tv_kcal_active)
        tvAimWeight = rootView.findViewById(R.id.fragment_foodtracker_tv_aim_weight)
        tvAimKfa = rootView.findViewById(R.id.fragment_foodtracker_tv_aim_kfa)

        tvCarbs = rootView.findViewById(R.id.fragment_foodtracker_tv_carb)
        tvProtein = rootView.findViewById(R.id.fragment_foodtracker_tv_protein)
        tvFett = rootView.findViewById(R.id.fragment_foodtracker_tv_fett)

        tvKcalRest = rootView.findViewById(R.id.fragment_foodtracker_tv_kcal_rest)

        tvAim = rootView.findViewById(R.id.fragment_foodtracker_tv_aim)
        tvAimProgress = rootView.findViewById(R.id.fragment_foodtracker_tv_aim_progress)

        pbList = arrayListOf(pbKcal,pbCarb,pbProtein,pbFett)
        tvList = arrayListOf(tvKcalAdded,tvKcalRest,tvCarbs,tvProtein,tvFett,tvAimWeight,tvAimKfa,tvAim)
    }

    // Toolbar:
    private fun initToolbar()
    {
        // Access to Toolbar.
        var toolbar = activity!!.toolbar
        toolbar.title = "Tagesübersicht"
        Log.d("Smeasy","FoodTrackerFragment - initToolbar - toolbarstatus = ${toolbar.menu}")
        toolbar.menu.clear()

        toolbar.inflateMenu(R.menu.menu_foodtracker)
        toolbar.setOnMenuItemClickListener(object: Toolbar.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                if(item!!.itemId == R.id.menu_foodtracker_weekreport)
                {
                    Handler().postDelayed({
                        var dialog = DialogFragmentWeekReport(foodViewModel)
                        dialog.show(fragmentManager!!,"WeekReport")
                    },100)


                }
                return true
            }

        })
        Log.d("Smeasy","FoodTrackerFragment - initToolbar - toolbarstatus = ${toolbar.menu}")
    }

    // PremiumCard Function
    private fun initPremiumCardView()
    {
        cardPremium = rootView.findViewById(R.id.foodtracker_card_premium)


        fun setCardHeight(state:Boolean)
        {
            var params = cardPremium.layoutParams

            if(state)
            {
                //params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                cardPremium.visibility = View.GONE
            }
            else
            {
                //params.height = 0
                cardPremium.visibility = View.VISIBLE
            }
            //cardPremium.requestLayout()
        }

        setCardHeight(sharePrefs.premiumStatus)
        Log.d("Smeasy","FoodTrackerFragmet - initPremiumCardView sharePrefs.premiumStatus = ${sharePrefs.premiumStatus}")



        btnPremium = rootView.findViewById(R.id.foodtracker_btn_premium)
        btnPremium.setOnClickListener {
           var dialog = DialogFragmentPremium()
            dialog.show(fragmentManager!!,"premium")

        }




    }

    // Rating Dialog Stuff:
    private fun initRatingDialog()
    {
        if(!sharePrefs.rateNeverStatus)
        {
            if(sharePrefs.lastRequestDate != "")
            {
                var diff = helper.getDaysBetweenDates(helper.getCurrentDate(),helper.getDateFromString(sharePrefs.lastRequestDate))
                if(diff > dayCountLimit || sharePrefs.rateCountAppStart > rateCountLimit)
                {
                    dialogRating = DialogAppRating(rootView.context)
                    sharePrefs.setNewRateCountAppStart(0)
                }
                else
                {
                    sharePrefs.setNewRateCountAppStart(sharePrefs.rateCountAppStart +1)
                }

            }
            else
            {
                sharePrefs.setNewLastRequestDate(helper.getStringFromDate(helper.getCurrentDate()))
            }


        }
    }

    // Undo Methoden:
    private fun undoMealEntryDelete(foodID:String,foodMenge:Float,meal:String)
    {
        // Sneakbar starten
        var snackbar = Snackbar.make(activity!!.findViewById(R.id.nav_host_fragment),"Rückgängig machen",Snackbar.LENGTH_LONG)



        snackbar.setAction("Rückgängig", object: View.OnClickListener{
            override fun onClick(v: View?) {
                foodViewModel.addNewMealEntry(foodID,foodMenge,meal)
            }

        })
        snackbar.show()
        // Click, soll das Löschen wieder rückgängig gemacht werden...
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Calendar Logik + Objekte initialisieren...
    private fun initCalendar()
    {
        btnCalendar = rootView.findViewById(R.id.foodtracker_btn_calendar)
        btnCalendar.text = foodViewModel.date
        btnCalendar.setOnClickListener {
            sharePrefs.setNewPremiumState(true)
            var dialog = DialogDatePicker(rootView.context,rootView)
            dialog.setOnDialogClickListener(object: DialogDatePicker.OnDialogClickListener
            {
                override fun setOnDialogClickListener(date: Date) {
                    if(foodViewModel.date != helper.getStringFromDate(date))
                    {
                        dialogLoader = DialogLoading(rootView.context)
                        foodViewModel.setNewDate(helper.getStringFromDate(date))
                        btnCalendar.text = helper.getStringFromDate(date)
                    }

                }

            })
        }



    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Observer
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
        Log.d("Smeasy","FoodTrackerFragment - initViewModelObserver -  started = $started")
        foodViewModel.getBreakfastEntries().observe( this, androidx.lifecycle.Observer
        {
            Log.d("Smeasy","FoodTrackerFragment - initViewModelObserver - breakfast-Observer: started = $started")
            if(!started)
            {
                Log.d("Smeasy","FoodTrackerFragment - initViewModelObserver - breakfast: !started - time: ${System.currentTimeMillis()}")
                initStatusView()
                /*mealCardAnimation("breakfast")
                mealCardAnimation("lunch")
                mealCardAnimation("dinner")
                mealCardAnimation("snack")*/
                dialogLoader.dismiss()


            }
            else
            {
                Log.d("Smeasy","FoodTrackerFragment - initViewModelObserver - breakfast-Observer: started = $started")
                updateStatusView()
                mealCardAnimation("breakfast")
                dialogLoader.dismiss()
            }
            adapterBreakfast.submitList(foodViewModel.getCalcedFoodsByMeal("breakfast"))


        })

        foodViewModel.getLunchEntries().observe( this, androidx.lifecycle.Observer
        {
            Log.d("Smeasy","FoodTrackerFragment - initViewModelObserver - lunch-Observer: started = $started")

            if(started)
            {
                Log.d("Smeasy","FoodTrackerFragment - initViewModelObserver - lunch-Observer: started = $started")
                updateStatusView()
                mealCardAnimation("lunch")
            }
            adapterLunch.submitList(foodViewModel.getCalcedFoodsByMeal("lunch"))

        })

        foodViewModel.getDinnerEntries().observe( this, androidx.lifecycle.Observer
        {
            Log.d("Smeasy","FoodTrackerFragment - initViewModelObserver - dinner-Observer: started = $started")

            if(started)
            {
                Log.d("Smeasy","FoodTrackerFragment - initViewModelObserver - dinner-Observer: started = $started")
                updateStatusView()
                mealCardAnimation("dinner")
            }
            adapterDinner.submitList(foodViewModel.getCalcedFoodsByMeal("dinner"))
        })

        foodViewModel.getSnackEntries().observe( this, androidx.lifecycle.Observer
        {
            Log.d("Smeasy","FoodTrackerFragment - initViewModelObserver - snacks-Observer: started = $started")

            if(started)
            {
                Log.d("Smeasy","FoodTrackerFragment - initViewModelObserver - snacks-Observer: started = $started")
                updateStatusView()
                mealCardAnimation("snack")
            }
            adapterSnacks.submitList(foodViewModel.getCalcedFoodsByMeal("snack"))


        })


    }


    ////////////////////////////////////////////////////////////////////////////////////////////////

    private fun initStatusView()
    {
        var progressValues:ArrayList<Float> = foodViewModel.getDailyValues()
        progressValues.add(smeasyPrefs.progressWeight)  // Wert für den Fortschritt
        progressValues.add(smeasyPrefs.progressKfa)     // Wert für den Fortschritt

        // MaxValues:
        var maxValues = foodViewModel.getDailyMaxValues()
        maxValues.add(100f)     // 100% für den Fortschritt
        maxValues.add(100f)     // 100% für den Fortschritt

        // Max-Werte für die Progressbars setzen:
        for((index,i) in pbList.withIndex())
        {
            if(index >0) i.max = 1000
            else
            {
                i.max = 3600
            }
        }

        // Statusanim starten...
        statusAnim = AnimationStatusView(rootView.context,pbList,tvList,progressValues,maxValues)
        statusAnim!!.setOnAnimationStatusViewListener(object: AnimationStatusView.OnAnimationStatusViewInitListener
        {
            override fun setOnAnimationStatusViewListener() {
                mealCardAnimation("breakfast")
                mealCardAnimation("lunch")
                mealCardAnimation("dinner")
                mealCardAnimation("snack")
                started = true
                Log.d("Smeasy","FoodTrackerFragment - statusViewAnimation - after Animation Listener:  started = $started")

            }

        })





    }

    private fun updateStatusView()
    {
        var progressValues:ArrayList<Float> = foodViewModel.getDailyValues()
        progressValues.add(smeasyPrefs.progressWeight)  // Wert für den Fortschritt
        progressValues.add(smeasyPrefs.progressKfa)     // Wert für den Fortschritt
        var maxValues = foodViewModel.getDailyMaxValues()
        statusAnim!!.setNewMaxValues(maxValues)
        statusAnim!!.animateNewValues(progressValues)
    }
    private fun mealCardAnimation(meal:String, delay:Int = 0)
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

    // Dialog aufrufen:
    private fun startTrackDialog(meal:String)
    {
        DialogFragmentFoodList(meal,foodViewModel).show(fragmentManager!!,meal)

        /*var dialog = DialogSingleChoiceList("Bitte Wählen","Wähle wie du dein Essen Tracken möchtest",
            arrayListOf("Aus Lebensmittel Liste","aus vorherigen Einträgen"),rootView.context,true,-1)

        dialog.onItemClickListener(object:DialogSingleChoiceList.OnDialogListListener{
            override fun onItemClickListener(value: String, pos: Int) {
                if(pos == 0)
                {
                    DialogFragmentFoodList(meal,foodViewModel).show(fragmentManager!!,meal)
                }
                else
                {
                    var historicDialog = DialogFragmentChooseHistoryMeals(foodViewModel,meal)
                    historicDialog.show(fragmentManager!!,"historicMeals")
                }
            }

        })*/


    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Implementierte Methoden:
    override fun onClick(p0: View?) {




        var handler = Handler()
        handler.postDelayed({
            when(p0)
            {
                btnAddBreakfast -> startTrackDialog("breakfast")
                btnAddLunch ->  startTrackDialog("lunch")
                btnAddDinner -> startTrackDialog("dinner")
                btnAddSnacks -> startTrackDialog("snack")
            }
        },125)

    }


    // Teste:
    override fun onPause() {
        super.onPause()
        //started = statusAnim != null
        Log.d("Smeasy","FoodTrackerFragment - onPause")
    }

    override fun onResume() {
        super.onResume()
        //started = statusAnim != null
        Log.d("Smeasy","FoodTrackerFragment - onResume")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        started = false
        Log.d("Smeasy","FoodTrackerFragment - onDestroyView")
    }

    override fun onStop() {
        super.onStop()
        started = true
        Log.d("Smeasy","FoodTrackerFragment - onStop")
    }


}