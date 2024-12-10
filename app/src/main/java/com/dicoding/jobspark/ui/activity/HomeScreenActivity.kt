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
import com.dicoding.jobspark.ui.viewmodel.HomeScreenViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeScreenActivity : AppCompatActivity() {

    private lateinit var recyclerViewJobs: RecyclerView
    private lateinit var jobAdapter: JobAdapter
    private lateinit var greetingTextView: TextView

    private val homeScreenViewModel: HomeScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        greetingTextView = findViewById(R.id.greeting_text)

        val sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val fullName = sharedPreferences.getString("FULL_NAME", "User")

        greetingTextView.text = getString(R.string.hello, fullName)

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

        jobAdapter = JobAdapter(mutableListOf(), isSimplified = false)
        recyclerViewJobs.adapter = jobAdapter

        observeViewModel()

        val token = sharedPreferences.getString("TOKEN", "")
        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }
        homeScreenViewModel.fetchJobs(token, 1, 10)
    }

    private fun observeViewModel() {
        homeScreenViewModel.jobList.observe(this) { jobs ->
            if (jobs.isNotEmpty()) {
                jobAdapter.updateData(jobs)
            }
        }

        homeScreenViewModel.errorMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        homeScreenViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                Toast.makeText(this, "Loading jobs...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
