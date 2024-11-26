package com.dicoding.jobspark.ui.activity

import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.jobspark.R

class EditPasswordActivity : AppCompatActivity() {
    private var isOldPasswordVisible = false
    private var isNewPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_password)

        val oldPasswordEditText: EditText = findViewById(R.id.oldPasswordEditText)
        val oldPasswordToggle: ImageView = findViewById(R.id.oldPasswordToggle)
        val newPasswordEditText: EditText = findViewById(R.id.newPasswordEditText)
        val newPasswordToggle: ImageView = findViewById(R.id.newPasswordToggle)
        val confirmPasswordEditText: EditText = findViewById(R.id.confirmPasswordEditText)
        val confirmPasswordToggle: ImageView = findViewById(R.id.confirmPasswordToggle)
        val backButton: ImageView = findViewById(R.id.backButton)
        val updateButton: Button = findViewById(R.id.updateButton)

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        oldPasswordToggle.setOnClickListener {
            togglePasswordVisibility(oldPasswordEditText, oldPasswordToggle, ::isOldPasswordVisible)
            isOldPasswordVisible = !isOldPasswordVisible
        }
        newPasswordToggle.setOnClickListener {
            togglePasswordVisibility(newPasswordEditText, newPasswordToggle, ::isNewPasswordVisible)
            isNewPasswordVisible = !isNewPasswordVisible
        }
        confirmPasswordToggle.setOnClickListener {
            togglePasswordVisibility(
                confirmPasswordEditText,
                confirmPasswordToggle,
                ::isConfirmPasswordVisible
            )
            isConfirmPasswordVisible = !isConfirmPasswordVisible
        }
        updateButton.setOnClickListener {
        }
    }

    private fun togglePasswordVisibility(
        editText: EditText,
        toggleButton: ImageView,
        isVisible: () -> Boolean
    ) {
        if (isVisible()) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            toggleButton.setImageResource(R.drawable.ic_visibility_off)
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT
            toggleButton.setImageResource(R.drawable.ic_visibility)
        }
        editText.setSelection(editText.text.length)
    }
}