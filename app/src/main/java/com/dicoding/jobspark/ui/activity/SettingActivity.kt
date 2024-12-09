package com.dicoding.jobspark.ui.activity

import android.content.Intent
import android.os.Bundle

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.jobspark.R


class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val updatePassword: LinearLayout = findViewById(R.id.changePasswordLayout)
        updatePassword.setOnClickListener {
            val intent = Intent(this, EditPasswordActivity::class.java)
            startActivity(intent)
        }

        val logoutLayout: LinearLayout = findViewById(R.id.logoutLayout)
        logoutLayout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi Logout")
            .setMessage("Apakah Anda yakin ingin keluar?")
            .setPositiveButton("Ya") { _, _ ->
                logoutUser()
            }
            .setNegativeButton("Tidak", null)
            .show()
    }

    private fun logoutUser() {
        Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()

        clearUserData()

        Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show()

        finish()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun clearUserData() {
        val sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("TOKEN")
        editor.remove("FULL_NAME")
        editor.apply()

        val jobSharedPreferences = getSharedPreferences("SAVED_JOBS", MODE_PRIVATE)
        val jobEditor = jobSharedPreferences.edit()
        jobEditor.remove("SAVED_JOBS_LIST")
        jobEditor.apply()
    }


}
