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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Fragment_Home_Page : Fragment(R.layout.fragment_home_page) {

    private lateinit var containerLayout: LinearLayout
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        containerLayout = view.findViewById(R.id.medicineContainer)

        fetchMedicines()
    }

    private fun fetchMedicines() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "User not logged in!", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("users")
            .document(userId)
            .collection("medicines")
            .get()
            .addOnSuccessListener { result ->
                containerLayout.removeAllViews()
                for (document in result) {
                    val medicine = Medicine(
                        id = document.id,  // Added id for deletion
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
        val deleteBtn = cardView.findViewById<MaterialButton>(R.id.btnDelete)

        nameText.text = medicine.name
        doseText.text = medicine.dose
        dateText.text = "${medicine.date} • ${medicine.time}"

        deleteBtn.setOnClickListener {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                db.collection("users")
                    .document(userId)
                    .collection("medicines")
                    .document(medicine.id)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "${medicine.name} deleted!", Toast.LENGTH_SHORT).show()
                        containerLayout.removeView(cardView) // Remove card from UI
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), "Failed to delete ${medicine.name}: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireContext(), "User not logged in!", Toast.LENGTH_SHORT).show()
            }
        }

        containerLayout.addView(cardView)
    }
}



