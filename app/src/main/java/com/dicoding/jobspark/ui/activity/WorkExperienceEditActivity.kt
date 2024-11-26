package com.dicoding.jobspark.ui.activity

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.jobspark.R

class WorkExperienceEditActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.work_experience_edit)

        val backButton: ImageView = findViewById(R.id.backButton)

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}