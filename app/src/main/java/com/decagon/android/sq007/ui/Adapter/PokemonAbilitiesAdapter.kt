package com.decagon.android.sq007.ui.Adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.R
import com.decagon.android.sq007.`interface`.IItemClickListener
import com.decagon.android.sq007.model.Ability
import com.google.android.material.chip.Chip

class PokemonAbilitiesAdapter(private var context: Context, private var abilitiesList: List<Ability>) :
    RecyclerView.Adapter<PokemonAbilitiesAdapter.MyViewHolder>() {

    // create a viewHolder class for your recycler view item
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var chip: Chip = itemView.findViewById(R.id.type_chip) as Chip
        private var iItemClickListener: IItemClickListener? = null

        init {
            chip.setOnClickListener { view -> iItemClickListener?.onClick(view, adapterPosition) }
        }
    }

    // inflate the layout to be used by each item on the recycler view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chip_item, parent, false)
        return MyViewHolder(view)
    }

    // returns the size of the abilitiesList
    override fun getItemCount(): Int {
        return abilitiesList.size
    }

    // binds the data and displays it in the specified location
    // specify the position where each property should be displayed
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.chip.text = abilitiesList[position].ability.name

        // set the chip background colour by the position
        holder.chip.chipBackgroundColor = when (position) {
            0 -> ColorStateList.valueOf(ContextCompat.getColor(this.context, R.color.defense))
            1 -> ColorStateList.valueOf(ContextCompat.getColor(this.context, R.color.fire))
            2 -> ColorStateList.valueOf(ContextCompat.getColor(this.context, R.color.hp))
            3 -> ColorStateList.valueOf(ContextCompat.getColor(this.context, R.color.special_attack))
            4 -> ColorStateList.valueOf(ContextCompat.getColor(this.context, R.color.special_defense))
            5 -> ColorStateList.valueOf(ContextCompat.getColor(this.context, R.color.defense))
            else -> ColorStateList.valueOf(ContextCompat.getColor(this.context, R.color.grass))
        }
    }
}
