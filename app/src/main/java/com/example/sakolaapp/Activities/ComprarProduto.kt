package com.example.sakolaapp.Activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.net.toUri
import com.example.sakolaapp.R
import com.example.sakolaapp.functional.adapters.DBO.AdicionarCarrinhoFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_comprar_produto.*
import java.util.*

class ComprarProduto : AppCompatActivity() {

    var count = 1   //Contador da quantidade de itens
    val auth = FirebaseAuth.getInstance()   //Referencia do FirebaseAuth

    //Referencia do Firebase Database ao caminho do Carrinho
    val Database = FirebaseDatabase.getInstance().reference.child("carrinho")
        .child(auth.currentUser?.uid.toString()).push()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comprar_produto)

        //Retorna os dados do produto clicado na lista de produtos
        val nome: String? = intent.getStringExtra("Nome")
        val img: String? = intent.getStringExtra("Img")
        val desc: String? = intent.getStringExtra("Desc")
        val Price: String? = intent.getStringExtra("Price")

        //Converter o preço recuperado para um valor Double
        val price: Double = Price.toString().toDouble()

        //Inserir a imagem recuperada na imageview
        Picasso.get().load(img).into(ImgProdutoComprar)

        //Inserir os dados recuperados nos campos apropriados na activity
        NomeProdutoComprar.text = nome
        TotalPrice.text = "R$ ${price.toString()}"
        DescProdutoComprar.text = desc

        //Método para aumentar a quantidade de produtos a se comprar
        AdcProduto.setOnClickListener {

            //Função para impedir de colocar valores negativos.
            if (count >= 0) {
                count++
                TotalPrice.text = "R$ ${(price * count).toString()}"
                QtdItemCompra.text = count.toString()
            }

        }

        //Função para impedir de colocar valores negativos.
        RmvProduto.setOnClickListener {
            if (count > 0) {
                count--
                TotalPrice.text = "R$ ${(price * count).toString()}"
                QtdItemCompra.text = count.toString()
            }
        }


        //Evento de Click no botão para confirmar carrinho
        TotalPrice.setOnClickListener {

            //Função para calcular o valor total dos itens no carrinho
            val totalPrice = price*count

            //Chamada da classe modelo AdicionarCarrinhoFirebase e passagem dos valores
            val carrinho = AdicionarCarrinhoFirebase(nome!!, count, totalPrice.toString(), img!!)

            //Função para salvar os dados no Database
            Database.setValue(carrinho)
                .addOnCompleteListener {

                    //Abrir a activity Finalizar Pedido e finalizar essa activity
                    val intent = Intent(this, FinalizarPedido::class.java)
                    startActivity(intent)
                    this.finish()
                }
                    //Tratamento de erros
                .addOnFailureListener {
                    Toast.makeText(this, "Not Ok", Toast.LENGTH_SHORT).show()
                }
        }
    }

}
