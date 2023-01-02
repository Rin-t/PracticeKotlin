package com.example.pokedex

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.URL

class RecyclerAdapter(private val pokemonList: ArrayList<Pokemon>): RecyclerView.Adapter<RecyclerAdapter.ViewHolderItem>() {

    private lateinit var itemViewClickListener: OnItemClickListener
    private val scope = CoroutineScope(Dispatchers.Main)

    interface  OnItemClickListener {
        fun onItemClick(pokemon: Pokemon)
    }

    inner class ViewHolderItem(itemView: View): RecyclerView.ViewHolder(itemView) {
        val pokemonImage: ImageView = itemView.findViewById(R.id.pokemonImageView)
        val pokemonNameTextView: TextView = itemView.findViewById(R.id.pokemonNameTextView)

        init {
            itemView.setOnClickListener {
                val pokemon = pokemonList[adapterPosition]
                itemViewClickListener.onItemClick(pokemon)
            }
        }
    }

    fun setItemViewClickListener(listener: OnItemClickListener) {
        this.itemViewClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItem {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokemon_item, parent, false)
        return ViewHolderItem(item)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolderItem, position: Int) {
        val name = pokemonList[position].name
        val id = pokemonList[position].id
        holder.pokemonNameTextView.text = "No.$id $name"
        scope.launch {
            val originalDeferred = scope.async(Dispatchers.IO) {
                getOriginalBitmap(position)
            }

            val originalBitmap = originalDeferred.await()
            holder.pokemonImage.setImageBitmap(originalBitmap)
        }
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    private fun getOriginalBitmap(position: Int): Bitmap {
        return URL(pokemonList[position].image).openStream().use {
            BitmapFactory.decodeStream(it)
        }
    }
}