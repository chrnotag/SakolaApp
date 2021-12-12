package com.example.sakolaapp.ui.estoque

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sakolaapp.Activities.AdicionarEstoque
import com.example.sakolaapp.R
import com.example.sakolaapp.functional.DBO.EstoqueFirebase
import com.example.sakolaapp.functional.adapters.recycleradapters.RecyclerAdapterEstoque
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.estoque_fragment.*
import kotlinx.android.synthetic.main.produtos_fragment.*
import kotlinx.android.synthetic.main.recycler_estoque.view.*

class Estoque : Fragment() {

    companion object {
        fun newInstance() = Estoque()
    }

    private lateinit var viewModel: EstoqueViewModel

    lateinit var adapter: GroupAdapter<GroupieViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.estoque_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = GroupAdapter()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EstoqueViewModel::class.java)

        val layoutManager = LinearLayoutManager(context)

        RecyclerEstoque.layoutManager = layoutManager
        RecyclerEstoque.adapter = adapter

        BuscarItens()

        FABAddItemEstoque.setOnClickListener {
            val intent = Intent(context, AdicionarEstoque::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private inner class EstoqueItens(internal val adEstoque: EstoqueFirebase):
        Item<GroupieViewHolder>() {


        override fun getLayout(): Int = R.layout.recycler_estoque

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.nome_item_estoque.text = adEstoque.nomeItem
            viewHolder.itemView.qtd_item_estoque.text = "Quantidade: ${adEstoque.qtdItem}"

            Picasso.get().load(adEstoque.img).into(viewHolder.itemView.imagem_item_estoque)
        }

    }

    fun BuscarItens(){
        FirebaseFirestore.getInstance()
            .collection("estoque")
            .addSnapshotListener { value, error ->
                error?.let {
                    return@addSnapshotListener
                }
                value?.let {
                    for (doc in it){
                        val produtos = doc.toObject(EstoqueFirebase::class.java)
                        adapter.add(EstoqueItens(produtos))
                    }
                }
            }
    }

}