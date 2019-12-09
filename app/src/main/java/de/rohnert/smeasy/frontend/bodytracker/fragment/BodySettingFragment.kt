package de.rohnert.smeasy.frontend.bodytracker.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import backend.helper.Helper
import com.google.android.material.tabs.TabLayout
import de.rohnert.smeasy.R
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smeasy.frontend.bodytracker.BodyViewModel
import de.rohnert.smeasy.frontend.bodytracker.dialogs.DialogBodyAim
import de.rohnert.smeasy.frontend.bodytracker.dialogs.DialogBodySettingsAim
import de.rohnert.smeasy.frontend.bodytracker.dialogs.DialogNutrition
import de.rohnert.smeasy.helper.dialogs.DialogSingleList
import kotlin.math.roundToInt

class BodySettingFragment: Fragment() {

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
    private var idTvBodyAimList:ArrayList<Int> =
        arrayListOf(
            R.id.bodysettings_bodyaim_tv_weight,
            R.id.bodysettings_bodyaim_tv_kfa,
            R.id.bodysettings_bodyaim_tv_bmi,
            R.id.bodysettings_bodyaim_tv_bauch,
            R.id.bodysettings_bodyaim_tv_brust,
            R.id.bodysettings_bodyaim_tv_hals,
            R.id.bodysettings_bodyaim_tv_huefte
            )

    // View Elemente:
    //TextViews:
    private var tvNutritionList:ArrayList<TextView> = ArrayList()
    private var pbNutritionList:ArrayList<ProgressBar> = ArrayList()
    private lateinit var tvAim:TextView
    private lateinit var tvAimWeight:TextView
    private var tvBodyAimList:ArrayList<TextView> = ArrayList()

    // Buttons:
    private lateinit var btnNutrition: Button
    private lateinit var btnAim:Button
    private lateinit var btnBodyAim:Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bodyViewModel = ViewModelProviders.of(this).get(BodyViewModel::class.java)
        rootView = inflater.inflate(R.layout.fragment_bodytracker_bodysettings, container, false)
        prefs = SharedAppPreferences(rootView.context)

        //
        initViews()


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
                var dialog = DialogNutrition(rootView.context)
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
                            tvAimWeight.text = "${helper.getFloatAsFormattedStringWithPattern(value,"#.#")} kg pro Woche"
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

        fun initBodyAimCard()
        {
            for(i in idTvBodyAimList)
            {
                tvBodyAimList.add(rootView.findViewById(i))
            }

            updateBodyAimCard()

            btnBodyAim = rootView.findViewById(R.id.bodysettings_bodyaim_btn)
            btnBodyAim.setOnClickListener {
                var dialog = DialogBodyAim()
                dialog.show(fragmentManager!!,"BodyAim")
                dialog.setOnDialogNutritionListener(object:DialogBodyAim.OnDialogBodyAimListener{
                    override fun setOnDialogBodyAimListener() {
                        updateBodyAimCard()
                    }

                })
            }


        }

        initNutritionCard()
        initAimCard()
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

    // BodyAimCard
    private fun updateBodyAimCard()
    {
        prefs = SharedAppPreferences(rootView.context)
        var values:ArrayList<Float> =
            arrayListOf(
                prefs.weightAim,
                prefs.kfaAim,
                prefs.bmiAim,
                prefs.bauchAim,
                prefs.brustAim,
                prefs.halsAim,
                prefs.huefteAim)
        tvBodyAimList[0].text = "${helper.getFloatAsFormattedString(values[0],"#")} kg"
        tvBodyAimList[1].text = "${helper.getFloatAsFormattedString(values[1],"#")} %"
        tvBodyAimList[2].text = "${helper.getFloatAsFormattedString(values[2],"#")}"
        tvBodyAimList[3].text = "${helper.getFloatAsFormattedString(values[3],"#")} cm"
        tvBodyAimList[4].text = "${helper.getFloatAsFormattedString(values[4],"#")} cm"
        tvBodyAimList[5].text = "${helper.getFloatAsFormattedString(values[5],"#")} cm"
        tvBodyAimList[6].text = "${helper.getFloatAsFormattedString(values[6],"#")} cm"

        /*for((index,i) in tvBodyAimList.withIndex())
        {
            i.text = "${helper.getFloatAsFormattedString(values[index],"#")}"
        }*/

    }

}