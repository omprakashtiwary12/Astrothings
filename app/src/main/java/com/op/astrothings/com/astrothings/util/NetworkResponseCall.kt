package com.op.astrothings.com.astrothings.util

import android.os.Bundle
import android.text.TextUtils
import com.op.astrothings.com.astrothings.data.model.errorResponseModel.ErrorResponseModel
import com.op.astrothings.com.astrothings.network.NetworkResponse
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import okhttp3.Request
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import timber.log.Timber

internal class NetworkResponseCall<S : Any, E : Any>(
    private val delegate: Call<S>,
    private val errorConverter: Converter<ResponseBody, E>,
) : Call<NetworkResponse<S, E>> {

    override fun enqueue(callback: Callback<NetworkResponse<S, E>>) {
        return delegate.enqueue(object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                val empty = "".toResponseBody(null)
                val body = response.body()
                val code = response.code()
                val error = response.errorBody()
                var apiName: String? = null

                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(NetworkResponse.Success(body))
                        )
                    } else {
                        // Response is successful but the body is null
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(NetworkResponse.Success(empty as S))
                        )
                    }
                } else {
                    val errorBody = when {
                        error == null -> null
                        error.contentLength() == 0L -> null
                        else -> try {
                            errorConverter.convert(error)
                        } catch (e: Exception) {
                            Timber.e(e)
                            null
                        }
                    }
                    if (code != 401) {
                        logApiEvent(code, apiName, getMessage(errorBody))
                    } else if (code == 401 && apiName!!.contains("ACS_TKN")) {
                        logApiEvent(code, apiName, getMessage(errorBody))
                    }

                    Timber.d(getMessage(errorBody))

                    if (errorBody != null) {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(NetworkResponse.ApiError(errorBody, code))
                        )
                    } else {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(NetworkResponse.UnknownError(null))
                        )
                    }
                }
            }

            override fun onFailure(call: Call<S>, throwable: Throwable) {
                Timber.d(throwable.toString())
                val networkResponse = when (throwable) {
                    is UnknownHostException -> NetworkResponse.NetworkError(IOException("Socket timeout!"))
                    is SocketTimeoutException -> NetworkResponse.NetworkError(IOException("Socket timeout!"))
                    is SocketException -> NetworkResponse.NetworkError(IOException("Socket exception!"))
                    is IOException -> NetworkResponse.NetworkError(throwable)
                    else -> NetworkResponse.UnknownError(throwable)
                }
                callback.onResponse(this@NetworkResponseCall, Response.success(networkResponse))
            }
        })
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = NetworkResponseCall(delegate.clone(), errorConverter)

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<NetworkResponse<S, E>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
    }

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()

    private fun getMessage(errorBody: E?): String {
        val message = "Unknown error!"
        try {
            if (errorBody is ErrorResponseModel) {
                if (!TextUtils.isEmpty(errorBody.message)) {
                    return errorBody.message
                }
                if (!TextUtils.isEmpty(errorBody.error)) {
                    return errorBody.error
                }
            }

            if (errorBody is String) {
                return errorBody
            }
        } catch (e: Exception) {
            Timber.e(e)
        }

        return message
    }

    private fun logApiEvent(code: Int, apiName: String?, msg: String) {
        if (!TextUtils.isEmpty(apiName)) {
            val params = Bundle()
            val details = "$apiName - $code - $msg"
        }
    }
}