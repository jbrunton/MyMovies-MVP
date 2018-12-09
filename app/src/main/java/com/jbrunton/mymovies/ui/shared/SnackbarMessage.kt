package com.jbrunton.mymovies.ui.shared

data class SnackbarMessage(
        val message: String,
        val actionLabel: String? = null,
        val action: (() -> Unit)? = null
)