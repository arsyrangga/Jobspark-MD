package com.dicoding.jobspark.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.jobspark.R
import com.dicoding.jobspark.data.remote.JobHistory
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
        val apiService = RetrofitClient.instance
        apiService.getJobHistory().enqueue(object : Callback<List<JobHistory>> {
            override fun onResponse(
                call: Call<List<JobHistory>>,
                response: Response<List<JobHistory>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        jobHistoryAdapter.submitList(it)
                    }
                } else {
                    Toast.makeText(
                        this@HistoryActivity,
                        "Failed to load history",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<JobHistory>>, t: Throwable) {
                Toast.makeText(this@HistoryActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val bottomNav: BottomNavigationView = findViewById(R.id.history_bottom_navigation)
        bottomNav.selectedItemId = R.id.history
    }
}