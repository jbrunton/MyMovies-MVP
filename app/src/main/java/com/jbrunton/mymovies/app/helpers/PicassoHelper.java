package com.jbrunton.mymovies.app.helpers;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PicassoHelper {
    public void loadImage(Context context, ImageView target, String url) {
        if (!url.isEmpty()) {
            Picasso.with(context)
                    .load(url)
                    .fit()
                    .into(target);
        }
    }
}
