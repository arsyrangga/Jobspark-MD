package com.dicoding.jobspark.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dicoding.jobspark.R
import com.dicoding.jobspark.data.remote.ImageRecognitionResponse
import com.dicoding.jobspark.data.remote.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class VerificationActivity : AppCompatActivity() {
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private var capturedImageUri: Uri? = null

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                capturedImageUri = it
                displayImage(it)
            } ?: run {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
            }
        }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestCameraPermissions()
        }

        findViewById<Button>(R.id.captureButton).setOnClickListener { takePhoto() }
        findViewById<Button>(R.id.galleryButton).setOnClickListener { openGallery() }

        findViewById<Button>(R.id.nextButton).setOnClickListener {
            if (capturedImageUri != null) {
                processImageRecognition(capturedImageUri!!)
            } else {
                Toast.makeText(
                    this,
                    "Please capture or select an image before proceeding",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            val previewView = findViewById<PreviewView>(R.id.previewView)
            preview.surfaceProvider = previewView.surfaceProvider

            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Toast.makeText(this, "Error initializing camera: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = File(outputDirectory, generateFileName())
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@VerificationActivity,
                        "Photo capture failed: ${exc.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    capturedImageUri = savedUri
                    displayImage(savedUri)
                    Toast.makeText(
                        this@VerificationActivity,
                        "Photo saved successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun displayImage(imageUri: Uri) {
        val capturedImageView = findViewById<ImageView>(R.id.capturedImageView)
        Glide.with(this).load(imageUri).into(capturedImageView)
    }

    private fun allPermissionsGranted(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val storagePermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        return cameraPermission == PackageManager.PERMISSION_GRANTED && storagePermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Toast.makeText(
                this,
                "Camera permission is needed to capture photos.",
                Toast.LENGTH_SHORT
            ).show()
        }
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()
            ?.let { File(it, resources.getString(R.string.app_name)).apply { mkdirs() } }
        return mediaDir ?: filesDir
    }

    private fun generateFileName(): String {
        return SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.US
        ).format(System.currentTimeMillis()) + ".jpg"
    }

    private fun getFileFromUri(uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)
        val file = File(cacheDir, generateFileName())
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file
    }

    private fun processImageRecognition(imageUri: Uri) {
        val file = getFileFromUri(imageUri)
        val requestFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData(
            "file", file.name, requestFile
        )

        val apiService = RetrofitClient.instance
        apiService.recognizeImage(body).enqueue(object : Callback<ImageRecognitionResponse> {
            override fun onResponse(
                call: Call<ImageRecognitionResponse>,
                response: Response<ImageRecognitionResponse>
            ) {
                if (response.isSuccessful) {
                    val imageUrl = response.body()?.data?.url
                    if (!imageUrl.isNullOrEmpty()) {
                        navigateToSuccessPage(imageUrl)
                    } else {
                        navigateToFailPage()
                    }
                } else {
                    Toast.makeText(
                        this@VerificationActivity,
                        "Upload failed: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigateToFailPage()
                }
            }

            override fun onFailure(call: Call<ImageRecognitionResponse>, t: Throwable) {
                Toast.makeText(
                    this@VerificationActivity,
                    "Network error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                navigateToFailPage()
            }
        })
    }

    private fun navigateToSuccessPage(imageUrl: String) {
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")
        val fullName = intent.getStringExtra("full_name")
        val birthDate = intent.getStringExtra("birth_date")
        val gender = intent.getStringExtra("gender")
        val address = intent.getStringExtra("address")
        val emergencyContact = intent.getStringExtra("emergency_contact")
        val intent = Intent(this, ModelSuccessActivity::class.java).apply {
            putExtra("captured_image_url", imageUrl)
            putExtra("email", email)
            putExtra("password", password)
            putExtra("full_name", fullName)
            putExtra("birth_date", birthDate)
            putExtra("gender", gender)
            putExtra("address", address)
            putExtra("emergency_contact", emergencyContact)
        }
        startActivity(intent)
        finish()
    }

    private fun navigateToFailPage() {
        val intent = Intent(this, ModelFailActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
