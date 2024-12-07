package com.dicoding.jobspark.data.remote

data class WorkExperienceRequest(
	val jobTitle: String,
	val company: String,
	val startDate: String,
	val endDate: String,
	val description: String
)

data class WorkExperienceResponse(
	val id: Int,
	val jobTitle: String,
	val company: String,
	val startDate: String,
	val endDate: String,
	val description: String
)

