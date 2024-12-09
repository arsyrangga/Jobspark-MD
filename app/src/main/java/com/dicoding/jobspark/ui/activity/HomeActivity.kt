package com.dicoding.jobspark.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.jobspark.R
import com.dicoding.jobspark.data.remote.JobListResponse
import com.dicoding.jobspark.data.remote.RetrofitClient
import com.dicoding.jobspark.ui.adapter.JobAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerViewJobs: RecyclerView
    private lateinit var jobAdapter: JobAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest_home)

        findViewById<TextView>(R.id.login_link).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        recyclerViewJobs = findViewById(R.id.recyclerView_jobs)
        recyclerViewJobs.layoutManager = LinearLayoutManager(this)

        jobAdapter = JobAdapter(mutableListOf(), isSimplified = true, isEditable = false)
        recyclerViewJobs.adapter = jobAdapter

        fetchJobs()
    }

    private fun fetchJobs() {
        val call = RetrofitClient.instance.getJobsWithoutToken(page = 1, limit = 100)

        call.enqueue(object : Callback<JobListResponse> {
            override fun onResponse(
                call: Call<JobListResponse>,
                response: Response<JobListResponse>
            ) {
                if (response.isSuccessful) {
                    val jobListResponse = response.body()
                    if (jobListResponse != null && jobListResponse.data.isNotEmpty()) {
                        jobAdapter.updateData(jobListResponse.data)
                    } else {
                        Toast.makeText(this@HomeActivity, "No jobs found", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this@HomeActivity, "Failed to fetch jobs", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<JobListResponse>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}
