package com.dicoding.jobspark.data.remote

data class JobListResponse(
    val status: Int,
    val message: String,
    val data: List<Job>,
    val pagination: Pagination
)

data class Pagination(
    val total_data: Int,
    val total_pages: Int,
    val current_page: Int,
    val limit: Int
)