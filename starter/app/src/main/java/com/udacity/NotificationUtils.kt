package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

private const val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(messageBody: String?, applicationContext: Context, status: String) {

    val intent = Intent(applicationContext, DetailActivity::class.java)
    intent.putExtra("fileName", messageBody)
    intent.putExtra("status", status)

    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        intent,
        PendingIntent.FLAG_MUTABLE
    )

    val action = NotificationCompat.Action.Builder(
        R.drawable.ic_assistant_black_24dp,
        applicationContext.getString(R.string.notification_button),
        pendingIntent
    ).build()

    val builder = NotificationCompat.Builder(applicationContext, "channelId")
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .addAction(action)

    notify(NOTIFICATION_ID, builder.build())
}
