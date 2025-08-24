package com.example.meditrack

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.meditrack.databinding.ActivityGetstartedBinding

class GetStartedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGetstartedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setTheme(R.style.Theme_MediTrack);
        //for splashscreen scope
        Thread.sleep(1000)
        installSplashScreen()

        enableEdgeToEdge()

        binding = ActivityGetstartedBinding.inflate(layoutInflater)



        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.getStartedLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonGetStarted.setOnClickListener {
            val intent = Intent(this, Login_And_Signup_Activity::class.java)
            startActivity(intent)
        }
    }
}
