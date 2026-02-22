package com.example.lb1

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class OutputFragment : Fragment() {

    // Interface to communicate with MainActivity
    interface OnCancelListener {
        fun onCancelClicked()
    }

    private var cancelListener: OnCancelListener? = null
    private lateinit var tvResult: TextView
    private lateinit var btnCancel: Button

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCancelListener) {
            cancelListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_output, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        displayReceivedData()
        setupListeners()
    }

    private fun initializeViews(view: View) {
        tvResult = view.findViewById(R.id.tvResult)
        btnCancel = view.findViewById(R.id.btnCancel)
    }

    private fun displayReceivedData() {
        // Retrieve the data from the Bundle
        val text = arguments?.getString("RESULT_TEXT")
        if (text != null) {
            tvResult.text = text
        }
    }

    private fun setupListeners() {
        btnCancel.setOnClickListener {
            // Notify MainActivity to close this fragment
            cancelListener?.onCancelClicked()
        }
    }

    override fun onDetach() {
        super.onDetach()
        cancelListener = null
    }
}