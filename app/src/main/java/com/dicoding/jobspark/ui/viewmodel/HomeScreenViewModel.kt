package com.dicoding.jobspark.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.jobspark.data.remote.Job
import com.dicoding.jobspark.data.remote.JobListResponse
import com.dicoding.jobspark.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeScreenViewModel : ViewModel() {

    private val _jobList = MutableLiveData<List<Job>>()
    val jobList: LiveData<List<Job>> get() = _jobList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun fetchJobs(token: String, page: Int, limit: Int) {
        _isLoading.value = true

        RetrofitClient.instance.getJobs("Bearer $token", page, limit)
            .enqueue(object : Callback<JobListResponse> {
                override fun onResponse(
                    call: Call<JobListResponse>,
                    response: Response<JobListResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val jobListResponse = response.body()
                        if (jobListResponse != null && jobListResponse.data.isNotEmpty()) {
                            _jobList.value = jobListResponse.data
                        } else {
                            _errorMessage.value = "No jobs found"
                        }
                    } else {
                        _errorMessage.value = "Failed to fetch jobs"
                    }
                }

                override fun onFailure(call: Call<JobListResponse>, t: Throwable) {
                    _isLoading.value = false
                    _errorMessage.value = "Network error: ${t.message}"
                }
            })
    }
}
