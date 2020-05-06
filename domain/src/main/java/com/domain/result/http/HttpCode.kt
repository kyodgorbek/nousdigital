package com.domain.result.http

sealed class HttpCode(val httpCode: Int, val body: ErrorBodyResponse? = null) {
    open class On400(val errorBody: ErrorBodyResponse) : HttpCode(400, errorBody) //Bad Request
    open class On401(val errorBody: ErrorBodyResponse) : HttpCode(401, errorBody) //Unauthorized
    open class On403(val errorBody: ErrorBodyResponse) : HttpCode(403, errorBody) //Forbidden
    open class On404(val errorBody: ErrorBodyResponse) : HttpCode(404, errorBody) //not found
    open class On408(val errorBody: ErrorBodyResponse) : HttpCode(408, errorBody) //request Timeout
    open class On429(val errorBody: ErrorBodyResponse) : HttpCode(429, errorBody) //Too many requests
    open class On500(val errorBody: ErrorBodyResponse) : HttpCode(500, errorBody) //Internal Server Error
    open class On501(val errorBody: ErrorBodyResponse) : HttpCode(501, errorBody) //Not Implemented
    open class On502(val errorBody: ErrorBodyResponse) : HttpCode(502, errorBody) //Bad Gateway
    open class On503(val errorBody: ErrorBodyResponse) : HttpCode(503, errorBody) //Service Unavailable
    open class On504(val errorBody: ErrorBodyResponse) : HttpCode(504, errorBody) //Gateway Timeout
    open class OnV3Error(val errorBody: ErrorBodyResponse) : HttpCode(errorBody.code?.toInt() ?: -1, errorBody) // All V3 errors with response 200-ok.
    open class OnV3ParseError : HttpCode(-1) // A V3 error that could not be parsed
    open class NetworkError(val errorBody: ErrorBodyResponse? = null) : HttpCode(999, errorBody) // Network problem
    open class Unknown(httpErrorCode: Int, val errorBody: ErrorBodyResponse) : HttpCode(httpErrorCode, errorBody)
    open class Socket(val errorBody: ErrorBodyResponse? = null) : HttpCode(900, errorBody)
    open class SocketTimeout(val errorBody: ErrorBodyResponse? = null) : HttpCode(901, errorBody)
    open class MalformedJson(val errorBody: ErrorBodyResponse? = null) : HttpCode(902, errorBody)
    open class EndOfFile(val errorBody: ErrorBodyResponse? = null) : HttpCode(903, errorBody)
    open class InterruptedOperation(val errorBody: ErrorBodyResponse? = null) : HttpCode(904, errorBody)
    open class IllegalState(val errorBody: ErrorBodyResponse? = null) : HttpCode(905, errorBody)
}

class HttpCodeException(val code: HttpCode) : Exception()

data class ErrorBodyResponse(
    val code: String? = null,
    val details: List<String?>? = null,
    val localizedMessage: String? = null,
    val message: String? = null
)

inline fun HttpCodeException.is400(action: (ErrorBodyResponse) -> Unit) {
    if (this.code is HttpCode.On400) {
        action(this.code.errorBody)
    }
}

inline fun HttpCodeException.is401(action: (ErrorBodyResponse) -> Unit) {
    if (this.code is HttpCode.On401) {
        action(this.code.errorBody)
    }
}

inline fun HttpCodeException.is404(action: (ErrorBodyResponse) -> Unit) {
    if (this.code is HttpCode.On404) {
        action(this.code.errorBody)
    }
}

inline fun HttpCodeException.is429(action: (ErrorBodyResponse) -> Unit) {
    if (this.code is HttpCode.On429) {
        action(this.code.errorBody)
    }
}

inline fun HttpCodeException.is500(action: (ErrorBodyResponse) -> Unit) {
    if (this.code is HttpCode.On500) {
        action(this.code.errorBody)
    }
}

inline fun HttpCodeException.is501(action: (ErrorBodyResponse) -> Unit) {
    if (this.code is HttpCode.On501) {
        action(this.code.errorBody)
    }
}

inline fun HttpCodeException.is502(action: (ErrorBodyResponse) -> Unit) {
    if (this.code is HttpCode.On502) {
        action(this.code.errorBody)
    }
}

inline fun HttpCodeException.is503(action: (ErrorBodyResponse) -> Unit) {
    if (this.code is HttpCode.On503) {
        action(this.code.errorBody)
    }
}

inline fun HttpCodeException.is504(action: (ErrorBodyResponse) -> Unit) {
    if (this.code is HttpCode.On504) {
        action(this.code.errorBody)
    }
}

class NetworkException(
    val code: HttpCode,
    val causeName: String? = null,
    val causeMessage: String? = null,
    rootCause: Throwable? = null
) : Exception(causeMessage, rootCause) {

    companion object {
        fun newInstance(
            code: Int,
            errorBody: ErrorBodyResponse,
            rootCause: Throwable? = null
        ): NetworkException {
            return when (code) {
                400 -> NetworkException(HttpCode.On400(errorBody), rootCause = rootCause)
                401 -> NetworkException(HttpCode.On401(errorBody), rootCause = rootCause)
                403 -> NetworkException(HttpCode.On403(errorBody), rootCause = rootCause)
                404 -> NetworkException(HttpCode.On404(errorBody), rootCause = rootCause)
                408 -> NetworkException(HttpCode.On408(errorBody), rootCause = rootCause)
                429 -> NetworkException(HttpCode.On429(errorBody), rootCause = rootCause)
                500 -> NetworkException(HttpCode.On500(errorBody), rootCause = rootCause)
                501 -> NetworkException(HttpCode.On501(errorBody), rootCause = rootCause)
                502 -> NetworkException(HttpCode.On502(errorBody), rootCause = rootCause)
                503 -> NetworkException(HttpCode.On503(errorBody), rootCause = rootCause)
                504 -> NetworkException(HttpCode.On504(errorBody), rootCause = rootCause)
                999 -> NetworkException(HttpCode.NetworkError(errorBody), rootCause = rootCause)
                else -> NetworkException(HttpCode.Unknown(code, errorBody), rootCause = rootCause)
            }
        }
    }
}

inline fun HttpCodeException.is403(action: (ErrorBodyResponse) -> Unit) {
    if (this.code is HttpCode.On403) {
        action(this.code.errorBody)
    }
}

inline fun HttpCodeException.is408(action: (ErrorBodyResponse) -> Unit) {
    if (this.code is HttpCode.On408) {
        action(this.code.errorBody)
    }
}

inline fun HttpCodeException.isUnknown(action: (ErrorBodyResponse) -> Unit) {
    if (this.code is HttpCode.Unknown) {
        action(this.code.errorBody)
    }
}

inline fun HttpCodeException.NetworkError(action: (ErrorBodyResponse?) -> Unit) {
    if (this.code is HttpCode.NetworkError) {
        action(this.code.errorBody)
    }
}
