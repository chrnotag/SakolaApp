package com.example.sakolaapp.functional.adapters.recycleradapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sakolaapp.R

class RecyclerAdapterEstoque: RecyclerView.Adapter<RecyclerAdapterEstoque.viewHolder>() {
    class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val nomeItem: TextView = itemView.findViewById(R.id.nome_item_estoque)
        val imgItem: ImageView = itemView.findViewById(R.id.imagem_item_estoque)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_estoque, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        listItens[position].let {
            holder.imgItem.setImageResource(it.second)
            holder.nomeItem.text = it.first
        }
    }

    override fun getItemCount(): Int = listItens.size

    val listItens: List<Pair<String, Int>> = listOf(
        Pair("Hamburguer", R.drawable.hamburguer),
        Pair("Queijo", R.drawable.queijo),
        Pair("Presunto",R.drawable.presunto)
    )
}