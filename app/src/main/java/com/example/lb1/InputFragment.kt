package com.example.lb1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

class InputFragment : Fragment() {

    private lateinit var spinnerBooks: Spinner
    private lateinit var radioGroupYears: RadioGroup
    private lateinit var btnOk: Button

    private val books = arrayOf(
        "Kobzar - T. Shevchenko",
        "The Forest Song - L. Ukrainka",
        "Tiger Trappers - I. Bagryanyi",
        "Shadows of Forgotten Ancestors - M. Kotsiubynsky"
    )

    interface OnDataPassedListener {
        fun onDataPassed(text: String)
    }

    private var dataPasser: OnDataPassedListener? = null

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        // Перевіряємо, чи Activity підписалася на наш інтерфейс
        if (context is OnDataPassedListener) {
            dataPasser = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        dataPasser = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        setupSpinner()
        setupListeners()
    }

    private fun initializeViews(view: View) {
        spinnerBooks = view.findViewById(R.id.spinnerBooks)
        radioGroupYears = view.findViewById(R.id.radioGroupYears)
        btnOk = view.findViewById(R.id.btnOk)
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            books
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
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
            showWarningDialog(getString(R.string.warning_message))
            return
        }

        val selectedRadioButton = requireView().findViewById<RadioButton>(selectedYearId)
        val selectedYear = selectedRadioButton.text.toString()

        val resultText = getString(R.string.result_format, selectedBook, selectedYear)

        dataPasser?.onDataPassed(resultText)
    }

    private fun showWarningDialog(message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Incomplete Data")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun clearForm() {
        if (::spinnerBooks.isInitialized && ::radioGroupYears.isInitialized) {
            spinnerBooks.setSelection(0)
            radioGroupYears.clearCheck()
        }
    }
}