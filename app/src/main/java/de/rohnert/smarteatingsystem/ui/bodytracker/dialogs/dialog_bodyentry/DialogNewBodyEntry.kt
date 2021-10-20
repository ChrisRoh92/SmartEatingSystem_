package de.rohnert.smarteatingsystem.ui.bodytracker.dialogs.dialog_bodyentry

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.textfield.TextInputLayout
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.data.sharedpreferences.SharedAppPreferences
import de.rohnert.smarteatingsystem.ui.bodytracker.BodyViewModel
import de.rohnert.smarteatingsystem.ui.bodytracker.dialogs.DialogCapturePhoto
import de.rohnert.smarteatingsystem.ui.premium.dialogs.DialogFragmentPremium

class DialogNewBodyEntry(var context: Context,var fragmentManager: FragmentManager, var bodyViewModel: BodyViewModel) :
    View.OnClickListener {


    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var view: View
    private lateinit var inflater: LayoutInflater
    private var prefs = SharedAppPreferences(context)

    // Content:
    private var imagePath = ""

    // Views:
    private var etList:ArrayList<TextInputLayout> = ArrayList()
    private var etIds:ArrayList<Int> = ArrayList()
    private lateinit var btnPhoto:ImageButton
    private lateinit var btnSave:Button
    private lateinit var btnAbort:Button

    // TextInputLayouts...



    init {
        initDialog()
    }


    // Dialog initialisieren...
    private fun initDialog()
    {

        builder = AlertDialog.Builder(context)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_bodyentry_create, null)
        builder.setView(view)


        initViews()



        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()

    }

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
            etList.add(view.findViewById(i))
        }

        // ImageButton initalisieren:
        btnPhoto = view.findViewById(R.id.dialog_bodyentry_btn_photo)

        // Button initialisieren:
        btnSave = view.findViewById(R.id.dialog_bodyentry_btn_save)
        btnAbort = view.findViewById(R.id.dialog_bodyentry_btn_abort)

        // Listener hinzufügen....

        btnSave.setOnClickListener(this)
        btnAbort.setOnClickListener(this)

        if(!prefs.premiumStatus)
        {


            btnPhoto.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_lock_black))
            btnPhoto.setColorFilter(ContextCompat.getColor(context,R.color.premium_dark))
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
//            var values = getDataFromTextInputs()
//            bodyViewModel.addNewBody(values[0],values[1],values[2],values[3],values[4],values[5],imagePath,)
            alertDialog.dismiss()
        }


    }


    // Implementierte Methoden
    override fun onClick(view: View?) {
        if(view == btnPhoto)
        {
            if(prefs.premiumStatus)
            {
                var captureDialog = DialogCapturePhoto(imagePath)
                captureDialog.show(fragmentManager,"capture")
                captureDialog.setOnDialogCapturePhotoListener(object:
                    DialogCapturePhoto.OnDialogCapturePhotoListener {
                    override fun setOnDialogCapturePhotoListener(dir: String) {
                        imagePath = dir
                        Log.d("Smeasy","DialogNewBodyEntry - captureDialog.listener : dir = $dir")
                    }

                })
            }
            else
            {
             var dialog = DialogFragmentPremium()
                dialog.show(fragmentManager,"Premium")
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
            alertDialog.dismiss()
        }
    }
}