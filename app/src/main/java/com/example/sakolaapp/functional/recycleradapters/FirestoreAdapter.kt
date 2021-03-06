package com.example.sakolaapp.functional.recycleradapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sakolaapp.R
import com.example.sakolaapp.functional.adapters.DBO.AdicionarCarrinhoFirebase
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.carrinho_layout.view.*

class FirestoreAdapter(options: FirebaseRecyclerOptions<AdicionarCarrinhoFirebase>) :
    FirebaseRecyclerAdapter<AdicionarCarrinhoFirebase, FirestoreAdapter.viewHolder>(options) {

    inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.carrinho_layout,parent,false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(
        holder: viewHolder,
        position: Int,
        model: AdicionarCarrinhoFirebase
    ) {
        holder.itemView.nomeProdutoCarrinho.text = model.produto
        holder.itemView.QtdProdutoCarrinho.text = model.qtd.toString()
        holder.itemView.PriceItemCarrinho.text = "R$ ${model.price}"

        Picasso.get().load(model.img).into(holder.itemView.ImgItemCarrinho)
    }
}