package com.fernfog.habit

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton

class HabitFragment(
    val reminder: Reminder,
    val adapter: MyFragmentPagerAdapter) : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_habit, container, false)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val textOfHabit: TextView = view.findViewById(R.id.textOfHabit)
        val deleteButton: MaterialButton = view.findViewById(R.id.deleteButton)

        val dbHelper = DBHelper(requireContext())


        textOfHabit.text = reminder.title
        Glide.with(this@HabitFragment)
            .load(reminder.photo)
            .into(imageView)

        deleteButton.setOnClickListener {
            reminder.requestCode?.let { it1 -> cancelAlarm(requireContext(), it1) }
            dbHelper.deleteHabitById(reminder.id)

            adapter.removeFragment(this)
        }

        return view;
    }

    fun cancelAlarm(context: Context?, requestCode: Int) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, MyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
    }
}