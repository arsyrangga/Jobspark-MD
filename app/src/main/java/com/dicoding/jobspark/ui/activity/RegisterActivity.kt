package com.dicoding.jobspark.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.jobspark.R

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val nextButton: ImageButton = findViewById(R.id.nextButton)

        nextButton.setOnClickListener {
            val intent = Intent(this, CompleteProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
