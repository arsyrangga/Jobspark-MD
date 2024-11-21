package com.dicoding.jobspark.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.jobspark.R

class VerificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_3)

        Handler().postDelayed({
            val intent = Intent(this, InterestActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}
