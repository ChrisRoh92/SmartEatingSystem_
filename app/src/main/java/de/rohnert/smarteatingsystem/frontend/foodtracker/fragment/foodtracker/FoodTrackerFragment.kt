package de.rohnert.smarteatingsystem.frontend.foodtracker.fragment.foodtracker

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.backend.databases.daily_database.MealEntry
import de.rohnert.smarteatingsystem.backend.databases.daily_database.helper.CalcedFood
import de.rohnert.smarteatingsystem.backend.helper.Helper
import de.rohnert.smarteatingsystem.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smarteatingsystem.backend.sharedpreferences.SharedPreferencesSmeasyValues
import de.rohnert.smarteatingsystem.frontend.foodtracker.adapter.MealCardItemAdapter
import de.rohnert.smarteatingsystem.frontend.foodtracker.animations.AnimationStatusView
import de.rohnert.smarteatingsystem.frontend.foodtracker.dialogs.DialogMealEntry
import de.rohnert.smarteatingsystem.frontend.foodtracker.dialogs.DialogMealEntryChanger
import de.rohnert.smarteatingsystem.frontend.foodtracker.dialogs.DialogMealList
import de.rohnert.smarteatingsystem.frontend.foodtracker.viewmodel.FoodViewModel
import de.rohnert.smarteatingsystem.frontend.foodtracker.viewmodel.lunchNames
import de.rohnert.smarteatingsystem.frontend.premium.dialogs.DialogFragmentPremium
import de.rohnert.smarteatingsystem.helper.animation.*
import de.rohnert.smarteatingsystem.helper.dialogs.DialogAppRating
import de.rohnert.smarteatingsystem.helper.others.WrapContentLinearLayoutManager
import de.rohnert.smarteatingsystem.helper.views.CustomProgressBar
import kotlinx.android.synthetic.main.app_bar_main.*
import java.lang.Exception
import kotlin.math.roundToInt


class FoodTrackerFragment: Fragment(), View.OnClickListener{

    // Allgemeine Variablen:
    private lateinit var rootView:View
    private val TAG = "Smeasy2"
    private lateinit var foodViewModel: FoodViewModel
    private lateinit var sharePrefs:SharedAppPreferences
    private lateinit var smeasyPrefs:SharedPreferencesSmeasyValues

    private var helper = Helper()
    private var started = false

    // StatusView:
    // ProgressViews:
    private lateinit var pbKcal: CustomProgressBar
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
    private lateinit var tvRest: TextView
    private lateinit var tvAimProgress: TextView

    // TextViews of the StatusView:
    private lateinit var tvAimWeight:TextView
    private lateinit var tvAimKfa:TextView

    // Das hier überdenken wir auch mal lieber ;)
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
    // Liste mit den TextViews der einzelnen MealCards zur Anzeige der Kcal
    private lateinit var mealCardKcalTvList:ArrayList<TextView>

    // PremiumCard - Löschen
    private lateinit var cardPremium:CardView
    private lateinit var btnPremium:Button

    ////////////////////////////////////////
    // Calendar Button:
    private lateinit var btnCalendar: Button

    // View Listen für StatusView
    private lateinit var pbList:ArrayList<ProgressBar>
    private lateinit var tvList:ArrayList<TextView>
    private lateinit var statusTvList:ArrayList<TextView>



    // Rating
    private lateinit var dialogRating:DialogAppRating
    private var rateCountLimit = 60
    private var dayCountLimit = 60


    // Animation:
    private lateinit var statusAnim:AnimationStatusView
    private var animationRunning = false


    // DateControl
    private lateinit var tabLayout:TabLayout
    private lateinit var btnLeft:ImageButton
    private lateinit var btnRight:ImageButton
    private lateinit var tvDayDate:TextView
    private lateinit var tvWeekDate:TextView
    private lateinit var weekArray:ArrayList<String>
    private var weekNumber:Int = 0
    private var initDateControl = false

