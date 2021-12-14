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
import com.example.sakolaapp.ui.produtos.Produtos
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_adicionar_produtos.*
import java.util.*

class AdicionarProdutos : AppCompatActivity() {

    private var selecionarUri: Uri? = null  //Variavel para salvar a imagem selecionada no dispositivo como URI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_produtos)

        AddImgproduto.setOnClickListener {      //Evento de click no Imageview
            SelecionarFoto()
        }

        SalvarProduto.setOnClickListener{       //Evento de click no botão salvar

            //Verifica se alguma imagem foi selecionada no dispositivo e se os campos estão completos
            if(selecionarUri != null
                && NomeProduto.text.isNotEmpty()
                && Price.text.isNotEmpty()
                && Desc.text.isNotEmpty()){

                    //Caso tudo ocorra bem, chamará o método para salvar os dados
                SalvarDados()

                //Tratamento de erros
            }else if(selecionarUri == null){
                Snackbar.make(Desc, "Por favor adicione uma imagem", Snackbar.LENGTH_SHORT).show()
            }else{
                Snackbar.make(Desc, "Verifique os campos", Snackbar.LENGTH_SHORT).show()
            }
        }

    }

    override fun onBackPressed() {  //função para ao pressionar o botão voltar, seja fechada essa activity
        //e a home seja chamada
        super.onBackPressed()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    //Função para abrir a galeria
    fun SelecionarFoto(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)   //Chamada do método sobrescrito passando o codigo de função
    }

    //Método para salvar dados
    fun SalvarDados(){

        val nomeArquivo = UUID.randomUUID().toString()  //Randomizar o nome da imagem a ser salva no Storage

        //Referencia do FirebaseStorage
        val reference = FirebaseStorage.getInstance().getReference("imagens/${nomeArquivo}")

        //Método para salvar a imagem no Storage
        selecionarUri?.let { uri ->

            reference.putFile(uri)

                .addOnSuccessListener {

                    //Caso a imagem seja salva com sucesso, irá retornar a Url
                    reference.downloadUrl.addOnSuccessListener { it ->

                        val img = it.toString() //Url da imagem salva no Storage

                        //Pegando os valores digitados pelo usuário
                        val name = NomeProduto.text.toString()
                        val price = Price.text.toString()
                        val description = Desc.text.toString()
                        val cod = Cod_Produto.text.toString()

                        //Chamada da classe Modelo RegistrarProdutosFirebase passando as informações capturadas acima
                        //Para serem salvas no Database
                        val produtos = RegistrarProdutoFirabase(cod, img, name, price, description)

                        //Adicionar os valores da classe modelo no Database
                        FirebaseDatabase.getInstance().reference.child("Produtos").child(produtos.cod)
                            .setValue(produtos)
                            .addOnCompleteListener {
                                //Caso salve com sucesso, chamará o método onBackPressed para fechar essa activity
                                Toast.makeText(this, "Sucesso ao salvar", Toast.LENGTH_LONG).show()
                                onBackPressed()

                            }
                                //Tratamento de erros
                            .addOnFailureListener {
                                Toast.makeText(this, "Falha ao salvar", Toast.LENGTH_LONG).show()
                            }
                    }
                }
                    //Pegar o progresso de upload da imagem para o Storage
                .addOnProgressListener {

                    progressBarAdiconarProduto.visibility = View.VISIBLE
                    atual_percent.visibility = View.VISIBLE

                    progressBarAdiconarProduto.progress = 0

                    //Calculo para a procentagem do Upload
                    val progress: Long = (100*it.bytesTransferred/it.totalByteCount)

                    progressBarAdiconarProduto.progress = progress.toInt()
                    atual_percent.text = "${progress.toString()}%"
                }
        }
    }

    //Método Sobrescrito para recuperar a imagem selecionada pelo usuário
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && data != null){
            selecionarUri = data?.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selecionarUri)

            //Recupera a imagem selecionada pelo usuário e insere no ImageView
            AddImgproduto.setImageBitmap(bitmap)
        }

    }
}