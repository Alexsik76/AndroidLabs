package com.example.lb1.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.lb1.model.Book
import java.io.BufferedReader
import java.io.InputStreamReader

class DatabaseManager(private val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "LibraryAdvanced.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_BOOKS = "books"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_AUTHOR = "author"
        private const val COLUMN_YEAR = "pub_year"
        private const val COLUMN_IS_READ = "is_read"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_BOOKS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT,
                $COLUMN_AUTHOR TEXT,
                $COLUMN_YEAR INTEGER,
                $COLUMN_IS_READ INTEGER DEFAULT 0
            )
        """.trimIndent()

        db.execSQL(createTableQuery)

        // Initializing data from CSV file in assets
        seedDatabaseFromCSV(db)
    }

    private fun seedDatabaseFromCSV(db: SQLiteDatabase) {
        try {
            val inputStream = context.assets.open("books.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))

            reader.useLines { lines ->
                lines.forEach { line ->
                    val tokens = line.split(";")
                    if (tokens.size >= 3) {
                        val cv = ContentValues().apply {
                            put(COLUMN_TITLE, tokens[0].trim())
                            put(COLUMN_AUTHOR, tokens[1].trim())
                            put(COLUMN_YEAR, tokens[2].trim().toIntOrNull() ?: 0)
                            put(COLUMN_IS_READ, 0)
                        }
                        db.insert(TABLE_BOOKS, null, cv)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKS")
        onCreate(db)
    }

    // --- CRUD OPERATIONS ---

    fun getAllBooks(): List<Book> {
        val bookList = mutableListOf<Book>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_BOOKS", null)

        if (cursor.moveToFirst()) {
            val idIdx = cursor.getColumnIndexOrThrow(COLUMN_ID)
            val titleIdx = cursor.getColumnIndexOrThrow(COLUMN_TITLE)
            val authorIdx = cursor.getColumnIndexOrThrow(COLUMN_AUTHOR)
            val yearIdx = cursor.getColumnIndexOrThrow(COLUMN_YEAR)
            val readIdx = cursor.getColumnIndexOrThrow(COLUMN_IS_READ)

            do {
                bookList.add(Book(
                    id = cursor.getLong(idIdx),
                    title = cursor.getString(titleIdx),
                    author = cursor.getString(authorIdx),
                    year = cursor.getInt(yearIdx),
                    isRead = cursor.getInt(readIdx) == 1
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return bookList
    }

    fun updateBookStatus(id: Long, isRead: Boolean): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(COLUMN_IS_READ, if (isRead) 1 else 0)
        }
        val result = db.update(TABLE_BOOKS, cv, "$COLUMN_ID=?", arrayOf(id.toString()))
        return result > 0
    }

    fun deleteBook(id: Long): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_BOOKS, "$COLUMN_ID=?", arrayOf(id.toString()))
        return result > 0
    }

    fun insertBook(book: Book): Long {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(COLUMN_TITLE, book.title)
            put(COLUMN_AUTHOR, book.author)
            put(COLUMN_YEAR, book.year)
            put(COLUMN_IS_READ, if (book.isRead) 1 else 0)
        }
        return db.insert(TABLE_BOOKS, null, cv)
    }
}