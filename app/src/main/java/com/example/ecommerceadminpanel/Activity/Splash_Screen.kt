package com.example.ecommerceadminpanel.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import com.example.ecommerceadminpanel.R
import com.google.firebase.auth.FirebaseAuth

class Splash_Screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.statusBarColor = ContextCompat.getColor(this, R.color.offwhite)


        var firebaseAuth = FirebaseAuth.getInstance()
        var user = firebaseAuth.currentUser

        Handler(Looper.getMainLooper()).postDelayed({
            if (user == null) {
                val mainIntent = Intent(this, MainActivity::class.java)
                startActivity(mainIntent)
            } else {
                val mainIntent = Intent(this, Upload_Product_Screen::class.java)
                startActivity(mainIntent)
            }
            finish()
        }, 3400)

    }
}