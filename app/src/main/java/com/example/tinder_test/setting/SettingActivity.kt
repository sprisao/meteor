package com.example.tinder_test.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.tinder_test.R
import com.example.tinder_test.auth.IntroActivity
import com.example.tinder_test.message.MyLikeListActivity
import com.example.tinder_test.message.MyMsgActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val mybtn = findViewById<Button>(R.id.myPageBtn)

        mybtn.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        val myMsg = findViewById<Button>(R.id.myMsg)
       myMsg.setOnClickListener {
           val intent = Intent(this, MyMsgActivity::class.java)
           startActivity(intent)
       }

        val btnLogout = findViewById<Button>(R.id.logoutBtn)

        btnLogout.setOnClickListener {
            val auth = Firebase.auth
            auth.signOut()
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        }

        val likedBtn = findViewById<Button>(R.id.likedBtn)

        likedBtn.setOnClickListener {

            val intent = Intent(this, MyLikeListActivity::class.java)
            startActivity(intent)
        }

    }
}
