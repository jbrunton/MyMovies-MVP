package com.jbrunton.mymovies.libs.ui

import com.jbrunton.mymovies.entities.SchedulerFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AndroidSchedulerFactory : SchedulerFactory {
    override val IO = Schedulers.io()
    override val Main = AndroidSchedulers.mainThread()
}