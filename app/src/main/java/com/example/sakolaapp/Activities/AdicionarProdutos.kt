package com.example.sakolaapp.Activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.example.sakolaapp.R
import com.example.sakolaapp.functional.adapters.DBO.RegistrarProdutoFirabase
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_adicionar_estoquew.*
import kotlinx.android.synthetic.main.activity_adicionar_produtos.*
import kotlinx.android.synthetic.main.recycler_produtos_layout.*
import java.util.*

class AdicionarProdutos : AppCompatActivity() {

    private var selecionarUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_produtos)

        AddImgproduto.setOnClickListener {
            SelecionarFoto()
        }
        SalvarProduto.setOnClickListener{
            if(selecionarUri != null
                && NomeProduto.text.isNotEmpty()
                && Price.text.isNotEmpty()
                && Desc.text.isNotEmpty()){
                SalvarDados()
            }else if(selecionarUri == null){
                Snackbar.make(Desc, "Por favor adicione uma imagem", Snackbar.LENGTH_SHORT).show()
            }else{
                Snackbar.make(Desc, "Verifique os campos", Snackbar.LENGTH_SHORT).show()
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    fun SelecionarFoto(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        if(intent != null){
            startActivityForResult(intent, 0)
        }
    }

    fun SalvarDados(){
        val nomeArquivo = UUID.randomUUID().toString()
        val reference = FirebaseStorage.getInstance().getReference("imagens/${nomeArquivo}")

        selecionarUri?.let { uri ->
            reference.putFile(uri)
                .addOnSuccessListener {
                    reference.downloadUrl.addOnSuccessListener { it ->
                        val img = it.toString()
                        val name = NomeProduto.text.toString()
                        val price = Price.text.toString()
                        val description = Desc.text.toString()

                        val produtos = RegistrarProdutoFirabase(img, name, price, description)

                        FirebaseFirestore.getInstance().collection("Produtos")
                            .add(produtos)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Sucesso ao salvar", Toast.LENGTH_LONG).show()
                                onBackPressed()

                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Falha ao salvar", Toast.LENGTH_LONG).show()
                            }
                    }
                }.addOnProgressListener {
                    progressBarAdiconarProduto.visibility = View.VISIBLE
                    atual_percent.visibility = View.VISIBLE
                    progressBarAdiconarProduto.progress = 0
                    val progress: Long = (100*it.bytesTransferred/it.totalByteCount)
                    progressBarAdiconarProduto.progress = progress.toInt()
                    atual_percent.text = "${progress.toString()}%"
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && data != null){
            selecionarUri = data?.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selecionarUri)
            AddImgproduto.setImageBitmap(bitmap)
        }

    }
}