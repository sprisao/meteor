package com.brucechoe.meteor

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.brucechoe.meteor.auth.UserDataModel
import com.brucechoe.meteor.setting.SettingActivity
import com.brucechoe.meteor.slider.CardStackAdapter
import com.brucechoe.meteor.utils.FirebaseAuthUtils
import com.brucechoe.meteor.utils.FirebaseRef
import com.brucechoe.meteor.utils.MyInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private val userDataList = mutableListOf<UserDataModel>()

    private var userCount = 0

    private lateinit var currentUserGender: String

    private val uid = FirebaseAuthUtils.getUid()

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
                if (direction == Direction.Right) {
                    userLikeOtherUser(uid, userDataList[userCount].uid.toString())
                }
                if (direction == Direction.Left) {
                }

                userCount += 1

                if (userCount == userDataList.count()) {
//                    Toast.makeText(this@MainActivity, "새로운 유저 받아오기", Toast.LENGTH_LONG).show()
                    getUserDataList(currentUserGender)
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

        getMyUserData()
    }

    private fun getMyUserData() {

        val postListener = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                Log.d(TAG, dataSnapshot.toString())

                val data = dataSnapshot.getValue(UserDataModel::class.java)

                Log.d(TAG, data!!.gender.toString())

                currentUserGender = data.gender.toString()
                MyInfo.myNickname = data.nickname.toString()

                getUserDataList(currentUserGender)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }

        }
        FirebaseRef.userInfoRef.child(uid).addValueEventListener(postListener)
    }

    private fun getUserDataList(currentUserGender: String) {
        val postListener = object : ValueEventListener {

            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children) {
                    val user = dataModel.getValue(UserDataModel::class.java)

                    if (user!!.gender.toString().equals(currentUserGender)) {

                    } else {
                        userDataList.add(user)
                    }
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

    private fun userLikeOtherUser(myUid: String, otherUid: String) {
        FirebaseRef.userLikeRef.child(myUid).child(otherUid).setValue("true")

        getOtherUserLikeList(otherUid)
    }

    private fun getOtherUserLikeList(otherUid : String){

        val postListener = object : ValueEventListener {

            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children) {
                    val likeUserkey = dataModel.key.toString()
                    if(likeUserkey.equals(uid)){
                        Toast.makeText(this@MainActivity, "매칭되었습니다🎉",Toast.LENGTH_SHORT).show()

                        createNotificationChannel()
                       sendNotification()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }

        }
        FirebaseRef.userLikeRef.child(otherUid).addValueEventListener(postListener)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Test_Channel", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(){
        var builder = NotificationCompat.Builder(this, "Test_Channel")
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
            .setContentTitle("New Match!!")
            .setContentText("Send some text to your match!")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Don't make your match to wait too long."))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)){
            notify(123, builder.build())
        }
    }
}

