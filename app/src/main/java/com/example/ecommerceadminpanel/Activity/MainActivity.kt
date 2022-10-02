package com.example.ecommerceadminpanel.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.ecommerceadminpanel.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        binding.logIn.setOnClickListener {
            LoginWithEmail_Password(
                binding.emailEdt.text.toString(),
                binding.passwordEdt.text.toString()
            )
        }

    }

    fun LoginWithEmail_Password(email: String, password: String) {
        var firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener { res ->

            binding.bkColorRelative.isVisible = true
            binding.progressBar.isVisible = true

            var intent = Intent(this, Upload_Product_Screen::class.java)
            startActivity(intent)
        }.addOnFailureListener { error ->
            Toast.makeText(this, "${error.message}", Toast.LENGTH_LONG).show()
            Log.e("TAG", "LoginWithEmail_Password: ${error.message}")
        }

    }

}