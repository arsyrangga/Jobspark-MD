package com.dicoding.jobspark.data.remote

data class User(
    val id: Int,
    val full_name: String,
    val email: String,
    val created_at: String
)

data class UserData(
    val token: String,
    val user: User
)
