package com.dicoding.jobspark.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cv_upload)

        val jobId = intent.getIntExtra("job_id", -1)
        val jobName = intent.getStringExtra("job_name")
        val companyName = intent.getStringExtra("company_name")

        val backButton: ImageView = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        findViewById<TextView>(R.id.job_title).text = jobName
        findViewById<TextView>(R.id.company_name).text = companyName

        fileUploadContainer = findViewById(R.id.file_upload_container)
        uploadCvButtonContainer = findViewById(R.id.upload_cv_button_container)
        fileNameTextView = findViewById(R.id.file_name)
        deleteButton = findViewById(R.id.delete_button)
        submitButton = findViewById(R.id.submit_button)

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
        selectFileIntent.type = "application/pdf"  // Limit to PDF files
        startActivityForResult(selectFileIntent, REQUEST_CODE_PICK_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                selectedFilePath = getFilePath(uri)
                selectedFileName = uri.lastPathSegment?.substringAfterLast("/")  // Get file name
                updateUIAfterFileSelected()
            }
        }
    }

    private fun getFilePath(uri: Uri): String? {
        return uri.path
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
        val file = File(selectedFilePath)

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
                        if (uploadStatus == "success") {
                            val resumeId = response.body()?.message?.toIntOrNull() ?: 0
                            if (resumeId != 0) {
                                applyForJob(jobId, resumeId)
                            } else {
                                Toast.makeText(
                                    this@UploadActivity,
                                    "Invalid resume ID",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@UploadActivity,
                                "Failed to upload resume: ${response.body()?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@UploadActivity,
                            "Failed to upload resume",
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
                        finish()
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
