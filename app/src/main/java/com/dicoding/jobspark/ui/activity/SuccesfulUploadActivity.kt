package com.dicoding.jobspark.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.jobspark.R

class SuccessUploadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.successful_upload)

        val backToHomeButton: Button = findViewById(R.id.back_to_home_button)
        backToHomeButton.setOnClickListener {
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
