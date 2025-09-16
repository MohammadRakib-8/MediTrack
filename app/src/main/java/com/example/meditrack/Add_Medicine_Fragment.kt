package com.example.meditrack

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class Add_Medicine_Fragment:Fragment(R.layout.fragment_add_medicine) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val db= Firebase.firestore
        val edtName = view.findViewById<EditText>(R.id.medicineName)
        val edtDose = view.findViewById<EditText>(R.id.dosage)
        val edtDate = view.findViewById<EditText>(R.id.datePicker)
        val edtTime = view.findViewById<EditText>(R.id.timePicker)
        val btnSave = view.findViewById<MaterialButton>(R.id.saveBtn)

        btnSave.setOnClickListener {
            val name = edtName.text.toString()
            val dose = edtDose.text.toString()
            val date = edtDate.text.toString()
            val time = edtTime.text.toString()

            if (name.isEmpty() || dose.isEmpty() || date.isEmpty() || time.isEmpty()) {
Toast.makeText(requireContext(),"Please fill all the fields !! ",Toast.LENGTH_SHORT).show()
            return@setOnClickListener
            }

            val medicine= hashMapOf(
                "name" to name,
                "dose" to dose,
                "date" to date,
                "time" to time
            )
            db.collection("medicines")
                .add(medicine)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(),"Medicine added successfully !! ",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{e->
                    Toast.makeText(requireContext(),"Error: ${e.message}",Toast.LENGTH_SHORT).show()
                }

        }
    }
}
