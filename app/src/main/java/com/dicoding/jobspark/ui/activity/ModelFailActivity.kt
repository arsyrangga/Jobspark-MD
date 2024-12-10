package com.dicoding.jobspark.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.jobspark.R

class ModelFailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_5)

        val retryButton: Button = findViewById(R.id.retryButton)
        retryButton.setOnClickListener {
            val intent = Intent(this, VerificationActivity::class.java).apply {
                putExtra("email", intent.getStringExtra("email"))
                putExtra("password", intent.getStringExtra("password"))
                putExtra("full_name", intent.getStringExtra("full_name"))
                putExtra("birth_date", intent.getStringExtra("birth_date"))
                putExtra("gender", intent.getStringExtra("gender"))
                putExtra("address", intent.getStringExtra("address"))
                putExtra("emergency_contact", intent.getStringExtra("emergency_contact"))
            }
            startActivity(intent)
            finish()
        }


    }
}
