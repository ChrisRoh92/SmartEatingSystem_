package de.rohnert.smarteatingsystem.frontend.bodytracker.fragment

import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smarteatingsystem.backend.helper.Helper
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smarteatingsystem.frontend.bodytracker.BodyViewModel
import de.rohnert.smarteatingsystem.frontend.bodytracker.adapter.AllowedFoodRecyclerAdapter
import de.rohnert.smarteatingsystem.frontend.bodytracker.adapter.BodyAimRecyclerAdapter
import de.rohnert.smarteatingsystem.frontend.bodytracker.dialogs.DialogAllowedFood
import de.rohnert.smarteatingsystem.frontend.bodytracker.dialogs.DialogBodySettingsAim
import de.rohnert.smarteatingsystem.frontend.bodytracker.dialogs.DialogNutrition
import de.rohnert.smarteatingsystem.frontend.premium.dialogs.DialogFragmentPremium
import de.rohnert.smarteatingsystem.utils.dialogs.DialogSingleLineInput
import de.rohnert.smarteatingsystem.utils.others.CustomDividerItemDecoration
import kotlin.math.roundToInt

class BodySettingFragment: Fragment()
{

    // Allgemeine Variablen:
    private lateinit var rootView: View
    private lateinit var bodyViewModel: BodyViewModel
    private lateinit var prefs:SharedAppPreferences
    private var helper = Helper()

    // View Ids:
    private var idTvNutritionList:ArrayList<Int> =
        arrayListOf(
            R.id.bodysettings_nutrition_tv_maxkcal,
            R.id.bodysettings_nutrition_tv_maxcarbs_percent,
            R.id.bodysettings_nutrition_tv_maxprotein_percent,
            R.id.bodysettings_nutrition_tv_maxfett_percent,
            R.id.bodysettings_nutrition_tv_maxcarbs,
            R.id.bodysettings_nutrition_tv_maxprotein,
            R.id.bodysettings_nutrition_tv_maxfett)
    private var idPbNutritionList:ArrayList<Int> =
        arrayListOf(
            R.id.bodysettings_nutrition_pb_carbs,
            R.id.bodysettings_nutrition_pb_protein,
            R.id.bodysettings_nutrition_pb_fett)


    // View Elemente:
    //TextViews:
    private var tvNutritionList:ArrayList<TextView> = ArrayList()
    private var pbNutritionList:ArrayList<ProgressBar> = ArrayList()
    private lateinit var tvAim:TextView
    private lateinit var tvAimWeight:TextView


    // BodyAimList
    private lateinit var rvBodyAim:RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var bodyAimAdapter: BodyAimRecyclerAdapter
    private lateinit var rvContent:ArrayList<String>

    // Allowed Foods
    private lateinit var cardAllowedFood: CardView
    private lateinit var rvAllowedFood:RecyclerView
    private lateinit var layoutManagerAllowedFood:LinearLayoutManager
    private lateinit var allowedFoodAdapter: AllowedFoodRecyclerAdapter
    private lateinit var rvAllowedFoodContent:ArrayList<String>
    private var units:ArrayList<String> = arrayListOf("kcal","g","g","g")
    private lateinit var tvTitle:TextView
    private lateinit var tvSubTitle:TextView
    private lateinit var divider:View

    // Buttons:
    private lateinit var btnNutrition: Button
    private lateinit var btnAim:Button
    private lateinit var btnAllowedFood:Button
    //private lateinit var btnBodyAim:Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bodyViewModel = ViewModelProviders.of(requireActivity()).get(BodyViewModel::class.java)
        rootView = inflater.inflate(R.layout.fragment_bodytracker_bodysettings, container, false)
        prefs = SharedAppPreferences(rootView.context)

        //
        Handler().postDelayed({
            initViews()
        },500)



