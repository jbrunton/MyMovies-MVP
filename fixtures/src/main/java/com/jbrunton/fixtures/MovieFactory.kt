package com.jbrunton.fixtures

import com.google.common.base.Optional
import com.jbrunton.entities.models.Movie
import org.joda.time.LocalDate

class MovieFactory {
    private var count = 0

    class Builder {
        var id: String? = ""
            private set

        var title: String? = ""
            private set

        var overview: Optional<String> = Optional.absent()
            private set

        var releaseDate: Optional<LocalDate> = Optional.absent()
            private set

        var posterUrl: Optional<String> = Optional.absent()
            private set

        var backdropUrl: Optional<String> = Optional.absent()
            private set

        var rating: String? = ""
            private set

        fun id(id: String) = apply { this.id = id }
        fun title(title: String) = apply { this.title = title }
        fun overview(overview: Optional<String>) = apply { this.overview = overview }
        fun releaseDate(releaseDate: Optional<LocalDate>) = apply { this.releaseDate = releaseDate }
        fun posterUrl(posterUrl: Optional<String>) = apply { this.posterUrl = posterUrl }
        fun backdropUrl(backdropUrl: Optional<String>) = apply { this.backdropUrl = backdropUrl }
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
                .overview(Optional.of("Overview for Movie " + id))
                .releaseDate(Optional.absent())
                .posterUrl(Optional.of("https://image.tmdb.org/t/p/w300/bIuOWTtyFPjsFDevqvF3QrD1aun.jpg"))
                .backdropUrl(Optional.of("https://image.tmdb.org/t/p/w300/LvmmDZxkTDqp0DX7mUo621ahdX.jpg"))
                .rating("")
    }

    fun create(): Movie {
        return builder().build()
    }
}
