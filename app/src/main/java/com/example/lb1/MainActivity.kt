package com.example.lb1

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // UI elements references
    private lateinit var spinnerBooks: Spinner
    private lateinit var radioGroupYears: RadioGroup
    private lateinit var btnOk: Button
    private lateinit var tvResult: TextView

    // Data source for the spinner
    private val books = arrayOf(
        "Kobzar - T. Shevchenko",
        "The Forest Song - L. Ukrainka",
        "Tiger Trappers - I. Bagryanyi",
        "Shadows of Forgotten Ancestors - M. Kotsiubynsky"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupSpinner()
        setupListeners()
    }

    private fun initializeViews() {
        spinnerBooks = findViewById(R.id.spinnerBooks)
        radioGroupYears = findViewById(R.id.radioGroupYears)
        btnOk = findViewById(R.id.btnOk)
        tvResult = findViewById(R.id.tvResult)
    }

    private fun setupSpinner() {
        // Create an adapter using the string array and a default spinner layout
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            books
        )
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        spinnerBooks.adapter = adapter
    }

    private fun setupListeners() {
        btnOk.setOnClickListener {
            handleOkButtonClick()
        }
    }

    private fun handleOkButtonClick() {
        val selectedBook = spinnerBooks.selectedItem.toString()
        val selectedYearId = radioGroupYears.checkedRadioButtonId

        if (selectedYearId == -1) {
            // Використовуємо текст з ресурсів
            showWarningDialog(getString(R.string.warning_message))
            return
        }

        val selectedRadioButton = findViewById<RadioButton>(selectedYearId)
        val selectedYear = selectedRadioButton.text.toString()

        // Використовуємо форматований рядок з ресурсів
        val resultText = getString(R.string.result_format, selectedBook, selectedYear)
        tvResult.text = resultText
    }

    private fun showWarningDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Incomplete Data")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}