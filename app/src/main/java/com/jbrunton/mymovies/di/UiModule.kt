package com.jbrunton.mymovies.di

import com.jbrunton.inject.module
import com.jbrunton.mymovies.nav.ResultRouter
import com.jbrunton.mymovies.ui.account.AccountViewModel
import com.jbrunton.mymovies.ui.account.favorites.FavoritesViewModel
import com.jbrunton.mymovies.ui.auth.LoginViewModel
import com.jbrunton.mymovies.ui.discover.DiscoverViewModel
import com.jbrunton.mymovies.ui.discover.GenreResultsViewModel
import com.jbrunton.mymovies.ui.discover.GenresViewModel
import com.jbrunton.mymovies.ui.moviedetails.MovieDetailsViewModel
import com.jbrunton.mymovies.usecases.search.SearchUseCase
import com.jbrunton.mymovies.ui.search.SearchViewModel

val UiModule = module {
    single { ResultRouter() }
    factory { SearchUseCase(get()) }
    factory { SearchViewModel(get()) }
    factory { DiscoverViewModel(get(), get()) }
    factory { GenresViewModel(get()) }
    factory { LoginViewModel(get()) }
    factory { AccountViewModel(get()) }
    factory { FavoritesViewModel(get()) }
    factory { (movieId: String) -> MovieDetailsViewModel(movieId, get(), get()) }
    factory { (genreId: String) -> GenreResultsViewModel(genreId, get()) }
}
