package com.example.sakolaapp.Activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import com.example.sakolaapp.R
import com.example.sakolaapp.databinding.ActivityAdicionarProdutosBinding
import com.example.sakolaapp.functional.DBO.EstoqueFirebase
import com.example.sakolaapp.functional.DBO.ReceitaFirebase
import com.example.sakolaapp.functional.adapters.DBO.RegistrarProdutoFirabase
import com.example.sakolaapp.ui.produtos.Produtos
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_adicionar_produtos.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class AdicionarProdutos : AppCompatActivity() {

    private var selecionarUri: Uri? =
        null  //Variavel para salvar a imagem selecionada no dispositivo como URI

    private lateinit var binding: ActivityAdicionarProdutosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdicionarProdutosBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        configurarChecks()
        configurarBtnChecks()

        AddImgproduto.setOnClickListener {      //Evento de click no Imageview
            SelecionarFoto()
        }

        SalvarProduto.setOnClickListener {       //Evento de click no botão salvar

            //Verifica se alguma imagem foi selecionada no dispositivo e se os campos estão completos
            if (selecionarUri != null
                && NomeProduto.text.isNotEmpty()
                && Price.text.isNotEmpty()
                && Desc.text.isNotEmpty()
            ) {

                //Caso tudo ocorra bem, chamará o método para salvar os dados
                SalvarDados()
                salvarReceita()

                //Tratamento de erros
            } else if (selecionarUri == null) {
                Snackbar.make(Desc, "Por favor adicione uma imagem", Snackbar.LENGTH_SHORT).show()
            } else {
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
    fun SelecionarFoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(
            intent,
            0
        )   //Chamada do método sobrescrito passando o codigo de função
    }

    //Método para salvar dados
    fun SalvarDados() {

        val nomeArquivo =
            UUID.randomUUID().toString()  //Randomizar o nome da imagem a ser salva no Storage

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
                        val cod = binding.codigoProduto.text.toString()

                        //Chamada da classe Modelo RegistrarProdutosFirebase passando as informações capturadas acima
                        //Para serem salvas no Database
                        val produtos = RegistrarProdutoFirabase(cod, img, name, price, description)

                        //Adicionar os valores da classe modelo no Database
                        FirebaseDatabase.getInstance().reference.child("Produtos")
                            .setValue(produtos)
                            .addOnCompleteListener {

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
                    val progress: Long = (100 * it.bytesTransferred / it.totalByteCount)

                    progressBarAdiconarProduto.progress = progress.toInt()
                    atual_percent.text = "${progress.toString()}%"
                }
        }
    }

    fun salvarReceita() {
        val nomeLanche = NomeProduto.text.toString()
        var pao: Int = 0
        var carne: Int = 0
        var queijo: Int = 0
        var presunto: Int = 0
        var salada: Int = 0
        var bacon: Int = 0
        var ovos: Int = 0

        if (checkPao.isChecked) {
            pao = qtdPao.text.toString().toInt()
        }
        if (checkCarne.isChecked) {
            carne = qtdCarne.text.toString().toInt()
        }

        if (checkQueijo.isChecked) {
            queijo = qtdQueijo.text.toString().toInt()
        }

        if (checkPresunto.isChecked)
            presunto = qtdPresunto.text.toString().toInt()

        if (checkSalada.isChecked)
            salada = qtdSalada.text.toString().toInt()

        if (checkBacon.isChecked)
            bacon = qtdBacon.text.toString().toInt()
        if (checkOvo.isChecked)
            ovos = qtdOvo.text.toString().toInt()

        val receita = ReceitaFirebase(
            bacon,
            carne,
            ovos,
            pao,
            presunto,
            queijo,
            salada,
            nomeLanche)

        FirebaseDatabase.getInstance().reference
            .child("Produtos")
            .child("receita")
            .setValue(receita)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Receita Salva com sucesso",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    //Método Sobrescrito para recuperar a imagem selecionada pelo usuário
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && data != null) {
            selecionarUri = data?.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selecionarUri)

            //Recupera a imagem selecionada pelo usuário e insere no ImageView
            AddImgproduto.setImageBitmap(bitmap)
        }

    }

    fun configurarChecks() {

        binding.checkPao.setOnClickListener {
            if (binding.checkPao.isChecked) {
                addPao.visibility = View.VISIBLE
                rmvPao.visibility = View.VISIBLE
                qtdPao.visibility = View.VISIBLE
            } else {
                addPao.visibility = View.GONE
                rmvPao.visibility = View.GONE
                qtdPao.visibility = View.GONE
            }
        }

        binding.checkCarne.setOnClickListener {
            if (binding.checkCarne.isChecked) {
                addCarne.visibility = View.VISIBLE
                rmvCarne.visibility = View.VISIBLE
                qtdCarne.visibility = View.VISIBLE
            } else {
                addCarne.visibility = View.GONE
                rmvCarne.visibility = View.GONE
                qtdCarne.visibility = View.GONE
            }
        }

        binding.checkQueijo.setOnClickListener {
            if (binding.checkQueijo.isChecked) {
                addQueijo.visibility = View.VISIBLE
                rmvQueijo.visibility = View.VISIBLE
                qtdQueijo.visibility = View.VISIBLE
            } else {
                addQueijo.visibility = View.GONE
                rmvQueijo.visibility = View.GONE
                qtdQueijo.visibility = View.GONE
            }
        }

        binding.checkPresunto.setOnClickListener {
            if (binding.checkPresunto.isChecked) {
                addPresunto.visibility = View.VISIBLE
                rmvPresunto.visibility = View.VISIBLE
                qtdPresunto.visibility = View.VISIBLE
            } else {
                addPresunto.visibility = View.GONE
                rmvPresunto.visibility = View.GONE
                qtdPresunto.visibility = View.GONE
            }
        }

        binding.checkSalada.setOnClickListener {
            if (binding.checkSalada.isChecked) {
                addSalada.visibility = View.VISIBLE
                rmvSalada.visibility = View.VISIBLE
                qtdSalada.visibility = View.VISIBLE
            } else {
                addSalada.visibility = View.GONE
                rmvSalada.visibility = View.GONE
                qtdSalada.visibility = View.GONE
            }
        }

        binding.checkBacon.setOnClickListener {
            if (binding.checkBacon.isChecked) {
                addBacon.visibility = View.VISIBLE
                rmvBacon.visibility = View.VISIBLE
                qtdBacon.visibility = View.VISIBLE
            } else {
                addBacon.visibility = View.GONE
                rmvBacon.visibility = View.GONE
                qtdBacon.visibility = View.GONE
            }
        }

        binding.checkOvo.setOnClickListener {
            if (binding.checkOvo.isChecked) {
                addOvo.visibility = View.VISIBLE
                rmvOvo.visibility = View.VISIBLE
                qtdOvo.visibility = View.VISIBLE
            } else {
                addOvo.visibility = View.GONE
                rmvOvo.visibility = View.GONE
                qtdOvo.visibility = View.GONE

            }
        }
    }


    fun configurarBtnChecks() {
        addPao.setOnClickListener {
            if (qtdPao.text.toString().toInt() >= 0) {
                qtdPao.text = (qtdPao.text.toString().toInt() + 1).toString()
            }
        }

        rmvPao.setOnClickListener {
            if (qtdPao.text.toString().toInt() > 0) {
                qtdPao.text = (qtdPao.text.toString().toInt() - 1).toString()
            }
        }

        addCarne.setOnClickListener {
            if (qtdCarne.text.toString().toInt() >= 0) {
                qtdCarne.text = (qtdCarne.text.toString().toInt() + 1).toString()
            }
        }

        rmvCarne.setOnClickListener {
            if (qtdCarne.text.toString().toInt() > 0) {
                qtdCarne.text = (qtdCarne.text.toString().toInt() - 1).toString()
            }
        }

        addQueijo.setOnClickListener {
            if (qtdQueijo.text.toString().toInt() >= 0) {
                qtdQueijo.text = (qtdQueijo.text.toString().toInt() + 1).toString()
            }
        }

        rmvQueijo.setOnClickListener {
            if (qtdQueijo.text.toString().toInt() > 0) {
                qtdQueijo.text = (qtdQueijo.text.toString().toInt() - 1).toString()
            }
        }

        addPresunto.setOnClickListener {
            if (qtdPresunto.text.toString().toInt() >= 0) {
                qtdPresunto.text = (qtdPresunto.text.toString().toInt() + 1).toString()
            }
        }

        rmvPresunto.setOnClickListener {
            if (qtdPresunto.text.toString().toInt() > 0) {
                qtdPresunto.text = (qtdPresunto.text.toString().toInt() - 1).toString()
            }
        }

        addSalada.setOnClickListener {
            if (qtdSalada.text.toString().toInt() >= 0) {
                qtdSalada.text = (qtdSalada.text.toString().toInt() + 1).toString()
            }
        }

        rmvSalada.setOnClickListener {
            if (qtdSalada.text.toString().toInt() > 0) {
                qtdSalada.text = (qtdSalada.text.toString().toInt() - 1).toString()
            }
        }

        addBacon.setOnClickListener {
            if (qtdBacon.text.toString().toInt() >= 0) {
                qtdBacon.text = (qtdBacon.text.toString().toInt() + 1).toString()
            }
        }

        rmvBacon.setOnClickListener {
            if (qtdBacon.text.toString().toInt() > 0) {
                qtdBacon.text = (qtdBacon.text.toString().toInt() - 1).toString()
            }
        }

        addOvo.setOnClickListener {
            if (qtdOvo.text.toString().toInt() >= 0) {
                qtdOvo.text = (qtdOvo.text.toString().toInt() + 1).toString()
            }
        }

        rmvOvo.setOnClickListener {
            if (qtdOvo.text.toString().toInt() > 0) {
                qtdOvo.text = (qtdOvo.text.toString().toInt() - 1).toString()
            }
        }
    }
}