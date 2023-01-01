package com.example.pokedex

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val adapter = RecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        adapter.setItemViewClickListener(
            object : RecyclerAdapter.OnItemClickListener {
                override fun onItemClick(pokemon: String) {
                    val intent = Intent(this@MainActivity, PokemonDetailActivity::class.java)
                    startActivity(intent)
                }
            }
        )
    }
}