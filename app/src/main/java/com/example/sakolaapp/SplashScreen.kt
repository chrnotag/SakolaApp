package com.example.sakolaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sakolaapp.Activities.HomeActivity
import com.example.sakolaapp.Activities.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SplashScreen : AppCompatActivity() {

    var user = FirebaseAuth.getInstance() //Instancia do FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //Chamada do método verificar usuário logado
        verificarUsuarioLogado()
    }

    //Método verificar usuário logado
    fun verificarUsuarioLogado(){

        //Verificação de usuário logado
        if(user.currentUser != null){

            //Caso logado, abrirá a home activity
            startActivity(Intent(this, HomeActivity::class.java))
            this.finish()
        }else{

            //Caso deslogado, abrirá a tela de Login
            startActivity(Intent(this, LoginActivity::class.java))
            this.finish()
        }
    }
}