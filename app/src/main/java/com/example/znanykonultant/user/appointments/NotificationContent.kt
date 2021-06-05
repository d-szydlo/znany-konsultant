package com.example.znanykonultant.user.appointments

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.znanykonultant.R
import com.example.znanykonultant.main.MainActivity

class NotificationContent: BroadcastReceiver() {

    var NOTIFICATION_ID = 100

    override fun onReceive(context: Context, intent: Intent?) {
        val title = intent?.extras?.getString("name")
        val desc = intent?.extras?.getString("description")
        var builder = NotificationCompat.Builder(context, "reminder")
                .setSmallIcon(R.drawable.bell)
                .setContentTitle("Twoje spotkanie $title zacznie się za 24h")
                .setContentText(desc)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        var notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
}