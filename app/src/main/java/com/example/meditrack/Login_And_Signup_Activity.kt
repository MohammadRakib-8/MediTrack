package com.example.meditrack

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.meditrack.databinding.ActivityForgetPasswordBinding
import com.example.meditrack.databinding.ActivityLoginAndSignupBinding

class Login_And_Signup_Activity : AppCompatActivity() {
      lateinit var binding:ActivityLoginAndSignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

     binding=ActivityLoginAndSignupBinding.inflate(layoutInflater)
     setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_signup_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.textViewForgetPasswordLoginP.setOnClickListener {
            val intent = Intent(this, Forget_Password::class.java)
            startActivity(intent)
        }


        binding.textViewSignUpLoginP.setOnClickListener {
            val intent =Intent(this,Signup_Activity::class.java)
            startActivity(intent)
        }

        binding.loginButtonLoginP.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}