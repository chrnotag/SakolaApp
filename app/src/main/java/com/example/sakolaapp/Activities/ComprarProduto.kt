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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_comprar_produto.*
import java.util.*

class ComprarProduto : AppCompatActivity() {

    var count = 1
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
        .collection(auth.currentUser?.uid.toString())
        .document("Carrinho")
        .collection("Carrinho")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comprar_produto)

        val nome: String? = intent.getStringExtra("Nome")
        val img: String? = intent.getStringExtra("Img")
        val desc: String? = intent.getStringExtra("Desc")
        val Price: String? = intent.getStringExtra("Price")

        val price: Double = Price.toString().toDouble()

        Picasso.get().load(img).into(ImgProdutoComprar)
        NomeProdutoComprar.text = nome
        TotalPrice.text = "R$ ${price.toString()}"
        DescProdutoComprar.text = desc

        AdcProduto.setOnClickListener {

            if (count >= 0) {
                count++
                TotalPrice.text = "R$ ${(price * count).toString()}"
                QtdItemCompra.text = count.toString()
            }

        }

        RmvProduto.setOnClickListener {
            if (count >= 0) {
                count--
                TotalPrice.text = "R$ ${(price * count).toString()}"
                QtdItemCompra.text = count.toString()
            }
        }



        TotalPrice.setOnClickListener {

            val totalPrice = price*count
            val carrinho = AdicionarCarrinhoFirebase(nome!!, count.toString(), totalPrice.toString(), img!!)
            firestore.add(carrinho)
                .addOnSuccessListener {
                    val intent = Intent(this, FinalizarPedido::class.java)
                    startActivity(intent)
                    this.finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Not Ok", Toast.LENGTH_SHORT).show()
                }
        }
    }

}
