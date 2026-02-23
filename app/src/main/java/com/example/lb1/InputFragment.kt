package com.example.lb1

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lb1.adapter.BookAdapter
import com.example.lb1.data.DatabaseManager
import com.example.lb1.model.Book


class InputFragment : Fragment() {

    private lateinit var rvBooks: RecyclerView
    private lateinit var rgFilter: RadioGroup
    private lateinit var btnAddBook: Button

    private lateinit var dbManager: DatabaseManager
    private lateinit var bookAdapter: BookAdapter

    interface OnDataPassedListener {
        fun onDataPassed(text: String)
    }

    private var dataPasser: OnDataPassedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDataPassedListener) {
            dataPasser = context
        }
        dbManager = DatabaseManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(view)
        setupFilter(view)

        btnAddBook = view.findViewById(R.id.btnAddBook)
        btnAddBook.setOnClickListener {
            showAddBookDialog()
        }
    }

    private fun setupRecyclerView(view: View) {
        rvBooks = view.findViewById(R.id.rvBooks)
        rvBooks.layoutManager = LinearLayoutManager(requireContext())

        bookAdapter = BookAdapter(
            books = dbManager.getAllBooks(),
            onStatusChanged = { id, isRead ->
                dbManager.updateBookStatus(id, isRead)
                refreshList()
            },
            onDeleteClicked = { id ->
                dbManager.deleteBook(id)
                refreshList()
                Toast.makeText(context, "Book removed", Toast.LENGTH_SHORT).show()
            }
        )
        rvBooks.adapter = bookAdapter
    }

    private fun setupFilter(view: View) {
        rgFilter = view.findViewById(R.id.rgFilter)
        rgFilter.setOnCheckedChangeListener { _, _ ->
            refreshList()
        }
    }

    private fun refreshList() {
        val allBooks = dbManager.getAllBooks()
        val filteredBooks = when (rgFilter.checkedRadioButtonId) {
            R.id.rbRead -> allBooks.filter { it.isRead }
            R.id.rbUnread -> allBooks.filter { !it.isRead }
            else -> allBooks
        }
        bookAdapter.updateData(filteredBooks)
    }

    private fun showAddBookDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_book, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.etTitle)
        val etAuthor = dialogView.findViewById<EditText>(R.id.etAuthor)
        val etYear = dialogView.findViewById<EditText>(R.id.etYear)

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton(R.string.btn_save) { dialog, _ ->
                val title = etTitle.text.toString().trim()
                val author = etAuthor.text.toString().trim()
                val yearStr = etYear.text.toString().trim()

                if (title.isNotEmpty() && author.isNotEmpty() && yearStr.isNotEmpty()) {
                    val year = yearStr.toIntOrNull() ?: 0
                    val newBook = Book(title = title, author = author, year = year)

                    val id = dbManager.insertBook(newBook)
                    if (id != -1L) {
                        refreshList()
                        dataPasser?.onDataPassed("Added: $title")
                    }
                    dialog.dismiss()
                } else {
                    Toast.makeText(context, R.string.error_empty_fields, Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(R.string.btn_cancel) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }
}