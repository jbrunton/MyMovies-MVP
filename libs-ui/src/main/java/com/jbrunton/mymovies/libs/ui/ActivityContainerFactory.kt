package com.jbrunton.mymovies.libs.ui

import androidx.appcompat.app.AppCompatActivity
import com.jbrunton.inject.Container

interface ActivityContainerFactory {
    fun createActivityContainer(activity: AppCompatActivity): Container
}