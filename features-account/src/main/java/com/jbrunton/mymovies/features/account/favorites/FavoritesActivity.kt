package com.jbrunton.mymovies.features.account.favorites

import androidx.appcompat.widget.Toolbar
import com.jbrunton.inject.injectViewModel
import com.jbrunton.mymovies.features.account.R
import com.jbrunton.mymovies.libs.ui.controllers.rootView
import com.jbrunton.mymovies.libs.ui.livedata.observe
import com.jbrunton.mymovies.libs.ui.views.BaseActivity

class FavoritesActivity : BaseActivity<FavoritesViewModel>() {
    override val viewModel: FavoritesViewModel by injectViewModel()
    val toolbar: Toolbar get() = findViewById(R.id.toolbar)
    private val layoutController by lazy { FavoritesViewController(viewModel) }

    override fun onCreateLayout() {
        setContentView(R.layout.activity_favorites)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = "Favorites"

        layoutController.onViewCreated(rootView)
    }

    override fun onBindListeners() {
        layoutController.error_try_again.setOnClickListener { viewModel.retry() }
    }

    override fun onObserveData() {
        super.onObserveData()
        viewModel.viewState.observe(this, layoutController::updateView)
    }
}
