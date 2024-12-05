package com.dicoding.jobspark.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CompleteProfileViewModel : ViewModel() {
    val fullName = MutableLiveData<String>()
    val birthDate = MutableLiveData<String>()
    val gender = MutableLiveData<String>()
    val address = MutableLiveData<String>()
    val emergencyContact = MutableLiveData<String>()

    fun isDataComplete(): Boolean {
        return !fullName.value.isNullOrEmpty() &&
                !birthDate.value.isNullOrEmpty() &&
                !gender.value.isNullOrEmpty() &&
                !address.value.isNullOrEmpty() &&
                !emergencyContact.value.isNullOrEmpty()
    }
}
