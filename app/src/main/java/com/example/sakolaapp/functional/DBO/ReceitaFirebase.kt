package com.example.sakolaapp.functional.DBO

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReceitaFirebase(
    val Bacon: Int? = 0,
    val Carne: Int? = 0,
    val Ovo: Int? = 0,
    val Pao: Int? = 0,
    val Presunto: Int? = 0,
    val Queijo: Int? = 0,
    val Salada: Int? = 0,
    val nome: String? = ""
) : Parcelable