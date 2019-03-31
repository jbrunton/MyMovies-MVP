package com.jbrunton.mymovies.ui.account

import com.jbrunton.entities.subscribe
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.nav.Navigator
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.usecases.account.AccountUseCase

class AccountViewModel(container: Container) : BaseLoadingViewModel<AccountViewState>(container) {
    val useCase: AccountUseCase by inject()

    override fun start() {
        subscribe(useCase.state) {
            viewState.postValue(AccountViewStateFactory.viewState(it))
        }
        useCase.start(schedulerContext)
    }

    override fun retry() {
        useCase.retry()
    }

    fun signOut() {
        useCase.signOut()
    }

    fun signIn(navigator: Navigator) {
        useCase.signIn(navigator::login)
    }
}