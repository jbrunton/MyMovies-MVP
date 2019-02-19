package com.jbrunton.entities

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable

class SchedulerContext(val factory: SchedulerFactory) {
    private var compositeDisposable = CompositeDisposable()

    fun <T> applySchedulers() = ObservableTransformer<T, T> {
        it.subscribeOn(factory.IO).observeOn(factory.Main)
    }

    fun <T> subscribe(source: Observable<T>, onNext: (T) -> Unit) {
        val disposable = source.compose(applySchedulers()).subscribe(onNext)
        compositeDisposable.add(disposable)
    }

    fun <T> subscribe(source: Observable<T>) {
        subscribe(source, {})
    }

    fun dispose() {
        compositeDisposable.clear()
    }
}

interface HasSchedulers {
    val schedulerContext: SchedulerContext
}

fun <T> HasSchedulers.subscribe(source: Observable<T>, onNext: (T) -> Unit) =
        schedulerContext.subscribe(source, onNext)

fun <T> HasSchedulers.subscribe(source: Observable<T>) =
        schedulerContext.subscribe(source, {})

fun HasSchedulers.applySchedulers(block: () -> (SchedulerContext) -> Unit) =
        block().invoke(schedulerContext)

//fun <T> HasSchedulers.withSchedulers(block: SchedulerContext.() -> T): T =
//        block.invoke(withSchedulers)

fun <T> withSchedulers(block: SchedulerContext.() -> T): (SchedulerContext) -> T = block
