package com.example.meditrack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.meditrack.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class Signup_Activity : AppCompatActivity() {

    lateinit var binding: ActivitySignupBinding
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var signupButton: Button

    private lateinit var auth: FirebaseAuth

    private var email: String? = null
    private var password: String? = null
    private var confirmPassword: String? = null
    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        emailEditText = findViewById(R.id.editTextEmailAddressSignUpP)
        nameEditText = findViewById(R.id.editTextNameSignUpP)
        passwordEditText = findViewById(R.id.editTextPasswordSignUpP)
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPasswordSignUpP)
        signupButton = findViewById(R.id.createAccountButtonSignUpP)

        if (savedInstanceState != null) {
            email = savedInstanceState.getString("email")
            name = savedInstanceState.getString("name")
            password = savedInstanceState.getString("password")
            confirmPassword = savedInstanceState.getString("confirmPassword")
        }
        emailEditText.setText(email)
        nameEditText.setText(name)
        passwordEditText.setText(password)
        confirmPasswordEditText.setText(confirmPassword)

        signupButton.setOnClickListener {
            email = emailEditText.text.toString().trim()
            name = nameEditText.text.toString().trim()
            password = passwordEditText.text.toString().trim()
            confirmPassword = confirmPasswordEditText.text.toString().trim()

            if (validateInputs()) {
                signUpWithFirebase(email!!, password!!)
            }
        }

        binding.textViewSignInHereSignUpP.setOnClickListener {
            val intent = Intent(this, Login_And_Signup_Activity::class.java)
            startActivity(intent)
        }
    }

    private fun validateInputs(): Boolean {
        if (name.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty() || confirmPassword.isNullOrEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password!!.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun signUpWithFirebase(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, Login_And_Signup_Activity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("email", emailEditText.text.toString())
        outState.putString("name", nameEditText.text.toString())
        outState.putString("password", passwordEditText.text.toString())
        outState.putString("confirmPassword", confirmPasswordEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        emailEditText.setText(savedInstanceState.getString("email"))
        nameEditText.setText(savedInstanceState.getString("name"))
        passwordEditText.setText(savedInstanceState.getString("password"))
        confirmPasswordEditText.setText(savedInstanceState.getString("confirmPassword"))
    }
}
