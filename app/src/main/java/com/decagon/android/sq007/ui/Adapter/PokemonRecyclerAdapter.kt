package com.decagon.android.sq007.ui.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.decagon.android.sq007.PokemonListDirections
import com.decagon.android.sq007.R
import com.decagon.android.sq007.`interface`.IItemClickListener
import com.decagon.android.sq007.common.Common
import com.decagon.android.sq007.model.Result

class PokemonRecyclerAdapter(internal val context: Context, internal var pokemonList: List<Result>) : RecyclerView.Adapter<PokemonRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var pokemonImageView: ImageView = itemView.findViewById(R.id.pokemon_image_view)
        internal var pokemonNameTextView: TextView = itemView.findViewById(R.id.pokemon_name_TV)

        private var itemClickListener: IItemClickListener? = null

        fun setItemClickListener(iItemClickListener: IItemClickListener) {
            this.itemClickListener = iItemClickListener
        }

        init {
            itemView.setOnClickListener { view -> itemClickListener?.onClick(view, adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.pokemon_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val url = pokemonList[position].url
        val imageNo = url.split("https://pokeapi.co/api/v2/pokemon/")[1].split("/")[0]
        Glide.with(context).load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$imageNo.png").into(holder.pokemonImageView)
        holder.pokemonNameTextView.text = pokemonList[position].name

        holder.setItemClickListener(object : IItemClickListener {
            override fun onClick(view: View, position: Int) {
                val pokeUrl = pokemonList[position].url
                Log.d("bindView", "onClick: ")
                val action = PokemonListDirections.actionPokemonList2ToPokemonDetailsFragment(pokeUrl)
                view.findNavController().navigate(action)
//                Toast.makeText(context, "Clicked Pokemon: " + pokemonList[position].name, Toast.LENGTH_SHORT).show()

                LocalBroadcastManager.getInstance(context)
                    .sendBroadcast(Intent(Common.KEY).putExtra("position", position))
            }
        })
    }
}
