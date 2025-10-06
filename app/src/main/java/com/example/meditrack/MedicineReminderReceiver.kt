package com.example.meditrack

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MedicineReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return

        val name = intent?.getStringExtra("medicineName") ?: "Medicine"
        val dose = intent?.getStringExtra("medicineDose") ?: ""

        val notification = NotificationCompat.Builder(context, "medicine_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Time for $name 💊")
            .setContentText("Take your dose: $dose")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI)
            .build()

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) return

        NotificationManagerCompat.from(context).notify(System.currentTimeMillis().toInt(), notification)
    }
}
