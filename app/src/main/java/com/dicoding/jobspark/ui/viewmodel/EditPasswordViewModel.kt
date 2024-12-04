package com.dicoding.jobspark.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditPasswordViewModel : ViewModel() {

    private val _isOldPasswordVisible = MutableLiveData(false)
    val isOldPasswordVisible: LiveData<Boolean> get() = _isOldPasswordVisible

    private val _isNewPasswordVisible = MutableLiveData(false)
    val isNewPasswordVisible: LiveData<Boolean> get() = _isNewPasswordVisible

    private val _isConfirmPasswordVisible = MutableLiveData(false)
    val isConfirmPasswordVisible: LiveData<Boolean> get() = _isConfirmPasswordVisible

    fun toggleOldPasswordVisibility() {
        _isOldPasswordVisible.value = _isOldPasswordVisible.value?.not()
    }

    fun toggleNewPasswordVisibility() {
        _isNewPasswordVisible.value = _isNewPasswordVisible.value?.not()
    }

    fun toggleConfirmPasswordVisibility() {
        _isConfirmPasswordVisible.value = _isConfirmPasswordVisible.value?.not()
    }
}
