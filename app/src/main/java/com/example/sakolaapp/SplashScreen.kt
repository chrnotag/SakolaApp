package com.example.sakolaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sakolaapp.Activities.HomeActivity
import com.example.sakolaapp.Activities.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {

    var user = FirebaseAuth.getInstance() //Instancia do FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        verificarUsuarioLogado()
    }

    fun verificarUsuarioLogado(){
        if(user.currentUser != null){
            startActivity(Intent(this, HomeActivity::class.java))
            this.finish()
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
            this.finish()
        }
    }
}