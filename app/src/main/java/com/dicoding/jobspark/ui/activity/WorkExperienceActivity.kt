package com.dicoding.jobspark.ui.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.jobspark.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class WorkExperienceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.work_experience)

        val backButton: ImageView = findViewById(R.id.backButton)
        val startDateEditText: EditText = findViewById(R.id.startDateEditText)
        val endDateEditText: EditText = findViewById(R.id.endDateEditText)

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        startDateEditText.setOnClickListener {
            showDatePickerDialog { date ->
                startDateEditText.setText(date)
            }
        }
        endDateEditText.setOnClickListener {
            showDatePickerDialog { date ->
                endDateEditText.setText(date)
            }
        }
    }

    private fun showDatePickerDialog(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = formatDate(selectedYear, selectedMonth, selectedDay)
                onDateSelected(formattedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val dateFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}