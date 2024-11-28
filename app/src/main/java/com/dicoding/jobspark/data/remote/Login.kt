package com.dicoding.jobspark.data.remote

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val status: Int,
    val message: String,
    val data: LoginData
)

data class LoginData(
    val id: Int,
    val full_name: String,
    val email: String,
    val token: String
)