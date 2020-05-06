package com.data.features.item.remote

import com.domain.models.Item
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface ItemRemoteSource {

    @GET("s/Njedq4WpjWz4KKk/download")
    suspend fun getItems(): Item
}