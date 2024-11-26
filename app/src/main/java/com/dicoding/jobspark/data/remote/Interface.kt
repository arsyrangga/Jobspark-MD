package com.dicoding.jobspark.data.remote

import kotlinx.coroutines.Job
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

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
data class Job(
    val id: Int,
    val title: String,
    val company: String,
    val location: String,
    val salary: String
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

data class UpdateAboutRequest(
    val about_me: String
)

data class UpdatePasswordRequest(
    val old_password: String,
    val new_password: String
)

data class ApplyJobRequest(
    val jobs_id: Int,
    val resume_id: Int
)

data class UserProfileResponse(
    val status: String,
    val data: User
)

data class UpdateResponse(
    val status: String,
    val message: String
)

data class JobListResponse(
    val status: String,
    val data: List<Job>
)

data class ApplyJobResponse(
    val status: String,
    val message: String
)

data class UploadResponse(
    val status: String,
    val message: String
)

data class Hobby(
    val id: Int,
    val name: String
)


interface ApiService {
    @POST("/api/auth/register")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @POST("/api/auth/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>
    // New Endpoints

    @GET("/api/auth/profile")
    fun getUserProfile(@Header("Authorization") token: String): Call<UserProfileResponse>

    @PUT("/api/auth/profile/about")
    fun updateAboutMe(
        @Header("Authorization") token: String,
        @Body updateAboutRequest: UpdateAboutRequest
    ): Call<UpdateResponse>

    @PUT("/api/auth/profile/password")
    fun updatePassword(
        @Header("Authorization") token: String,
        @Body updatePasswordRequest: UpdatePasswordRequest
    ): Call<UpdateResponse>

    @GET("/api/hobby")
    fun getHobbies(@Header("Authorization") token: String): Call<List<Hobby>>

    @GET("/api/jobs")
    fun getJobs(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<JobListResponse>

    @POST("/api/jobs/apply")
    fun applyJob(
        @Header("Authorization") token: String,
        @Body applyJobRequest: ApplyJobRequest
    ): Call<ApplyJobResponse>

    @Multipart
    @POST("/api/resume/upload")
    fun uploadResume(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): Call<UploadResponse>
}
