package com.dicoding.jobspark.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.jobspark.R
import com.dicoding.jobspark.ui.adapter.JobAdapter
import com.dicoding.jobspark.ui.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerViewJobs: RecyclerView
    private lateinit var jobAdapter: JobAdapter

    private val homeViewModel: HomeViewModel by viewModels()

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

        observeViewModel()

        homeViewModel.fetchJobs(page = 1, limit = 100)
    }

    private fun observeViewModel() {
        homeViewModel.jobList.observe(this) { jobs ->
            if (jobs.isNotEmpty()) {
                jobAdapter.updateData(jobs)
            }
        }

        homeViewModel.errorMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        homeViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                Toast.makeText(this, "Loading jobs...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
