package com.jbrunton.mymovies.ui.moviedetails

import android.text.Html
import android.view.View
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.PicassoHelper
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewController
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.content_movie_details.*
import kotlinx.android.synthetic.main.item_movie_card_list.*

class MovieDetailsViewController : BaseLoadingViewController<MovieDetailsViewState>() {
    override val layout = R.layout.activity_movie_details
    override val contentView: View get() = movie_details
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