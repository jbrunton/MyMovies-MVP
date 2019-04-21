package com.jbrunton.libs.ui

import com.jbrunton.entities.SchedulerFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AndroidSchedulerFactory : SchedulerFactory {
    override val IO = Schedulers.io()
    override val Main = AndroidSchedulers.mainThread()
}