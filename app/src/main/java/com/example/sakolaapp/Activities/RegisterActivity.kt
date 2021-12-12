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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance() //Setando o Firebase Auth

    val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        Registrar.setOnClickListener {  //Método para chamada de registro do usuário
            val email = emailRegister.text.toString()
            val senha = senhaRegister.text.toString()
            val nome = nomeUser.text.toString()
            val sobrenome = sobrenomeUser.text.toString()
            val id = idUser.text.toString()

            createAccount(email, senha)
        }

    }

    private fun createAccount(email: String, password: String) {  //Registrar Novo Usuário

        var confsenha = confSenhaRegister.text.toString() //Recebe a confirmação da senha
        val nome = nomeUser.text.toString()
        val sobrenome = sobrenomeUser.text.toString()
        val id = idUser.text.toString()

        //Verifica se as senhas são iguais
        if (password.isNotEmpty()
            && confsenha.isNotEmpty()
            && email.isNotEmpty()
            && password.equals(confsenha)
            && nome.isNotEmpty()
            && sobrenome.isNotEmpty()) {

            //Iniciar registro de novo usuário
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    progressBarRegister.visibility = View.VISIBLE
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser //Retorna o ID do usuário atual

                        firestore.collection(auth.currentUser?.uid.toString())
                            .document("informations")
                            .collection("informations")
                            .add(dadosUserFirebase(nome, sobrenome, email, id))
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this,
                                    "Usuário Cadastrado com sucesso",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                updateUI(user) //Inicia o Aplicativo
                            }
                            .addOnFailureListener {

                            }
                    } else {
                        progressBarRegister.visibility = View.GONE
                        //Tratamentos de Erros
                        updateUI(null)
                    }
                }
                .addOnFailureListener {
                    when(it){
                        is FirebaseAuthInvalidUserException -> {
                            emailRegister.setHintTextColor(Color.rgb(218,69,33))
                            Snackbar.make(Constain, "Por favor insira um email válido!", Snackbar.LENGTH_SHORT).show()
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            emailRegister.setHintTextColor(Color.rgb(218,69,33))
                            Snackbar.make(Constain, "Por favor insira um email válido!", Snackbar.LENGTH_SHORT).show()
                        }
                        is FirebaseAuthUserCollisionException -> {
                            emailRegister.setHintTextColor(Color.rgb(218,69,33))
                            Snackbar.make(Constain, "Email já cadastrado!", Snackbar.LENGTH_SHORT).show()
                        }
                        is FirebaseAuthWeakPasswordException -> {
                            senhaRegister.setHintTextColor(Color.rgb(218,69,33))
                            Snackbar.make(Constain, "A senha deve conter no minimo 6 carácteres", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
        } else { //Verificação de Erro de Formulário
            if(nome.isEmpty()){
                nomeUser.setHintTextColor(Color.rgb(218,69,33))
                Snackbar.make(Constain, "Por favor, verifique os campos marcados", Snackbar.LENGTH_SHORT).show()
            }else if(sobrenome.isEmpty()){
                sobrenomeUser.setHintTextColor(Color.rgb(218,69,33))
                Snackbar.make(Constain, "Por favor, verifique os campos marcados", Snackbar.LENGTH_SHORT).show()
            }else if(id.isEmpty()){
                idUser.setHintTextColor(Color.rgb(218,69,33))
                Snackbar.make(Constain, "Por favor, verifique os campos marcados", Snackbar.LENGTH_SHORT).show()
            }else if (password.isEmpty()){
                senhaRegister.setHintTextColor(Color.rgb(218,69,33))
                Snackbar.make(Constain, "Por favor, verifique os campos marcados", Snackbar.LENGTH_SHORT).show()
            }else if(confsenha.isEmpty()){
                confSenhaRegister.setHintTextColor(Color.rgb(218,69,33))
                Snackbar.make(Constain, "Por favor, verifique os campos marcados", Snackbar.LENGTH_SHORT).show()
            }else if(password != confsenha){
                senhaRegister.setTextColor(Color.rgb(218,69,33))
                confSenhaRegister.setTextColor(Color.rgb(218,69,33))
                Snackbar.make(Constain, "Senhas não conferem", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    private fun updateUI(user: FirebaseUser?) {
        // Abrir a homeActivity
        if (user != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            this.finish()
        }

    }
}