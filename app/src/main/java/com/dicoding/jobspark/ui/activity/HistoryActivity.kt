package com.dicoding.jobspark.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.jobspark.R
import com.dicoding.jobspark.data.remote.JobHistoryResponse
import com.dicoding.jobspark.data.remote.RetrofitClient
import com.dicoding.jobspark.ui.adapter.JobHistoryAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryActivity : AppCompatActivity() {

    private lateinit var jobHistoryAdapter: JobHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val bottomNav: BottomNavigationView = findViewById(R.id.history_bottom_navigation)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_history)

        recyclerView.layoutManager = LinearLayoutManager(this)
        jobHistoryAdapter = JobHistoryAdapter { jobHistory ->
            val intent = Intent(this, DetailHistoryActivity::class.java)
            intent.putExtra("jobHistoryId", jobHistory.id)
            startActivity(intent)
        }
        recyclerView.adapter = jobHistoryAdapter

        loadJobHistory()

        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    val intent = Intent(this, HomeScreenActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.search -> {
                    val intent = Intent(this, SearchActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.history -> {
                    if (this::class.java != HistoryActivity::class.java) {
                        val intent = Intent(this, HistoryActivity::class.java)
                        startActivity(intent)
                    }
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
    }

    private fun loadJobHistory() {
        val token = getAuthToken()

        if (token != null) {
            val apiService = RetrofitClient.instance

            Log.d("HistoryActivity", "Sending request with token: $token")

            apiService.getJobHistory("Bearer $token")
                .enqueue(object : Callback<JobHistoryResponse> {
                    override fun onResponse(
                        call: Call<JobHistoryResponse>,
                        response: Response<JobHistoryResponse>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.data?.let {
                                it.forEach { jobHistory ->
                                    Log.d("HistoryActivity", "Response Data: $it")
                                    Log.d("HistoryActivity", "Job Title: ${jobHistory.job_name}")
                                }
                                jobHistoryAdapter.submitList(it)
                            }
                        } else {
                            Log.e(
                                "HistoryActivity",
                                "Failed response: ${response.code()} ${response.message()}"
                            )
                            Toast.makeText(
                                this@HistoryActivity,
                                "Failed to load history",
                                Toast.LENGTH_SHORT
                            ).show()
                            if (response.code() == 401) {
                                Toast.makeText(
                                    this@HistoryActivity,
                                    "Session expired. Please log in again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this@HistoryActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }

                    override fun onFailure(call: Call<JobHistoryResponse>, t: Throwable) {
                        Log.e("HistoryActivity", "API call failed: ${t.message}")
                        Toast.makeText(
                            this@HistoryActivity,
                            "Error: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        } else {
            Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getAuthToken(): String? {
        val sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", null)
        Log.d("HistoryActivity", "Token retrieved: $token")
        return token
    }

    override fun onResume() {
        super.onResume()
        val bottomNav: BottomNavigationView = findViewById(R.id.history_bottom_navigation)
        bottomNav.selectedItemId = R.id.history
    }
}
