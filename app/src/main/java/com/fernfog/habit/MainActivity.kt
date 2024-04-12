package com.fernfog.habit

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.CursorWindow
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import java.lang.reflect.Field


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.setAccessible(true)
            field.set(null, 100 * 1024 * 1024)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        findViewById<MaterialButton>(R.id.addHabitButton).setOnClickListener {
            finish()
            startActivity(Intent(this@MainActivity, AddActivity::class.java))
        }

        var viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val adapter: MyFragmentPagerAdapter = MyFragmentPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        val dbHelper = DBHelper(this@MainActivity)

        val allHabits: List<Reminder> = dbHelper.getAllReminders()

        for (reminder in allHabits) {

            adapter.addFragment(HabitFragment())

        }


    }

    fun cancelAlarm(context: Context?, requestCode: Int) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, MyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
    }

    fun getHexColor(context: Context, colorResourceId: Int): String {
        val colorInt = ContextCompat.getColor(context, colorResourceId)
        return String.format("#%06X", 0xFFFFFF and colorInt)
    }
}