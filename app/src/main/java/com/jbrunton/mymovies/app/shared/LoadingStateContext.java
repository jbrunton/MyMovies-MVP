package com.jbrunton.mymovies.app.shared;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbrunton.mymovies.R;

import butterknife.BindView;

import static com.jbrunton.mymovies.app.converters.VisibilityConverter.toVisibility;

public class LoadingStateContext {
    @BindView(R.id.loading_indicator) View loadingIndicator;
    @BindView(R.id.error_case) View errorCase;
    @BindView(R.id.error_text) TextView errorText;
    @BindView(R.id.error_image) ImageView errorImage;
    @BindView(R.id.error_try_again) Button errorTryAgainButton;

    public void updateView(LoadingViewState viewState) {
        loadingIndicator.setVisibility(toVisibility(viewState.showLoadingIndicator()));
        errorCase.setVisibility(toVisibility(viewState.showError()));
        errorText.setText(viewState.errorMessage());
        errorImage.setImageResource(viewState.errorIcon());
        errorTryAgainButton.setVisibility(toVisibility(viewState.showTryAgainButton()));
    }
}
