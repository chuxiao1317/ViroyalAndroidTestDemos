package com.viroyal.sloth.nucleus.factory;

import com.viroyal.sloth.nucleus.presenter.Presenter;

public interface PresenterFactory<P extends Presenter> {
    P createPresenter();
}
