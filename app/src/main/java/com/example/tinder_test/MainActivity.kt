package com.example.tinder_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tinder_test.slider.CardStackAdapter
import com.yuyakaido.android.cardstackview.CardStackLayoutManager

class MainActivity : AppCompatActivity() {

    lateinit var cardStackAdapter: CardStackAdapter
    lateinit var manager: CardStackLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
