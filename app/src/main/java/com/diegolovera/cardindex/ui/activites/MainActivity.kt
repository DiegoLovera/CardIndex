package com.diegolovera.cardindex.ui.activites

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diegolovera.cardindex.R
import com.diegolovera.cardindex.data.models.Card
import com.diegolovera.cardindex.security.PasswordManager
import com.diegolovera.cardindex.ui.BaseSecureActivity
import com.diegolovera.cardindex.ui.adapters.CardListAdapter
import com.diegolovera.cardindex.ui.dialogs.CardFormDialog
import com.diegolovera.cardindex.ui.dialogs.PasswordDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : BaseSecureActivity() {
    private lateinit var mRecyclerCards: RecyclerView
    private lateinit var mAdapterCards: CardListAdapter

    private lateinit var mMainViewModel: MainViewModel

    private lateinit var mFabMain: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)

        mMainViewModel = ViewModelProvider(this, MainViewModelFactory(this)).get(MainViewModel::class.java)

        mRecyclerCards = findViewById(R.id.content_main_recycler_cards)
        mAdapterCards =
            CardListAdapter(
                this,
                ArrayList(),
                mMainViewModel
            )

        mRecyclerCards.setHasFixedSize(true)
        mRecyclerCards.layoutManager = LinearLayoutManager(this)
        mRecyclerCards.adapter = mAdapterCards

        mMainViewModel.getCards().observe(this@MainActivity, Observer {
            mAdapterCards.setData(it)
        })

        mFabMain = findViewById(R.id.main_fab)
        mFabMain.setOnClickListener {
            if (PasswordManager.userPassword.isEmpty()) {
                showPasswordMissing(object : PasswordDialog.PasswordFormListener {
                    override fun onPasswordSet(password: String) {
                        PasswordManager.userPassword = password
                        showAddCardForm()
                    }
                })
            } else {
                showAddCardForm()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId ?: 0
        if (id == R.id.main_menu_item_password) {
            showPasswordMissing()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAddCardForm() {
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
