package com.example.tinder_test

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tinder_test.auth.IntroActivity
import com.example.tinder_test.auth.UserDataModel
import com.example.tinder_test.setting.SettingActivity
import com.example.tinder_test.slider.CardStackAdapter
import com.example.tinder_test.utils.FirebaseRef
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private val userDataList = mutableListOf<UserDataModel>()

    private var userCount = 0

    lateinit var cardStackAdapter: CardStackAdapter
    lateinit var manager: CardStackLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val setting = findViewById<ImageView>(R.id.settingIcon)
        setting.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        val cardStackView = findViewById<CardStackView>(R.id.cardStackView)
        manager = CardStackLayoutManager(baseContext, object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {
            }

            override fun onCardSwiped(direction: Direction?) {
               if(direction == Direction.Right){
                   Toast.makeText(this@MainActivity,"right",Toast.LENGTH_LONG).show()
               }
                if(direction == Direction.Left){
                    Toast.makeText(this@MainActivity,"right",Toast.LENGTH_LONG).show()
                }

                userCount = userCount + 1
                if(userCount == userDataList.count()){
                    Toast.makeText(this@MainActivity,"새로운 유저 받아오기",Toast.LENGTH_LONG).show()
                    getUserDataList()
                }
            }

            override fun onCardRewound() {
            }

            override fun onCardCanceled() {
            }

            override fun onCardAppeared(view: View?, position: Int) {
            }

            override fun onCardDisappeared(view: View?, position: Int) {
            }
        })

        val testList = mutableListOf<String>()
        testList.add("a")
        testList.add("b")
        testList.add("c")

        cardStackAdapter = CardStackAdapter(baseContext, userDataList)
        cardStackView.layoutManager = manager
        cardStackView.adapter = cardStackAdapter

        getUserDataList()
    }

    private fun getUserDataList() {
        val postListener = object : ValueEventListener {

            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children){
                    val user = dataModel.getValue(UserDataModel::class.java)
                    userDataList.add(user!!)
                }

                cardStackAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }

        }
        FirebaseRef.userInfoRef.addValueEventListener(postListener)
    }
}

