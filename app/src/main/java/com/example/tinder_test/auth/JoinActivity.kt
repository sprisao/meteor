package com.example.tinder_test.auth

import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.tinder_test.R
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_intro.*
import kotlinx.android.synthetic.main.activity_join.*

class JoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)


        btnJoin.setOnClickListener{
            val email = findViewById<TextInputEditText>(R.id.emailArea)
            val pwd = findViewById<TextInputEditText>(R.id.pwdArea)

            Log.d(TAG, email.text.toString())
            Log.d(TAG, pwd.text.toString())
        }
    }
}
