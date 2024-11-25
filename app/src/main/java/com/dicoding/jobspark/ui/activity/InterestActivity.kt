package com.dicoding.jobspark.ui.activity

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.jobspark.R

class InterestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_last)

        val backButton: ImageView = findViewById(R.id.backButton)

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val hobbies = listOf(
            "Seni", "Musik", "Menari", "Kerajinan tangan", "Berkebun", "Memasak",
            "Merawat hewan", "Menggambar atau melukis", "Teknologi dasar (komputer, tablet)",
            "Fotografi", "Lingkungan dan alam"
        )

        val specialSkills = listOf(
            "Membaca dasar", "Menulis sederhana", "Mendengarkan instruksi",
            "Berbicara dengan orang lain", "Kerja tim", "Ketelitian", "Koordinasi tangan dan mata",
            "Kesabaran", "Kreativitas", "Mengikuti langkah-langkah sederhana",
            "Menggunakan alat sederhana", "Pemecahan masalah sederhana", "Mandiri dengan supervisi",
            "Menjaga kebersihan", "Mengatur waktu", "Ketahanan fisik ringan"
        )

        val healthConditions = listOf(
            "Menggunakan kursi roda", "Membutuhkan kacamata", "Menggunakan alat bantu dengar",
            "Kesulitan berdiri terlalu lama", "Tidak boleh mengangkat beban berat",
            "Membutuhkan lingkungan kerja tenang", "Tidak cocok di suhu panas",
            "Tidak cocok di suhu dingin", "Alergi bahan kimia tertentu", "Membutuhkan istirahat berkala"
        )

        val hobbySpinner = findViewById<Spinner>(R.id.hobbySpinner)
        val specialSkillSpinner = findViewById<Spinner>(R.id.specialSkillSpinner)
        val healthConditionSpinner = findViewById<Spinner>(R.id.healthConditionSpinner)

        val hobbyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, hobbies)
        val skillAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, specialSkills)
        val healthAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, healthConditions)

        hobbySpinner.adapter = hobbyAdapter
        specialSkillSpinner.adapter = skillAdapter
        healthConditionSpinner.adapter = healthAdapter

        val daftarButton = findViewById<Button>(R.id.registerButton)
        daftarButton.setOnClickListener {
        }
    }
}
