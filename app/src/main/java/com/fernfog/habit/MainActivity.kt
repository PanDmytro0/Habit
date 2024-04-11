package com.fernfog.habit

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<MaterialButton>(R.id.addHabitButton).setOnClickListener {
            finish()
            startActivity(Intent(this@MainActivity, AddActivity::class.java))
        }

        val dbHelper = DBHelper(this@MainActivity)

        val allHabits: List<Reminder> = dbHelper.getAllReminders()

        for (reminder in allHabits) {
//            val cardView = CardView(this@MainActivity)
//            val cardViewParams = LinearLayout.LayoutParams(
//                600,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//            cardViewParams.setMargins(0, 16, 0, 16)
//            cardView.layoutParams = cardViewParams
//            cardView.radius = 32f
//            cardView.elevation = 8f
//            val cardContentLayout = LinearLayout(this@MainActivity)
//            cardContentLayout.orientation = LinearLayout.VERTICAL
//            cardContentLayout.layoutParams = ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//            cardContentLayout.orientation = LinearLayout.VERTICAL
//            cardContentLayout.gravity = Gravity.CENTER
//            cardContentLayout.setPadding(16, 16, 16, 16)
//
//
//            val textViewName = TextView(this@MainActivity)
//            textViewName.textSize = 40f
//            textViewName.setText(reminder.title)
//            val layoutParams = RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT
//            )
//            layoutParams.bottomMargin = 40
//            textViewName.layoutParams = layoutParams
//
//            val textViewStartTime = TextView(this@MainActivity)
//            textViewStartTime.setText(reminder.time)
//            textViewStartTime.layoutParams = layoutParams
//
//            val customFont = ResourcesCompat.getFont(this@MainActivity, R.font.marmelad)
//            textViewName.setTypeface(customFont)
//
//
//            textViewName.setTypeface(customFont)
//            textViewStartTime.setTypeface(customFont)
//            textViewName.setTextColor(Color.parseColor(getHexColor(requireContext(), R.color.textColor)))
//            textViewStartTime.setTextColor(Color.parseColor(getHexColor(requireContext(), R.color.textColor)))
//
//
//            cardContentLayout.addView(textViewName)
//            cardContentLayout.addView(textViewStartTime)
//
//
//            textViewName.gravity = Gravity.CENTER
//
//
//            val button = MaterialButton(this@MainActivity)
//            button.setOnClickListener {
//                reminder.requestCode?.let { it1 -> cancelAlarm(this@MainActivity, it1) }
//                dbHelper.deleteHabitById(reminder.id)
//
//                remindCategoriesList.removeView(cardView)
//            }
//            button.text = getString(R.string.deleteCardButton)
//            button.layoutParams = ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//            button.gravity = Gravity.CENTER
//            button.setTypeface(customFont)
//            button.setTextColor(Color.parseColor(getHexColor(requireContext(), R.color.textColor)))
//            button.setBackgroundColor(Color.parseColor(getHexColor(requireContext(), R.color.buttonColor)))
//
//            cardContentLayout.addView(button)
//
//            cardView.addView(cardContentLayout)
//            remindCategoriesList.addView(cardView)
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