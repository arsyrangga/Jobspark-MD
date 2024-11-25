package com.dicoding.jobspark.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.jobspark.R

class CompleteProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_2)

        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")

        val fullNameEditText = findViewById<EditText>(R.id.fullNameEditText)
        val birthDateEditText = findViewById<EditText>(R.id.birthDateEditText)
        val genderSpinner = findViewById<Spinner>(R.id.genderSpinner)

        val genderOptions = arrayOf("Laki-laki", "Perempuan", "Lainnya")
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = genderAdapter
        val addressEditText = findViewById<EditText>(R.id.addressEditText)
        val emergencyContactEditText = findViewById<EditText>(R.id.emergencyContactEditText)

        val nextButton: ImageButton = findViewById(R.id.nextButton)

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
}
