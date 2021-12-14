package com.example.sakolaapp.Activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.sakolaapp.R
import com.example.sakolaapp.functional.DBO.dadosUserFirebase
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance() //Setando o Firebase Auth

    val firestore = FirebaseDatabase.getInstance().reference //Referencia do Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //Método para chamada de registro do usuário
        Registrar.setOnClickListener {

            //Resgatar dados digitados pelo usuário
            val email = emailRegister.text.toString()
            val senha = senhaRegister.text.toString()
            val nome = nomeUser.text.toString()
            val sobrenome = sobrenomeUser.text.toString()
            val id = idUser.text.toString()

            //Chamar o método para Registrar usuário
            createAccount(email, senha)
        }

    }

    //Método para registrar Usuário com email e senha
    private fun createAccount(email: String, password: String) {

        //Pegar as informações digitadas pelo usuário
        var confsenha = confSenhaRegister.text.toString()
        val nome = nomeUser.text.toString()
        val sobrenome = sobrenomeUser.text.toString()
        val id = idUser.text.toString()

        //Verifica se as senhas são iguais
        if (password.isNotEmpty()
            && confsenha.isNotEmpty()
            && email.isNotEmpty()
            && password.equals(confsenha)
            && nome.isNotEmpty()
            && sobrenome.isNotEmpty()
        ) {

            //Iniciar registro de novo usuário
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->

                    progressBarRegister.visibility = View.VISIBLE

                    if (task.isSuccessful) {

                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser //Retorna o ID do usuário atual

                        //Salva os dados do Usuário no Database
                        firestore.child("usuarios")
                            .child(auth.currentUser?.uid.toString())
                            .setValue(dadosUserFirebase(nome, sobrenome, email, id))
                            .addOnSuccessListener {

                                //Mensagem para confirmar o registro
                                Toast.makeText(
                                    this,
                                    "Usuário Cadastrado com sucesso",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                updateUI(user) //Chamada do método updateUi
                            }
                            .addOnFailureListener {

                            }
                    } else {

                        progressBarRegister.visibility = View.GONE

                        //Tratamentos de Erros
                        updateUI(null)  //Chamada do método UpdateUi
                    }
                }
                //Tratamento de Erros
                .addOnFailureListener {
                    when (it) {

                        //Email inválido
                        is FirebaseAuthInvalidUserException -> {
                            emailRegister.setHintTextColor(Color.rgb(218, 69, 33))
                            Snackbar.make(
                                Constain,
                                "Por favor insira um email válido!",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }

                        //Credeciais Inválidas
                        is FirebaseAuthInvalidCredentialsException -> {
                            emailRegister.setHintTextColor(Color.rgb(218, 69, 33))
                            Snackbar.make(
                                Constain,
                                "Por favor insira um email válido!",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }

                        //Usuário já existente
                        is FirebaseAuthUserCollisionException -> {
                            emailRegister.setHintTextColor(Color.rgb(218, 69, 33))
                            Snackbar.make(Constain, "Email já cadastrado!", Snackbar.LENGTH_SHORT)
                                .show()
                        }

                        //Senha fraca
                        is FirebaseAuthWeakPasswordException -> {
                            senhaRegister.setHintTextColor(Color.rgb(218, 69, 33))
                            Snackbar.make(
                                Constain,
                                "A senha deve conter no minimo 6 carácteres",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            //Verificação de Erro dos campos do formulário
        } else {

            if (nome.isEmpty()) {
                nomeUser.setHintTextColor(Color.rgb(218, 69, 33))
                Snackbar.make(
                    Constain,
                    "Por favor, verifique os campos marcados",
                    Snackbar.LENGTH_SHORT
                ).show()

            } else if (sobrenome.isEmpty()) {

                sobrenomeUser.setHintTextColor(Color.rgb(218, 69, 33))
                Snackbar.make(
                    Constain,
                    "Por favor, verifique os campos marcados",
                    Snackbar.LENGTH_SHORT
                ).show()

            } else if (id.isEmpty()) {

                idUser.setHintTextColor(Color.rgb(218, 69, 33))
                Snackbar.make(
                    Constain,
                    "Por favor, verifique os campos marcados",
                    Snackbar.LENGTH_SHORT
                ).show()

            } else if (password.isEmpty()) {

                senhaRegister.setHintTextColor(Color.rgb(218, 69, 33))
                Snackbar.make(
                    Constain,
                    "Por favor, verifique os campos marcados",
                    Snackbar.LENGTH_SHORT
                ).show()

            } else if (confsenha.isEmpty()) {

                confSenhaRegister.setHintTextColor(Color.rgb(218, 69, 33))
                Snackbar.make(
                    Constain,
                    "Por favor, verifique os campos marcados",
                    Snackbar.LENGTH_SHORT
                ).show()

            } else if (password != confsenha) {
                senhaRegister.setTextColor(Color.rgb(218, 69, 33))
                confSenhaRegister.setTextColor(Color.rgb(218, 69, 33))
                Snackbar.make(Constain, "Senhas não conferem", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    // Abrir a homeActivity
    private fun updateUI(user: FirebaseUser?) {

        if (user != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            this.finish()
        }

    }
}