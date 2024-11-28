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
