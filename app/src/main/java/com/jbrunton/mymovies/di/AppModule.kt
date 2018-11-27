package com.jbrunton.mymovies.di

open class AppModule : Module {
    override fun registerTypes(container: Container) {
        container.register(
                schedulersModule(),
                httpModule(),
                uiModule()
        )
    }

    open fun schedulersModule(): Module = SchedulersModule()
    open fun httpModule(): Module = HttpModule()
    open fun uiModule(): Module = UiModule()
}