package com.dicoding.jobspark.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.jobspark.data.remote.RetrofitClient
import com.dicoding.jobspark.data.remote.UpdatePasswordRequest
import com.dicoding.jobspark.data.remote.UpdateResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditPasswordViewModel : ViewModel() {

    private val _isOldPasswordVisible = MutableLiveData(false)
    val isOldPasswordVisible: LiveData<Boolean> get() = _isOldPasswordVisible

    private val _isNewPasswordVisible = MutableLiveData(false)
    val isNewPasswordVisible: LiveData<Boolean> get() = _isNewPasswordVisible

    private val _isConfirmPasswordVisible = MutableLiveData(false)
    val isConfirmPasswordVisible: LiveData<Boolean> get() = _isConfirmPasswordVisible

    private val _updatePasswordResponse = MutableLiveData<String>()
    val updatePasswordResponse: LiveData<String> get() = _updatePasswordResponse

    fun toggleOldPasswordVisibility() {
        _isOldPasswordVisible.value = _isOldPasswordVisible.value?.not()
    }

    fun toggleNewPasswordVisibility() {
        _isNewPasswordVisible.value = _isNewPasswordVisible.value?.not()
    }

    fun toggleConfirmPasswordVisibility() {
        _isConfirmPasswordVisible.value = _isConfirmPasswordVisible.value?.not()
    }

    fun updatePassword(oldPassword: String, newPassword: String, token: String) {
        val updatePasswordRequest = UpdatePasswordRequest(oldPassword, newPassword)
        RetrofitClient.instance.updatePassword("Bearer $token", updatePasswordRequest)
            .enqueue(object : Callback<UpdateResponse> {
                override fun onResponse(
                    call: Call<UpdateResponse>,
                    response: Response<UpdateResponse>
                ) {
                    if (response.isSuccessful) {
                        _updatePasswordResponse.value = "Password updated successfully."
                    } else {
                        _updatePasswordResponse.value =
                            "Failed to update password. Please try again."
                    }
                }

                override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                    Log.d("EditPasswordViewModel", "Failure: ${t.message}")
                    _updatePasswordResponse.value = "Network error. Please try again later."
                }
            })
    }
}
