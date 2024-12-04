package com.dicoding.jobspark.ui.activity

import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.jobspark.R
import com.dicoding.jobspark.ui.viewmodel.EditPasswordViewModel

class EditPasswordActivity : AppCompatActivity() {

    private val viewModel: EditPasswordViewModel by viewModels()

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

        viewModel.isOldPasswordVisible.observe(this) { isVisible ->
            updatePasswordVisibility(oldPasswordEditText, oldPasswordToggle, isVisible)
        }

        oldPasswordToggle.setOnClickListener {
            viewModel.toggleOldPasswordVisibility()
        }

        viewModel.isNewPasswordVisible.observe(this) { isVisible ->
            updatePasswordVisibility(newPasswordEditText, newPasswordToggle, isVisible)
        }

        newPasswordToggle.setOnClickListener {
            viewModel.toggleNewPasswordVisibility()
        }

        viewModel.isConfirmPasswordVisible.observe(this) { isVisible ->
            updatePasswordVisibility(confirmPasswordEditText, confirmPasswordToggle, isVisible)
        }

        confirmPasswordToggle.setOnClickListener {
            viewModel.toggleConfirmPasswordVisibility()
        }

        updateButton.setOnClickListener {
            // Implement update password logic
        }
    }

    private fun updatePasswordVisibility(
        editText: EditText,
        toggleButton: ImageView,
        isVisible: Boolean
    ) {
        if (isVisible) {
            editText.inputType = InputType.TYPE_CLASS_TEXT
            toggleButton.setImageResource(R.drawable.ic_visibility)
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            toggleButton.setImageResource(R.drawable.ic_visibility_off)
        }
        editText.setSelection(editText.text.length)
    }
}
