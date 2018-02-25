package com.jbrunton.mymovies.helpers

import android.content.Context
import android.widget.ImageView

import com.squareup.picasso.Picasso

class PicassoHelper {
    fun loadImage(context: Context, target: ImageView, url: String) {
        if (!url.isEmpty()) {
            Picasso.with(context)
                    .load(url)
                    .fit()
                    .into(target)
        }
    }
}
