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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_adicionar_estoquew.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.recycler_estoque.*
import java.util.*

class AdicionarEstoque : AppCompatActivity() {

    val firestore = FirebaseFirestore.getInstance()
        .collection("estoque")

    var selecionarUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_estoquew)

        SalvarEstoque.setOnClickListener {
            if(selecionarUri != null
                && NomeItemEstoque.text.isNotEmpty()
                && QtdItemEstoque.text.isNotEmpty()){
                    SalvarDados()
            }else if(selecionarUri == null){
                Snackbar.make(QtdItemEstoque, "Por favor selecione uma Imagem", Snackbar.LENGTH_SHORT).show()
            }else{
                Snackbar.make(QtdItemEstoque, "Verifique os campos", Snackbar.LENGTH_SHORT).show()
            }
        }

        AddImgEstoque.setOnClickListener {
            PegarImagem()
        }
    }

    private fun SalvarDados() {
        val nomeArquivo = UUID.randomUUID().toString()
        val storage = FirebaseStorage.getInstance()
            .getReference("imagens/${nomeArquivo}")
        selecionarUri?.let { uri ->
            storage.putFile(uri)
                .addOnSuccessListener {
                    storage.downloadUrl.addOnSuccessListener {
                        val img = it.toString()
                        val nome = NomeItemEstoque.text.toString()
                        val qtd = QtdItemEstoque.text.toString()
                        val estoque = EstoqueFirebase(img, nome, qtd)

                        firestore.add(estoque)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this,
                                    "Item Salvo com Sucesso",
                                    Toast.LENGTH_LONG
                                ).show()

                                this.finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this,
                                    "Falha ao salvar Item",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    }
                }
                .addOnProgressListener {
                    progressBarEstoque.visibility = View.VISIBLE
                    atual_percent_estoque.visibility = View.VISIBLE

                    progressBarEstoque.progress = 0
                    atual_percent_estoque.text = "0%"

                    val progress: Long = (100 * it.bytesTransferred / it.totalByteCount)

                    progressBarEstoque.progress = progress.toInt()

                    atual_percent_estoque.text = "${progress.toInt().toString()}%"
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Falha ao Carregar Imagem",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == 0) {
            selecionarUri = data?.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selecionarUri)
            AddImgEstoque.setImageBitmap(bitmap)
        }
    }

    fun PegarImagem() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        if (intent != null) {
            startActivityForResult(intent, 0)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

}