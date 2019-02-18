package com.jbrunton.fixtures

import com.jbrunton.async.AsyncResult
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

object NetworkErrorFixtures {
    fun <T> networkErrorResult() = AsyncResult.failure<T>(IOException())

    fun httpErrorResultBody(message: String) =
            ResponseBody.create(MediaType.parse("application/json"), "{ \"status_message\": \"Some Error\" }")

    fun <T> httpErrorResult(code: Int, message: String) =
            AsyncResult.failure<T>(HttpException(Response.error<Unit>(401, httpErrorResultBody(message))))
}