package com.example.lb1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.lb1.data.DatabaseManager

class OutputFragment : Fragment() {

    private lateinit var tvTotalCount: TextView
    private lateinit var tvReadCount: TextView
    private lateinit var tvUnreadCount: TextView
    private lateinit var dbManager: DatabaseManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_output, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbManager = DatabaseManager(requireContext())

        tvTotalCount = view.findViewById(R.id.tvTotalCount)
        tvReadCount = view.findViewById(R.id.tvReadCount)
        tvUnreadCount = view.findViewById(R.id.tvUnreadCount)

        updateStatistics()
    }

    override fun onResume() {
        super.onResume()
        updateStatistics()
    }

    private fun updateStatistics() {
        val allBooks = dbManager.getAllBooks()
        val total = allBooks.size
        val read = allBooks.count { it.isRead }
        val unread = total - read

        tvTotalCount.text = getString(R.string.stats_value_format, total)
        tvReadCount.text = getString(R.string.stats_value_format, read)
        tvUnreadCount.text = getString(R.string.stats_value_format, unread)
    }
}