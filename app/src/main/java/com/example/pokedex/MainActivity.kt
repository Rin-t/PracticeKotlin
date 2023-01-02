package com.example.pokedex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val adapter = RecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        setupItemViewClickListener()

        val url = "https://pokeapi.co/api/v2/pokemon/1/"
        pokemonTask(url)
    }

    private fun pokemonTask(url: String) {
        lifecycleScope.launch {
            val result = pokemonBackGroundTask(url)
            pokemonJsonTask(result)
        }
    }

    private suspend fun pokemonBackGroundTask(url: String): String {

        val response = withContext(Dispatchers.IO) {
            var httpResult = ""
            try {
                val urlObject = URL(url)
                val reader = BufferedReader(InputStreamReader(urlObject.openStream()))
                httpResult = reader.readText()
            } catch (error: IOException) {
                error.printStackTrace()
            } catch (error: JSONException) {
                error.printStackTrace()
            }
            return@withContext httpResult
        }
        return response
    }

    private fun pokemonJsonTask(result: String) {
        val jsonObject = JSONObject(result)
        val name = jsonObject.getString("name")
        println(name)
    }


    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
    }

    private fun setupItemViewClickListener() {
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