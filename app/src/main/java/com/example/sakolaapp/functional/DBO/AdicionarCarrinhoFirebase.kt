package com.example.sakolaapp.functional.adapters.DBO

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AdicionarCarrinhoFirebase(
    val produto:String = "",
    val qtd:String = "",
    val price:String = "",
    val img:String = ""
):Parcelable