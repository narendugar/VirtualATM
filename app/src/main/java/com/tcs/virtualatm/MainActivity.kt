package com.tcs.virtualatm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var currentNotificationID = 0
    private val notificationTitle: String? = "Authorize Card"
    private val notificationText: String? = "Authorize ATM Card"
    private var icon: Bitmap? = null
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var notificationBuilder: Notification.Builder
    private val channelId = "com.tcs.cardlessatm"
    private val description = "Test notification"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val atmPushButton = findViewById<Button>(R.id.atm_insert_button)
        icon = BitmapFactory.decodeResource(this.resources, R.mipmap.ic_launcher);
        atmPushButton.setOnClickListener {
            sendNotification()
        }
    }

    private fun sendNotification() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationIntent = Intent()
        notificationIntent.action = Intent.ACTION_VIEW
        notificationIntent.setClassName("com.tcs.cardlessatm", "com.tcs.cardlessatm.MainActivity")

        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            notificationBuilder = Notification.Builder(this, channelId)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(icon)
                .setContentIntent(pendingIntent)
        } else {
            notificationBuilder = Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(icon)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setContentIntent(pendingIntent)
        }

        currentNotificationID++
        var notificationId = currentNotificationID
        if (notificationId == Int.MAX_VALUE - 1) notificationId = 0
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}