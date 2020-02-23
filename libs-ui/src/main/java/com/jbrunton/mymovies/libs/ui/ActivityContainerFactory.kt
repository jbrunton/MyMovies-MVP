package com.jbrunton.mymovies.libs.ui

import androidx.appcompat.app.AppCompatActivity
import com.jbrunton.inject.Container
import org.koin.core.module.Module

interface ActivityContainerFactory {
    fun createActivityModule(activity: AppCompatActivity): Module
}
