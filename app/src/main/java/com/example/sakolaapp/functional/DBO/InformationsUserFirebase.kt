package com.example.sakolaapp.functional.DBO

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InformationsUserFirebase(
    val Nome: String = "",
    val Sobrenome: String = "",
    val ID: String = "",
    val Email: String = ""
): Parcelable