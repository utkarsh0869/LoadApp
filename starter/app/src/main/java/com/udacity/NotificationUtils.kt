package com.example.loadapp

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.R

private const val NOTIFICATION_ID = 0
private const val REQUEST_CODE = 0
private const val FLAGS = 0

/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
// May need to pass notification Id as a arg here.
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context, status: String) {

    val builder = NotificationCompat.Builder(
        applicationContext,
        "channelId"
    )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext
            .getString(R.string.notification_title))
        .setContentText(messageBody)


    notify(NOTIFICATION_ID, builder.build())
    Log.d("NotificationUtil", "notification?")

//    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
//    contentIntent.apply {
//        putExtra("fileName", messageBody)
//        putExtra("status", status)
//    }

//    val contentPendingIntent = PendingIntent.getActivity(applicationContext, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//    val action = NotificationCompat.Action.Builder(0,"Show Details",contentPendingIntent).build()
//    val notificationBuilder = NotificationCompat.Builder(applicationContext, "channelId")
//        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
//        .setContentTitle("Download Complete")
//        .setContentText(messageBody)
//        .setContentIntent(contentPendingIntent)
//        .setAutoCancel(true)
//        .setPriority(NotificationCompat.PRIORITY_HIGH)
//        .addAction(action)
//
//    notify(NOTIFICATION_ID, notificationBuilder.build())
}