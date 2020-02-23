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
import org.koin.androidx.viewmodel.dsl.viewModel

val UiModule = org.koin.dsl.module {
    single { Navigator() }

    viewModel { MainViewModel(get()) }

    factory { LoginUseCase(get()) }
    viewModel { LoginViewModel(get()) }
    factory { LoginViewStateFactory(get()) }

    factory { SearchUseCase(get(), get()) }
    viewModel { SearchViewModel(get()) }
    factory { SearchViewStateFactory(get()) }

    factory { MovieDetailsUseCase(get(), get()) }
    viewModel { (movieId: String) -> MovieDetailsViewModel(movieId, get()) }
    factory { MovieDetailsViewStateFactory(get()) }

    factory { DiscoverUseCase(get(), get()) }
    viewModel { DiscoverViewModel(get()) }

    viewModel { AccountViewModel(get()) }
    factory { AccountUseCase(get()) }
    viewModel { FavoritesViewModel(get()) }
    factory { FavoritesUseCase(get()) }
}
