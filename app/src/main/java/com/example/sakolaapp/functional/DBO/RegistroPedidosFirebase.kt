package com.example.sakolaapp.functional.DBO

data class RegistroPedidosFirebase(
    val nome: String = "",
    val sobrenome: String = "",
    val imgPerfil: String? = "",
    val pedidos: Int = 0
)