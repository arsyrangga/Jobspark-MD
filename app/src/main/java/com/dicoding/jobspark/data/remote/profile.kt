package com.dicoding.jobspark.data.remote

data class UserResponse(
    val status: Int,
    val message: String,
    val data: ProfileData
)

data class ProfileData(
    val id: Int,
    val full_name: String,
    val email: String,
    val about_me: String,
    val birth_date: String,
    val gender: String,
    val address: String,
    val emergency_number: String,
    val profile_img: String,
    val hobby: String,
    val special_ability: String,
    val health_condition: String,
    val role: String?,
    val created_at: String,
    val updated_at: String
)