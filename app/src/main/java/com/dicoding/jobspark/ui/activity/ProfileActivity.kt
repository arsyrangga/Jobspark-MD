package com.dicoding.jobspark.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.jobspark.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
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
    }

    override fun onResume() {
        super.onResume()
        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.profile
    }
}