package com.example.meditrack

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView

class Fragment_Setting_Page : Fragment(R.layout.fragment_setting_page) {

    private lateinit var switchNotifications: Switch
    private lateinit var cardAccount: MaterialCardView
    private lateinit var cardAbout: MaterialCardView

    private val PREFS_NAME = "MyAppPrefs"
    private val KEY_NOTIFICATIONS = "notificationsEnabled"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        switchNotifications = view.findViewById(R.id.switchNotifications)
        cardAccount = view.findViewById(R.id.cardAccount)
        cardAbout = view.findViewById(R.id.cardAbout)

        val sharedPref = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isEnabled = sharedPref.getBoolean(KEY_NOTIFICATIONS, true) // default ON
        switchNotifications.isChecked = isEnabled

        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            sharedPref.edit().putBoolean(KEY_NOTIFICATIONS, isChecked).apply() // Save state

            if (isChecked) {
                Toast.makeText(requireContext(), "Notifications Enabled", Toast.LENGTH_SHORT).show()
                enableMedicineReminders()
            } else {
                Toast.makeText(requireContext(), "Notifications Disabled", Toast.LENGTH_SHORT).show()
                disableMedicineReminders()
            }
        }

        cardAccount.setOnClickListener {
            Toast.makeText(requireContext(), "Account Settings Not Implemented Yet :) ", Toast.LENGTH_SHORT).show()
        }

        cardAbout.setOnClickListener {
            val url = "https://github.com/MohammadRakib-8/MediTrack"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    // --------------------- Reminder logic ---------------------

    private fun enableMedicineReminders() {
        val intent = Intent(requireContext(), MedicineReminderReceiver::class.java)
        intent.putExtra("medicineName", "Demo Medicine")
        intent.putExtra("medicineDose", "1 Tablet")

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 10000, // 10 seconds
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun disableMedicineReminders() {
        val intent = Intent(requireContext(), MedicineReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}
