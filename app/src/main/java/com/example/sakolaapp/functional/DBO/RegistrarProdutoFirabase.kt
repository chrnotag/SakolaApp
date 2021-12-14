package com.example.sakolaapp.functional.adapters.DBO

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.net.URI
import java.net.URL

@Parcelize
data class RegistrarProdutoFirabase(
    val cod: String = "",
    val img: String = "",
    val name: String = "",
    val price: String = "",
    val description: String = ""
):Parcelable