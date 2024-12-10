package com.dicoding.jobspark.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.jobspark.data.remote.LoginRequest
import com.dicoding.jobspark.data.remote.LoginResponse
import com.dicoding.jobspark.data.remote.RetrofitClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> get() = _loginState

    fun loginUser(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)
        _loginState.value = LoginState.Loading

        RetrofitClient.instance.loginUser(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.data?.token
                    val fullName = response.body()?.data?.full_name
                    if (token != null && fullName != null) {
                        _loginState.value = LoginState.Success(token, fullName)
                    } else {
                        _loginState.value = LoginState.Error("Token or full name not received")
                    }
                } else {
                    val errorMessage = parseError(response)
                    _loginState.value = LoginState.Error(errorMessage)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _loginState.value = LoginState.Error("Network error: ${t.message}")
            }
        })
    }

    private fun parseError(response: Response<LoginResponse>): String {
        return try {
            val errorBody = response.errorBody()?.string()
            val errorJson = JSONObject(errorBody ?: "{}")
            errorJson.getString("message") ?: "Login failed"
        } catch (e: Exception) {
            "Login failed"
        }
    }
}

sealed class LoginState {
    data object Loading : LoginState()
    data class Success(val token: String, val fullName: String) : LoginState()
    data class Error(val message: String) : LoginState()
}
