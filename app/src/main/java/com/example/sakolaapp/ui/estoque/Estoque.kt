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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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

    lateinit var adapter: GroupAdapter<GroupieViewHolder>   //Instancia da biblioteca Groupie

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.estoque_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = GroupAdapter() //Setando o adaptador no GroupAdapter

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Configuração do viewModel
        viewModel = ViewModelProvider(this)[EstoqueViewModel::class.java]

        //Configurações do RecyclerView
        val layoutManager = LinearLayoutManager(context)
        RecyclerEstoque.layoutManager = layoutManager
        RecyclerEstoque.adapter = adapter

        //Chamada do método BuscarItens
        BuscarItens()

        //Evento de clicque no botão para adicionar itens no estoque
        FABAddItemEstoque.setOnClickListener {
            val intent = Intent(context, AdicionarEstoque::class.java)
            startActivity(intent)
        }
    }

    //Classe do adaptador do Groupie
    private inner class EstoqueItens(internal val adEstoque: EstoqueFirebase) : Item<GroupieViewHolder>() {

        //Layout Usado no RecyclerView
        override fun getLayout(): Int = R.layout.recycler_estoque

        //Passando dados para o RecyclerView
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.nome_item_estoque.text = adEstoque.nomeItem
            viewHolder.itemView.qtd_item_estoque.text = "Quantidade: ${adEstoque.qtdItem}"

            //Adicionando a imagem ao ImageView
            Picasso.get().load(adEstoque.img).into(viewHolder.itemView.imagem_item_estoque)
        }

    }

    //Método para buscar Itens no Database
    fun BuscarItens() {
        FirebaseDatabase.getInstance()
            .reference
            .child("estoque")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    //Buscando todos os childrens no estoque
                    snapshot.children.forEach {

                        //adicionando os valores ao classe modelo EstoqueFirebase
                        val produtos = it.getValue(EstoqueFirebase::class.java)

                        //passandos os dados recuperados para o adapter do Groupie
                        adapter.add(EstoqueItens(produtos!!))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

}