package com.jbrunton.mymovies.di

import com.jbrunton.mymovies.ui.account.AccountViewModel
import com.jbrunton.mymovies.ui.auth.LoginViewModel
import com.jbrunton.mymovies.ui.discover.DiscoverViewModel
import com.jbrunton.mymovies.ui.discover.GenreResultsViewModel
import com.jbrunton.mymovies.ui.discover.GenresViewModel
import com.jbrunton.mymovies.ui.moviedetails.MovieDetailsViewModel
import com.jbrunton.mymovies.ui.search.SearchViewModel

class UiModule : Module {
    override fun registerTypes(container: Container) {
        container.apply {
            factory { SearchViewModel(get()) }
            factory { DiscoverViewModel(get()) }
            factory { GenresViewModel(get()) }
            factory { LoginViewModel(get()) }
            factory { AccountViewModel(get()) }
            factory { (movieId: String) -> MovieDetailsViewModel(movieId, get()) }
            factory { (genreId: String) -> GenreResultsViewModel(genreId, get()) }
        }
    }
}