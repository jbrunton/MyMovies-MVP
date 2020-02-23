package com.jbrunton.mymovies.libs.ui

import androidx.appcompat.app.AppCompatActivity
import org.koin.core.module.Module

interface ActivityModuleFactory {
    fun createActivityModule(activity: AppCompatActivity): Module
}
