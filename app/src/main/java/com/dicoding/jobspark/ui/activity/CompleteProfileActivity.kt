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
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.jobspark.R
import java.util.Calendar

class CompleteProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_2)

        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")

        val fullNameEditText = findViewById<EditText>(R.id.fullNameEditText)
        val birthDateEditText = findViewById<EditText>(R.id.birthDateEditText)
        birthDateEditText.setOnClickListener {
            showDatePickerDialog(birthDateEditText)
        }
        val genderSpinner = findViewById<Spinner>(R.id.genderSpinner)
        val genderOptions = arrayOf("Laki-laki", "Perempuan")
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = genderAdapter
        val addressEditText = findViewById<EditText>(R.id.addressEditText)
        val emergencyContactEditText = findViewById<EditText>(R.id.emergencyContactEditText)

        val nextButton: ImageButton = findViewById(R.id.nextButton)
        val backButton: ImageView = findViewById(R.id.backButton)

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        nextButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString()
            val birthDate = birthDateEditText.text.toString()
            val gender = genderSpinner.selectedItem.toString()
            val address = addressEditText.text.toString()
            val emergencyContact = emergencyContactEditText.text.toString()

            if (fullName.isNotEmpty() && birthDate.isNotEmpty() && gender.isNotEmpty() && address.isNotEmpty() && emergencyContact.isNotEmpty()) {
                val intent = Intent(this, VerificationActivity::class.java).apply {
                    putExtra("email", email)
                    putExtra("password", password)
                    putExtra("full_name", fullName)
                    putExtra("birth_date", birthDate)
                    putExtra("gender", gender)
                    putExtra("address", address)
                    putExtra("emergency_contact", emergencyContact)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "$selectedYear/${selectedMonth + 1}/$selectedDay"
                editText.setText(formattedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
}

