package com.dicoding.jobspark.ui.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.jobspark.R
import com.dicoding.jobspark.data.remote.RetrofitClient
import com.dicoding.jobspark.data.remote.WorkExperienceRequest
import com.dicoding.jobspark.data.remote.WorkExperienceResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*

class WorkExperienceActivity : AppCompatActivity() {

    private lateinit var jobTitleEditText: EditText
    private lateinit var companyEditText: EditText
    private lateinit var startDateEditText: EditText
    private lateinit var endDateEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var currentlyWorkingCheckBox: CheckBox
    private lateinit var saveButton: Button
    private lateinit var birthDateIcon: ImageView
    private lateinit var birthDateIcon2: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.work_experience)

        initializeViews()
        setupListeners()
    }

    private fun initializeViews() {
        jobTitleEditText = findViewById(R.id.jobTitleEditText)
        companyEditText = findViewById(R.id.companyEditText)
        startDateEditText = findViewById(R.id.startDateEditText)
        endDateEditText = findViewById(R.id.endDateEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        currentlyWorkingCheckBox = findViewById(R.id.currentlyWorkingCheckBox)
        saveButton = findViewById(R.id.saveButton)
        birthDateIcon = findViewById(R.id.birthDateIcon)
        birthDateIcon2 = findViewById(R.id.birthDateIcon2)

        startDateEditText.isFocusable = false
        endDateEditText.isFocusable = false
    }

    private fun setupListeners() {
        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val dateClickListener = { targetEditText: EditText ->
            showDatePickerDialog { date ->
                targetEditText.setText(date)
            }
        }

        startDateEditText.setOnClickListener { dateClickListener(startDateEditText) }
        birthDateIcon.setOnClickListener { dateClickListener(startDateEditText) }

        val endDateClickListener = {
            if (!currentlyWorkingCheckBox.isChecked) {
                dateClickListener(endDateEditText)
            }
        }

        endDateEditText.setOnClickListener { endDateClickListener() }
        birthDateIcon2.setOnClickListener { endDateClickListener() }

        currentlyWorkingCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                endDateEditText.setText("Present")
                endDateEditText.isEnabled = false
                birthDateIcon2.isEnabled = false
            } else {
                endDateEditText.setText("")
                endDateEditText.isEnabled = true
                birthDateIcon2.isEnabled = true
            }
        }

        saveButton.setOnClickListener {
            if (validateInput()) {
                saveWorkExperience()
            }
        }
    }

    private fun validateInput(): Boolean {
        val jobTitle = jobTitleEditText.text.toString().trim()
        val company = companyEditText.text.toString().trim()
        val startDate = startDateEditText.text.toString()
        val endDate = endDateEditText.text.toString()
        val description = descriptionEditText.text.toString().trim()

        when {
            jobTitle.isEmpty() -> {
                showError("Masukkan judul pekerjaan")
                jobTitleEditText.requestFocus()
                return false
            }
            company.isEmpty() -> {
                showError("Masukkan nama perusahaan")
                companyEditText.requestFocus()
                return false
            }
            startDate.isEmpty() -> {
                showError("Pilih tanggal mulai")
                startDateEditText.requestFocus()
                return false
            }
            !currentlyWorkingCheckBox.isChecked && endDate.isEmpty() -> {
                showError("Pilih tanggal berakhir")
                endDateEditText.requestFocus()
                return false
            }
            description.isEmpty() -> {
                showError("Masukkan deskripsi pekerjaan")
                descriptionEditText.requestFocus()
                return false
            }
        }
        return true
    }

    private fun saveWorkExperience() {
        val token = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
            .getString("TOKEN", "")

        if (token.isNullOrEmpty()) {
            showError("Silakan login terlebih dahulu")
            return
        }

        saveButton.isEnabled = false

        val request = WorkExperienceRequest(
            jobTitle = jobTitleEditText.text.toString().trim(),
            company = companyEditText.text.toString().trim(),
            startDate = startDateEditText.text.toString(),
            endDate = if (currentlyWorkingCheckBox.isChecked) "Present"
            else endDateEditText.text.toString(),
            description = descriptionEditText.text.toString().trim()
        )

        Log.d("WorkExperience", "Request: ${Gson().toJson(request)}")

        RetrofitClient.instance.saveWorkExperience("Bearer $token", request)
            .enqueue(object : Callback<WorkExperienceResponse> {
                override fun onResponse(
                    call: Call<WorkExperienceResponse>,
                    response: Response<WorkExperienceResponse>
                ) {
                    saveButton.isEnabled = true
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@WorkExperienceActivity,
                            "Pengalaman kerja berhasil disimpan",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("WorkExperience", "Error Response: $errorBody")
                        when (response.code()) {
                            401 -> showError("Sesi telah berakhir. Silakan login kembali")
                            400 -> showError("Data yang dimasukkan tidak valid")
                            else -> showError("Terjadi kesalahan pada server")
                        }
                    }
                }

                override fun onFailure(call: Call<WorkExperienceResponse>, t: Throwable) {
                    saveButton.isEnabled = true
                    when (t) {
                        is UnknownHostException ->
                            showError("Tidak dapat terhubung ke server. Periksa koneksi internet Anda")
                        is SocketTimeoutException ->
                            showError("Koneksi timeout. Silakan coba lagi")
                        else -> {
                            showError("Terjadi kesalahan jaringan: ${t.message}")
                            Log.e("WorkExperience", "Network error", t)
                        }
                    }
                }
            })
    }

    private fun showDatePickerDialog(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = formatDate(selectedYear, selectedMonth, selectedDay)
                onDateSelected(formattedDate)
            },
            year,
            month,
            day
        ).show()
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
