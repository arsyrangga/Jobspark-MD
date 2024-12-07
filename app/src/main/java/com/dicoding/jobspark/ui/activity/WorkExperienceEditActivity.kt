package com.dicoding.jobspark.ui.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.jobspark.R
import com.dicoding.jobspark.data.remote.RetrofitClient
import com.dicoding.jobspark.data.remote.WorkExperienceRequest
import com.dicoding.jobspark.data.remote.WorkExperienceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class WorkExperienceEditActivity : AppCompatActivity() {

    private lateinit var jobTitleEditText: EditText
    private lateinit var companyEditText: EditText
    private lateinit var startDateEditText: EditText
    private lateinit var endDateEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var backButton: ImageView
    private lateinit var currentlyWorkingCheckBox: CheckBox
    private lateinit var deleteButton: Button // Tombol Hapus
    private lateinit var workExperienceId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.work_experience_edit)

        initializeViews()
        setupListeners()

        workExperienceId = intent.getStringExtra("WORK_EXPERIENCE_ID") ?: return

        loadWorkExperience()
    }

    private fun initializeViews() {
        jobTitleEditText = findViewById(R.id.jobTitleEditText)
        companyEditText = findViewById(R.id.companyEditText)
        startDateEditText = findViewById(R.id.startDateEditText)
        endDateEditText = findViewById(R.id.endDateEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        saveButton = findViewById(R.id.saveButton)
        backButton = findViewById(R.id.backButton)
        currentlyWorkingCheckBox = findViewById(R.id.currentlyWorkingCheckBox)
        deleteButton = findViewById(R.id.deleteButton) // Inisialisasi tombol hapus

        startDateEditText.isFocusable = false
        endDateEditText.isFocusable = false
    }

    private fun setupListeners() {
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val dateClickListener = { targetEditText: EditText ->
            showDatePickerDialog { date ->
                targetEditText.setText(date)
            }
        }

        startDateEditText.setOnClickListener { dateClickListener(startDateEditText) }
        endDateEditText.setOnClickListener { dateClickListener(endDateEditText) }

        saveButton.setOnClickListener {
            if (validateInput()) {
                updateWorkExperience()
            }
        }

        deleteButton.setOnClickListener {
            deleteWorkExperience()
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

    private fun loadWorkExperience() {
        val token = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
            .getString("TOKEN", "")

        if (token.isNullOrEmpty()) {
            showError("Silakan login terlebih dahulu")
            return
        }

        RetrofitClient.instance.getWorkExperienceById(workExperienceId, "Bearer $token")
            .enqueue(object : Callback<WorkExperienceResponse> {
                override fun onResponse(
                    call: Call<WorkExperienceResponse>,
                    response: Response<WorkExperienceResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        data?.let {
                            jobTitleEditText.setText(it.jobTitle)
                            companyEditText.setText(it.company)
                            startDateEditText.setText(it.startDate)
                            endDateEditText.setText(it.endDate)
                            descriptionEditText.setText(it.description)
                            currentlyWorkingCheckBox.isChecked = it.endDate == "Present"
                        }
                    } else {
                        showError("Gagal memuat data pengalaman kerja")
                    }
                }

                override fun onFailure(call: Call<WorkExperienceResponse>, t: Throwable) {
                    showError("Terjadi kesalahan jaringan: ${t.message}")
                }
            })
    }

    private fun updateWorkExperience() {
        val token = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
            .getString("TOKEN", "")

        if (token.isNullOrEmpty()) {
            showError("Silakan login terlebih dahulu")
            return
        }

        val request = WorkExperienceRequest(
            jobTitle = jobTitleEditText.text.toString().trim(),
            company = companyEditText.text.toString().trim(),
            startDate = startDateEditText.text.toString(),
            endDate = if (currentlyWorkingCheckBox.isChecked) "Present" else endDateEditText.text.toString(),
            description = descriptionEditText.text.toString().trim()
        )

        RetrofitClient.instance.updateWorkExperience(workExperienceId, "Bearer $token", request)
            .enqueue(object : Callback<WorkExperienceResponse> {
                override fun onResponse(
                    call: Call<WorkExperienceResponse>,
                    response: Response<WorkExperienceResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@WorkExperienceEditActivity, "Pengalaman kerja berhasil diperbarui", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        showError("Gagal memperbarui pengalaman kerja")
                    }
                }

                override fun onFailure(call: Call<WorkExperienceResponse>, t: Throwable) {
                    showError("Terjadi kesalahan jaringan: ${t.message}")
                }
            })
    }

    private fun deleteWorkExperience() {
        val token = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
            .getString("TOKEN", "")

        if (token.isNullOrEmpty()) {
            showError("Silakan login terlebih dahulu")
            return
        }

        RetrofitClient.instance.deleteWorkExperience(workExperienceId, "Bearer $token")
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@WorkExperienceEditActivity, "Pengalaman kerja berhasil dihapus", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        showError("Gagal menghapus pengalaman kerja")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    showError("Terjadi kesalahan jaringan: ${t.message}")
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
