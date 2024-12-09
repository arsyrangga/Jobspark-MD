package com.dicoding.jobspark.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.jobspark.data.remote.JobHistoryDetail
import com.dicoding.jobspark.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailHistoryViewModel : ViewModel() {

    private val _jobHistoryDetail = MutableLiveData<JobHistoryDetail>()
    val jobHistoryDetail: LiveData<JobHistoryDetail> get() = _jobHistoryDetail

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchJobHistoryDetail(jobHistoryId: Int) {
        val apiService = RetrofitClient.instance

        apiService.getJobHistoryDetail(jobHistoryId).enqueue(object : Callback<JobHistoryDetail> {
            override fun onResponse(
                call: Call<JobHistoryDetail>,
                response: Response<JobHistoryDetail>
            ) {
                if (response.isSuccessful) {
                    Log.d("DetailHistoryViewModel", "Response received: ${response.body()}")
                    _jobHistoryDetail.value = response.body()
                } else {
                    Log.e("DetailHistoryViewModel", "Error: ${response.code()}")
                    _errorMessage.value = "Failed to load job history details"
                }
            }

            override fun onFailure(call: Call<JobHistoryDetail>, t: Throwable) {
                Log.e("DetailHistoryViewModel", "Failure: ${t.message}")
                _errorMessage.value = "Error: ${t.message}"
            }
        })
    }
}
