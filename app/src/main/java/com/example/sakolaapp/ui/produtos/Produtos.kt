package com.example.sakolaapp.ui.produtos

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sakolaapp.Activities.ComprarProduto
import com.example.sakolaapp.R
import com.example.sakolaapp.functional.adapters.DBO.RegistrarProdutoFirabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.xwray.groupie.*
import kotlinx.android.synthetic.main.produtos_fragment.*
import kotlinx.android.synthetic.main.recycler_produtos_layout.view.*

class Produtos : Fragment() {

    companion object {
        fun newInstance() = Produtos()
    }

    //Referencia do FirebaseAuth
    //Recuperando o Uid do usuário logado
    val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

    lateinit var adapter: GroupAdapter<GroupieViewHolder>   //Pega a Instancia do Groupíe Biblioteca

    private lateinit var viewModel: ProdutosViewModel   //Instancia do ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.produtos_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = GroupAdapter() //Retorna o GroupAdapter da Biblioteca Groupie

        //Configuração do RecyclerView
        val layoutManager = LinearLayoutManager(context)
        recycler_produtos.adapter = adapter
        recycler_produtos.layoutManager = layoutManager

        //Chamada do método BuscarProdutos
        buscarProdutos()
    }

    //Inner Class para o groupie viewHolder
    private inner class ProdutosItem(internal val adProdutos: RegistrarProdutoFirabase) :
        Item<GroupieViewHolder>() {

        //Layout do RecyclerView
        override fun getLayout(): Int = R.layout.recycler_produtos_layout

        //Inserindo os dados no RecyclerView e outras configurações
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {

            //Enviando dados para as views do RecyclerView
            viewHolder.itemView.nameproduct.text = adProdutos.name
            viewHolder.itemView.priceproduct.text = "R$ ${adProdutos.price}"
            viewHolder.itemView.descriptionproduct.text = adProdutos.description

            //Inserindo Imagem no ImageView
            Picasso.get().load(adProdutos.img).into(viewHolder.itemView.imageProduct)

            //Evento de clique nos itens do RecyclerView
            viewHolder.itemView.setOnClickListener {

                //Enviar os dados por Intent para outra activity
                val intent = Intent(context, ComprarProduto::class.java)
                intent.putExtra("Nome", adProdutos.name)
                intent.putExtra("Desc", adProdutos.description)
                intent.putExtra("Price", adProdutos.price)
                intent.putExtra("Img", adProdutos.img)

                //Inicar a outra activity
                startActivity(intent)
            }

        }
    }

    //Método buscar produtos
    private fun buscarProdutos() {

        FirebaseDatabase.getInstance().reference.child("Produtos")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    snapshot.children.forEach {

                        //Inserindo os valores recuperados na classe modelo RegistrarProdutosFirebase
                        val produtos = it.getValue(RegistrarProdutoFirabase::class.java)

                        //Inserir os dados recuperados no adapter
                        adapter.add(ProdutosItem(produtos!!))
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProdutosViewModel::class.java]

        //Evento de clique no botão de adicionar produtos
        AdicionarProdutos.setOnClickListener {
            val intent =
                Intent(context, com.example.sakolaapp.Activities.AdicionarProdutos::class.java)
            startActivity(intent)
        }
    }
}