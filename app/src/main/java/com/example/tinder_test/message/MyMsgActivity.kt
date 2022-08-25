package com.example.tinder_test.message

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import com.example.tinder_test.R
import com.example.tinder_test.auth.UserDataModel
import com.example.tinder_test.message.fcm.MsgAdapter
import com.example.tinder_test.message.fcm.MsgModel
import com.example.tinder_test.utils.FirebaseAuthUtils
import com.example.tinder_test.utils.FirebaseRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MyMsgActivity : AppCompatActivity() {
    val TAG = "getMyMsg"

    lateinit var listviewAdapter : MsgAdapter

    val msgList = mutableListOf<MsgModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_msg)

        val listview = findViewById<ListView>(R.id.msgListView)

        listviewAdapter= MsgAdapter(this, msgList)
        listview.adapter = listviewAdapter

        getMyMsg()

    }

    private fun getMyMsg() {
        val postListener = object : ValueEventListener {

            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // Listview 데이터가 중복되지 않도록 지워준다.
                msgList.clear()

                for (dataModel in dataSnapshot.children) {
                    val msg = dataModel.getValue(MsgModel::class.java)
                    msgList.add(msg!!)
                    Log.e(TAG,msg.toString())
                }

                // 메세지 역순으로 쌓이도록
                msgList.reverse()

                listviewAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }

        }

        FirebaseRef.userMsgRef.child(FirebaseAuthUtils.getUid()).addValueEventListener(postListener)
    }
}