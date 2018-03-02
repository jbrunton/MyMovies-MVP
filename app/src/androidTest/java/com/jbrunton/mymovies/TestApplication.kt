package com.jbrunton.mymovies

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.jbrunton.entities.GenresRepository
import com.jbrunton.entities.MoviesRepository
import com.jbrunton.mymovies.search.SearchViewModel
import com.nhaarman.mockito_kotlin.mock

class TestApplication : MyMoviesApplication() {
    override fun createDependencyGraph(): ApplicationDependencies {
        return object : ApplicationDependencies {
            override val moviesRepository: MoviesRepository = mock<MoviesRepository>()
            override val genresRepository: GenresRepository = mock<GenresRepository>()

            override val searchViewModelFactory: ViewModelProvider.Factory = object : ViewModelProvider.NewInstanceFactory() {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return object : SearchViewModel(moviesRepository) {
                        override fun performSearch(query: String) {
                        }
                    } as T
                }
            }
        }
    }
}
