package de.rohnert.smarteatingsystem.frontend.bodytracker.dialogs

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import de.rohnert.smarteatingsystem.backend.helper.Helper
import de.rohnert.smarteatingsystem.R
import java.io.File
import java.io.IOException


class DialogCapturePhoto(var imagePath:String): DialogFragment(), View.OnClickListener {


    private lateinit var rootView:View
    private var helper = Helper()

    // Interface:
    private lateinit var mListener:OnDialogCapturePhotoListener

    // Views:
    private lateinit var btnCapture:Button
    private lateinit var btnSave:Button
    private lateinit var btnAbort:Button
    private lateinit var photo:ImageView

    // Camera Stuff:
    private val REQUEST_TAKE_PHOTO = 1
    private val CONTENT_REQUEST = 1337
    private var output: File? = null
    private var currentImagePath:String? = null


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
        rootView = inflater.inflate(R.layout.dialog_bodyentry_capturephoto, container, false)



        initViews()






        return rootView
    }
    private fun initViews()
    {
        // ImageView
        photo = rootView.findViewById(R.id.dialog_capturephoto_image)
        try {
            var bitmap2 = BitmapFactory.decodeFile(imagePath)
            photo.setImageBitmap(bitmap2)
            photo.rotation = 270f
        }catch (e:Exception)
        {
            e.printStackTrace()
        }

        btnCapture = rootView.findViewById(R.id.dialog_capturephoto_btn_capture)
        btnSave = rootView.findViewById(R.id.dialog_capturephoto_btn_save)
        btnAbort = rootView.findViewById(R.id.dialog_capturephoto_btn_abort)

        // Listener:
        /*var values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Picture")
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
            var imageUri = rootView.context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
            )


            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)


            if (intent.resolveActivity(rootView.context.packageManager) != null) {
                startActivityForResult(intent, PICTURE_RESULT)
            }*/

            /*val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val dir1 = rootView.context.filesDir
            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)

            output = File(dir1, "CameraContentDemo.jpeg")
            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output))

            startActivityForResult(i, CONTENT_REQUEST)*/

            /*var cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(cameraIntent.resolveActivity(rootView.context.packageManager) != null)
            {
                var imageFile:File? = null
                try
                {

                    imageFile = getImageFile()

                }catch (e:IOException)
                {
                    e.printStackTrace()
                }

                if(imageFile!=null)
                {
                    var imageUri = FileProvider.getUriForFile(rootView.context,"de.rohnert.smeasy.fileprovider",imageFile)
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
                    startActivityForResult(cameraIntent,IMAGE_REQUEST)
                }
            }*/

        btnCapture.setOnClickListener(this)
        btnAbort.setOnClickListener(this)
        btnSave.setOnClickListener(this)
        }

    private fun startSaveProcess()
    {
        if(currentImagePath!=null)
        {
            if(mListener!=null)
            {
                mListener.setOnDialogCapturePhotoListener(currentImagePath!!)
                dismiss()
            }
        }
    }





    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent

            takePictureIntent.resolveActivity(rootView.context.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    ex.printStackTrace()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        rootView.context,
                        "de.rohnert.smeasy.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = helper.getStringFromDateWithPattern(helper.getCurrentDate(),"ddMMyyyy")
        val storageDir: File = rootView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentImagePath = absolutePath
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var bitmap2 = BitmapFactory.decodeFile(currentImagePath)

        photo.setImageBitmap(bitmap2)
        photo.rotation = 270f



        /*if (requestCode === REQUEST_CAPTURE_IMAGE && resultCode === RESULT_OK) {
            if (data != null && data.extras != null) {
                val imageBitmap = data!!.extras!!.get("data") as Bitmap
                photo.setImageBitmap(imageBitmap)
            }
        }*/
    }

    // Implementierte Methoden:
    override fun onClick(p0: View?) {
        if(p0 == btnCapture)
        {
            dispatchTakePictureIntent()
        }
        else if(p0 == btnAbort)
        {
            dismiss()
        }
        else
        {
            // Pfad speichern....
            startSaveProcess()
        }
    }


    // Interface
    interface OnDialogCapturePhotoListener
    {
        fun setOnDialogCapturePhotoListener(dir:String)
    }
    fun setOnDialogCapturePhotoListener(mListener:OnDialogCapturePhotoListener)
    {
        this.mListener = mListener
    }







    /*

    private fun getImageFile():File
    {
        var timeStamp = "24112019"
        var imageName = "smeasy_bodytracker_${timeStamp}_"
        var storageDir = rootView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        var imageFile:File? = null
        try
        {
            imageFile = File.createTempFile(imageName,".jpg",storageDir)
            currentImagePath = imageFile.absolutePath
        } catch(e:IOException)
        {
            e.printStackTrace()
        }


        return imageFile!!

    }

*/
}