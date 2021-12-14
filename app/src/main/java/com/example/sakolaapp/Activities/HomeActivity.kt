package com.example.sakolaapp.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.example.sakolaapp.R
import com.example.sakolaapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class HomeActivity : AppCompatActivity() {

    //Váriavel para configurar a AppBar
    private lateinit var appBarConfiguration: AppBarConfiguration

    //Váriavel para receber o Binding da activity
    private lateinit var binding: ActivityMainBinding

    //Referencia do FirebaseAuth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Configurar o binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configurar a AppBar
        setSupportActionBar(binding.appBarMain.toolbar)

        //Referencia do FirebaseAuth
        auth = FirebaseAuth.getInstance()

        //Configurar o Drawerlayout
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        //Configurar o Drawer com a ActionBar e passar os ids do menu
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.produtos, R.id.pedidos, R.id.estoque, R.id.faturamento, R.id.exit
        ), drawerLayout)

        //Configurar Actionbar com o NavControler
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    //Colocar os itens de menu na ActionBar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    //Funções dos botões da ActionBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.cart -> {
                val intent = Intent(this, FinalizarPedido::class.java)
                startActivity(intent)
                return true
            }
            R.id.exit -> {
                Deslogar()
                return true
            }
            else -> return false
        }
    }

    //Suporte para o Navigation
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    //Método para Deslogar usuário e voltar para a tela de login
    fun Deslogar(){
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}