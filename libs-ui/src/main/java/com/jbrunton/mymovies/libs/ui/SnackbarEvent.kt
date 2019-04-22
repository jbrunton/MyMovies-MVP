package com.jbrunton.mymovies.libs.ui

import com.google.android.material.snackbar.Snackbar

data class SnackbarEvent(
        val message: String,
        val actionLabel: String? = null,
        val action: (() -> Unit)? = null,
        val duration: Int = Snackbar.LENGTH_SHORT
)
