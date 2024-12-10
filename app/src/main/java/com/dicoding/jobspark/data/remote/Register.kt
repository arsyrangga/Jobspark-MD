package com.dicoding.jobspark.data.remote

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

data class ImageRecognitionResponse(
    val status: Int,
    val message: String,
    val data: ImageData
)

data class ImageData(
    val url: String
)


data class RegisterResponse(
    val status: String,
    val message: String,
    val data: UserData
)