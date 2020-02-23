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
import org.kodein.di.Kodein
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.*

val UiModule = Module("UiModule") {
    bind() from singleton { Navigator() }

    bind() from provider { MainViewModel(instance()) }

    bind() from provider { LoginUseCase(instance()) }
    bind() from provider { LoginViewModel(instance()) }
    bind() from provider { LoginViewStateFactory(instance()) }

    bind() from provider { SearchUseCase(instance(), instance()) }
    bind() from provider { SearchViewModel(instance()) }
    bind() from provider { SearchViewStateFactory(instance()) }

    bind() from provider { MovieDetailsUseCase(instance(), instance()) }
    bind() from factory { movieId: String -> MovieDetailsViewModel(movieId, instance()) }
    bind() from provider { MovieDetailsViewStateFactory(instance()) }

    bind() from provider { DiscoverUseCase(instance(), instance()) }
    bind() from provider { DiscoverViewModel(instance()) }

    bind() from provider { AccountViewModel(instance()) }
    bind() from provider { AccountUseCase(instance()) }
    bind() from provider { FavoritesViewModel(instance()) }
    bind() from provider { FavoritesUseCase(instance()) }
}
