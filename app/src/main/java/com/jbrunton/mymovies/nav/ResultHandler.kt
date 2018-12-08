package com.jbrunton.mymovies.nav

import android.content.Intent

internal abstract class ResultHandler(val requestCode: Int) {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (requestCode == this.requestCode) {
            onResult(resultCode, data)
            return true
        }
        return false
    }

    abstract fun onResult(resultCode: Int, data: Intent?)
}
