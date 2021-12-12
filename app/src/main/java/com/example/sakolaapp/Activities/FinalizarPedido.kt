package com.example.sakolaapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sakolaapp.R
import com.example.sakolaapp.functional.adapters.DBO.AdicionarCarrinhoFirebase
import com.example.sakolaapp.functional.recycleradapters.FirestoreAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_comprar_produto.*
import kotlinx.android.synthetic.main.cabecalho_confirmar_compra.*
import kotlinx.android.synthetic.main.carrinho_layout.view.*
import kotlinx.android.synthetic.main.deixar_pedido_layout.view.*

class FinalizarPedido : AppCompatActivity() {

    val adapter2 = adapterMenu()

    val auth = FirebaseAuth.getInstance()

    val uid = auth.currentUser?.uid.toString()

    var nome = ""
    var sobrenome = ""
    val email = auth.currentUser?.email.toString()
    var count = 0

    val firestore = FirebaseFirestore.getInstance()
    lateinit var adapter3: FirestoreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cabecalho_confirmar_compra)

        val layoutManager2 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_Deixar_Pedido.layoutManager = layoutManager2
        recycler_Deixar_Pedido.adapter = adapter2

        buscarProduto()

        finalizarPedido.setOnClickListener {
            registrarPedido()
        }

        setupRecyclerView()
    }

    override fun onBackPressed() {
        this.finish()
    }

    inner class adapterMenu() : RecyclerView.Adapter<adapterMenu.viewHolder>() {
        inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.deixar_pedido_layout, parent, false)
            return viewHolder(view)
        }

        override fun onBindViewHolder(holder: viewHolder, position: Int) {
            holder.itemView.item_deixar_pedido.text = list[position]
            holder.itemView.setOnClickListener {
            }
        }

        override fun getItemCount(): Int = list.size
    }

    var subtotal: Double = 0.0
    private fun buscarProduto() {
        firestore
            .collection(uid)
            .document("Carrinho")
            .collection("Carrinho")
            .addSnapshotListener { value, error ->
                error?.let {
                    return@addSnapshotListener
                }
                value?.let {
                    for (doc in value) {
                        subtotal += doc.get("price").toString().toDouble()
                        subtotalPrice.text = subtotal.toString()
                        Total.text = subtotalPrice.text.toString()
                        PrecoTotal.text = Total.text.toString()
                        count++
                    }
                }

            }
    }


    private fun registrarPedido() {
        firestore
            .collection(uid)
            .document("informations")
            .collection("informations")
            .addSnapshotListener { value, error ->
                error?.let {
                    return@addSnapshotListener
                }
                value?.let {
                    for (doc in it){

                    }
                }
            }

    }

    val list = listOf<String>(
        "No portão da Casa/Prédio",
        "Com o porteiro do Prédio/Condominio",
        "Encontrar com Entregador"
    )

    fun setupRecyclerView() {
        val referenc: Query = FirebaseFirestore.getInstance()
            .collection(uid)
            .document("Carrinho")
            .collection("Carrinho")
        val options =
            FirestoreRecyclerOptions.Builder<AdicionarCarrinhoFirebase>()
                .setQuery(referenc, AdicionarCarrinhoFirebase::class.java)
                .build()
        adapter3 = FirestoreAdapter(options)


        val layoutManager = LinearLayoutManager(this)
        recycler_Carrinho.layoutManager = layoutManager
        recycler_Carrinho.adapter = adapter3
    }

    override fun onStart() {
        super.onStart()
        adapter3.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter3.stopListening()
    }

}