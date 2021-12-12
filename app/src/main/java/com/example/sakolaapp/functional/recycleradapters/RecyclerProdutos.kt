package com.example.sakolaapp.functional.adapters.recycleradapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sakolaapp.Activities.ComprarProduto
import com.example.sakolaapp.R
import com.example.sakolaapp.functional.adapters.DBO.RegistrarProdutoFirabase

class RecyclerProdutos : RecyclerView.Adapter<RecyclerProdutos.viewHolder>() {

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val PedidosItem: ConstraintLayout
        val imgItem: ImageView = itemView.findViewById(R.id.imageProduct)
        val nameItem: TextView = itemView.findViewById(R.id.nameproduct)
        val price: TextView = itemView.findViewById(R.id.priceproduct)
        val description: TextView = itemView.findViewById(R.id.descriptionproduct)

        init {
            PedidosItem = itemView.findViewById(R.id.Produto_Item)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(com.example.sakolaapp.R.layout.recycler_produtos_layout, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.PedidosItem.setOnClickListener {
            val intent = Intent(holder.itemView.context, ComprarProduto::class.java)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
    }

    override fun getItemCount(): Int = 1
}