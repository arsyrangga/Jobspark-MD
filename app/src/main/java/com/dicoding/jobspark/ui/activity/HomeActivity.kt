package com.dicoding.jobspark.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        recyclerViewJobs = findViewById(R.id.recyclerView_jobs)
        recyclerViewJobs.layoutManager = LinearLayoutManager(this)

        fetchJobs()
    }

    private fun fetchJobs() {
        RetrofitClient.instance.getJobs("",1, 10)
            .enqueue(object : Callback<JobListResponse> {
                override fun onResponse(
                    call: Call<JobListResponse>,
                    response: Response<JobListResponse>
                ) {
                    if (response.isSuccessful) {
                        val jobListResponse = response.body()
                        Log.d("API_RESPONSE", jobListResponse.toString())
                        if (jobListResponse != null && jobListResponse.data.isNotEmpty()) {
                            jobAdapter = JobAdapter(jobListResponse.data)
                            recyclerViewJobs.adapter = jobAdapter
                        } else {
                            Toast.makeText(
                                this@HomeActivity,
                                "No jobs found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@HomeActivity,
                            "Failed to fetch jobs",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<JobListResponse>, t: Throwable) {
                    Toast.makeText(
                        this@HomeActivity,
                        "Network error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

}
