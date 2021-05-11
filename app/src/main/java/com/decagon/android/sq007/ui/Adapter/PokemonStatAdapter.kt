package com.decagon.android.sq007.ui.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.R
import com.decagon.android.sq007.`interface`.IItemClickListener
import com.decagon.android.sq007.model.Stat
import com.google.android.material.chip.Chip

class PokemonStatAdapter(private var context: Context, private var statList: List<Stat>) :
    RecyclerView.Adapter<PokemonStatAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var chip: Chip
        private var iItemClickListener: IItemClickListener? = null

        fun setItemClickListener(iItemClickListener: IItemClickListener) {
            this.iItemClickListener = iItemClickListener
        }

        init {
            chip = itemView.findViewById(R.id.type_chip) as Chip
            chip.setOnClickListener { view -> iItemClickListener?.onClick(view, adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chip_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return statList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.chip.text = statList[position].stat.name

//        holder.chip.changeBackgroundColor[Common.getColorByType(typeList[position])]
    }
}
