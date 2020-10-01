package de.rohnert.smarteatingsystem.frontend.bodytracker.dialogs

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputLayout
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.backend.helper.Helper
import de.rohnert.smarteatingsystem.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smarteatingsystem.frontend.bodytracker.BodyViewModel
import de.rohnert.smarteatingsystem.frontend.premium.dialogs.DialogFragmentPremium
import java.util.*
import kotlin.collections.ArrayList

class DialogFragmentNewBodyEntry(var bodyViewModel:BodyViewModel,var date: Date = Helper().getCurrentDate()): DialogFragment(), View.OnClickListener {

    // Allgemeine Variablen:
    private lateinit var rootView: View
    private lateinit var prefs: SharedAppPreferences

    // Content:
    private var imagePath = ""

    // Views:
    private var etList:ArrayList<TextInputLayout> = ArrayList()
    private var etIds:ArrayList<Int> = ArrayList()
    private lateinit var btnPhoto: ImageButton
    private lateinit var btnSave: Button
    private lateinit var btnAbort: Button


    ///////////////////////////////////////////////////////////////////////////////////////////////


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
        Log.d("Smeasy", "DialogFragmentFoodList - onCreateView")
        rootView = inflater.inflate(R.layout.dialog_bodyentry_create, container, false)

        prefs = SharedAppPreferences(rootView.context)


        initViews()



        return rootView
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private fun initViews()
    {
        // EditTexts initialisieren
        etIds = arrayListOf(
            R.id.dialog_bodyentry_et_weight,
            R.id.dialog_bodyentry_et_kfa,
            R.id.dialog_bodyentry_et_bauch,
            R.id.dialog_bodyentry_et_brust,
            R.id.dialog_bodyentry_et_hals,
            R.id.dialog_bodyentry_et_huefte)
        for(i in etIds)
        {
            etList.add(rootView.findViewById(i))
        }

        // ImageButton initalisieren:
        btnPhoto = rootView.findViewById(R.id.dialog_bodyentry_btn_photo)

        // Button initialisieren:
        btnSave = rootView.findViewById(R.id.dialog_bodyentry_btn_save)
        btnAbort = rootView.findViewById(R.id.dialog_bodyentry_btn_abort)

        // Listener hinzufügen....

        btnSave.setOnClickListener(this)
        btnAbort.setOnClickListener(this)

        if(!prefs.premiumStatus)
        {


            btnPhoto.setImageDrawable(ContextCompat.getDrawable(rootView.context,R.drawable.ic_lock_black))
            btnPhoto.setColorFilter(ContextCompat.getColor(rootView.context,R.color.premium_dark))
        }

        btnPhoto.setOnClickListener(this)



    }

    private fun getDataFromTextInputs():ArrayList<Float>
    {
        var export:ArrayList<Float> = ArrayList()
        for(i in etList)
        {
            if(i.editText!!.text.isNullOrEmpty())
            {
                export.add(0f)
            }
            else
            {
                export.add(i.editText!!.text.toString().toFloat())
            }
        }

        return export
    }

    private fun startSaveProcess()
    {
        if(!bodyViewModel.checkIfBodyExist())
        {
            var values = getDataFromTextInputs()
            bodyViewModel.addNewBody(values[0],values[1],values[2],values[3],values[4],values[5],imagePath,Helper().getStringFromDate(date))
            dismiss()
        }
        else
        {
            var values = getDataFromTextInputs()
            bodyViewModel.addNewBody(values[0],values[1],values[2],values[3],values[4],values[5],imagePath,Helper().getStringFromDate(date))
            dismiss()
        }


    }


    // Implementierte Methoden
    override fun onClick(view: View?) {
        if(view == btnPhoto)
        {

                if(imagePath.isNullOrEmpty())
                {
                    val captureDialog = DialogFragmentBodyPhoto(Helper().getStringFromDate(date))
                    captureDialog.show(parentFragmentManager,"Photo Capture")
                    captureDialog.setOnCapturePhoto(object:DialogFragmentBodyPhoto.OnCapturePhoto{
                        override fun setOnCapturePhoto(uri: Uri?) {
                            imagePath = uri?.path ?: ""
                        }

                    })
                }
                else
                {
                    val dialog = DialogFragmentPhotoPreview(imagePath,false)
                    dialog.show(parentFragmentManager,"PhotoPreview")
                    dialog.setOnRetryClickListener(object:DialogFragmentPhotoPreview.OnRetryClickListener{
                        override fun setOnRetryClickListener() {
                            val captureDialog = DialogFragmentBodyPhoto(Helper().getStringFromDate(date))
                            captureDialog.show(parentFragmentManager,"Photo Capture")
                            captureDialog.setOnCapturePhoto(object:DialogFragmentBodyPhoto.OnCapturePhoto{
                                override fun setOnCapturePhoto(uri: Uri?) {
                                    imagePath = uri?.path ?: ""
                                }

                            })
                        }

                    })
                }



//
        }
        else if(view == btnSave)
        {
            // Methode zum speichern:
            startSaveProcess()

        }
        else
        {
            dismiss()
        }
    }


}