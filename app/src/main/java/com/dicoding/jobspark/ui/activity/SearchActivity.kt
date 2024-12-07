package com.dicoding.jobspark.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.jobspark.R
import com.dicoding.jobspark.data.remote.ApiService
import com.dicoding.jobspark.data.remote.Job
import com.dicoding.jobspark.data.remote.JobListResponse
import com.dicoding.jobspark.data.remote.RetrofitClient
import com.dicoding.jobspark.ui.adapter.JobAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var jobAdapter: JobAdapter
    private val allJobsList = mutableListOf<Job>()
    private val filteredJobsList = mutableListOf<Job>()
    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val isSimplified = true

        recyclerView = findViewById(R.id.recyclerView_jobs)
        recyclerView.layoutManager = LinearLayoutManager(this)
        jobAdapter = JobAdapter(filteredJobsList, isSimplified)
        recyclerView.adapter = jobAdapter

        searchEditText = findViewById(R.id.search_bar)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://jobspark-api-299853389560.asia-southeast2.run.app/api/")  // Replace with your actual API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        fetchJobs()

        searchEditText.addTextChangedListener {
            val keyword = it.toString().trim()
            filterJobs(keyword)
        }

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    val intent = Intent(this, HomeScreenActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.search -> {
                    if (this::class.java != SearchActivity::class.java) {
                        val intent = Intent(this, SearchActivity::class.java)
                        startActivity(intent)
                    }
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
    }

    override fun onResume() {
        super.onResume()
        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.search
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
                        allJobsList.clear()
                        allJobsList.addAll(jobListResponse.data)
                        filterJobs("")
                    } else {
                        Toast.makeText(this@SearchActivity, "No jobs found", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this@SearchActivity, "Failed to fetch jobs", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<JobListResponse>, t: Throwable) {
                Toast.makeText(
                    this@SearchActivity,
                    "Network error: ${t.message}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    private fun filterJobs(keyword: String) {
        filteredJobsList.clear()
        if (keyword.isEmpty()) {
            filteredJobsList.addAll(allJobsList)
        } else {
            filteredJobsList.addAll(
                allJobsList.filter { it.job_name.contains(keyword, ignoreCase = true) }
            )
        }
        jobAdapter.notifyDataSetChanged()
    }
}

