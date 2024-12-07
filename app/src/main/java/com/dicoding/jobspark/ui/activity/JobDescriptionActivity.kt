package com.dicoding.jobspark.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.jobspark.R
import com.dicoding.jobspark.data.remote.JobDetailResponse
import com.dicoding.jobspark.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobDescriptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.description_job)

        val jobId = intent.getIntExtra("job_id", -1)
        if (jobId == -1) {
            Toast.makeText(this, "Job ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        fetchJobDetails(jobId)

        val backButton: ImageView = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val applyButton: Button = findViewById(R.id.apply_button)
        applyButton.setOnClickListener {
            val jobDetails = intent.extras
            val intent = Intent(this, UploadActivity::class.java).apply {
                putExtra("job_id", jobId)
                putExtra("job_name", jobDetails?.getString("job_name"))
                putExtra("company_name", jobDetails?.getString("company_name"))
                putExtra("job_image_url", jobDetails?.getString("job_image_url"))
            }
            startActivity(intent)
        }
    }

    private fun fetchJobDetails(jobId: Int) {
        val sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", "")

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitClient.instance.getJobDetail("Bearer $token", jobId)
            .enqueue(object : Callback<JobDetailResponse> {
                override fun onResponse(
                    call: Call<JobDetailResponse>,
                    response: Response<JobDetailResponse>
                ) {
                    if (response.isSuccessful) {
                        val jobDetails = response.body()?.data
                        jobDetails?.let {
                            findViewById<TextView>(R.id.job_title).text = it.jobName
                            findViewById<TextView>(R.id.company_name).text = it.companyName
                            findViewById<TextView>(R.id.job_location).text = it.location
                            findViewById<TextView>(R.id.job_salary).text = it.salary
                            findViewById<TextView>(R.id.job_description).text = it.jobDescription
                            findViewById<TextView>(R.id.job_qualification).text = it.qualification
                            findViewById<TextView>(R.id.min_experience).text = it.minExperience
                            findViewById<TextView>(R.id.job_type).text = it.jobType

                            val jobImageView: ImageView = findViewById(R.id.job_image)
                            val imageUrl = it.image

                            Glide.with(this@JobDescriptionActivity)
                                .load(imageUrl)
                                .placeholder(R.drawable.placeholder_image)
                                .into(jobImageView)

                            intent.putExtra("job_name", it.jobName)
                            intent.putExtra("company_name", it.companyName)
                            intent.putExtra("job_image_url", imageUrl)
                        }
                    } else {
                        Toast.makeText(
                            this@JobDescriptionActivity,
                            "Failed to fetch job details",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<JobDetailResponse>, t: Throwable) {
                    Toast.makeText(
                        this@JobDescriptionActivity,
                        "Network error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}

