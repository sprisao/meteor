package com.example.tinder_test.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.tinder_test.MainActivity
import com.example.tinder_test.R
import com.example.tinder_test.utils.FirebaseAuthUtils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {

    private val TAG = "IntroActivity"
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val uid = FirebaseAuthUtils.getUid()

        if (uid !== "null") {
            Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }, 1000)
        }


        Log.d(TAG, auth.currentUser?.uid.toString())

        joinBtn.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

    }
}
