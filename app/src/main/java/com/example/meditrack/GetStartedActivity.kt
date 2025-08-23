package com.example.meditrack

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.meditrack.databinding.ActivityGetstartedBinding

class GetStartedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGetstartedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGetstartedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonGetStarted.setOnClickListener {
            val intent = Intent(this, Login_And_Signup_Activity::class.java)
            startActivity(intent)
        }
    }
}
