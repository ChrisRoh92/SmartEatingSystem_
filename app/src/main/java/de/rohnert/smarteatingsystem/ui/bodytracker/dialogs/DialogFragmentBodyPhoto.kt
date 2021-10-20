package de.rohnert.smarteatingsystem.ui.bodytracker.dialogs

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.data.helper.Helper
import kotlinx.android.synthetic.main.dialog_bodyphoto.*
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class DialogFragmentBodyPhoto(val date:String):DialogFragment()
{

    // Allgemeine Variablen:
    private lateinit var rootView: View
    private var helper = Helper()

    // View Elemente:
    private lateinit var btnClose:ImageButton
    private lateinit var btnFlip:ImageButton
    private lateinit var btnTakePhoto:Button

    // CameraX:
    private var preview: Preview? = null
    private var imageCapture:ImageCapture? = null
    private var camera:Camera? = null
    private var cameraSelection = true

    // File Elements:
    private lateinit var outputDirectory: File

    // Interface:
    private lateinit var mListener:OnCapturePhoto





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

        rootView = inflater.inflate(R.layout.dialog_bodyphoto, container, false)

        initButtons()
        initCamera()

        return rootView
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private fun initButtons()
    {
        btnClose = rootView.findViewById(R.id.dialog_photo_btn_close)
        btnFlip = rootView.findViewById(R.id.dialog_photo_btn_flip)
        btnTakePhoto = rootView.findViewById(R.id.dialog_photo_btn_takepicture)

        // Listener:
        btnClose.setOnClickListener { dismiss() }

        btnFlip.setOnClickListener { flipCamera() }

        btnTakePhoto.setOnClickListener { takePhoto() }
    }


    private fun initCamera()
    {

        if(allPermissionsGranted())
        {
            startCamera()
        }
        else
        {
            ActivityCompat.requestPermissions(
                requireActivity(),REQUIRED_PERMISSIONS,REQUEST_CODE_PERMISSIONS)

        }

        outputDirectory = getOutputDirectory()!!

    }







    private fun startCamera()
    {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())
        cameraProviderFuture.addListener(Runnable {

            val cameraProvider = cameraProviderFuture.get() as ProcessCameraProvider

            preview = Preview.Builder().apply {


            }.build()

            imageCapture = ImageCapture.Builder().apply {

            }.build()


            var cameraSelector:CameraSelector = if(cameraSelection) {
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
            } else {
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()
            }



            try {

                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(this@DialogFragmentBodyPhoto,
                cameraSelector,preview,imageCapture)
                preview?.setSurfaceProvider(dialog_photo_preview!!.createSurfaceProvider())

            }catch (e:Exception)
            {
                Log.e(TAG,"Use case binding failed",e)
            }

        },ContextCompat.getMainExecutor(requireContext()))
    }

    private fun stopCamera()
    {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())
        val cameraProvider = cameraProviderFuture.get() as ProcessCameraProvider
        cameraProvider.unbindAll()

    }

    // Method to take a Photo :)
    private fun takePhoto()
    {
        val imageCapture = imageCapture ?: return

        // Create File:
        val file = File(outputDirectory,
        SimpleDateFormat(FILENAME_FORMAT, Locale.GERMANY).format(System.currentTimeMillis())+".jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object :ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val test = Uri.fromFile(file)
                    Log.d(TAG,"Photo Caputre succeded! - uri = $test")

                    val dialog = DialogFragmentPhotoPreview(test.path!!)
                    dialog.show(parentFragmentManager,"PhotoPreview")
                    dialog.setOnSaveClickListener(object:DialogFragmentPhotoPreview.OnSaveClickListener{
                        override fun setOnSaveClickListener() {
                            mListener.setOnCapturePhoto(test)
                            stopCamera()
                            dismiss()
                        }

                    })


                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG,"Photo Captpure Failed ${exception.message}",exception)
                }

            }
        )

    }

    // Method to flip the Camera:
    private fun flipCamera()
    {
        cameraSelection = !cameraSelection
        startCamera()

    }


    //////////////////////////////////////////////////////////////
    // Utils:
    private fun allPermissionsGranted():Boolean
    {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(requireContext(),it) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == REQUEST_CODE_PERMISSIONS)
        {
            if(allPermissionsGranted())
            {
                startCamera()
            }
            else
            {
                Toast.makeText(requireContext(),"No Permission granted!!",Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun getOutputDirectory():File?
    {
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull()?.let {
            File(it,resources.getString(R.string.app_name)).apply { mkdir() }}

            return if(mediaDir != null && mediaDir.exists())
                mediaDir
            else
                requireActivity().filesDir

    }

    companion object
    {
        private const val TAG = "DialogFragmentBodyPhoto"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-ss"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
    }

    // Interface, zum zurückgeben der File-adresse!:
    interface OnCapturePhoto
    {
        fun setOnCapturePhoto(uri: Uri?)
    }

    fun setOnCapturePhoto(mListener:OnCapturePhoto)
    {
        this.mListener = mListener
    }


}