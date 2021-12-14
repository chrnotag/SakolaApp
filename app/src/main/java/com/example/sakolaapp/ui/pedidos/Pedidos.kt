package com.example.sakolaapp.ui.pedidos

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sakolaapp.R
import com.example.sakolaapp.functional.adapters.recycleradapters.RecyclerPedidosAdapter
import kotlinx.android.synthetic.main.pedidos_fragment.*

class Pedidos : Fragment() {

    companion object {
        fun newInstance() = Pedidos()
    }

    private lateinit var viewModel: PedidosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pedidos_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PedidosViewModel::class.java)

        //Configurando o RecyclerView
        val layoutManager = LinearLayoutManager(context)
        val adapter = RecyclerPedidosAdapter()
        recyclerPedidos.layoutManager = layoutManager
        recyclerPedidos.adapter = adapter
    }

}