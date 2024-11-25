package com.dicoding.jobspark.data.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class RegisterRequest(
    val full_name: String,
    val email: String,
    val password: String,
    val about_me: String,
    val birth_date: String,
    val gender: String,
    val address: String,
    val emergency_number: String,
    val profile_img: String,
    val hobby: String,
    val interest: String,
    val special_ability: String,
    val health_condition: String
)

data class RegisterResponse(
    val status: String,
    val message: String,
    val data: UserData
)

data class UserData(
    val token: String,
    val user: User
)

data class User(
    val id: Int,
    val full_name: String,
    val email: String,
    val created_at: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val status: String,
    val message: String,
    val data: UserData
)

interface ApiService {
    @POST("/api/auth/register")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @POST("/api/auth/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>
}
