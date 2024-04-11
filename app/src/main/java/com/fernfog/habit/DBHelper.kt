package com.fernfog.habit

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

class DBHelper(context: Context?) :
    SQLiteOpenHelper(context, "reminderDatabase", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val queryReminders =
            "CREATE TABLE reminders (id INTEGER PRIMARY KEY, title TEXT, time TEXT, requestCode INTEGER, photo BLOB)"
        db.execSQL(queryReminders)
    }

    fun addNewReminder(
        title: String?,
        time: String?,
        requestCode: Int?,
        photo: ByteArray?
    ) {
        try {
            this.writableDatabase.use { db ->
                val values = ContentValues()
                values.put("title", title)
                values.put("time", time)
                values.put("requestCode", requestCode)
                values.put("photo", photo)
                db.insert("reminders", null, values)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAllReminders(): List<Reminder> {
        val reminders: MutableList<Reminder> = ArrayList()
        try {
            this.readableDatabase.use { db ->
                val query = "SELECT * FROM reminders"
                try {
                    db.rawQuery(query, null).use { cursor ->
                        if (cursor != null && cursor.moveToFirst()) {
                            do {
                                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                                val time = cursor.getString(cursor.getColumnIndexOrThrow("time"))
                                val requestCode = cursor.getInt(cursor.getColumnIndexOrThrow("requestCode"))
                                val photo = cursor.getBlob(cursor.getColumnIndexOrThrow("photo"))

                                val reminder = Reminder(id, title, time, requestCode, photo)
                                reminders.add(reminder)
                            } while (cursor.moveToNext())
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return reminders
    }

    fun deleteHabitById(reminderId: Int?) {
        try {
            this.writableDatabase.use { db ->
                val whereClause = "id = ?"
                val whereArgs = arrayOf(reminderId.toString())
                db.delete("reminders", whereClause, whereArgs)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS reminders")
        onCreate(db)
    }

    companion object {
        fun checkDatabaseExists(context: Context): Boolean {
            val dbFile = context.getDatabasePath("reminderDatabase")
            return dbFile.exists()
        }
    }
}

data class Reminder(
    val id: Int?,
    val title: String?,
    val time: String?,
    val requestCode: Int?,
    val photo: ByteArray?
)