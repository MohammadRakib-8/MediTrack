package com.example.meditrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class Fragment_Home_Page : Fragment(R.layout.fragment_home_page) {

    private lateinit var containerLayout: LinearLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        containerLayout = view.findViewById(R.id.medicineContainer)

        fetchMedicines()
    }

    private fun fetchMedicines() {
        val db = Firebase.firestore

        db.collection("medicines")
            .get()
            .addOnSuccessListener { result ->
                containerLayout.removeAllViews()

                for (document in result) {
                    val medicine = Medicine(
                        name = document.getString("name") ?: "",
                        dose = document.getString("dose") ?: "",
                        date = document.getString("date") ?: "",
                        time = document.getString("time") ?: ""
                    )

                    addMedicineCard(medicine)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addMedicineCard(medicine: Medicine) {
        val cardView = LayoutInflater.from(requireContext())
            .inflate(R.layout.item_medicine_card, containerLayout, false) as MaterialCardView

        val nameText = cardView.findViewById<TextView>(R.id.txtMedicineName)
        val doseText = cardView.findViewById<TextView>(R.id.txtDose)
        val dateText = cardView.findViewById<TextView>(R.id.txtDateTime)
        val editBtn = cardView.findViewById<MaterialButton>(R.id.btnEdit)

        nameText.text = medicine.name
        doseText.text = medicine.dose
        dateText.text = "${medicine.date} • ${medicine.time}"

        editBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Edit ${medicine.name}", Toast.LENGTH_SHORT).show()
        }

        containerLayout.addView(cardView)
    }
}
