package com.dicoding.jobspark.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.jobspark.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            goToNextActivity()
            finish()
        }, 3000)
    }

    private fun goToNextActivity(){
        Intent(this, MainActivity::class.java).also{
            startActivity(it)
            finish()
        }
    }
}