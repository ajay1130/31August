package com.example.a31august

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.FLASH_MODE_AUTO
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.a31august.databinding.ActivityMainBinding
import java.io.File
import java.lang.Exception
import java.util.*
private const val REQUEST_CODE_CAMERA =100
class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var camera:Camera
    lateinit var preview:Preview
    lateinit var imageCapture:ImageCapture
    lateinit var cameraSelector:CameraSelector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        if(checkCameraPermissionGranted()){
            startCamera()
        }
        else{
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CODE_CAMERA)
        }

        binding.cameraCaptureButton.setOnClickListener {
            takePhoto()
        }

        binding.buttonCameraselector.setOnClickListener {
            cameraSelector = if(cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                CameraSelector.DEFAULT_FRONT_CAMERA
            else
                CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }




    }

    private fun startCamera() {
        val cameraProviderListner = ProcessCameraProvider.getInstance(this)
        cameraProviderListner.addListener(
            {
                val cameraProvider = cameraProviderListner.get()
                preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }
                imageCapture = ImageCapture
                    .Builder()
                    .setFlashMode(FLASH_MODE_AUTO)
                    .build()


                try{
                    cameraProvider.unbindAll()
                    camera = cameraProvider.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                }
                catch (ex:Exception){
                    Toast.makeText(this,ex.message,Toast.LENGTH_LONG).show()
                }
            },
            ContextCompat.getMainExecutor(this)
        )
    }

    private fun takePhoto() {
        val file = File(externalMediaDirs.firstOrNull(),"cameraapp-${Random().nextInt(1000)}.jpg")
        val output = ImageCapture.OutputFileOptions.Builder(file).build()
        imageCapture.takePicture(output,ContextCompat.getMainExecutor(this),object:ImageCapture.OnImageSavedCallback{
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                Toast.makeText(this@MainActivity,"Image saved.. $file",Toast.LENGTH_LONG).show()
            }

            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(this@MainActivity,"Image not saved.. ${exception.message}",Toast.LENGTH_LONG).show()
            }

        })
    }


    private fun checkCameraPermissionGranted():Boolean=
        ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==
                PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE_CAMERA){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startCamera()
            }
            else{
                Toast.makeText(this,"Permission not granted..",Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    fun showColorPlatte(view: android.view.View) {
        Intent(this,ColorPlatteActivity::class.java).also {
            startActivity(it)
        }
    }
}