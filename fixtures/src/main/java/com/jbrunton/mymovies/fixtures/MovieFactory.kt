package com.jbrunton.mymovies.fixtures

import com.jbrunton.mymovies.entities.models.Movie
import org.joda.time.LocalDate

class MovieFactory {
    private var count = 0

    class Builder {
        var id: String? = ""
            private set

        var title: String? = ""
            private set

        var overview: String? = null
            private set

        var releaseDate: LocalDate? = null
            private set

        var posterUrl: String? = null
            private set

        var backdropUrl: String? = null
            private set

        var rating: String? = ""
            private set

        fun id(id: String) = apply { this.id = id }
        fun title(title: String) = apply { this.title = title }
        fun overview(overview: String?) = apply { this.overview = overview }
        fun releaseDate(releaseDate: LocalDate?) = apply { this.releaseDate = releaseDate }
        fun posterUrl(posterUrl: String?) = apply { this.posterUrl = posterUrl }
        fun backdropUrl(backdropUrl: String?) = apply { this.backdropUrl = backdropUrl }
        fun rating(rating: String) = apply { this.rating = rating }

        fun build() = Movie(
                id = id ?: "",
                title = title ?: "",
                overview = overview,
                releaseDate = releaseDate,
                posterUrl = posterUrl,
                backdropUrl = backdropUrl,
                rating = rating ?: ""
        )
    }

    fun builder(): Builder {
        val id = Integer.toString(++count)
        return Builder()
                .id(id)
                .title("Movie " + id)
                .overview("Overview for Movie " + id)
                .posterUrl("https://image.tmdb.org/t/p/w300/bIuOWTtyFPjsFDevqvF3QrD1aun.jpg")
                .backdropUrl("https://image.tmdb.org/t/p/w300/LvmmDZxkTDqp0DX7mUo621ahdX.jpg")
                .rating("")
    }

    fun create(): Movie {
        return builder().build()
    }
}
