package com.fernfog.habit

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

class AddActivity : AppCompatActivity() {

    lateinit var mImage: ImageView
    val PICK_IMAGE_FILE = 1
    var image: ByteArray? = null

    var timeString = ""
    var dateString = ""

    val ALARM_REQUEST_CODE_PREFIX = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        mImage = findViewById(R.id.imageView)

        val textInputLayout: TextInputLayout = findViewById(R.id.textInputLayout)


        findViewById<MaterialButton>(R.id.choosePhotoButton).setOnClickListener {
            openFile()
        }

        findViewById<MaterialButton>(R.id.openDatePickerButton).setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            var datePickerDialog = DatePickerDialog(this@AddActivity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                dateString = "$year-" + (month+1) + "-$dayOfMonth"
                Log.d("date", dateString)
            }, year, month, day)

            datePickerDialog.show()
        }

        findViewById<MaterialButton>(R.id.openTimePickerButton).setOnClickListener {
            val cal = Calendar.getInstance()
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val minute = cal.get(Calendar.MINUTE)

            var timePickerDialog = TimePickerDialog(this@AddActivity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                timeString = "$hourOfDay:$minute"
                Log.d("time", timeString)

            }, hour, minute, true)

            timePickerDialog.show()
        }

        findViewById<MaterialButton>(R.id.addHabitButton).setOnClickListener {
            val text: String = textInputLayout.editText?.text.toString()

            val time: String = "$dateString $timeString"

            scheduleAlarm(this@AddActivity, text, time, convertTimeToMillis(time))

            finish()
            startActivity(Intent(this@AddActivity, MainActivity::class.java))
        }
    }

    fun openFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.setType("image/jpeg")
        startActivityForResult(intent, PICK_IMAGE_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        if (requestCode == PICK_IMAGE_FILE && resultCode == RESULT_OK) {
            if (data != null) {
                val imageUri = data.data

                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)

                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                image = stream.toByteArray()

                Glide.with(this)
                    .load(image)
                    .into(mImage)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun convertTimeToMillis(time: String): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date = sdf.parse(time)
        return date?.time ?: 0
    }

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleAlarm(context: Context, titleOfPush: String, timeStr: String, triggerTimeMillis: Long) {

        val dbHelper = DBHelper(context)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, MyReceiver::class.java)

            intent.putExtra("titleOfPush", titleOfPush)

            val uniqueId = System.currentTimeMillis().toInt()
            val requestCode = ALARM_REQUEST_CODE_PREFIX + uniqueId

            intent.putExtra("requestCode", requestCode)
            intent.putExtra("triggerTimeMillis", triggerTimeMillis)

            val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)

            dbHelper.addNewReminder(titleOfPush, timeStr, requestCode, image)

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent)
    }
}
