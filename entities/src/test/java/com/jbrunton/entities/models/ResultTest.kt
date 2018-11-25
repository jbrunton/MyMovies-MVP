package com.jbrunton.entities.models

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.lang.NullPointerException
import javax.xml.ws.http.HTTPException

class ResultTest {
    val value = 123
    val cachedValue = 456
    val error = Throwable("error")

    @Test
    fun returnsTheValue() {
        assertThat(success(value).get()).isEqualTo(value)
    }

    @Test
    fun returnsTheCachedValueIfLoading() {
        assertThat(loading(value).get()).isEqualTo(value)
    }

    @Test(expected = NullPointerException::class)
    fun throwsIfLoadingAndNoCachedValue() {
        loading(null).get()
    }

    @Test
    fun returnsTheCachedValueIfFailure() {
        assertThat(failure(IllegalStateException(), value).get()).isEqualTo(value)
    }

    @Test(expected = IllegalStateException::class)
    fun throwsIfFailureAndNoCachedValue() {
        failure(IllegalStateException(), null).get()
    }

    @Test
    fun convertsSuccessToValueOrNull() {
        assertThat(success(value).getOrNull()).isEqualTo(value)
    }

    @Test
    fun convertsLoadingToValueOrNull() {
        assertThat(loading(cachedValue).getOrNull()).isEqualTo(cachedValue)
        assertThat(loading(null).getOrNull()).isNull()
    }

    @Test
    fun convertsFailureToValueOrNull() {
        assertThat(failure(error, cachedValue).getOrNull()).isEqualTo(cachedValue)
        assertThat(failure(error,null).getOrNull()).isNull()
    }

    @Test
    fun transformsSuccessfulResults() {
        val result = success(2).map { it * 2 }
        assertThat(result.get()).isEqualTo(4)
    }

    @Test
    fun transformsLoadingResults() {
        val result = loading(2).map { it * 2 }
        assertThat(result.get()).isEqualTo(4)
    }

    @Test
    fun transformsFailureResults() {
        val result = failure(error, 2).map { it * 2 }
        assertThat(result.get()).isEqualTo(4)
    }

    @Test
    fun transformsOnlySuccessResults() {
        val result = success(1).onSuccess {
            Result.Success(it.value * 2)
        }
        assertThat(result).isEqualTo(Result.Success(2))
    }

    @Test
    fun doesNotTransformOtherResultsWhenExpectingSuccess() {
        loading(null).onSuccess { throw IllegalStateException() }
        failure(error,null).onSuccess { throw IllegalStateException() }
    }

    @Test
    fun transformsOnlyLoadingResults() {
        val result = loading(1).onLoading {
            Result.Success(it.get() * 2)
        }
        assertThat(result).isEqualTo(Result.Success(2))
    }

    @Test
    fun doesNotTransformOtherResultsWhenExpectingLoading() {
        success(value).onLoading { throw IllegalStateException() }
        failure(error,null).onLoading { throw IllegalStateException() }
    }

    @Test
    fun transformsOnlyFailureResults() {
        val result = failure(error,1).onFailure {
            Result.Success(it.get() * 2)
        }
        assertThat(result).isEqualTo(Result.Success(2))
    }

    @Test
    fun doesNotTransformOtherResultsWhenExpectingFailure() {
        success(value).onFailure { throw IllegalStateException() }
        loading(value).onFailure { throw IllegalStateException() }
    }

    @Test
    fun transformsErrors() {
        val result = failure(HTTPException(401), 1).onError(HTTPException::class) {
            map { Result.Success(it.get() * 2) }
        }
        assertThat(result).isEqualTo(Result.Success(2))
    }

    @Test
    fun transformsSpecificErrors() {
        val result = failure(HTTPException(401), 1)

        val transformedResult = result.onError(HTTPException::class) {
            map { Result.Success(it.get() * 2) } whenever { it.statusCode == 401 }
        }
        val otherResult = result.onError(HTTPException::class) {
            map { Result.Success(it.get() * 2) } whenever { it.statusCode == 400 }
        }

        assertThat(transformedResult).isEqualTo(Result.Success(2))
        assertThat(otherResult).isEqualTo(result)
    }

    @Test
    fun zipsSuccesses() {
        val result = success(2).zipWith(success(3)) { x, y -> x * y }
        assertThat(result).isEqualTo(Result.Success(6))
    }

    @Test
    fun zipsLeftFailures() {
        val result = failure(error, 2).zipWith(success(3)) { x, y -> x * y }
        assertThat(result).isEqualTo(Result.Failure(error, 6))
    }

    @Test
    fun zipsRightFailures() {
        val result = success(3).zipWith(failure(error, 2)) { x, y -> x * y }
        assertThat(result).isEqualTo(Result.Failure(error, 6))
    }

    @Test
    fun zipsLeftLoadingResults() {
        val result = loading(2).zipWith(success(3)) { x, y -> x * y }
        assertThat(result).isEqualTo(Result.Loading(6))
    }

    @Test
    fun zipsRightLoadingResults() {
        val result = success(2).zipWith(loading(3)) { x, y -> x * y }
        assertThat(result).isEqualTo(Result.Loading(6))
    }

    private fun success(value: Int): Result<Int> {
        return Result.Success(value)
    }

    private fun loading(cachedValue: Int?): Result<Int> {
        return Result.Loading(cachedValue)
    }

    private fun failure(error: Throwable, cachedValue: Int?): Result<Int> {
        return Result.Failure(error, cachedValue)
    }
}