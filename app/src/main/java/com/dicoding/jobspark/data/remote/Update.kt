package com.dicoding.jobspark.data.remote

data class UpdateAboutRequest(
    val about_me: String
)

data class UpdatePasswordRequest(
    val old_password: String,
    val new_password: String
)

data class UpdateResponse(
    val status: String,
    val message: String
)