    // State übersicht:
    private var animDuration = 750L
    private var animDelay = 500L
    private var firstAnimation = true
    private var newProgress:ArrayList<Float> = ArrayList()
    private var oldProgress:ArrayList<Float> = ArrayList()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_foodtracker, container, false)
        foodViewModel = ViewModelProvider(requireActivity()).get(FoodViewModel::class.java)

        sharePrefs = SharedAppPreferences(rootView.context)
        smeasyPrefs = SharedPreferencesSmeasyValues(rootView.context)
        //initPremiumCardView()
        /*if(!sharePrefs.appInitalStart)
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

        }*/


        initMealCards()
        initStatusViewObjects()

        initToolbar()


        initDateControlCardView()

        initViewModelObserver()



        return rootView
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Views initialisieren...
    private fun initDateControlCardView()
    {
        tabLayout = rootView.findViewById(R.id.foodtracker_tablayout)
        tabLayout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(!animationRunning)
                {
                    if(!initDateControl)
                    {
                        initDateControl = true
                    }
                    else
                    {
                        Log.d(TAG,"tab?.position!! = ${tab?.position!!}")
                        foodViewModel.setNewDate(weekArray[tab?.position!!])
                        //started = false

                    }
                }


            }

        })


        // Init the Other Stuff:
        btnLeft = rootView.findViewById(R.id.foodtracker_btn_left)
        btnRight = rootView.findViewById(R.id.foodtracker_btn_right)

        btnLeft.setOnClickListener {
            foodViewModel.setNewDate(helper.getStringFromDate(helper.getDateWithOffsetDays(foodViewModel.mDate,-7)))

        }

        btnRight.setOnClickListener {
            foodViewModel.setNewDate(helper.getStringFromDate(helper.getDateWithOffsetDays(foodViewModel.mDate,+7)))

        }

        tvDayDate = rootView.findViewById(R.id.foodtracker_tv_date)
        tvWeekDate = rootView.findViewById(R.id.foodtracker_tv_week)

        // Observer For LifeData
        foodViewModel.getLiveDate().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d(TAG,"initDateControl = $initDateControl")
            tvDayDate.setTextWithAnimation(helper.getStringFromDate(it))
            weekArray = helper.getWeekListAsString(it)
            weekNumber = helper.getWeekNumberFromDate(it)
            tvWeekDate.setTextWithAnimation("${weekArray[0]} - ${weekArray.last()} (KW $weekNumber)")
            // Folgendes nur einmalig am Anfang initialisieren
            if(!initDateControl)
            {

                tabLayout.getTabAt(helper.getDayOfWeekFromDate(it))?.select()
                initDateControl = true

                //tabLayout.setScrollPosition(helper.getDayOfWeekFromDate(it),0f,true)
            }

        })















    }
    // Mealcards initialisieren
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

            mealCardKcalTvList = arrayListOf(tvKcalBreakfast,tvKcalLunch,tvKcalDinner,tvKcalSnacks)
        }

        fun initMealCardItemClickListener()
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

        initViews()

        btnAddBreakfast.setOnClickListener(this)
        btnAddLunch.setOnClickListener(this)
        btnAddDinner.setOnClickListener(this)
        btnAddSnacks.setOnClickListener(this)

        initMealCardItemClickListener()

        // Animationen....
        var animator = AnimationUtils.loadLayoutAnimation(rootView.context, R.anim.layout_animation_fall_down)
        var rvList:ArrayList<RecyclerView> = arrayListOf(rvBreakfast,rvLunch,rvDinner,rvSnacks)
        for(i in rvList)
        {
            i.layoutAnimation = animator
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

        tvRest = rootView.findViewById(R.id.fragment_foodtracker_tv_rest)
        tvAimProgress = rootView.findViewById(R.id.fragment_foodtracker_tv_aim_progress)

        pbList = arrayListOf(pbKcal,pbCarb,pbProtein,pbFett)
        tvList = arrayListOf(tvKcalAdded,tvKcalRest,tvCarbs,tvProtein,tvFett,tvAimWeight,tvAimKfa,tvRest)
        statusTvList = arrayListOf(tvKcalAdded,tvKcalRest,tvCarbs,tvProtein,tvFett)

        for((index,i) in pbList.withIndex())
        {
            if(index >0) i.max = 1000
            else
            {
                i.max = 3600
            }
        }
    }
    // Toolbar:
    private fun initToolbar()
    {
        // Access to Toolbar.
        var toolbar = activity!!.toolbar
        toolbar.title = "Tagesübersicht"
        toolbar.menu.clear()
        toolbar.inflateMenu(R.menu.menu_foodtracker)

        // Click Listener for the Toolbar
        toolbar.setOnMenuItemClickListener { item ->
            if(item!!.itemId == R.id.menu_foodtracker_weekreport)
            {
                // TODO("Normales Fragment mit den Navigation Components einbinden!")
                findNavController().navigate(R.id.action_foodtracker_foodreport)

                //DialogFragmentWeekReport(foodViewModel).show(parentFragmentManager,"WeekReport")

            }
            else if(item!!.itemId == R.id.menu_foodtracker_today)
            {
                if(foodViewModel.date != helper.getStringFromDate(helper.getCurrentDate()))
                {

                    tabLayout.getTabAt(helper.getDayOfWeekFromDate(helper.getCurrentDate()))?.select()


                }

            }

            true
        }

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


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Calendar Logik + Objekte initialisieren...
    private fun initCalendar()
    {
        /*btnCalendar = rootView.findViewById(R.id.foodtracker_btn_calendar)
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
        }*/



    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Observer
    private fun initViewModelObserver()
    {
        // Observer for the different Lists for the mealcard!
        foodViewModel.getBreakfastEntries().observe( viewLifecycleOwner, androidx.lifecycle.Observer
        {

            /*Log.d(TAG,"foodViewModel.getBreakfastEntries().observe started = $started")
            Log.d(TAG,"getBreakfastEntries().observe - current Daily = ${foodViewModel.localDaily}")
            if(started)
            {
                updateStatusView()
                mealCardAnimation(lunchNames[0])

            }*/

            foodViewModel.getCalcedFoodsByMeal("breakfast") {

                if(!started)
                {
                    adapterBreakfast.submitList(it)
                    mealCardAnimation(lunchNames[0])
                }
                else if(started)
                {
                    adapterBreakfast.submitList(it)
                    mealCardAnimation(lunchNames[0])
                    Log.d(TAG,"FoodTrackerFragment - getBreakfastEntries().observe - started = $started")
                    updateStatusView{

                    }

                }




            }





        })

        foodViewModel.getLunchEntries().observe( viewLifecycleOwner, androidx.lifecycle.Observer
        {

            foodViewModel.getCalcedFoodsByMeal("lunch") {
                Log.d(TAG,"getLunchEntries().observe - foodViewModel.getCalcedFoodsByMeal(\"breakfast\") = $it")
                if (!started)
                {
                    adapterLunch.submitList(it)
                    mealCardAnimation(lunchNames[1])
                }

                if(started)
                    Log.d(TAG,"FoodTrackerFragment - getLunchEntries().observe - started = $started")
                    adapterLunch.submitList(it)
                    mealCardAnimation(lunchNames[1])
                    updateStatusView {

                    }
            }



        })

        foodViewModel.getDinnerEntries().observe( viewLifecycleOwner, androidx.lifecycle.Observer
        {

            foodViewModel.getCalcedFoodsByMeal("dinner") {
                Log.d(TAG,"getDinnerEntries().observe - foodViewModel.getCalcedFoodsByMeal(\"breakfast\") = $it")
                adapterDinner.submitList(it)
                mealCardAnimation(lunchNames[2])
                if(started)
                    Log.d(TAG,"FoodTrackerFragment - getDinnerEntries().observe - started = $started")
                    updateStatusView({})
            }


        })

        foodViewModel.getSnackEntries().observe( viewLifecycleOwner, androidx.lifecycle.Observer
        {

            foodViewModel.getCalcedFoodsByMeal("snack") {
                Log.d(TAG,"getSnackEntries().observe - foodViewModel.getCalcedFoodsByMeal(\"breakfast\") = $it")
                adapterSnacks.submitList(it)
                mealCardAnimation(lunchNames[3])
                if(started)
                    Log.d(TAG,"FoodTrackerFragment - getSnackEntries().observe - started = $started")
                    updateStatusView({})
            }

        })

        // Observer for all Lists
        foodViewModel.getMeallistReady().observe(viewLifecycleOwner, Observer
        {
            Log.d(TAG,"foodViewModel.getMeallistReady().observe")
            if(!started)
            {
                initStatusView {}


            }



        })


    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // TODO("Animation für alle Views überarbeiten!")
    // Methode initialisiert die StatusCard
    private fun initStatusView(callback: () -> Unit)
    {
        fun initAnimation()
        {
            Log.d(TAG,"FoodTrackerFragment - initStatusView - initAnimation() @${System.currentTimeMillis()}")
            // Get the max daily values from FoodViewModel:
            foodViewModel.getDailyMaxValues{maxValues ->
                maxValues.add(100f)     // 100% für den Fortschritt
                maxValues.add(100f)     // 100% für den Fortschritt

                Log.d(TAG,"Max-Values = $maxValues")
                Log.d(TAG,"newProgress = $newProgress")

                // TODO("Das folgende muss in jedem Fall in das FoodViewModeL!")
                // Nur für Testzwecke hier:
                val statusTextContent:ArrayList<String> = ArrayList()
                statusTextContent.add("${helper.getFloatAsFormattedString(newProgress[0],"#")} Kcal")
                statusTextContent.add("${helper.getFloatAsFormattedString(maxValues[0]-newProgress[0],"#")} Kcal")
                statusTextContent.add("${helper.getFloatAsFormattedString(newProgress[1])} g / ${maxValues[1]} g")
                statusTextContent.add("${helper.getFloatAsFormattedString(newProgress[2])} g / ${maxValues[2]} g")
                statusTextContent.add("${helper.getFloatAsFormattedString(newProgress[3])} g / ${maxValues[3]} g")
                statusTextContent.add("${helper.getFloatAsFormattedString(newProgress[4])} %")
                statusTextContent.add("${helper.getFloatAsFormattedString(newProgress[5])} %")
                statusTextContent.add("${helper.getFloatAsFormattedString(maxValues[0])} Kcal")



                // Tablayout sperren, solange die Animation läuft!
                tabLayout.disable()

                // Wenn App das 1.mal gestartet wird, soll folgende Animation ablaufen
                if(firstAnimation)
                {
                    // Alle TextViews im StatusView auf Alpha = 0f und TranslationX = -50f setzen
                    tvList.forEach { tv ->
                        tv.alpha = 0f
                        tv.translationX = -50f
                    }

                    for((index,i) in pbList.withIndex())
                    {

                        val toValue = ((newProgress[index]/maxValues[index])*i.max)
                        i.animateInit(0, toValue.roundToInt(),{
                            oldProgress[index] = newProgress[index]
                            if(index == pbList.lastIndex){
                                started = true
                                for((jIndex,j) in tvList.withIndex())
                                {
                                    j.show(statusTextContent[jIndex],{},300,0)
                                }

                                tabLayout.enable()
                                callback()
                                started = true


                            }
                        },animDuration,animDelay)
                    }
                    firstAnimation = false
                }
                // Sonst die Animation verwenden, bei dem nur
                else
                {
                    tvList.forEach { tv ->
                        tv.hide({},300,0) }
                    for((index,i) in pbList.withIndex())
                    {
                        val toValue = (newProgress[index]/maxValues[index])*i.max
                        i.animateToValueWithMax(oldProgress[index].roundToInt(),toValue.roundToInt(),{
                            oldProgress[index] = newProgress[index]
                            if(index == pbList.lastIndex)
                            {
                                started = true
                                for((jIndex,j) in tvList.withIndex())
                                {
                                    j.show(statusTextContent[jIndex],{},300,0)
                                }

                                tabLayout.enable()
                                callback()

                            }
                        },animDuration,animDelay)
                    }
                }




            }

        }

        foodViewModel.getCalcedFoodOfDay{dailyValues ->
            newProgress = dailyValues
            newProgress.add(smeasyPrefs.progressWeight)  // Wert für den Fortschritt
            newProgress.add(smeasyPrefs.progressKfa)     // Wert für den Fortschritt
            // Sofern oldProgress noch leer ist, füllen!
            if(oldProgress.isEmpty())
            {
                for(i in newProgress)
                    oldProgress.add(0f)
            }
            initAnimation()

        }







    }
    // Methode aktualisiert Werte in der StatusCard
    private fun updateStatusView(callback:()->Unit)
    {
        if(!animationRunning && started)
        {
            animationRunning = true
            var progressValues: ArrayList<Float>
            Log.d(TAG,"FoodTrackerFragment - updateStatusView() @${System.currentTimeMillis()} - started = $started")
            foodViewModel.getCalcedFoodOfDay{
                progressValues = it
                progressValues.add(smeasyPrefs.progressWeight)  // Wert für den Fortschritt
                progressValues.add(smeasyPrefs.progressKfa)     // Wert für den Fortschritt
                newProgress = progressValues
                Log.d(TAG,"FoodTracker - updateStatusView new ProgressValues = $progressValues")
                foodViewModel.getDailyMaxValues{maxValues ->
                    maxValues.add(100f)     // 100% für den Fortschritt
                    maxValues.add(100f)     // 100% für den Fortschritt

                    // TODO("Das folgende muss in jedem Fall in das FoodViewModeL!")
                    // Nur für Testzwecke hier:
                    val statusTextContent:ArrayList<String> = ArrayList()
                    statusTextContent.add("${helper.getFloatAsFormattedString(newProgress[0],"#")} Kcal")
                    statusTextContent.add("${helper.getFloatAsFormattedString(maxValues[0]-newProgress[0],"#")} Kcal")
                    statusTextContent.add("${helper.getFloatAsFormattedString(newProgress[1])} g / ${maxValues[1]} g")
                    statusTextContent.add("${helper.getFloatAsFormattedString(newProgress[2])} g / ${maxValues[2]} g")
                    statusTextContent.add("${helper.getFloatAsFormattedString(newProgress[3])} g / ${maxValues[3]} g")
                    statusTextContent.add("${helper.getFloatAsFormattedString(newProgress[4])} %")
                    statusTextContent.add("${helper.getFloatAsFormattedString(newProgress[5])} %")
                    statusTextContent.add("${helper.getFloatAsFormattedString(maxValues[0])} Kcal")

                    tabLayout.disable()
                    Log.d(TAG,"FoodTracker - newProgress = $newProgress")

                    tvList.forEach { tv ->
                        tv.hide({},300,0) }
                    for((index,i) in pbList.withIndex())
                    {
                        val toValue = (newProgress[index]/maxValues[index])*i.max
                        Log.d(TAG,"FoodTracker - updateStatusView() - maxValues = ${maxValues[0]} with Index = $index")
                        Log.d(TAG,"FoodTracker - updateStatusView() - newProgress = ${newProgress[index]} with Index = $index")
                        Log.d(TAG,"FoodTracker - updateStatusView() - i.max = ${i.max} with Index = $index")
                        Log.d(TAG,"FoodTracker - updateStatusView() - toValue = $toValue with Index = $index")
                        i.animateToValueWithMax(oldProgress[index].roundToInt(),toValue.roundToInt(),{
                            oldProgress[index] = newProgress[index]
                            if(index == pbList.lastIndex)
                            {
                                //started = true
                                for((jIndex,j) in tvList.withIndex())
                                {
                                    j.show(statusTextContent[jIndex],{},300,0)
                                }

                                tabLayout.enable()
                                callback()
                                animationRunning = false

                            }
                        },animDuration,animDelay)
                    }


                }


            }
        }



    }
    // Methode animiert die Kcal Anzeige von einer bestimmten MealCard
    private fun mealCardAnimation(meal:String, delay:Int = 0)
    {

        fun animate(tv:TextView, content:String)
        {
            tv.setTextWithAnimation(content)
        }

        foodViewModel.getMealValues(meal){
            Log.d(TAG,"lunchNames.indexOf(meal) with meal = $meal and index = ${lunchNames.indexOf(meal)}")
            val tv = mealCardKcalTvList[lunchNames.indexOf(meal)]
            animate(tv,"${helper.getFloatAsFormattedString(it[0],"#")} Kcal")
        }



    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Utils
    private fun TabLayout.disable()
    {

        val tabStrip = this.getChildAt(0) as LinearLayout
        tabStrip.isEnabled = false
        this.background = ContextCompat.getDrawable(requireContext(),R.color.backgroundLight2)
        for (i in 0 until tabStrip.childCount) {
            tabStrip.getChildAt(i).isClickable = false

        }

    }

    private fun TabLayout.enable()
    {

        try {
            val tabStrip = this.getChildAt(0) as LinearLayout
            tabStrip.isEnabled = false
            this.background = ContextCompat.getDrawable(requireContext(),R.color.white)
            for (i in 0 until tabStrip.childCount) {
                tabStrip.getChildAt(i).isClickable = true
            }
        }catch (e:Exception)
        {
            Log.e(TAG,"FoodTrackerFragment - ${e.message}",e)
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Implementierte Methoden:
    override fun onClick(p0: View?) {

        // Methode ruft FoodChooserFragment auf und übergibt Wert für Mahlzeit
        fun startFoodChooser(meal:String)
        {
            val bundle:Bundle = Bundle().apply {
                putString("sMeal",meal)
            }
            findNavController().navigate(R.id.action_foodtracker_foodchooser,bundle)
        }

        // Unterscheidet die verschiedenen Buttons
        when(p0)
        {
            btnAddBreakfast -> startFoodChooser("breakfast")
            btnAddLunch ->  startFoodChooser("lunch")
            btnAddDinner -> startFoodChooser("dinner")
            btnAddSnacks -> startFoodChooser("snack")
        }

    }









    // Auf die Lifecycles reagieren
    override fun onDestroyView() {
        super.onDestroyView()
        started = false
        initDateControl = false
        Log.d(TAG,"FoodTrackerFragment - onDestroyView")
    }

    override fun onStop() {
        super.onStop()
        //started = true
        Log.d(TAG,"FoodTrackerFragment - onStop")
    }


}