package com.dicoding.jobspark.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.jobspark.R
import com.dicoding.jobspark.data.remote.LoginRequest
import com.dicoding.jobspark.data.remote.LoginResponse
import com.dicoding.jobspark.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton: Button = findViewById(R.id.loginButton)
        val registerButton: Button = findViewById(R.id.regbutton)
        val emailEditText = findViewById<EditText>(R.id.emailLoginEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordLoginEditText)
        val passwordToggle: ImageView = findViewById(R.id.passwordToggle)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        passwordToggle.setOnClickListener {
            togglePasswordVisibility(passwordEditText, passwordToggle)
        }

    }

    private fun togglePasswordVisibility(passwordEditText: EditText, passwordToggle: ImageView) {
        if (isPasswordVisible) {
            passwordEditText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            passwordToggle.setImageResource(R.drawable.ic_visibility_off)
        } else {
            passwordEditText.inputType = InputType.TYPE_CLASS_TEXT
            passwordToggle.setImageResource(R.drawable.ic_visibility)
        }
        passwordEditText.setSelection(passwordEditText.text.length)
        isPasswordVisible = !isPasswordVisible
    }

    private fun loginUser(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)
        RetrofitClient.instance.loginUser(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    val token = loginResponse?.data?.token

                    if (token != null) {
                        saveToken(token)
                        Log.d("Login", "Token saved: $token")

                        // Pindah ke HomeScreenActivity
                        val intent = Intent(this@LoginActivity, HomeScreenActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Token not received", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("Login", "Failure: ${t.message}")
                Toast.makeText(this@LoginActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun saveToken(token: String) {
        val sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("TOKEN", token)
        editor.apply()
    }


}
