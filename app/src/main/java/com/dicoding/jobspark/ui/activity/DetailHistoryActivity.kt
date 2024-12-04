package com.dicoding.jobspark.ui.activity

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.jobspark.R
import com.dicoding.jobspark.ui.viewmodel.DetailHistoryViewModel

class DetailHistoryActivity : AppCompatActivity() {

    private lateinit var jobTitleText: TextView
    private lateinit var companyNameText: TextView
    private lateinit var statusText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var dateAppliedText: TextView

    private val viewModel: DetailHistoryViewModel by viewModels()

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
            viewModel.fetchJobHistoryDetail(jobHistoryId)
            observeViewModel()
        } else {
            Toast.makeText(this, "Invalid Job History ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        viewModel.jobHistoryDetail.observe(this) { jobHistoryDetail ->
            jobTitleText.text = jobHistoryDetail.jobTitle
            companyNameText.text = jobHistoryDetail.companyName
            statusText.text = jobHistoryDetail.status
            descriptionText.text = jobHistoryDetail.description
            dateAppliedText.text = jobHistoryDetail.dateApplied
        }

        viewModel.errorMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
