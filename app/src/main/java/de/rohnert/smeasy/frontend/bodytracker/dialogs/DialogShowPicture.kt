package de.rohnert.smeasy.frontend.bodytracker.dialogs

import android.app.AlertDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import backend.helper.Helper
import de.rohnert.smeasy.R
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences

class DialogShowPicture(var context: Context, var dir:String)
{
    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var view: View
    private lateinit var inflater: LayoutInflater
    private lateinit var share: SharedAppPreferences
    private var helper = Helper()

    // Views:
    private lateinit var image:ImageView
    private lateinit var tv:TextView
    private lateinit var btn: Button

    init {
        initDialog()
    }


    // Dialog initialisieren...
    private fun initDialog()
    {

        builder = AlertDialog.Builder(context,android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_show_picture, null)
        builder.setView(view)


        initViews()
        loadImage()




        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()

    }

    private fun initViews()
    {
        image = view.findViewById(R.id.dialog_show_picture_image)
        tv = view.findViewById(R.id.dialog_show_picture_tv)
        btn = view.findViewById(R.id.dialog_show_picture_btn)

        // Listener:
        btn.setOnClickListener{
            alertDialog.dismiss()}
    }

    private fun loadImage()
    {
        if(dir != "")
        {
            try {
                var bitmap2 = BitmapFactory.decodeFile(dir)
                image.setImageBitmap(bitmap2)
                //image.rotation = 270f
            }catch (e:Exception)
            {
                e.printStackTrace()
                tv.visibility = View.GONE
            }
        }
        else
        {
            tv.visibility = View.GONE
        }

    }
}