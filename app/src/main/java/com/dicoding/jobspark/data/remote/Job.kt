package com.dicoding.jobspark.data.remote

data class Job(
    val id: Int,
    val job_name: String,
    val image: String,
    val company_name: String,
    val location: String,
    val position: String,
    val job_type: String,
    val salary: String
)

data class JobListResponse(
    val status: Int,
    val message: String,
    val data: List<Job>,
    val pagination: Pagination
)

data class ApplyJobRequest(
    val jobs_id: Int,
    val resume_id: Int
)

data class ApplyJobResponse(
    val status: String,
    val message: String
)

data class UploadResponse(
    val status: String,
    val message: String
)

data class Pagination(
    val total_data: Int,
    val total_pages: Int,
    val current_page: Int,
    val limit: Int
)

