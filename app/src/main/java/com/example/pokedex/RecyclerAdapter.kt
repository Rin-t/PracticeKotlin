package com.example.pokedex

import android.content.Intent
import android.location.GnssAntennaInfo.Listener
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolderItem>() {

    private lateinit var itemViewClickListener: OnItemClickListener

    interface  OnItemClickListener {
        fun onItemClick(pokemon: String)
    }

    inner class ViewHolderItem(itemView: View): RecyclerView.ViewHolder(itemView) {
        val pokemonImage: ImageView = itemView.findViewById(R.id.pokemonImageView)
        val pokemonNameTextView: TextView = itemView.findViewById(R.id.pokemonNameTextView)

        init {
            itemView.setOnClickListener {
                itemViewClickListener.onItemClick("ピカチュウ")
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

    override fun onBindViewHolder(holder: ViewHolderItem, position: Int) {
    }

    override fun getItemCount(): Int {
        return 10
    }
}