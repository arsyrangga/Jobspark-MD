package com.dicoding.jobspark.data.remote

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("/api/auth/register")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @POST("/api/auth/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

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
        @Header("Authorization") token: String? = null,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<JobListResponse>

    @GET("/api/jobs/{id}")
    fun getJobDetail(
        @Header("Authorization") token: String,
        @retrofit2.http.Path("id") jobId: Int
    ): Call<JobDetailResponse>

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

    @GET("api/jobHistory")
    fun getJobHistory(): Call<List<JobHistory>>

    @GET("api/jobHistory/{id}")
    fun getJobHistoryDetail(@Path("id") id: Int): Call<JobHistoryDetail>

    @POST("user/save-job/{jobId}")
    fun saveJob(
        @Header("Authorization") token: String,
        @Path("jobId") jobId: Int
    ): Call<Void>

    @DELETE("user/delete-job/{jobId}")
    fun deleteSavedJob(
        @Header("Authorization") token: String,
        @Path("jobId") jobId: Int
    ): Call<Void>

    @GET("jobs")
    fun getJobsWithoutToken(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<JobListResponse>
}
