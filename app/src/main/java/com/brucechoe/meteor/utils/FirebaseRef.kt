package com.brucechoe.meteor.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRef {

    companion object {
        val database = Firebase.database("https://tinderclone-736d2-default-rtdb.asia-southeast1.firebasedatabase.app")
        val userInfoRef = database.getReference("userInfo")
        val userLikeRef = database.getReference("userLikes")
        val userMsgRef = database.getReference("userMsg")
    }
}
