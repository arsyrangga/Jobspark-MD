package com.dicoding.jobspark.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.jobspark.R
import com.dicoding.jobspark.data.remote.ApplyJobRequest
import com.dicoding.jobspark.data.remote.ApplyJobResponse
import com.dicoding.jobspark.data.remote.RetrofitClient
import com.dicoding.jobspark.data.remote.UploadResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UploadActivity : AppCompatActivity() {

    private var selectedFilePath: String? = null
    private val REQUEST_CODE_PICK_FILE = 100
    private var selectedFileName: String? = null

    private lateinit var fileUploadContainer: LinearLayout
    private lateinit var uploadCvButtonContainer: LinearLayout
    private lateinit var fileNameTextView: TextView
    private lateinit var deleteButton: ImageView
    private lateinit var submitButton: Button
    private lateinit var jobImageView: ImageView
    private lateinit var jobTitleTextView: TextView
    private lateinit var companyNameTextView: TextView

    private var jobImageUrl: String? = null
    private var jobTitle: String? = null
    private var companyName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cv_upload)

        val jobId = intent.getIntExtra("job_id", -1)
        jobTitle = intent.getStringExtra("job_title")
        companyName = intent.getStringExtra("company_name")
        jobImageUrl = intent.getStringExtra("job_image_url")

        val backButton: ImageView = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        fileUploadContainer = findViewById(R.id.file_upload_container)
        uploadCvButtonContainer = findViewById(R.id.upload_cv_button_container)
        fileNameTextView = findViewById(R.id.file_name)
        deleteButton = findViewById(R.id.delete_button)
        submitButton = findViewById(R.id.submit_button)
        jobImageView = findViewById(R.id.job_image)
        jobTitleTextView = findViewById(R.id.job_title)
        companyNameTextView = findViewById(R.id.company_name)

        jobTitleTextView.text = jobTitle ?: "Job Title Not Available"
        companyNameTextView.text = companyName ?: "Company Name Not Available"

        jobImageUrl?.let {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.placeholder_image)
                .into(jobImageView)
        }

        submitButton.setOnClickListener {
            if (selectedFilePath != null) {
                uploadResume(jobId)
            } else {
                Toast.makeText(this, "Please upload your CV first.", Toast.LENGTH_SHORT).show()
            }
        }

        uploadCvButtonContainer.setOnClickListener {
            openFilePicker()
        }

        deleteButton.setOnClickListener {
            selectedFilePath = null
            selectedFileName = null
            updateUIAfterFileDeleted()
        }
    }

    private fun openFilePicker() {
        val selectFileIntent = Intent(Intent.ACTION_GET_CONTENT)
        selectFileIntent.type = "application/pdf"
        startActivityForResult(selectFileIntent, REQUEST_CODE_PICK_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                selectedFilePath = getFilePath(uri)
                selectedFileName = uri.lastPathSegment?.substringAfterLast("/")
                updateUIAfterFileSelected()
            }
        }
    }

    private fun getFilePath(uri: Uri): String? {
        val inputStream = contentResolver.openInputStream(uri)
        if (inputStream == null) {
            Log.e("FileError", "Failed to open InputStream for URI: $uri")
            return null
        }

        // Save the input stream to a local file
        val file = File(filesDir, "upload_${System.currentTimeMillis()}.pdf")
        try {
            inputStream.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            return file.absolutePath // Returning the file path as String
        } catch (e: Exception) {
            Log.e("FileError", "Error while copying the file: ${e.message}")
        }

        return null
    }

    private fun updateUIAfterFileSelected() {
        fileUploadContainer.visibility = View.VISIBLE
        uploadCvButtonContainer.visibility = View.GONE

        fileNameTextView.text = selectedFileName ?: "No file selected"
    }

    private fun updateUIAfterFileDeleted() {
        fileUploadContainer.visibility = View.GONE
        uploadCvButtonContainer.visibility = View.VISIBLE
    }

    private fun uploadResume(jobId: Int) {
        if (selectedFilePath != null) {
            val file = File(selectedFilePath!!) // Ensure selectedFilePath is non-null
            val requestBody: RequestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData("file", file.name, requestBody)

            val sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
            val token = sharedPreferences.getString("TOKEN", "")

            if (token.isNullOrEmpty()) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return
            }

            RetrofitClient.instance.uploadResume("Bearer $token", filePart)
                .enqueue(object : Callback<UploadResponse> {
                    override fun onResponse(
                        call: Call<UploadResponse>,
                        response: Response<UploadResponse>
                    ) {
                        if (response.isSuccessful) {
                            val uploadStatus = response.body()?.status
                            val uploadMessage = response.body()?.message
                            val resumeData = response.body()?.data

                            Log.d("UploadResponse", "Status: $uploadStatus, Message: $uploadMessage, ResumeData: $resumeData")

                            // Ensure that 'status' is checked
                            if (uploadStatus == 200 && uploadMessage == "SUCCESS") {
                                if (resumeData != null && resumeData.id != 0) {
                                    applyForJob(jobId, resumeData.id)
                                } else {
                                    Log.d("UploadResponse", "Response: ${response.body()}")
                                    Toast.makeText(
                                        this@UploadActivity,
                                        "Invalid resume ID",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    this@UploadActivity,
                                    "Failed to upload resume: $uploadMessage",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@UploadActivity,
                                "Failed to upload resume: ${response.code()} - ${response.message()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                        Toast.makeText(
                            this@UploadActivity,
                            "Network error: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun applyForJob(jobId: Int, resumeId: Int) {
        val applyJobRequest = ApplyJobRequest(jobs_id = jobId, resume_id = resumeId)

        val sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", "")

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitClient.instance.applyJob("Bearer $token", applyJobRequest)
            .enqueue(object : Callback<ApplyJobResponse> {
                override fun onResponse(
                    call: Call<ApplyJobResponse>,
                    response: Response<ApplyJobResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@UploadActivity,
                            "Application submitted successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Navigate to the success screen after successful application submission
                        val intent = Intent(this@UploadActivity, SuccessUploadActivity::class.java)
                        startActivity(intent)
                        finish()  // Close the current activity
                    } else {
                        Toast.makeText(
                            this@UploadActivity,
                            "Failed to apply for the job",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ApplyJobResponse>, t: Throwable) {
                    Toast.makeText(
                        this@UploadActivity,
                        "Network error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

}
