package com.domain.result.http

import com.domain.result.Result

inline class HttpResult<T>(private val result: Result<T>) {

    companion object {
        inline fun <T> success(value: T): HttpResult<T> = HttpResult(Result.ok(value))
        inline fun <T> requestError(code: Int = -1, errorBody: ErrorBodyResponse): HttpResult<T> =
            HttpResult(HttpFailure<Nothing>(code, errorBody).getFailure())
        inline fun <T> requestError(exception: Exception): HttpResult<T> = HttpResult(Result.error(exception))
        inline fun <T> error(exception: Exception): HttpResult<T> = HttpResult(Result.error(exception))
        inline fun <T> error(
            code: Int = -1,
            errorBody: ErrorBodyResponse
        ): HttpResult<T> =
            HttpResult(HttpFailure<Nothing>(code, errorBody).getFailure())
    }

    class HttpFailure<T>(private val code: Int, private val errorBody: ErrorBodyResponse) {

        fun getFailure(): Result<T> = when (code) {
            400 -> Result.error(HttpCodeException(HttpCode.On400(errorBody)))
            401 -> Result.error(HttpCodeException(HttpCode.On401(errorBody)))
            403 -> Result.error(HttpCodeException(HttpCode.On403(errorBody)))
            404 -> Result.error(HttpCodeException(HttpCode.On404(errorBody)))
            408 -> Result.error(HttpCodeException(HttpCode.On408(errorBody)))
            429 -> Result.error(HttpCodeException(HttpCode.On429(errorBody)))
            500 -> Result.error(HttpCodeException(HttpCode.On500(errorBody)))
            501 -> Result.error(HttpCodeException(HttpCode.On501(errorBody)))
            502 -> Result.error(HttpCodeException(HttpCode.On502(errorBody)))
            503 -> Result.error(HttpCodeException(HttpCode.On503(errorBody)))
            504 -> Result.error(HttpCodeException(HttpCode.On504(errorBody)))
            999 -> Result.error(HttpCodeException(HttpCode.NetworkError(errorBody)))
            else -> Result.error(HttpCodeException(HttpCode.Unknown(code, errorBody)))
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getResponse(): T = when (result.get()) {
        is Result.Failure -> throw (result.get() as Result.Failure).exception as HttpCodeException
        else -> result.get() as T
    }

    @Suppress()
    fun safeResponse(): ContinuationBuilder<HttpResult<T>> = ContinuationBuilder { HttpResult(result) }

    fun get(): Result<T> = result
}

fun <T, R> HttpResult<T>.map(transform: (T) -> R): HttpResult<R> = when (val value = this.get().get()) {
    is Result.Failure -> HttpResult(Result(value))
    else -> HttpResult.success(transform(value as T))
}

fun <T, R> HttpResult<T>.flatMap(transform: (T) -> HttpResult<R>): HttpResult<R> = when (val value = this.get().get()) {
    is Result.Failure -> HttpResult(Result(value))
    else -> transform(value as T)
}

inline class ContinuationBuilder<T>(val run: suspend () -> T)

suspend infix fun <T> ContinuationBuilder<T>.or(defaultValue: (value : Exception) -> T): T = try {
    this.run()
} catch (e: Exception) { defaultValue(e) }

suspend fun <T> HttpResult<T>.run(success: (value : T) -> Unit, error: (error : Exception) -> Unit, finish: () -> Unit) = try {
    success(this.getResponse())
    finish()
} catch (e: Exception) {
    error(e)
    finish()
}

suspend infix fun <T> ContinuationBuilder<T>.orMaybe(defaultValue: T?): T? = try {
    this.run()
} catch (e: Exception) { defaultValue }
