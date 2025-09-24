package com.example.meditrack

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView

class Fragment_Setting_Page : Fragment(R.layout.fragment_setting_page) {

    private lateinit var switchNotifications: Switch
    private lateinit var cardAccount: MaterialCardView
    private lateinit var cardAbout: MaterialCardView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        switchNotifications = view.findViewById(R.id.switchNotifications)
        cardAccount = view.findViewById(R.id.cardAccount)
        cardAbout = view.findViewById(R.id.cardAbout)

        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(requireContext(), "Notifications Enabled", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(requireContext(), "Notifications Disabled", Toast.LENGTH_SHORT).show()

            }
        }

        cardAccount.setOnClickListener {
            Toast.makeText(requireContext(), "Account Settings Not Implemented Yet Bro :) ", Toast.LENGTH_SHORT).show()

        }

        cardAbout.setOnClickListener {
            val url = "https://github.com/MohammadRakib-8/MediTrack"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)

        }
    }
}
