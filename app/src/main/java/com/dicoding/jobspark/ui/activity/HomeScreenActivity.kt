package com.dicoding.jobspark.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.jobspark.R
import com.dicoding.jobspark.data.remote.JobListResponse
import com.dicoding.jobspark.data.remote.RetrofitClient
import com.dicoding.jobspark.ui.adapter.JobAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeScreenActivity : AppCompatActivity() {

    private lateinit var recyclerViewJobs: RecyclerView
    private lateinit var jobAdapter: JobAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    if (this::class.java != HomeScreenActivity::class.java) {
                        val intent = Intent(this, HomeScreenActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }

                R.id.search -> {
                    val intent = Intent(this, SearchActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.history -> {
                    val intent = Intent(this, HistoryActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.saved -> {
                    val intent = Intent(this, SavedActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        recyclerViewJobs = findViewById(R.id.recyclerView_jobs)
        recyclerViewJobs.layoutManager = LinearLayoutManager(this)

        fetchJobs()
    }

    private fun fetchJobs() {
        val sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", "")

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitClient.instance.getJobs("Bearer $token", 1, 10)
            .enqueue(object : Callback<JobListResponse> {
                override fun onResponse(
                    call: Call<JobListResponse>,
                    response: Response<JobListResponse>
                ) {
                    if (response.isSuccessful) {
                        val jobListResponse = response.body()
                        if (jobListResponse != null && jobListResponse.data.isNotEmpty()) {
                            jobAdapter = JobAdapter(jobListResponse.data, isSimplified = false)
                            recyclerViewJobs.adapter = jobAdapter
                        } else {
                            Toast.makeText(
                                this@HomeScreenActivity,
                                "No jobs found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@HomeScreenActivity,
                            "Failed to fetch jobs",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<JobListResponse>, t: Throwable) {
                    Toast.makeText(
                        this@HomeScreenActivity,
                        "Network error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
