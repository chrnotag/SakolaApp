package com.example.sakolaapp.functional.DBO

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InformationsUserFirebase(
    val nome: String = "",
    val sobrenome: String = "",
    val id: Int = 0,
    val email: String = "",
    val imgPerfil: String? = ""
): Parcelable