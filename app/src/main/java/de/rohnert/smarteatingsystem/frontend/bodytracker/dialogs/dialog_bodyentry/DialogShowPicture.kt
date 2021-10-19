package de.rohnert.smarteatingsystem.frontend.bodytracker.dialogs.dialog_bodyentry

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import de.rohnert.smarteatingsystem.R


class DialogShowPicture(var context: Context, var dir:String)
{
    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var view: View
    private lateinit var inflater: LayoutInflater

    // Views:
    private lateinit var image:ImageView

    private lateinit var btn: Button

    init {
        initDialog()
    }


    // Dialog initialisieren...
    private fun initDialog()
    {

        builder = AlertDialog.Builder(context,R.style.FullScreenDialog)
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

        btn = view.findViewById(R.id.dialog_show_picture_btn)

        // Listener:
        btn.setOnClickListener{
            alertDialog.dismiss()}
    }

    private fun loadImage()
    {
        if(dir.isNotEmpty())
        {
            try {
                var bitmap2 = BitmapFactory.decodeFile(dir).rotateImage(90f)
                image.setImageBitmap(bitmap2)



                image.rotation = 0f
            }catch (e:Exception)
            {
                e.printStackTrace()

            }
        }


    }

    private fun Bitmap.rotateImage(angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            this, 0, 0, this.width, this.height,
            matrix, true
        )
    }
}