package com.dicoding.jobspark.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.jobspark.R

class ModelSuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_6)

        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")
        val fullName = intent.getStringExtra("full_name")
        val birthDate = intent.getStringExtra("birth_date")
        val gender = intent.getStringExtra("gender")
        val address = intent.getStringExtra("address")
        val emergencyContact = intent.getStringExtra("emergency_contact")
        val capturedImageUrl = intent.getStringExtra("captured_image_url")

        val nextButton: Button = findViewById(R.id.nextButton)
        nextButton.setOnClickListener {
            val intent = Intent(this, InterestActivity::class.java).apply {
                putExtra("captured_image_url", capturedImageUrl)
                putExtra("email", email)
                putExtra("password", password)
                putExtra("full_name", fullName)
                putExtra("birth_date", birthDate)
                putExtra("gender", gender)
                putExtra("address", address)
                putExtra("emergency_contact", emergencyContact)
            }
            startActivity(intent)
            finish()
        }
    }
}
