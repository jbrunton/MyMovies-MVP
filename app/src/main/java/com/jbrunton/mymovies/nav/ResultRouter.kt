package com.jbrunton.mymovies.nav

import android.content.Intent
import java.util.*

class ResultRouter {
    private val resultHandlers = LinkedList<ResultHandler>()

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val it = resultHandlers.iterator()
        while (it.hasNext()) {
            val handler = it.next()
            if (handler.onActivityResult(requestCode, resultCode, data)) {
                it.remove()
            }
        }
    }

    fun route(requestCode: Int, listener: (resultCode: Int, data: Intent?) -> Unit) {
        resultHandlers.add(object : ResultHandler(requestCode) {
            override fun onResult(resultCode: Int, data: Intent?) {
                listener(resultCode, data)
            }
        })
    }
}