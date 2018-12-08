package com.jbrunton.mymovies.ui.shared

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DebounceTextWatcher(
        override val coroutineContext: CoroutineContext,
        val action: (String) -> Unit
) : TextWatcher, CoroutineScope {
    private var previousValue = ""

    companion object {
        val Delay: Long = 300
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val newValue = s.toString().trim()
        if (newValue == previousValue)
            return

        previousValue = newValue

        launch {
            delay(Delay)
            if (newValue != previousValue)
                return@launch

            action(newValue)
        }
    }

    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}

fun EditText.onTextChanged(context: CoroutineContext, action: (String) -> Unit): Unit =
        addTextChangedListener(DebounceTextWatcher(context, action))
