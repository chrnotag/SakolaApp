package com.example.sakolaapp.functional.DBO

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EstoqueFirebase(
    val img: String = "",
    val nomeItem: String = "",
    val qtdItem: Int = 0
):Parcelable