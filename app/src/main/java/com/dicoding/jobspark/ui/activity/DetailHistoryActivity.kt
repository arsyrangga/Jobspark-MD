package com.dicoding.jobspark.ui.activity

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.jobspark.R
import com.dicoding.jobspark.data.remote.JobHistoryDetail
import com.dicoding.jobspark.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailHistoryActivity : AppCompatActivity() {

    private lateinit var jobTitleText: TextView
    private lateinit var companyNameText: TextView
    private lateinit var statusText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var dateAppliedText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_history)

        jobTitleText = findViewById(R.id.job_title_text)
        companyNameText = findViewById(R.id.company_name_text)
        statusText = findViewById(R.id.status_text)
        descriptionText = findViewById(R.id.description_text)
        dateAppliedText = findViewById(R.id.date_applied_text)

        val jobHistoryId = intent.getIntExtra("jobHistoryId", -1)

        if (jobHistoryId != -1) {
            fetchJobHistoryDetail(jobHistoryId)
        } else {
            Toast.makeText(this, "Invalid Job History ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchJobHistoryDetail(id: Int) {
        val apiService = RetrofitClient.instance
        apiService.getJobHistoryDetail(id).enqueue(object : Callback<JobHistoryDetail> {
            override fun onResponse(
                call: Call<JobHistoryDetail>,
                response: Response<JobHistoryDetail>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { jobHistoryDetail ->
                        jobTitleText.text = jobHistoryDetail.jobTitle
                        companyNameText.text = jobHistoryDetail.companyName
                        statusText.text = jobHistoryDetail.status
                        descriptionText.text = jobHistoryDetail.description
                        dateAppliedText.text = jobHistoryDetail.dateApplied
                    }
                } else {
                    Toast.makeText(
                        this@DetailHistoryActivity,
                        "Failed to load details",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<JobHistoryDetail>, t: Throwable) {
                Toast.makeText(
                    this@DetailHistoryActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
