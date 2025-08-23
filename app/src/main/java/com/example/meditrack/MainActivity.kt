package com.example.meditrack

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.meditrack.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if(savedInstanceState==null){
            replaceFragment(Fragment_Home_Page())
        }

        // Use view binding for layout
        binding = ActivityMainBinding.inflate(layoutInflater)
//        //for splashscreen scope
//        Thread.sleep(1000)
//        installSplashScreen()
        setContentView(binding.root)

        // Handle system bar insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

         //Button listeners
        binding.btnHome.setOnClickListener {
            replaceFragment(Fragment_Home_Page())
        }

        binding.btnTask.setOnClickListener {
            replaceFragment(Add_Medicine_Fragment())
        }

        binding.btnSetting.setOnClickListener {
            replaceFragment(Fragment_Setting_Page())
        }




    }

    fun replaceFragment(fragment:androidx.fragment.app.Fragment)
    {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer,fragment)
        fragmentTransaction.commit()
    }

}
