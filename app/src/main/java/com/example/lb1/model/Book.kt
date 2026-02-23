package com.example.lb1.model

data class Book(
    val id: Long = 0,
    val title: String,
    val author: String,
    val year: Int,
    var isRead: Boolean = false
) {
    override fun toString(): String {
        return "$title by $author ($year)"
    }
}