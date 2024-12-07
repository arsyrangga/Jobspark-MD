package com.dicoding.jobspark.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.jobspark.R
import com.dicoding.jobspark.data.remote.RetrofitClient
import com.dicoding.jobspark.data.remote.UpdateAboutRequest
import com.dicoding.jobspark.data.remote.UpdateResponse
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

    private lateinit var aboutDescriptionTextView: TextView
    private lateinit var editAboutDescription: EditText
    private lateinit var editIconAbout: ImageView
    private lateinit var saveDescriptionButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        fullNameTextView = findViewById(R.id.profileName)
        jobPositionTextView = findViewById(R.id.jobPosition)
        companyNameTextView = findViewById(R.id.companyName)
        jobDurationTextView = findViewById(R.id.jobDuration)

        aboutDescriptionTextView = findViewById(R.id.aboutDescription)
        editAboutDescription = findViewById(R.id.editAboutDescription)
        editIconAbout = findViewById(R.id.editIconAbout)
        saveDescriptionButton = findViewById(R.id.saveDescriptionButton)

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

        editIconAbout.setOnClickListener {
            startEditingDescription()
        }

        saveDescriptionButton.setOnClickListener {
            saveDescription()
        }
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
                        showError("Gagal memuat data pekerjaan")
                    }
                }

                override fun onFailure(call: Call<WorkExperienceResponse>, t: Throwable) {
                    Log.e("ProfileActivity", "Error: ${t.message}")
                    showError("Terjadi kesalahan jaringan: ${t.message}")
                }
            })

        val sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val aboutDescription = sharedPreferences.getString("ABOUT_DESCRIPTION", "Deskripsi tentang saya")
        aboutDescriptionTextView.text = aboutDescription
    }

    private fun startEditingDescription() {
        aboutDescriptionTextView.visibility = View.GONE
        editAboutDescription.visibility = View.VISIBLE
        saveDescriptionButton.visibility = View.VISIBLE
        editAboutDescription.setText(aboutDescriptionTextView.text)
    }

    private fun saveDescription() {
        val newDescription = editAboutDescription.text.toString()

        if (newDescription.isNotBlank()) {
            val sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
            sharedPreferences.edit().putString("ABOUT_DESCRIPTION", newDescription).apply()

            updateAboutMe(newDescription)

            aboutDescriptionTextView.text = newDescription
            aboutDescriptionTextView.visibility = View.VISIBLE
            editAboutDescription.visibility = View.GONE
            saveDescriptionButton.visibility = View.GONE
        } else {
            Toast.makeText(this, "Deskripsi tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateAboutMe(description: String) {
        val token = getSharedPreferences("USER_PREFS", MODE_PRIVATE).getString("TOKEN", "")
        if (token.isNullOrEmpty()) {
            showError("Silakan login terlebih dahulu")
            return
        }

        val request = UpdateAboutRequest(about_me = description)

        RetrofitClient.instance.updateAboutMe("Bearer $token", request)
            .enqueue(object : Callback<UpdateResponse> {
                override fun onResponse(
                    call: Call<UpdateResponse>,
                    response: Response<UpdateResponse>
                ) {}

                override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                    showError("Terjadi kesalahan jaringan: ${t.message}")
                }
            })
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