        return rootView
    }

    private fun initViews()
    {

        fun initNutritionCard()
        {
            // TextViews:
            for(i in idTvNutritionList)
            {
                tvNutritionList.add(rootView.findViewById(i))
            }
            // ProgressBar:
            for(i in idPbNutritionList)
            {
                pbNutritionList.add(rootView.findViewById(i))
            }

            updateNutritionCard()

            btnNutrition = rootView.findViewById(R.id.bodysettings_btn_nutrition)
            btnNutrition.setOnClickListener{
                var dialog = DialogNutrition(rootView.context,bodyViewModel)
                dialog.setOnDialogNutritionListener(object: DialogNutrition.OnDialogNutritionListener{
                    override fun setOnDialogNutritionListener() {
                        Log.d("Smeasy","BodySettingsFragment - DialogNutritionListener...")
                        updateNutritionCard()
                    }

                })
        }


        }

        fun initAimCard()
        {
            tvAim = rootView.findViewById(R.id.bodysettings_aim_tv)
            tvAim.text = prefs.aim
            tvAimWeight = rootView.findViewById(R.id.bodysettings_aim_tv_weight)
            tvAimWeight.text = "${helper.getFloatAsFormattedStringWithPattern(prefs.aimWeightLoss,"#.#")} kg pro Woche"
            btnAim = rootView.findViewById(R.id.bodysettings_aim_btn)
            btnAim.setOnClickListener {
                /*var dialog = DialogSingleList("Ziel","Wähle dein Zuel aus", arrayListOf("Abnehmen","Gewicht Halten","Aufbauen"),rootView.context)
                dialog.onItemClickListener(object : DialogSingleList.OnDialogListListener{
                    override fun onItemClickListener(value: String, pos: Int) {
                        tvAim.text = value
                        prefs.setNewAim(value)
                    }

                })*/
                var dialog = DialogBodySettingsAim(rootView.context,prefs)
                dialog.setOnDialogBodySettingsAimListener(object:DialogBodySettingsAim.OnDialogBodySettingsAimListener{
                    override fun setOnDialogBodySettingsAimListener(aim: String, value: Float) {
                        var newValues = false
                        var oldValue = prefs.aimWeightLoss

                        if(aim != prefs.aim)
                        {
                            prefs.setNewAim(aim)
                            newValues = true
                            tvAim.text = prefs.aim
                        }
                        if(value != prefs.aimWeightLoss)
                        {
                            prefs.setNewAimWeightLoss(value)
                            newValues = true
                            tvAimWeight.text = "${helper.getFloatAsFormattedStringWithPattern(value,"#.##")} kg pro Woche"
                            var newKcal = prefs.maxKcal + (1000f*(value-oldValue))
                            prefs.setNewMaxKcal(newKcal)
                        }
                        if(newValues)
                        {
                            updateNutritionCard()
                        }

                    }

                })
            }
        }

        fun initAllowedFoodCard()
        {

            // Init Views:
            cardAllowedFood = rootView.findViewById(R.id.bodytracker_settings_card_allowedfood)
            btnAllowedFood = rootView.findViewById(R.id.bodysettings_allowed_food_btn)
            tvTitle = rootView.findViewById(R.id.bodysettings_allowed_food_tv_title)
            tvSubTitle = rootView.findViewById(R.id.bodysettings_allowed_food_tv_subtitle)
            divider = rootView.findViewById(R.id.bodysettings_allowed_food_divider)

            // RecyclerViewStuff:
            rvAllowedFood = rootView.findViewById(R.id.bodysettings_allowed_food_rv)
            layoutManagerAllowedFood = LinearLayoutManager(rootView.context,RecyclerView.VERTICAL,false)
            rvAllowedFoodContent = getAllowedFoodContent()
            allowedFoodAdapter = AllowedFoodRecyclerAdapter(rvAllowedFoodContent,prefs.premiumStatus)
            rvAllowedFood.layoutManager = layoutManagerAllowedFood
            rvAllowedFood.adapter = allowedFoodAdapter


            btnAllowedFood.setOnLongClickListener {
                cardAllowedFood.setCardBackgroundColor(ContextCompat.getColor(rootView.context,android.R.color.white))
                btnAllowedFood.visibility = View.GONE
                allowedFoodAdapter.updateContent(rvAllowedFoodContent,prefs.premiumStatus)
                true
            }

            if(prefs.premiumStatus)
            {
                cardAllowedFood.setCardBackgroundColor(ContextCompat.getColor(rootView.context,android.R.color.white))
                btnAllowedFood.visibility = View.GONE
                divider.visibility = View.GONE
                tvTitle.setTextColor(ContextCompat.getColor(rootView.context,R.color.textColor1))
                tvSubTitle.setTextColor(ContextCompat.getColor(rootView.context,android.R.color.tab_indicator_text))
                cardAllowedFood.setPadding(0,0,0,8)
            }
            else
            {
                cardAllowedFood.setCardBackgroundColor(ContextCompat.getColor(rootView.context,R.color.background))
                btnAllowedFood.setOnClickListener {startPremiumDialog()
                }

            }




            rvAllowedFood.addItemDecoration(
                CustomDividerItemDecoration(RecyclerView.VERTICAL, rootView.context, 0))
            allowedFoodAdapter.setOnItemClickListener(object:AllowedFoodRecyclerAdapter.OnItemClickListener{
                override fun setOnItemClickListener(pos: Int) {
                    if(prefs.premiumStatus)
                    {
                        var dialog = DialogAllowedFood(rootView.context,prefs.getMinAllowedFoodValues()[pos],prefs.getMaxAllowedFoodValues()[pos],units[pos],pos)
                        dialog.setOnDialogButtonClickListener(object: DialogAllowedFood.OnDialogButtonClickListener{
                            override fun setOnDialogButtonClickListener(min: Float, max: Float) {
                                setNewAllowedFoodValues(pos,min,max)
                            }

                        })
                    }
                    else
                    {
                        startPremiumDialog()
                    }

                }

            })


            // Button...



        }


        fun initBodyAimCard()
        {

            rvBodyAim = rootView.findViewById(R.id.bodysettings_bodyaim_rv)
            layoutManager = LinearLayoutManager(rootView.context,RecyclerView.VERTICAL,false)
            rvBodyAim.layoutManager = layoutManager

            var bodyAimList:ArrayList<Float> = ArrayList()
            bodyAimList.add(prefs.weightAim)
            bodyAimList.add(prefs.kfaAim)
            bodyAimList.add(prefs.bmiAim)

            bodyAimList.add(prefs.bauchAim)
            bodyAimList.add(prefs.brustAim)
            bodyAimList.add(prefs.halsAim)
            bodyAimList.add(prefs.huefteAim)

            var unitList:ArrayList<String> = arrayListOf(" kg"," %",""," cm"," cm"," cm"," cm")

            // Daten für RecyclerView abholen:
            rvContent= ArrayList()
            for((index,i) in bodyAimList.withIndex())
            {
                if(i != -1f)
                {
                    rvContent.add(helper.getFloatAsFormattedStringWithPattern(i,"#.##") +unitList[index])
                }
                else
                {
                    rvContent.add("Keine Angabe gemacht")
                }

            }



            bodyAimAdapter = BodyAimRecyclerAdapter(rvContent)
            rvBodyAim.adapter = bodyAimAdapter
            rvBodyAim.addItemDecoration(
                CustomDividerItemDecoration(RecyclerView.VERTICAL, rootView.context, 0)
            )

            var units:ArrayList<String> = arrayListOf(" kg"," %","","cm","cm","cm","cm")
            var dialogTitles:ArrayList<String> =
                arrayListOf(
                    "Zielgewicht angeben",
                    "Ziel Körperfettanteil angeben",
                    "Ziel BodyMassIndex angeben",
                    "Ziel Bauchumfang angeben",
                    "Ziel Brustumfang angeben",
                    "Ziel Halsumfang angeben",
                    "Ziel Hüftumfang angeben")
            var dialogSubTitles:ArrayList<String> =
                arrayListOf(
                    "in kg",
                    "in %",
                    "",
                    "in cm",
                    "in cm",
                    "in cm",
                    "in cm")
            var bodyAimValue:ArrayList<Float> = ArrayList()
            bodyAimValue.add(prefs.weightAim)
            bodyAimValue.add(prefs.kfaAim)
            bodyAimValue.add(prefs.bmiAim)
            bodyAimValue.add(prefs.bauchAim)
            bodyAimValue.add(prefs.brustAim)
            bodyAimValue.add(prefs.halsAim)
            bodyAimValue.add(prefs.huefteAim)


            bodyAimAdapter.setOnItemClickListener(object:BodyAimRecyclerAdapter.OnItemClickListener{
                override fun setOnItemClickListener(pos: Int) {
                    if(pos != 2)
                    {
                        var inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
                        var value = bodyAimValue[pos]
                        if(value == -1f)
                        {
                            value = 0f
                        }
                        var dialog = DialogSingleLineInput(dialogTitles[pos],dialogSubTitles[pos],rootView.context,inputType,helper.getFloatAsFormattedStringWithPattern(value,"#.##"))
                        dialog.onDialogClickListener(object:DialogSingleLineInput.OnDialogListener{
                            override fun onDialogClickListener(export: String) {

                            }

                            override fun onDialogClickListener(export: Float) {
                                if(export != 0f)
                                {
                                    setNewAimValue(pos,export)
                                }
                                else
                                {
                                    Toast.makeText(rootView.context,"Es wurde kein Wert übernommen",Toast.LENGTH_SHORT).show()
                                }

                            }

                        })
                    }
                    else
                    {
                        Toast.makeText(rootView.context,"Der BMI wird automatisch aus deinem Zielgewicht und deiner Körpergröße berechnet",Toast.LENGTH_SHORT).show()
                    }


                }

            })


            //updateBodyAimCard()

            /*btnBodyAim = rootView.findViewById(R.id.bodysettings_bodyaim_btn)
            btnBodyAim.setOnClickListener {
                var dialog = DialogBodyAim()
                dialog.show(fragmentManager!!,"BodyAim")
                dialog.setOnDialogNutritionListener(object:DialogBodyAim.OnDialogBodyAimListener{
                    override fun setOnDialogBodyAimListener() {
                        updateBodyAimCard()
                    }

                })
            }*/


        }

        initNutritionCard()
        initAimCard()
        initAllowedFoodCard()
        initBodyAimCard()
    }

    // NutritionCard:
    private fun updateNutritionCard()
    {
        prefs = SharedAppPreferences(rootView.context)
        fun Int.calcValues(maxKcal:Int, k:Float):Int
        {
            return (((this/100f) * maxKcal)/k).roundToInt()
        }

        var maxKcal = prefs.maxKcal.roundToInt()
        var maxValues:ArrayList<Int> = arrayListOf(prefs.maxCarb.roundToInt(),prefs.maxProtein.roundToInt(),prefs.maxFett.roundToInt())
        var maxCalcValues:ArrayList<Int> = ArrayList()
        maxCalcValues.add(maxValues[0].calcValues(maxKcal,4.1f))
        maxCalcValues.add(maxValues[1].calcValues(maxKcal,4.1f))
        maxCalcValues.add(maxValues[2].calcValues(maxKcal,9.2f))

        var printValues:ArrayList<Int> = arrayListOf(maxKcal)
        printValues.addAll(maxValues)
        printValues.addAll(maxCalcValues)
        tvNutritionList[0].text = "${printValues[0]} kcal"
        tvNutritionList[1].text = "${printValues[1]} %"
        tvNutritionList[2].text = "${printValues[2]} %"
        tvNutritionList[3].text = "${printValues[3]} %"
        tvNutritionList[4].text = "${printValues[4]} g"
        tvNutritionList[5].text = "${printValues[5]} g"
        tvNutritionList[6].text = "${printValues[6]} g"

        for((index,i) in pbNutritionList.withIndex())
        {
            i.progress = maxValues[index]
        }

    }


    // PremiumDialog Starten:
    private fun startPremiumDialog()
{
    var dialog = DialogFragmentPremium()
    dialog.show(fragmentManager!!,"Premium")
}

    // BodyAim Setter Methoden
    private fun setNewAimValue(pos:Int,value:Float)
    {
        when(pos)
        {
            0 ->
            {
                prefs.setNewWeightAim(value)
                prefs.setNewBmiAim()
                rvContent[pos] = ("${helper.getFloatAsFormattedStringWithPattern(value,"#.##")} kg")
                rvContent[2] = ("${helper.getFloatAsFormattedStringWithPattern(getBMI(value),"#.##")} ")

            }
            1 ->
            {
                prefs.setNewKfaAim(value)
                rvContent[pos] = ("${helper.getFloatAsFormattedStringWithPattern(value,"#.##")} %")
            }
            3 ->
            {
                prefs.setNewBauchAim(value)
                rvContent[pos] = ("${helper.getFloatAsFormattedStringWithPattern(value,"#.##")} cm")
            }
            4 ->
            {
                prefs.setNewBrustAim(value)
                rvContent[pos] = ("${helper.getFloatAsFormattedStringWithPattern(value,"#.##")} cm")
            }
            5 ->
            {
                prefs.setNewHalsAim(value)
                rvContent[pos] = ("${helper.getFloatAsFormattedStringWithPattern(value,"#.##")} cm")
            }
            6 ->
            {
                prefs.setNewHueftAim(value)
                rvContent[pos] = ("${helper.getFloatAsFormattedStringWithPattern(value,"#.##")} cm")
            }
        }
        bodyAimAdapter.updateData(rvContent)
        bodyViewModel.updateBodyProgress()
    }


    // Eigentlich gehört das nach extern:
    private fun getBMI(weightAim:Float):Float
    {
        var value = prefs.userHeight/100f
        return weightAim/(value*value)
    }

    private fun setNewAllowedFoodValues(pos:Int,min:Float,max:Float)
    {
        updateAllowedFoodContent(pos,min,max)
        when(pos)
        {
            0 ->
            {
                prefs.setNewMinAllowedKcal(min)
                prefs.setNewMaxAllowedKcal(max)

            }
            1 ->
            {
                prefs.setNewMinAllowedCarbs(min)
                prefs.setNewMaxAllowedCarbs(max)
            }
            2 ->
            {
                prefs.setNewMinAllowedProtein(min)
                prefs.setNewMaxAllowedProtein(max)
            }
            3 ->
            {
                prefs.setNewMinAllowedFett(min)
                prefs.setNewMaxAllowedFett(max)
            }
        }

    }

    private fun getAllowedFoodContent():ArrayList<String>
    {
        var maxValues = arrayListOf(prefs.maxAllowedKcal,prefs.maxAllowedCarbs,prefs.maxAllowedProtein,prefs.maxAllowedFett)
        var minValues = arrayListOf(prefs.minAllowedKcal,prefs.minAllowedCarbs,prefs.minAllowedProtein,prefs.minAllowedFett)

        Log.d("Smeasy","BodySettingFragment - getAllowedFoodContent(): maxValues = $maxValues")
        Log.d("Smeasy","BodySettingFragment - getAllowedFoodContent(): minValues = $minValues")

        var export:ArrayList<String> = ArrayList()

        for((index,i) in maxValues.withIndex())
        {
            if(i <= 0f && minValues[index] <= 0f)
            {
                export.add("Keine Einschränkung")
            }
            else if(i == -1f && minValues[index]>0)
            {
                export.add("min. ${minValues[index]} ${units[index]}")
            }
            else if(i > 0f && minValues[index] == 0f)
            {
                export.add("max. $i ${units[index]}")
            }
            else if(i > 0f && minValues[index] > 0f)
            {
                export.add("min. ${minValues[index]} ${units[index]} und max. $i ${units[index]}")
            }
        }


        return export
    }

    private fun updateAllowedFoodContent(pos:Int,min:Float,max:Float)
    {



        if(max == -1f && min == 0f)
        {
            rvAllowedFoodContent[pos] = ("Keine Einschränkung")
        }
        else if(max == -1f && min>0)
        {
            rvAllowedFoodContent[pos] = ("min. $min ${units[pos]}")
        }
        else if(max > 0f && min == 0f)
        {
            rvAllowedFoodContent[pos] = ("max. $max ${units[pos]}")
        }
        else if(max > 0f && min > 0f)
        {
            rvAllowedFoodContent[pos] = ("min. $min ${units[pos]} und max. $max ${units[pos]}")
        }
        allowedFoodAdapter.updateContent(rvAllowedFoodContent,prefs.premiumStatus)
    }


}