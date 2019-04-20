package com.jbrunton.mymovies.ui.account

import com.jbrunton.libs.ui.BaseLoadingViewController
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.PicassoHelper
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.layout_account_details.*

class AccountViewController : BaseLoadingViewController<AccountViewState>(), LayoutContainer {
    override val layout = R.layout.fragment_account
    override val contentView get() = account_details
    private val picassoHelper = PicassoHelper()

    override fun updateContentView(viewState: AccountViewState) {
        account_username.text = viewState.username
        account_name.text = viewState.name
        sign_in.visibility = viewState.signInVisibility
        account_links.visibility = viewState.linksVisibility

        picassoHelper.loadImage(context!!, avatar, viewState.avatarUrl)
    }
}