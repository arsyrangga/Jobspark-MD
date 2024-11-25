package com.dicoding.jobspark.ui.activity

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import android.widget.ImageButton
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.jobspark.R

class RegisterActivity : AppCompatActivity() {

    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val passwordToggle: ImageView = findViewById(R.id.passwordToggle)

        val confirmPasswordEditText: EditText = findViewById(R.id.confirmPasswordEditText)
        val passwordToggle2: ImageView = findViewById(R.id.passwordToggle2)

        val nextButton: ImageButton = findViewById(R.id.nextButton)

        passwordToggle.setOnClickListener {
            togglePasswordVisibility(passwordEditText, passwordToggle)
        }

        passwordToggle2.setOnClickListener {
            toggleConfirmPasswordVisibility(confirmPasswordEditText, passwordToggle2)
        }

        nextButton.setOnClickListener {
            val intent = Intent(this, CompleteProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun togglePasswordVisibility(passwordEditText: EditText, passwordToggle: ImageView) {
        if (isPasswordVisible) {
            passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            passwordToggle.setImageResource(R.drawable.ic_visibility_off)
        } else {
            passwordEditText.inputType = InputType.TYPE_CLASS_TEXT
            passwordToggle.setImageResource(R.drawable.ic_visibility)
        }
        passwordEditText.setSelection(passwordEditText.text.length)
        isPasswordVisible = !isPasswordVisible
    }

    private fun toggleConfirmPasswordVisibility(confirmPasswordEditText: EditText, passwordToggle2: ImageView) {
        if (isConfirmPasswordVisible) {
            confirmPasswordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            passwordToggle2.setImageResource(R.drawable.ic_visibility_off)
        } else {
            confirmPasswordEditText.inputType = InputType.TYPE_CLASS_TEXT
            passwordToggle2.setImageResource(R.drawable.ic_visibility)
        }
        confirmPasswordEditText.setSelection(confirmPasswordEditText.text.length)
        isConfirmPasswordVisible = !isConfirmPasswordVisible
    }
}
