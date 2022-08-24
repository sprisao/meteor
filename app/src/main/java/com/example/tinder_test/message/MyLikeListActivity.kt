package com.example.tinder_test.message

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tinder_test.R
import com.example.tinder_test.auth.UserDataModel
import com.example.tinder_test.message.fcm.NotificationData
import com.example.tinder_test.message.fcm.PushNotification
import com.example.tinder_test.message.fcm.RetrofitInstance
import com.example.tinder_test.utils.FirebaseAuthUtils
import com.example.tinder_test.utils.FirebaseRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MyLikeListActivity : AppCompatActivity() {

    private val TAG = "MyLikeListActivity"
    private val uid = FirebaseAuthUtils.getUid()

    private val likeUserList = mutableListOf<UserDataModel>()
    private val likeUserListUid = mutableListOf<String>()

    private lateinit var listViewAdapter: ListViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_like_list)

        val userListView = findViewById<ListView>(R.id.userListView)
        listViewAdapter = ListViewAdapter(this, likeUserList)
        userListView.adapter = listViewAdapter

        //내가 좋아요한 유저들
        getMyLikeList()

//       userListView.setOnItemClickListener { parent, view, position, id ->
//
//           // Push Notification 데이터
//           val title = "Hi"
//           val message = "Message"
//           val token = likeUserList[position].token.toString()
//
//           // Push Notification 전송
//           if (title.isNotEmpty() && message.isNotEmpty()) {
//               PushNotification(
//                   NotificationData(title, message),
//                   token
//               ).also {
//                   sendNotification(it)
//               }
//           }
//
//
//       }


        userListView.setOnItemLongClickListener { parent, view, position, id ->
            Toast.makeText(this, "test", Toast.LENGTH_LONG).show()
            checkMatch(likeUserList[position].uid.toString())
            return@setOnItemLongClickListener (true)
        }


    }


    private fun checkMatch(otherUid: String) {

        val postListener = object : ValueEventListener {

            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                Log.d(TAG, otherUid)
                Log.e(TAG, dataSnapshot.toString())

                // 상대방의 좋아요가 비어있다? -> ㅂㅅ
                if (dataSnapshot.children.count() == 0) {
                    Toast.makeText(this@MyLikeListActivity, "Like leer", Toast.LENGTH_LONG)
                        .show()
                } else {
                    for (dataModel in dataSnapshot.children) {

                        val likeUserkey = dataModel.key.toString()
                        if (likeUserkey.equals(uid)) {
                            // 서로 좋아요 한 경우
                            showDialog()
                            Toast.makeText(
                                this@MyLikeListActivity,
                                "This is a match",
                                Toast.LENGTH_LONG
                            ).show()

                        } else {
                            // 상대가 좋아요 하지 않은 경우
                            Toast.makeText(
                                this@MyLikeListActivity,
                                "Not a Matched",
                                Toast.LENGTH_LONG
                            ).show()

                        }

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


    private fun getMyLikeList() {

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children) {
                    likeUserListUid.add(dataModel.key.toString())
                }
                getUserDataList()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }

        }
        FirebaseRef.userLikeRef.child(uid).addValueEventListener(postListener)
    }

    private fun getUserDataList() {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children) {
                    val user = dataModel.getValue(UserDataModel::class.java)

                    if (likeUserListUid.contains(user?.uid)) {
                        likeUserList.add(user!!)
                    }
                }

                listViewAdapter.notifyDataSetChanged()
                Log.d(TAG, likeUserList.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }

        }
        FirebaseRef.userInfoRef.addValueEventListener(postListener)
    }


    private fun sendNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val response = RetrofitInstance.api.postNotification(notification)
                if (response.isSuccessful) {
                    Log.d(TAG, "Message 성공적으로 전송됨")
                } else {
                    Log.e(TAG, response.body().toString())
                }

            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }

    // Dialog
    private fun showDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView).setTitle("메세지 보내기")

       val mAlertDialog =  mBuilder.show()

        val btn = mAlertDialog.findViewById<Button>(R.id.sendBtn)

        btn?.setOnClickListener{
            mAlertDialog.dismiss()
        }

    }

}
