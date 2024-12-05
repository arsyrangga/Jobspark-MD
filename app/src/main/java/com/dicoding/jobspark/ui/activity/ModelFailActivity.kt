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
            val intent = Intent(this, VerificationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
