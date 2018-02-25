package com.jbrunton.mymovies.helpers

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

fun TextView.afterTextChange(listener: (s: Editable) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            listener.invoke(s)
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
