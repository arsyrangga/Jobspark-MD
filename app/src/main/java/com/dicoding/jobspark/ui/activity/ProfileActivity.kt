package com.dicoding.jobspark.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.jobspark.R
import com.dicoding.jobspark.ui.viewmodel.ProfileViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.hdodenhof.circleimageview.CircleImageView

@Suppress("UNUSED_EXPRESSION")
class ProfileActivity : AppCompatActivity() {

    private lateinit var fullNameTextView: TextView
    private lateinit var aboutDescriptionTextView: TextView
    private lateinit var editAboutDescription: EditText
    private lateinit var editIconAbout: ImageView
    private lateinit var saveDescriptionButton: ImageView
    private lateinit var profileImage: CircleImageView

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileImage = findViewById(R.id.profileImage)
        fullNameTextView = findViewById(R.id.profileName)
        aboutDescriptionTextView = findViewById(R.id.aboutDescription)
        editAboutDescription = findViewById(R.id.editAboutDescription)
        editIconAbout = findViewById(R.id.editIconAbout)
        saveDescriptionButton = findViewById(R.id.saveDescriptionButton)

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> startActivity(Intent(this, HomeScreenActivity::class.java))
                R.id.search -> startActivity(Intent(this, SearchActivity::class.java))
                R.id.history -> startActivity(Intent(this, HistoryActivity::class.java))
                R.id.saved -> startActivity(Intent(this, SavedActivity::class.java))
                R.id.profile -> if (this::class.java != ProfileActivity::class.java) {
                    startActivity(Intent(this, ProfileActivity::class.java))
                }
                else -> false
            }
            true
        }

        val settingButton: ImageView = findViewById(R.id.settingsIcon)
        settingButton.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }

        editIconAbout.setOnClickListener { startEditingDescription() }
        saveDescriptionButton.setOnClickListener { saveDescription() }

        observeViewModel()
        loadProfileData()
    }

    override fun onResume() {
        super.onResume()
        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.profile
    }

    private fun observeViewModel() {
        viewModel.fullName.observe(this) { name ->
            fullNameTextView.text = name
        }

        viewModel.imageuri.observe(this) { imageUrl ->
            Glide.with(this) // atau requireContext() jika di Fragment
                .load(imageUrl)
                .placeholder(R.drawable.pp)
                .error(R.drawable.pp)
                .into(profileImage)
        }

        viewModel.aboutDescription.observe(this) { description ->
            aboutDescriptionTextView.text = description
        }

        viewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun loadProfileData() {
        val sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", "")
        viewModel.loadProfileData(token)
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
            val token = sharedPreferences.getString("TOKEN", "")
            viewModel.updateAboutMe(token, newDescription)

            aboutDescriptionTextView.visibility = View.VISIBLE
            editAboutDescription.visibility = View.GONE
            saveDescriptionButton.visibility = View.GONE
        } else {
            Toast.makeText(this, "Deskripsi tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
    }
}