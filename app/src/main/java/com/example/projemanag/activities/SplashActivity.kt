package com.example.projemanag.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.projemanag.databinding.ActivitySplashBinding
import com.example.projemanag.firebase.FirestoreClass

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val typeface : Typeface = Typeface.createFromAsset(assets, "carbon bl.ttf")
        binding.tvAppName.typeface = typeface

        android.os.Handler().postDelayed({

            var currentUserID = FirestoreClass().getCurrentUserId()

            if (currentUserID.isNotEmpty()){
                startActivity(Intent(this, MainActivity::class.java))
            }else{
                startActivity(Intent(this, IntroActivity::class.java))
            }
            finish()
        }, 2500)
    }
}