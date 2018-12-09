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
        val divider: View?

        init {
            movieCardView = itemView.findViewById(R.id.movie_card_view)
            titleView = itemView.findViewById(R.id.title)
            releaseDateView = itemView.findViewById(R.id.release_date)
            ratingView = itemView.findViewById(R.id.rating)
            poster = itemView.findViewById(R.id.poster)
            divider = itemView.findViewById<View?>(R.id.divider)
        }
    }

    class ViewHolderFactory(private val context: Context) : BaseRecyclerAdapter.ViewHolderFactory<MovieSearchResultViewState, ViewHolder> {

        private val picassoHelper = PicassoHelper()

        override fun createViewHolder(view: View): ViewHolder {
            return ViewHolder(view)
        }

        override fun bindHolder(holder: ViewHolder, item: MovieSearchResultViewState, items: List<MovieSearchResultViewState>, position: Int) {
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
            if (holder.divider != null) {
                holder.divider.visibility = if (position < items.size - 1) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }
}
