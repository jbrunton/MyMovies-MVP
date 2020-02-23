package com.jbrunton.mymovies.libs.ui

import androidx.appcompat.app.AppCompatActivity
import org.kodein.di.Kodein

interface ActivityContainerFactory {
    fun createActivityContainer(activity: AppCompatActivity): Kodein
}
