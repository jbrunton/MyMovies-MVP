package com.jbrunton.mymovies.ui.moviedetails

import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jbrunton.libs.ui.BaseLoadingViewController
import com.jbrunton.libs.ui.PicassoHelper
import com.jbrunton.mymovies.R

class MovieDetailsViewController : BaseLoadingViewController<MovieDetailsViewState>() {
    override val layout = R.layout.activity_movie_details
    override val contentView: View get() = containerView.findViewById(R.id.movie_details)

    val movie_title: TextView get() = containerView.findViewById(R.id.movie_title)
    val rating: TextView get() = containerView.findViewById(R.id.rating)
    val overview: TextView get() = containerView.findViewById(R.id.overview)
    val release_date: TextView get() = containerView.findViewById(R.id.release_date)
    val favorite: View get() = containerView.findViewById(R.id.favorite)
    val unfavorite: View get() = containerView.findViewById(R.id.unfavorite)
    val poster: ImageView get() = containerView.findViewById(R.id.poster)
    val backdrop: ImageView get() = containerView.findViewById(R.id.backdrop)

    private val picassoHelper = PicassoHelper()

    override fun updateContentView(viewState: MovieDetailsViewState) {
        movie_title.text = viewState.title
        rating.text = Html.fromHtml(viewState.rating)
        overview.text = viewState.overview
        release_date.text = viewState.yearReleased
        favorite.visibility = viewState.favoriteVisibility
        unfavorite.visibility = viewState.unfavoriteVisibility
        picassoHelper.loadSearchResultImage(context, poster, viewState.posterUrl)
        picassoHelper.loadImage(context, backdrop, viewState.backdropUrl)
    }
}