package com.jbrunton.networking

class DescriptiveError : RuntimeException {
    val isNetworkError: Boolean

    constructor(message: String, isNetworkError: Boolean) : super(message) {
        this.isNetworkError = isNetworkError
    }

    constructor(message: String, cause: Throwable, isNetworkError: Boolean) : super(message, cause) {
        this.isNetworkError = isNetworkError
    }

    companion object {

        fun from(throwable: Throwable): DescriptiveError {
            return throwable as? DescriptiveError ?: DescriptiveError(
                    messageFromThrowable(throwable),
                    throwable,
                    false)
        }

        private fun messageFromThrowable(throwable: Throwable) = throwable.message ?: "Unexpected Error"
    }

    override val message: String
        get() = super.message!!
}