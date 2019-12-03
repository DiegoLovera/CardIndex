package com.diegolovera.cardindex.ui.activites

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diegolovera.cardindex.*
import com.diegolovera.cardindex.data.Card
import com.diegolovera.cardindex.ui.adapters.CardListAdapter
import com.diegolovera.cardindex.ui.dialogs.CardFormDialog
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
        mAdapterCards =
            CardListAdapter(
                this,
                ArrayList()
            )

        mRecyclerCards.setHasFixedSize(true)
        mRecyclerCards.layoutManager = LinearLayoutManager(this)
        mRecyclerCards.adapter = mAdapterCards

        mMainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mMainViewModel.getCards().observe(this@MainActivity, Observer {
            mAdapterCards.setData(it)
        })

        mFabMain = findViewById(R.id.main_fab)
        mFabMain.setOnClickListener {
            val cardDialog = CardFormDialog(
                this@MainActivity,
                object : CardFormDialog.CardFormListener {
                    override fun onCardCreated(cardCrated: Card) {
                        mMainViewModel.addCard(cardCrated)
                    }
                })
            cardDialog.show()
            cardDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            cardDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }
}
