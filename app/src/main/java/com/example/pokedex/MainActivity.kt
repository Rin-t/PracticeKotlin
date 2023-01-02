package com.example.pokedex

import android.annotation.SuppressLint
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
    private val pokemonList = ArrayList<Pokemon>()
    private val adapter = RecyclerAdapter(pokemonList)
    private val fetchPokemonNumber = 151

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        setupItemViewClickListener()

        for (i in 1..fetchPokemonNumber) {
            val url = "https://pokeapi.co/api/v2/pokemon/$i/"
            pokemonTask(url)
        }
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

    @SuppressLint("NotifyDataSetChanged")
    private fun pokemonJsonTask(result: String) {
        val jsonObject = JSONObject(result)
        val name = jsonObject.getString("name")
        val sprites = jsonObject.getJSONObject("sprites")
        val image = sprites.getString("front_default")
        val id = jsonObject.getInt("id")
        val pokemon = Pokemon(id, name, image)
        pokemonList.add(pokemon)
        if (pokemonList.size == fetchPokemonNumber) {
            pokemonList.sortBy { it.id }
            adapter.notifyDataSetChanged()
        }
    }


    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
    }

    private fun setupItemViewClickListener() {
        adapter.setItemViewClickListener(
            object : RecyclerAdapter.OnItemClickListener {
                override fun onItemClick(pokemon: Pokemon) {
                    val intent = Intent(this@MainActivity, PokemonDetailActivity::class.java)
                    intent.putExtra("name", pokemon.name)
                    intent.putExtra("image", pokemon.image)
                    intent.putExtra("id", pokemon.id)
                    startActivity(intent)
                }
            }
        )
    }
}