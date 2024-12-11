package com.dicoding.jobspark.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.jobspark.data.remote.RetrofitClient
import com.dicoding.jobspark.data.remote.UpdateAboutRequest
import com.dicoding.jobspark.data.remote.UpdateResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {

    private val _aboutDescription = MutableLiveData<String>()
    val aboutDescription: LiveData<String> get() = _aboutDescription

    private val _fullName = MutableLiveData<String>()
    val fullName: LiveData<String> get() = _fullName

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun loadProfileData(token: String?, aboutDescription: String) {
        if (token.isNullOrEmpty()) {
            _error.value = "Silakan login terlebih dahulu"
            return
        }
        _aboutDescription.value = aboutDescription
    }

    fun loadFullName(fullName: String) {
        _fullName.value = fullName
    }

    fun updateAboutMe(token: String?, description: String) {
        if (token.isNullOrEmpty()) {
            _error.value = "Silakan login terlebih dahulu"
            return
        }

        val request = UpdateAboutRequest(about_me = description)

        RetrofitClient.instance.updateAboutMe("Bearer $token", request)
            .enqueue(object : Callback<UpdateResponse> {
                override fun onResponse(
                    call: Call<UpdateResponse>,
                    response: Response<UpdateResponse>
                ) {
                    if (response.isSuccessful) {
                        _aboutDescription.value = description
                    } else {
                        _error.value = "Gagal memperbarui deskripsi"
                    }
                }

                override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                    _error.value = "Terjadi kesalahan jaringan: ${t.message}"
                }
            })
    }
}