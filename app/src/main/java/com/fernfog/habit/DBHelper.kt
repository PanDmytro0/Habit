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
            "CREATE TABLE reminders (id INTEGER PRIMARY KEY, title TEXT, time TEXT, requestCode INTEGER)"
        db.execSQL(queryReminders)
    }

    fun addNewReminder(
        title: String?,
        time: String?,
        requestCode: Int?
    ) {
        try {
            this.writableDatabase.use { db ->
                val values = ContentValues()
                values.put("title", title)
                values.put("time", time)
                values.put("requestCode", requestCode)
                db.insert("reminders", null, values)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAllRemindersByCategory(desiredCategory: String): List<Reminder> {
        val reminders: MutableList<Reminder> = ArrayList()
        try {
            this.readableDatabase.use { db ->
                val query = "SELECT * FROM reminders WHERE category = ?"
                try {
                    db.rawQuery(query, arrayOf(desiredCategory)).use { cursor ->
                        if (cursor != null && cursor.moveToFirst()) {
                            do {
                                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                                val time = cursor.getString(cursor.getColumnIndexOrThrow("time"))
                                val requestCode = cursor.getInt(cursor.getColumnIndexOrThrow("requestCode"))

                                val reminder = Reminder(id, title, time, requestCode)
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

    fun deleteReminderById(reminderId: Int?) {
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
    val requestCode: Int?
)