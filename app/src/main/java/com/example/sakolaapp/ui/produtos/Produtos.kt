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
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.xwray.groupie.*
import kotlinx.android.synthetic.main.produtos_fragment.*
import kotlinx.android.synthetic.main.recycler_produtos_layout.view.*

class Produtos : Fragment() {

    companion object {
        fun newInstance() = Produtos()
    }

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
        adapter = GroupAdapter()    //Retorna o GroupAdapter da Biblioteca Groupie

        val layoutManager = LinearLayoutManager(context)    //LayoutManager

        recycler_produtos.adapter = adapter     //Seta o adapter do Recycler Produtos
        recycler_produtos.layoutManager = layoutManager     //Seta o LayoutManager

        buscarProdutos()    //Chamada do Método interno para buscar produtos
    }

    //Inner Class para o groupie viewHolder
    private inner class ProdutosItem(internal val adProdutos: RegistrarProdutoFirabase) :
        Item<GroupieViewHolder>() {
        override fun getLayout(): Int = R.layout.recycler_produtos_layout

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {

            viewHolder.itemView.nameproduct.text = adProdutos.name
            viewHolder.itemView.priceproduct.text = "R$ ${adProdutos.price}"
            viewHolder.itemView.descriptionproduct.text = adProdutos.description

            Picasso.get().load(adProdutos.img).into(viewHolder.itemView.imageProduct)

            viewHolder.itemView.setOnClickListener {
                val intent = Intent(context, ComprarProduto::class.java)

                intent.putExtra("Nome", adProdutos.name)
                intent.putExtra("Desc", adProdutos.description)
                intent.putExtra("Price", adProdutos.price)
                intent.putExtra("Img", adProdutos.img)

                startActivity(intent)
            }

        }
    }

    private fun buscarProdutos() {

        FirebaseFirestore.getInstance().collection("Produtos")
            .addSnapshotListener { value, error ->
                error?.let {
                    return@addSnapshotListener
                }
                value?.let {
                    for (doc in value) {
                        val produtos = doc.toObject(RegistrarProdutoFirabase::class.java)
                        adapter.add(ProdutosItem(produtos))
                    }
                }
            }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProdutosViewModel::class.java]


        AdicionarProdutos.setOnClickListener {
            val intent =
                Intent(context, com.example.sakolaapp.Activities.AdicionarProdutos::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }
}