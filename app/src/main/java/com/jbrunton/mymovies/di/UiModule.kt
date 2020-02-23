package com.jbrunton.mymovies.di

import com.jbrunton.mymovies.features.account.AccountViewModel
import com.jbrunton.mymovies.features.account.favorites.FavoritesViewModel
import com.jbrunton.mymovies.features.search.SearchViewModel
import com.jbrunton.mymovies.features.search.SearchViewStateFactory
import com.jbrunton.mymovies.libs.ui.nav.Navigator
import com.jbrunton.mymovies.ui.auth.LoginViewModel
import com.jbrunton.mymovies.ui.auth.LoginViewStateFactory
import com.jbrunton.mymovies.features.discover.DiscoverViewModel
import com.jbrunton.mymovies.ui.main.MainViewModel
import com.jbrunton.mymovies.ui.moviedetails.MovieDetailsViewModel
import com.jbrunton.mymovies.ui.moviedetails.MovieDetailsViewStateFactory
import com.jbrunton.mymovies.usecases.account.AccountUseCase
import com.jbrunton.mymovies.usecases.auth.LoginUseCase
import com.jbrunton.mymovies.usecases.discover.DiscoverUseCase
import com.jbrunton.mymovies.usecases.favorites.FavoritesUseCase
import com.jbrunton.mymovies.usecases.moviedetails.MovieDetailsUseCase
import com.jbrunton.mymovies.usecases.search.SearchUseCase

val KoinUiModule = org.koin.dsl.module {
    single { Navigator() }

    factory { MainViewModel() }

    factory { LoginUseCase(get()) }
    factory { LoginViewModel() }
    factory { LoginViewStateFactory(get()) }

    factory { SearchUseCase(get(), get()) }
    factory { SearchViewModel() }
    factory { SearchViewStateFactory(get()) }

    factory { MovieDetailsUseCase(get(), get()) }
    factory { (movieId: String) -> MovieDetailsViewModel(movieId) }
    factory { MovieDetailsViewStateFactory(get()) }

    factory { DiscoverUseCase(get(), get()) }
    factory { DiscoverViewModel() }

    factory { AccountViewModel() }
    factory { AccountUseCase(get()) }
    factory { FavoritesViewModel() }
    factory { FavoritesUseCase(get()) }
}
