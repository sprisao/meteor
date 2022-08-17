package com.example.tinder_test.message.fcm

import com.example.tinder_test.message.fcm.Repo.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {
        private val retrofit by lazy {
            Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory((GsonConverterFactory.create())).build()
        }
        val api = retrofit.create(NotiAPI::class.java)
    }
}