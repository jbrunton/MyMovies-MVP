package com.jbrunton.mymovies.di

import kotlin.reflect.KClass

class ResolutionFailure(klass: KClass<*>) : RuntimeException("Unable to resolve type ${klass.qualifiedName}")