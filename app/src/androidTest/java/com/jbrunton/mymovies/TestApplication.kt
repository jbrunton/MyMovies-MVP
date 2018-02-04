package com.jbrunton.mymovies

import com.jbrunton.entities.GenresRepository
import com.jbrunton.entities.MoviesRepository

import org.mockito.Mockito.mock

class TestApplication : MyMoviesApplication() {
    override fun createDependencyGraph(): ApplicationDependencies {
        return object : ApplicationDependencies {
            override val moviesRepository: MoviesRepository = mock(MoviesRepository::class.java)
            override val genresRepository: GenresRepository = mock(GenresRepository::class.java)
        }
    }
}
