package com.dicoding.jobspark.data.remote

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

interface ApiService {

    @Multipart
    @POST("upload")
    fun recognizeImage(@Part file: MultipartBody.Part): Call<ImageRecognitionResponse>

    @POST("auth/register")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @POST("auth/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("auth/profile")
    fun getProfile(
        @Header("Authorization") token: String? = null,
    ): Call<UserResponse>

    @PUT("auth/profile/about")
    fun updateAboutMe(
        @Header("Authorization") token: String,
        @Body updateAboutRequest: UpdateAboutRequest
    ): Call<UpdateResponse>

    @PUT("auth/profile/password")
    fun updatePassword(
        @Header("Authorization") token: String,
        @Body updatePasswordRequest: UpdatePasswordRequest
    ): Call<UpdateResponse>

    @GET("jobs")
    fun getJobs(
        @Header("Authorization") token: String? = null,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<JobListResponse>

    @GET("jobs/{id}")
    fun getJobDetail(
        @Header("Authorization") token: String,
        @retrofit2.http.Path("id") jobId: Int
    ): Call<JobDetailResponse>

    @POST("jobs/apply")
    fun applyJob(
        @Header("Authorization") token: String,
        @Body applyJobRequest: ApplyJobRequest
    ): Call<ApplyJobResponse>

    @Multipart
    @POST("resume/upload")
    fun uploadResume(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): Call<UploadResponse>

    @GET("jobHistory")
    fun getJobHistory(
        @Header("Authorization") token: String
    ): Call<JobHistoryResponse>


    @GET("jobs")
    fun getJobsWithoutToken(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<JobListResponse>


}
