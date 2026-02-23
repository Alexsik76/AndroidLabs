package com.example.lb1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lb1.R
import com.example.lb1.model.Book

class BookAdapter(
    private var books: List<Book> = emptyList(),
    private val onStatusChanged: (Long, Boolean) -> Unit,
    private val onDeleteClicked: (Long) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvBookTitle)
        val author: TextView = view.findViewById(R.id.tvBookAuthor)
        val cbIsRead: CheckBox = view.findViewById(R.id.cbIsRead)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        val context = holder.itemView.context

        holder.title.text = book.title

        holder.author.text = context.getString(R.string.author_year_format, book.author, book.year)

        holder.cbIsRead.setOnCheckedChangeListener(null)
        holder.cbIsRead.isChecked = book.isRead

        holder.cbIsRead.setOnCheckedChangeListener { _, isChecked ->
            onStatusChanged(book.id, isChecked)
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClicked(book.id)
        }
    }

    override fun getItemCount() = books.size


    fun updateData(newBooks: List<Book>) {
        val diffCallback = BookDiffCallback(this.books, newBooks)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.books = newBooks
        diffResult.dispatchUpdatesTo(this)
    }

    private class BookDiffCallback(
        private val oldList: List<Book>,
        private val newList: List<Book>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldPos: Int, newPos: Int) =
            oldList[oldPos].id == newList[newPos].id

        override fun areContentsTheSame(oldPos: Int, newPos: Int) =
            oldList[oldPos] == newList[newPos]
    }
}