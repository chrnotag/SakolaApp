package com.example.sakolaapp.functional.adapters.recycleradapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivities
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.sakolaapp.Activities.DetalhesPedido
import com.example.sakolaapp.R

class RecyclerPedidosAdapter: RecyclerView.Adapter<RecyclerPedidosAdapter.viewHolder>() {

    class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.recycler_pedidos, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            val intent =Intent(holder.itemView.context, DetalhesPedido::class.java)
            startActivity(holder.itemView.context, intent, null)
        }

    }

    override fun getItemCount(): Int = 10
}