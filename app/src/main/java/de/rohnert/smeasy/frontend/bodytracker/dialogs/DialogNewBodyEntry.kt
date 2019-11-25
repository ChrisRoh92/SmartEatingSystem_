package de.rohnert.smeasy.frontend.bodytracker.dialogs

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import de.rohnert.smeasy.R
import de.rohnert.smeasy.frontend.bodytracker.BodyViewModel
import de.rohnert.smeasy.frontend.premium.dialogs.DialogPremiumAlert

class DialogNewBodyEntry(var context: Context,var fragmentManager: FragmentManager, var bodyViewModel: BodyViewModel) :
    View.OnClickListener {


    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var view: View
    private lateinit var inflater: LayoutInflater

    // Content:
    private var imagePath = ""

    // Views:
    private var etList:ArrayList<TextInputLayout> = ArrayList()
    private var etIds:ArrayList<Int> = ArrayList()
    private lateinit var btnPhoto:ImageButton
    private lateinit var btnSave:Button
    private lateinit var btnAbort:Button

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
        btnPhoto.setOnClickListener(this)
        btnSave.setOnClickListener(this)
        btnAbort.setOnClickListener(this)

    }


    // Implementierte Methoden
    override fun onClick(view: View?) {
        if(view == btnPhoto)
        {
//            var dialog = DialogPremiumAlert(view.context)
            var captureDialog = DialogCapturePhoto(imagePath)
            captureDialog.show(fragmentManager,"capture")
            captureDialog.setOnDialogCapturePhotoListener(object: DialogCapturePhoto.OnDialogCapturePhotoListener{
                override fun setOnDialogCapturePhotoListener(dir: String) {
                    imagePath = dir
                    Log.d("Smeasy","DialogNewBodyEntry - captureDialog.listener : dir = $dir")
                }

            })
        }
        else if(view == btnSave)
        {
            // Methode zum speichern:
        }
        else
        {
            alertDialog.dismiss()
        }
    }
}