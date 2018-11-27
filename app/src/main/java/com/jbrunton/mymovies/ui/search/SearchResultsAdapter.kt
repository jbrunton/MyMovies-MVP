package com.jbrunton.mymovies.ui.search

import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.PicassoHelper
import com.jbrunton.mymovies.ui.moviedetails.MovieDetailsActivity
import com.jbrunton.mymovies.ui.movies.MovieSearchResultViewState
import com.jbrunton.mymovies.ui.shared.BaseRecyclerAdapter

class SearchResultsAdapter(
        context: Context,
        layoutId: Int
) : BaseRecyclerAdapter<MovieSearchResultViewState, SearchResultsAdapter.ViewHolder>(
        layoutId,
        ViewHolderFactory(context)
) {

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val titleView: TextView
        val releaseDateView: TextView?
        val poster: ImageView
        val ratingView: TextView?
        val movieCardView: View

        init {
            movieCardView = itemView.findViewById(R.id.movie_card_view)
            titleView = itemView.findViewById<View>(R.id.title) as TextView
            releaseDateView = itemView.findViewById<View>(R.id.release_date) as TextView?
            ratingView = itemView.findViewById<View>(R.id.rating) as TextView?
            poster = itemView.findViewById<View>(R.id.poster) as ImageView
        }
    }

    class ViewHolderFactory(private val context: Context) : BaseRecyclerAdapter.ViewHolderFactory<MovieSearchResultViewState, ViewHolder> {

        private val picassoHelper = PicassoHelper()

        override fun createViewHolder(view: View): ViewHolder {
            return ViewHolder(view)
        }

        override fun bindHolder(holder: ViewHolder, item: MovieSearchResultViewState) {
            holder.titleView.text = item.title
            if (holder.releaseDateView != null) {
                holder.releaseDateView.text = item.yearReleased
            }
            if (holder.ratingView != null) {
                holder.ratingView.text = Html.fromHtml(item.rating)
            }
            picassoHelper.loadSearchResultImage(context, holder.poster, item.posterUrl)
            holder.movieCardView.setOnClickListener {
                val intent = Intent(context, MovieDetailsActivity::class.java)
                intent.putExtra("MOVIE_ID", item.movieId)
                context.startActivity(intent)
            }
        }
    }
}