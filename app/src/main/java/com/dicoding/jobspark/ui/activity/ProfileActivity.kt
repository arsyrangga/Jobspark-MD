package com.dicoding.jobspark.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.jobspark.R
import com.dicoding.jobspark.data.remote.RetrofitClient
import com.dicoding.jobspark.data.remote.WorkExperienceResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var fullNameTextView: TextView
    private lateinit var jobPositionTextView: TextView
    private lateinit var companyNameTextView: TextView
    private lateinit var jobDurationTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        fullNameTextView = findViewById(R.id.profileName)
        jobPositionTextView = findViewById(R.id.jobPosition)
        companyNameTextView = findViewById(R.id.companyName)
        jobDurationTextView = findViewById(R.id.jobDuration)

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)

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
                    if (this::class.java != ProfileActivity::class.java) {
                        val intent = Intent(this, ProfileActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }

                else -> false
            }
        }

        val settingButton: ImageView = findViewById(R.id.settingsIcon)
        settingButton.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        val addWorkButton: ImageView = findViewById(R.id.addIconWork)
        addWorkButton.setOnClickListener {
            val intent = Intent(this, WorkExperienceActivity::class.java)
            startActivity(intent)
        }

        val editWorkButton: ImageView = findViewById(R.id.editIcon)
        editWorkButton.setOnClickListener {
            val intent = Intent(this, WorkExperienceEditActivity::class.java)
            startActivity(intent)
        }

        loadProfileData()
    }

    override fun onResume() {
        super.onResume()
        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.profile
        val sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val fullName = sharedPreferences.getString("FULL_NAME", "User")

        fullNameTextView.text = fullName
    }

    private fun loadProfileData() {
        val token = getSharedPreferences("USER_PREFS", MODE_PRIVATE).getString("TOKEN", "")

        if (token.isNullOrEmpty()) {
            showError("Silakan login terlebih dahulu")
            return
        }

        RetrofitClient.instance.getWorkExperienceById("1", "Bearer $token")
            .enqueue(object : Callback<WorkExperienceResponse> {
                override fun onResponse(
                    call: Call<WorkExperienceResponse>,
                    response: Response<WorkExperienceResponse>
                ) {
                    if (response.isSuccessful) {
                        val workExperience = response.body()
                        workExperience?.let {
                            jobPositionTextView.text = it.jobTitle
                            companyNameTextView.text = it.company
                            jobDurationTextView.text = "${it.startDate} - ${it.endDate}"
                        }
                    } else {
                        Log.e("ProfileActivity", "Failed to fetch work experience")
                        showError("Gagal memuat data")
                    }
                }

                override fun onFailure(call: Call<WorkExperienceResponse>, t: Throwable) {
                    Log.e("ProfileActivity", "Error: ${t.message}")
                    showError("Terjadi kesalahan jaringan: ${t.message}")
                }
            })
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
