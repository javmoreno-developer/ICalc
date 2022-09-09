package com.example.calcriver.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.calcriver.MainActivity
import com.example.calcriver.MainViewModel
import com.example.calcriver.R
import com.example.calcriver.databinding.ItemBtnBinding

class itemAdapter(private val lista : List<String>,private val colores : List<String>,private val onClickListener: OnClickListener,val context : MainActivity ) : RecyclerView.Adapter<itemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)


        return itemViewHolder(layoutInflater.inflate(R.layout.item_btn,parent,false),context)
    }

    override fun onBindViewHolder(holder: itemViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onClickListener.onClick(lista[position])
        }
        holder.render(lista[position],colores[position])
    }

    override fun getItemCount(): Int {
        return lista.size
    }
    class OnClickListener(val clickListener: (meme: String) -> Unit) {
        fun onClick(meme: String) = clickListener(meme)
    }
}

