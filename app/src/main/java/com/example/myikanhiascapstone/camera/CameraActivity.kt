package com.example.myikanhiascapstone.camera

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.getBitmap
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.myikanhiascapstone.MainActivity
import com.example.myikanhiascapstone.databinding.ActivityCameraBinding
import com.example.myikanhiascapstone.detail.DetailResultActivity
import com.example.myikanhiascapstone.ml.ModelSmallWithMetadata
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    lateinit var bitmap: Bitmap

    companion object {
        private const val TAG = "CameraActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.captureImage.setOnClickListener { takePhoto() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.switchCamera.setOnClickListener {
            cameraSelector = if (cameraSelector.equals(CameraSelector.DEFAULT_BACK_CAMERA)) CameraSelector.DEFAULT_FRONT_CAMERA
            else CameraSelector.DEFAULT_BACK_CAMERA

            startCamera()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createFile(application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Gagal mengambil gambar.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Log.d(TAG, "onImageSaved: ${output.savedUri}" )
                    var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, output.savedUri)
                    bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, false)
                    val hasil = Classifier.classifyImage(this@CameraActivity, bitmap)
                    Log.d(TAG, "onImageSaved: ${hasil}")
                    val intent = Intent(this@CameraActivity, DetailResultActivity::class.java)
                    intent.putExtra("result", hasil)
                    intent.putExtra("capture", output.savedUri.toString())
                    startActivity(intent)
                }
            }
        )
    }

    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                Toast.makeText(
                    this@CameraActivity,
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    fun getMax(arr:FloatArray) : Int{
        var ind = 0;
        var min = 0.0f;

        for(i in 0..1000)
        {
            if(arr[i] > min)
            {
                min = arr[i]
                ind = i;
            }
        }
        return ind
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult( ActivityResultContracts.StartActivityForResult()) { result ->
        Log.d(TAG, "launcherIntentGallery: ")
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImgUri: Uri = result.data?.data as Uri
            try{
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImgUri)
                startDetectResultActivity(bitmap)
                Log.d(TAG, "launcherIntentGallery: try")
            }catch (e: Exception){
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "launcherIntentGallery: bitmap", e)
            }
        }
    }

    private fun startDetectResultActivity(bitmap: Bitmap){
        Log.d(TAG, "StartDetectResultActivity")
        val intent = Intent(this, DetailResultActivity::class.java)
        val fileUri = createTemporaryFile(bitmap)
        Log.d(TAG, "Uri: $fileUri")
        var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
        bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, false)
        val hasil = Classifier.classifyImage(this@CameraActivity, bitmap)
        Log.d(TAG, "onImageSaved: ${hasil}")
        intent.putExtra("result", hasil)
        intent.putExtra("capture", fileUri.toString())
        startActivity(intent)
    }

    private fun createTemporaryFile(bitmap: Bitmap): Uri {
        Log.d(TAG, "createTemporaryFile: started")
        val directory: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val tempFile = File.createTempFile("tempFile", ".jpg", directory)
        try {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG,100 , byteArrayOutputStream)
            val outputStream: OutputStream = FileOutputStream(tempFile)
            outputStream.write(byteArrayOutputStream.toByteArray())
            outputStream.close()
        }catch (e: Exception){
            Log.e(TAG, "createTemporaryFile: FAILED", )
        }
        Log.d(TAG, "createTemporaryFile: returned uri: ${tempFile.toUri()}")
        return tempFile.toUri()
    }
}