package de.rohnert.smeasy.frontend.bodytracker.dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import backend.helper.Helper
import com.google.android.material.textfield.TextInputLayout
import de.rohnert.smeasy.R
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smeasy.frontend.foodtracker.helper.WeekReportCreator

class DialogBodyAim():DialogFragment(), View.OnClickListener {


    // Allgemeine Variablen:
    private lateinit var rootView: View
    private lateinit var sharePrefs: SharedAppPreferences
    private var helper = Helper()

    // Interface
    private lateinit var mListener:OnDialogBodyAimListener

    // Content:
    private var weightAim:Float = 0f
    private var kfaAim:Float = 0f
    private var bmiAim:Float = 0f
    private var bauchAim:Float = 0f
    private var brustAim:Float = 0f
    private var halsAim:Float = 0f
    private var huefteAim:Float = 0f

    // View Elemente:
    private var etList:ArrayList<TextInputLayout> = ArrayList()
    private var idEtList:ArrayList<Int> =
        arrayListOf(
            R.id.dialog_bodysettings_bodyaim_et_weight,
            R.id.dialog_bodysettings_bodyaim_et_kfa,
            R.id.dialog_bodysettings_bodyaim_et_bauch,
            R.id.dialog_bodysettings_bodyaim_et_brust,
            R.id.dialog_bodysettings_bodyaim_et_hals,
            R.id.dialog_bodysettings_bodyaim_et_huefte)
    private lateinit var btnAbort: Button
    private lateinit var btnSave:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            R.style.FullScreenDialog
        )


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogAnimation
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        rootView = inflater.inflate(R.layout.dialog_bodysettings_bodyaim, container, false)

        initContent()

        initViews()

        return rootView
    }

    private fun initContent()
    {
        sharePrefs = SharedAppPreferences(rootView.context)

        weightAim = sharePrefs.weightAim
        kfaAim = sharePrefs.kfaAim
        bmiAim = sharePrefs.bmiAim
        bauchAim = sharePrefs.bauchAim
        brustAim = sharePrefs.brustAim
        halsAim = sharePrefs.halsAim
        huefteAim = sharePrefs.huefteAim
    }

    private fun initViews()
    {
        for(i in idEtList)
        {
            etList.add(rootView.findViewById(i))
        }

        // den Views die Werte zuweisen:
        var values = arrayListOf(weightAim,kfaAim,bauchAim,brustAim,halsAim,huefteAim)
        Log.d("Smeasy","DialogBodyAim - etList: $etList")
        for((index,i) in etList.withIndex())
        {
            i.editText!!.setText("${helper.getFloatAsFormattedString(values[index],"#")}")
        }



        // Buttons:
        btnAbort = rootView.findViewById(R.id.dialog_bodysettings_bodyaim_btn_abort)
        btnSave = rootView.findViewById(R.id.dialog_bodysettings_bodyaim_btn_save)

        // Listener anmelden:
        btnAbort.setOnClickListener(this)
        btnSave.setOnClickListener(this)





    }


    private fun startSavingProcess()
    {
        var empty = false
        for(i in etList)
        {
            if(i.editText!!.text.isNullOrEmpty())
            {
                empty = true
                break
            }
        }
        if(!empty)
        {
            getContentFromEditText()
            if(kfaAim < 100 && mListener!=null)
            {
                sharePrefs = SharedAppPreferences(rootView.context)
                sharePrefs.setNewWeightAim(weightAim)
                sharePrefs.setNewKfaAim(kfaAim)
                sharePrefs.setNewBmiAim()
                sharePrefs.setNewBauchAim(bauchAim)
                sharePrefs.setNewBrustAim(brustAim)
                sharePrefs.setNewHalsAim(halsAim)
                sharePrefs.setNewHueftAim(huefteAim)
                mListener.setOnDialogBodyAimListener()
                dismiss()
            }
            else
            {
                Toast.makeText(rootView.context,"Körperfettanteil muss unter 100% liegen",Toast.LENGTH_SHORT).show()
            }
        }
        else
        {
            Toast.makeText(rootView.context,"Bitte alle Felder ausfüllen",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getContentFromEditText()
    {
        weightAim = etList[0].editText!!.text.toString().toFloat()
        kfaAim = etList[1].editText!!.text.toString().toFloat()
        //bmiAim = sharePrefs.bmiAim
        bauchAim = etList[2].editText!!.text.toString().toFloat()
        brustAim = etList[3].editText!!.text.toString().toFloat()
        halsAim = etList[4].editText!!.text.toString().toFloat()
        huefteAim = etList[5].editText!!.text.toString().toFloat()
    }


    // Implementierte Methoden:
    override fun onClick(btn: View?) {
        if(btn == btnSave)
        {
            startSavingProcess()

        }
        else
        {
            dismiss()
        }
    }


    // Interface
    interface OnDialogBodyAimListener
    {
        fun setOnDialogBodyAimListener()
    }

    fun setOnDialogNutritionListener(mListener:OnDialogBodyAimListener)
    {
        this.mListener = mListener
    }
}