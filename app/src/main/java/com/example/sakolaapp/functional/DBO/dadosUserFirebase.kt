package com.example.sakolaapp.functional.DBO

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class dadosUserFirebase(
    val nome: String = "",
    val sobrenome: String = "",
    val email: String = "",
    val id: String = "",
    val imgPerfil: String? = ""
):Parcelable