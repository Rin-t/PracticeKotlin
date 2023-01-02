package com.example.pokedex

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.URL

class PokemonDetailActivity : AppCompatActivity() {

    private val scope = CoroutineScope(Dispatchers.Main)

//    private val image = intent.getStringExtra("image")
//    private val id = intent.getIntExtra("id", 0)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_detail)

        val textView: TextView = findViewById(R.id.textView)
        val imageView: ImageView = findViewById(R.id.imageView)
        val backButton: Button = findViewById(R.id.backButton)
        val name = intent.getStringExtra("name")
        val id = intent.getIntExtra("id", 0)
        textView.text = "No.$id $name"

        val image = intent.getStringExtra("image")

        scope.launch {
            val originalDeferred = scope.async(Dispatchers.IO) {
                getOriginalBitmap(image!!)
            }
            val originalBitmap = originalDeferred.await()
            imageView.setImageBitmap(originalBitmap)
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun getOriginalBitmap(image: String): Bitmap {
        return URL(image).openStream().use {
            BitmapFactory.decodeStream(it)
        }
    }
}