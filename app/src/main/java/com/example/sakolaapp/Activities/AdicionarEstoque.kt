package com.example.sakolaapp.Activities

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.example.sakolaapp.R
import com.example.sakolaapp.functional.DBO.EstoqueFirebase
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_adicionar_estoquew.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.recycler_estoque.*
import java.util.*

class AdicionarEstoque : AppCompatActivity() {

    val firestore =
        FirebaseDatabase.getInstance().reference.child("estoque")   //Referencia ao estoque no Firebase

    var selecionarUri: Uri? =
        null      //Variavel para guardar a URL da imagem após salvar no Firebase Storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_estoquew)

        SalvarEstoque.setOnClickListener {   //Ação de clique no botão salvar
            if (selecionarUri != null        //Verificação de campos
                && NomeItemEstoque.text.isNotEmpty()
                && QtdItemEstoque.text.isNotEmpty()
            ) {
                SalvarDados()           //Chamada de metódo que salva os dados
            } else if (selecionarUri == null) {        //Controle de Erros
                Snackbar.make(
                    QtdItemEstoque,
                    "Por favor selecione uma Imagem",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                Snackbar.make(QtdItemEstoque, "Verifique os campos", Snackbar.LENGTH_SHORT).show()
            }
        }

        AddImgEstoque.setOnClickListener {      //Ação do toque na área da imagem
            PegarImagem()       //Chamada de método para pegar a imagem selecionada
        }
    }

    private fun SalvarDados() {     //Método para salvar dados no firebase

        val nomeArquivo = UUID.randomUUID()
            .toString()  //Randomiza um nome para a imagem a ser salva no Firebase Storage

        val storage = FirebaseStorage.getInstance()
            .getReference("imagens/${nomeArquivo}")     //Referencia ao Firebase Storage

        selecionarUri?.let { uri ->     //Passar a imagem pega do dispositivo como Uri para o Storage
            storage.putFile(uri)
                .addOnSuccessListener {

                    storage.downloadUrl.addOnSuccessListener {   //Método para pegar o link da imagem salva no Storage

                        val img = it.toString()     //Recupera o link da imagem retornado pelo método

                        val nome = NomeItemEstoque.text.toString()  //Recupera o nome digitado

                        val qtd = QtdItemEstoque.text.toString()
                            .toInt()    //Recupera a quantidade digitada

                        val cod = cod_estoque.text.toString()

                        val estoque = EstoqueFirebase(
                            img,
                            nome,
                            qtd
                        )   //Instancia da classe modelo EstoqueFirebase e passagem de dados


                        //Função para salvar os dados do estoque no Firebase Database
                        firestore.child(cod).setValue(estoque)

                            .addOnCompleteListener {
                                Toast.makeText(
                                    this,
                                    "Item Salvo com Sucesso",
                                    Toast.LENGTH_LONG
                                ).show()

                                updateUi()  //Chamada da função UpdateUi que faz a passagem de volta para a activity Home
                            }
                            .addOnFailureListener {     //Tratamento de erros
                                Toast.makeText(
                                    this,
                                    "Falha ao salvar Item",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    }
                }

                //Método que tráz o andamento do upload da imagem para o Storage e envia os dados para a ProgressBar
                .addOnProgressListener {

                    progressBarEstoque.visibility = View.VISIBLE
                    atual_percent_estoque.visibility = View.VISIBLE

                    progressBarEstoque.progress = 0
                    atual_percent_estoque.text = "0%"

                    val progress: Long = (100 * it.bytesTransferred / it.totalByteCount)    //Conta para saber a porcentagem atual

                    progressBarEstoque.progress = progress.toInt()

                    atual_percent_estoque.text = "${progress.toInt().toString()}%"
                }
                    //Método para tratamento de erros do upload
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Falha ao Carregar Imagem",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    //Método sobrescrito para resgatar a imagem selecionada no dispositivo
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null && requestCode == 0) {     //Verificando se Há imagem selecionada
            //Caso haja imagem selecionada, irá converte-la em bitmap e inserir no ImageView
            selecionarUri = data?.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selecionarUri)
            AddImgEstoque.setImageBitmap(bitmap)    //Inserindo Imagem no ImageView
        }
    }

    fun PegarImagem() {     //Função que abre a galeria para selecionar imagem
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)   //Chamada do método sobrescrito e passagem de código de função
    }

    override fun onBackPressed() {      //Função para finalizar a acitivty quando pressionado o botão voltar
        super.onBackPressed()
        this.finish()
    }

    fun updateUi() {        //Função para finalizar a activity quando chamado
        this.finish()
    }

}