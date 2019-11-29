package com.diegolovera.cardindex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var mRecyclerCards: RecyclerView
    private lateinit var mAdapterCards: CardListAdapter

    private lateinit var mMainViewModel: MainViewModel

    private lateinit var mFabMain: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRecyclerCards = findViewById(R.id.content_main_recycler_cards)
        mAdapterCards = CardListAdapter(this, ArrayList())

        mRecyclerCards.setHasFixedSize(true)
        mRecyclerCards.layoutManager = LinearLayoutManager(this)
        mRecyclerCards.adapter = mAdapterCards

        mMainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mMainViewModel.getCards().observe(this@MainActivity, Observer {
            mAdapterCards.setData(it)
        })
    }
}