package com.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Data(
    @SerializedName("id") val id: Long? = 0,
    @SerializedName("title") val title: String? = "",
    @SerializedName("description") val description: String? = "",
    @SerializedName("imageUrl") val image: String? = ""
) : Parcelable

@Parcelize
data class Item(
    @SerializedName("items") val data: List<Data> = emptyList()
) : Parcelable