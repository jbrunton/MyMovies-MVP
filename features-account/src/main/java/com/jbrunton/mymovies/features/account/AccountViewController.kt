package com.jbrunton.mymovies.features.account

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jbrunton.mymovies.libs.kotterknife.bindView
import com.jbrunton.mymovies.libs.ui.PicassoHelper
import com.jbrunton.mymovies.libs.ui.controllers.BaseLoadingViewController

class AccountViewController : BaseLoadingViewController<AccountViewState>() {
    override val layout = R.layout.fragment_account
    override val contentView get() = view.findViewById<View>(R.id.account_details)
    private val picassoHelper = PicassoHelper()

    private val account_username: TextView by bindView(R.id.account_username)
    private val account_name: TextView by bindView(R.id.account_name)
    private val sign_in: View by bindView(R.id.sign_in)
    private val account_links: View by bindView(R.id.account_links)
    private val avatar: ImageView by bindView(R.id.avatar)

    override fun updateContentView(viewState: AccountViewState) {
        account_username.text = viewState.username
        account_name.text = viewState.name
        sign_in.visibility = viewState.signInVisibility
        account_links.visibility = viewState.linksVisibility

        picassoHelper.loadImage(context!!, avatar, viewState.avatarUrl)
    }
}
