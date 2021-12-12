package com.example.sakolaapp.Activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sakolaapp.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {

    lateinit var user: FirebaseAuth //Instancia do FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )

        user = FirebaseAuth.getInstance() //Pegando a instancia atual

        registerScreen.setOnClickListener {  //Abrir activity de cadastro
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        Entrar.setOnClickListener {     //Logar com email e senha
            val email = emailLogin.text.toString()      //Retorna email digitado
            val senha = senhaLogin.text.toString()      //Retorna Senha digitada

            logarUsuário(email, senha)   //Chama o método de Login com email e senha
        }
    }

    private fun logarUsuário(email: String, senha: String) { //método de login

        if(email.isNotEmpty() && senha.isNotEmpty()){
        user.signInWithEmailAndPassword(email, senha)
            .addOnSuccessListener { //Se o usuário logar, será enviado para a tela principal
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                this.finish()
            }
            .addOnFailureListener {
                when (it) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        Snackbar.make(
                            registerScreen,
                            "Usuário ou senha Inválidos",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        emailLogin.setTextColor(Color.rgb(218,63,39))
                    }
                    is FirebaseAuthInvalidUserException -> {
                        Snackbar.make(
                            registerScreen,
                            "Email não cadastrado",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        emailLogin.setTextColor(Color.rgb(218,63,39))
                    }
                }
            }
        }else {
            Snackbar.make(registerScreen, "Por favor verifique os campos", Snackbar.LENGTH_SHORT).show()
        }
    }
}