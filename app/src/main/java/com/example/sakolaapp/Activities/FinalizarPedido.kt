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
import com.example.sakolaapp.functional.DBO.EstoqueFirebase
import com.example.sakolaapp.functional.DBO.InformationsUserFirebase
import com.example.sakolaapp.functional.DBO.ReceitaFirebase
import com.example.sakolaapp.functional.DBO.RegistroPedidosFirebase
import com.example.sakolaapp.functional.adapters.DBO.AdicionarCarrinhoFirebase
import com.example.sakolaapp.functional.adapters.DBO.RegistrarProdutoFirabase
import com.example.sakolaapp.functional.recycleradapters.FirestoreAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_comprar_produto.*
import kotlinx.android.synthetic.main.cabecalho_confirmar_compra.*
import kotlinx.android.synthetic.main.carrinho_layout.view.*
import kotlinx.android.synthetic.main.deixar_pedido_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

class FinalizarPedido : AppCompatActivity() {

    val adapter2 = adapterMenu()

    val auth = FirebaseAuth.getInstance()   //Referencia do Firebase Auth

    val uid = auth.currentUser?.uid.toString()  //Recuperar o Uid do usuário logado

    var count = 0

    //Referencia do Database no carrinho com o identificador do Uid do usuário logado
    val Database = FirebaseDatabase.getInstance().reference.child("carrinho")
        .child(uid)

    //Referncia ao FirebaseDatabase pedidos com Uid do usuário logado (Ainda não consigo entender a logica
    // dessa parte)
    val registrarPedidos = FirebaseDatabase.getInstance().reference.child("pedidos")
        .child(auth.currentUser?.uid.toString())

    //Referencia do estoque (Não sei como debitar do estoque)
    val debEstoque = FirebaseDatabase.getInstance().reference.child("estoque")

    //Instancia do FirestoreAdapter
    lateinit var adapter3: FirestoreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cabecalho_confirmar_compra)

        //Configurações do RecyclerView Deixar Pedido
        RecyclerDeixarPedido()

        //Chamada do Método buscarProduto
        buscarProduto()

        //Evento de click no botão Finalizar Pedido
        finalizarPedido.setOnClickListener {
            //Chamada do método buscarInformaçõesUsuario
            buscarInformaçõesUsuario()
        }
        //Configurar o recyclerView dos itens no carrinho
        setupRecyclerView()
    }

    override fun onBackPressed() {
        this.finish()
    }

    //Configurações do RecyclerView com informções da onde deixar o pedido
    fun RecyclerDeixarPedido() {
        val layoutManager2 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_Deixar_Pedido.layoutManager = layoutManager2
        recycler_Deixar_Pedido.adapter = adapter2
    }

    //Classe interna para configurar o RecyclerView Deixar Produto
    inner class adapterMenu() : RecyclerView.Adapter<adapterMenu.viewHolder>() {

        //Classe responsavel pelo ViewHolder
        inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {

            //Retornando a view usada no recyclerview
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.deixar_pedido_layout, parent, false)
            return viewHolder(view)
        }

        override fun onBindViewHolder(holder: viewHolder, position: Int) {

            //Colocar as informações da lista com os dados para deixar o produto
            holder.itemView.item_deixar_pedido.text = list[position]
            holder.itemView.setOnClickListener {
            }
        }

        //retorna a quantidade de itens dentro da lista
        override fun getItemCount(): Int = list.size
    }

    //Váriavel subtotal para calcular o valor da soma dos produtos dentro do carrinho
    var subtotal: Double = 0.0

    //Método para buscar os produtos dentro do carrinho
    private fun buscarProduto() {

        //Função para buscar os itens dentro do Carrinho
        Database.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                //Função para mapear todos os childrens dentro do carrinho
                snapshot.children.forEach {

                    //Enviando os dados recuperados para a classe modelo AdicionarCarrinhoFirebase
                    val carrinho = it.getValue(AdicionarCarrinhoFirebase::class.java)

                    //Calcular os preços do carrinho com o preço da entrega
                    // (que esta usando valor fixo 5)
                    subtotal += carrinho?.price.toString().toDouble()
                    subtotalPrice.text = subtotal.toString()
                    Total.text = (subtotal + 5).toString()
                    PrecoTotal.text = Total.text.toString()

                    //Retornando quantos itens há no carrinho
                    count++
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    //Método para buscar Informações do Usuário (Não consigo saber o que fazer aqui para registrar
    // um pedido, não sei a lógica para registrar um pedido no firebase e depois retornar as
    // informações do pedido na listagem de pedidos na homeactivity)
    fun buscarInformaçõesUsuario() {

        //Pegar data atual e deixar no formato abaixo
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        //Não sei como registrar um produto, ainda não entendi a lógica.
        registrarPedidos.child("data").setValue(currentDate.toString())
        registrarPedidos.child("qtdPedidos").setValue(count)
    }

    //Lista com os dados para aparecer no recyclerview Deixar Pedido
    val list = listOf<String>(
        "No portão da Casa/Prédio",
        "Com o porteiro do Prédio/Condominio",
        "Encontrar com Entregador"
    )

    fun setupRecyclerView() {

        //Configurar o Recyclerview com dados do Firebase
        val options =
            FirebaseRecyclerOptions.Builder<AdicionarCarrinhoFirebase>()
                .setQuery(Database, AdicionarCarrinhoFirebase::class.java)
                .build()

        //Colocar as informações recuperadas no adaptador
        adapter3 = FirestoreAdapter(options)

        //Configurações do RecyclerView
        val layoutManager = LinearLayoutManager(this)
        recycler_Carrinho.layoutManager = layoutManager
        recycler_Carrinho.adapter = adapter3
    }

    //Inicar a escuta do adaptador
    override fun onStart() {
        super.onStart()
        adapter3.startListening()
    }

    //Finalizar a escuta do adaptador
    override fun onStop() {
        super.onStop()
        adapter3.stopListening()
    }
}