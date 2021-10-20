package de.rohnert.smarteatingsystem.ui.bodytracker.dialogs

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.data.helper.Helper


class DialogFragmentPhotoPreview(var dir:String, var fromPhotoDialog:Boolean = true): DialogFragment()
{

    // Allgemeine Variablen:
    private lateinit var rootView: View
    private var helper = Helper()

    // View Elemente:
    private lateinit var btnOk:Button
    private lateinit var btnRetry:Button
    private lateinit var imageView:ImageView

    // Interface:
    private lateinit var mListener:OnSaveClickListener
    private lateinit var mRetryListener:OnRetryClickListener


    ///////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            R.style.AppTheme
        )


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogAnimation
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        rootView = inflater.inflate(R.layout.dialog_photo_preview, container, false)

        initViews()
        loadImage()

        return rootView
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private fun initViews()
    {
        btnOk = rootView.findViewById(R.id.dialog_photopreview_btn_ok)
        btnRetry = rootView.findViewById(R.id.dialog_photopreview_btn_retry)
        imageView = rootView.findViewById(R.id.dialog_photopreview_imageview)

        btnOk.setOnClickListener {
            if(fromPhotoDialog)
            {
                mListener.setOnSaveClickListener()
                dismiss()
            }
            else
            {
                dismiss()
            }

        }
        btnRetry.setOnClickListener {
            if(fromPhotoDialog)
            {
                dismiss()
            }
            else
            {
                mRetryListener.setOnRetryClickListener()
                dismiss()
            }


        }


    }

    private fun loadImage()
    {
        if(dir.isNotEmpty())
        {
            try {
                var bitmap2 = BitmapFactory.decodeFile(dir).rotateImage(270f)


                /*val exif = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ExifInterface(File(dir))
                } else {
                    null
                }
                 val orientation = exif?.getAttributeInt(ExifInterface.TAG_ORIENTATION,1)

                val degree = exifToDegrees(orientation!!).toFloat()

                bitmap2?.rotateImage(degree ?: 0f)*/


                imageView.setImageBitmap(bitmap2)


            }catch (e:Exception)
            {
                e.printStackTrace()

            }
        }


    }

    private fun exifToDegrees(exifOrientation: Int?): Int {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270
        }
        return 0
    }

    private fun Bitmap.rotateImage(angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            this, 0, 0, this.width, this.height,
            matrix, true
        )
    }

    interface OnSaveClickListener
    {
        fun setOnSaveClickListener()
    }

    fun setOnSaveClickListener(mListener:OnSaveClickListener)
    {
        this.mListener = mListener
    }

    interface OnRetryClickListener
    {
        fun setOnRetryClickListener()
    }
    fun setOnRetryClickListener(mRetryListener:OnRetryClickListener)
    {
        this.mRetryListener = mRetryListener
    }



}