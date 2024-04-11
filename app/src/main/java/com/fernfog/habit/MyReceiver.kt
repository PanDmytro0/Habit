package com.fernfog.habit

import android.app.NotificationChannel
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val titleOfPush = intent.getStringExtra("titleOfPush")
        val triggerTimeMillis = intent.getLongExtra("triggerTimeMillis", 0)

        Toast.makeText(context, "Received: $titleOfPush at $triggerTimeMillis", Toast.LENGTH_SHORT).show()

        NotificationUtils.showNotification(context, titleOfPush.toString(), triggerTimeMillis)
    }

}
