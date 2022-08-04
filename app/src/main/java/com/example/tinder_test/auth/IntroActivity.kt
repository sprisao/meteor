package com.example.tinder_test.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.tinder_test.R
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        joinBtn.setOnClickListener{
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

//        val joinBtn: Button = findViewById(R.id.joinBtn)
//        joinBtn.setOnClickListener{
//            val intent = Intent(this, JoinActivity::class.java)
//            startActivity(intent)
//        }
    }
}
