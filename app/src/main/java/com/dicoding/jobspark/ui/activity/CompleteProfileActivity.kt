package com.dicoding.jobspark.ui.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.jobspark.R
import com.dicoding.jobspark.ui.viewmodel.CompleteProfileViewModel
import java.util.Calendar

class CompleteProfileActivity : AppCompatActivity() {

    private val viewModel: CompleteProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_2)

        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")

        val fullNameEditText = findViewById<EditText>(R.id.fullNameEditText)
        val birthDateEditText = findViewById<EditText>(R.id.birthDateEditText)
        val genderSpinner = findViewById<Spinner>(R.id.genderSpinner)
        val addressEditText = findViewById<EditText>(R.id.addressEditText)
        val emergencyContactEditText = findViewById<EditText>(R.id.emergencyContactEditText)

        val genderOptions = arrayOf("Laki-laki", "Perempuan")
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = genderAdapter

        viewModel.fullName.observe(this) { fullNameEditText.setText(it) }
        viewModel.birthDate.observe(this) { birthDateEditText.setText(it) }
        viewModel.gender.observe(this) { genderSpinner.setSelection(genderOptions.indexOf(it)) }
        viewModel.address.observe(this) { addressEditText.setText(it) }
        viewModel.emergencyContact.observe(this) { emergencyContactEditText.setText(it) }

        birthDateEditText.setOnClickListener {
            showDatePickerDialog()
        }

        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        findViewById<ImageButton>(R.id.nextButton).setOnClickListener {
            viewModel.fullName.value = fullNameEditText.text.toString()
            viewModel.birthDate.value = birthDateEditText.text.toString()
            viewModel.gender.value = genderSpinner.selectedItem.toString()
            viewModel.address.value = addressEditText.text.toString()
            viewModel.emergencyContact.value = emergencyContactEditText.text.toString()

            if (viewModel.isDataComplete()) {
                val intent = Intent(this, VerificationActivity::class.java).apply {
                    putExtra("email", email)
                    putExtra("password", password)
                    putExtra("full_name", viewModel.fullName.value)
                    putExtra("birth_date", viewModel.birthDate.value)
                    putExtra("gender", viewModel.gender.value)
                    putExtra("address", viewModel.address.value)
                    putExtra("emergency_contact", viewModel.emergencyContact.value)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedMonth = String.format("%02d", selectedMonth + 1)
                val formattedDay = String.format("%02d", selectedDay)
                val formattedDate = "$selectedYear-$formattedMonth-$formattedDay"
                viewModel.birthDate.value = formattedDate
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

}
