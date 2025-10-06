package com.example.meditrack

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import java.util.Locale

class Add_Medicine_Fragment : Fragment(R.layout.fragment_add_medicine) {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val edtName = view.findViewById<EditText>(R.id.medicineName)
        val edtDose = view.findViewById<EditText>(R.id.dosage)
        val edtDate = view.findViewById<EditText>(R.id.datePicker)
        val edtTime = view.findViewById<EditText>(R.id.timePicker)
        val btnSave = view.findViewById<MaterialButton>(R.id.saveBtn)

        // DATE PICKER
        edtDate.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                edtDate.setText(String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year))
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        // TIME PICKER
        edtTime.setOnClickListener {
            val c = Calendar.getInstance()
            TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
                edtTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute))
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
        }

        btnSave.setOnClickListener {
            val name = edtName.text.toString().trim()
            val dose = edtDose.text.toString().trim()
            val date = edtDate.text.toString().trim()
            val time = edtTime.text.toString().trim()

            if (name.isEmpty() || dose.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all the fields!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = auth.currentUser?.uid
            if (userId == null) {
                Toast.makeText(requireContext(), "User not logged in!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val medicine = hashMapOf(
                "name" to name,
                "dose" to dose,
                "date" to date,
                "time" to time
            )

            db.collection("users")
                .document(userId)
                .collection("medicines")
                .add(medicine)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Medicine added successfully!", Toast.LENGTH_SHORT).show()

                    requestExactAlarmPermission()
                    scheduleMedicineReminder(name, dose, date, time)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(requireContext(), "Please allow exact alarms in settings", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)
            }
        }
    }

    private fun scheduleMedicineReminder(name: String, dose: String, date: String, time: String) {
        try {
            val parts = date.split("/")
            val timeParts = time.split(":")

            val calendar = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, parts[0].toInt())
                set(Calendar.MONTH, parts[1].toInt() - 1)
                set(Calendar.YEAR, parts[2].toInt())
                set(Calendar.HOUR_OF_DAY, timeParts[0].toInt())
                set(Calendar.MINUTE, timeParts[1].toInt())
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            if (calendar.timeInMillis <= System.currentTimeMillis()) {
                Toast.makeText(requireContext(), "Cannot set reminder in the past!", Toast.LENGTH_SHORT).show()
                return
            }

            val intent = Intent(requireContext(), MedicineReminderReceiver::class.java).apply {
                putExtra("medicineName", name)
                putExtra("medicineDose", dose)
            }

            val requestCode = (name + date + time).hashCode()
            val pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }

            Toast.makeText(requireContext(), "Reminder set for $date $time", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error scheduling reminder: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
