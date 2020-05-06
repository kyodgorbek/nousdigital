package com.data.helpers

import com.domain.result.http.ErrorBodyResponse
import com.domain.result.http.HttpResult
import com.google.gson.Gson
import retrofit2.Call

object WebService {

    fun <T> executeSafe(call: Call<T>): HttpResult<T> {
        val gson = Gson()
        try {
            val response = call.execute()
            response.let { safeResponse ->
                val body = safeResponse.body()
                if (safeResponse.isSuccessful.not() || body == null) {
                    var errorBodyString = "{}"
                    safeResponse.errorBody()?.let {
                        val errorBody = it.string()
                        if (errorBody.isBlank().not()) {
                            errorBodyString = errorBody
                        }
                    }
                    val errorBody = gson.fromJson(errorBodyString, ErrorBodyResponse::class.java)
                    return HttpResult.error(safeResponse.code(), errorBody)
                }
                return HttpResult.success(body)
            }
        } catch (e: Exception) {
            return HttpResult.error(errorBody = ErrorBodyResponse(message = e.message))
        }
    }
}