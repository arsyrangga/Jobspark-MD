package com.dicoding.jobspark.ui.viewmodel

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

    fun fetchJobHistoryDetail(id: Int) {
        val apiService = RetrofitClient.instance
        apiService.getJobHistoryDetail(id).enqueue(object : Callback<JobHistoryDetail> {
            override fun onResponse(
                call: Call<JobHistoryDetail>,
                response: Response<JobHistoryDetail>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _jobHistoryDetail.postValue(it)
                    }
                } else {
                    _errorMessage.postValue("Failed to load details")
                }
            }

            override fun onFailure(call: Call<JobHistoryDetail>, t: Throwable) {
                _errorMessage.postValue("Error: ${t.message}")
            }
        })
    }
}
