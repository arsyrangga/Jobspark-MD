package com.dicoding.jobspark.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.jobspark.data.remote.RetrofitClient
import com.dicoding.jobspark.data.remote.UpdateAboutRequest
import com.dicoding.jobspark.data.remote.UpdateResponse
import com.dicoding.jobspark.data.remote.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {

    private val _aboutDescription = MutableLiveData<String?>()
    val aboutDescription: MutableLiveData<String?> get() = _aboutDescription

    private val _fullName = MutableLiveData<String?>()
    val fullName: MutableLiveData<String?> get() = _fullName

    private val _imageuri = MutableLiveData<String?>()
    val imageuri: MutableLiveData<String?> get() = _imageuri

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun loadProfileData(token: String?) {
        if (token.isNullOrEmpty()) {
            _error.value = "Silakan login terlebih dahulu"
            return
        }



        if (token.isEmpty()) {
            _error.value = "Silakan login terlebih dahulu"
            return
        }

        RetrofitClient.instance.getProfile("Bearer $token")
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful) {
                        val about = response.body()?.data?.about_me
                        val name = response.body()?.data?.full_name
                        val image = response.body()?.data?.profile_img
                        _fullName.value = name
                        _aboutDescription.value = about
                        _imageuri.value = image

                    } else {
                        _error.value = "Gagal mengambil data profil"
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    _error.value = "Terjadi kesalahan jaringan: ${t.message}"
                }
            })
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