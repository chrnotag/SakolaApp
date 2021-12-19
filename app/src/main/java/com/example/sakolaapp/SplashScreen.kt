package com.example.sakolaapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.sakolaapp.Activities.HomeActivity
import com.example.sakolaapp.Activities.LoginActivity
import com.example.sakolaapp.functional.DBO.EstoqueFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.sql.DriverManager

class SplashScreen : AppCompatActivity() {

    var user = FirebaseAuth.getInstance() //Instancia do FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        //Chamada do método verificar usuário logado
        verificarUsuarioLogado()
    }

    //Método verificar usuário logado
    fun verificarUsuarioLogado() {

        //Verificação de usuário logado
        if (user.currentUser != null) {

            //Caso logado, abrirá a home activity
            startActivity(Intent(this, HomeActivity::class.java))
            this.finish()
        } else {

            //Caso deslogado, abrirá a tela de Login
            startActivity(Intent(this, LoginActivity::class.java))
            this.finish()
        }
    }

    /*fun trazerEstoque() {
        FirebaseDatabase.getInstance().reference
            .child("estoque")
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    snapshot.children.forEach {
                        val v = it.getValue(EstoqueFirebase::class.java)

                        if (v!!.nomeItem == "Pao") {

                            FirebaseDatabase
                                .getInstance().reference
                                .child("estoque")
                                .child(it.key.toString())
                                .child("qtdItem")
                                .setValue(v.qtdItem - 1)
                                .addOnCompleteListener {
                                }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }*/
}