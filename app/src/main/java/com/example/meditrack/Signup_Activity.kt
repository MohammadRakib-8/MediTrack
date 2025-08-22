package com.example.meditrack

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Signup_Activity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var signupButton: Button


private var email:String?= null
private var password:String?= null
    private var confirmPassword:String?= null
    private var name :String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize views
        emailEditText = findViewById(R.id.editTextNameSignUpP)
        nameEditText = findViewById(R.id.editTextEmailAddressSignUpP)
        passwordEditText = findViewById(R.id.editTextPasswordSignUpP)
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPasswordSignUpP)
        signupButton = findViewById(R.id.createAccountButtonSignUpP)

        // Restore saved state if available
        if (savedInstanceState != null) {
            email = savedInstanceState.getString("email")
            name = savedInstanceState.getString("name")
            password = savedInstanceState.getString("password")
            confirmPassword = savedInstanceState.getString("confirmPassword")
        }

        // Apply restored values to EditTexts
        emailEditText.setText(email)
        nameEditText.setText(name)
        passwordEditText.setText(password)
        confirmPasswordEditText.setText(confirmPassword)

        // Button click listener (example)
        signupButton.setOnClickListener {
            email = emailEditText.text.toString()
            name = nameEditText.text.toString()
            password = passwordEditText.text.toString()
            confirmPassword = confirmPasswordEditText.text.toString()


        }
    }

    // Save user input when activity may be destroyed
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("email", emailEditText.text.toString())
        outState.putString("name", nameEditText.text.toString())
        outState.putString("password", passwordEditText.text.toString())
        outState.putString("confirmPassword", confirmPasswordEditText.text.toString())
    }

    // Optional: called after onStart(), restore state if needed
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        emailEditText.setText(savedInstanceState.getString("email"))
        nameEditText.setText(savedInstanceState.getString("name"))
        passwordEditText.setText(savedInstanceState.getString("password"))
        confirmPasswordEditText.setText(savedInstanceState.getString("confirmPassword"))
    }
}