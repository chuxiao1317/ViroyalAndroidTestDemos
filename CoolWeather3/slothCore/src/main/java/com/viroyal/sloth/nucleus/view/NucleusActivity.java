package com.viroyal.sloth.nucleus.view;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.viroyal.sloth.app.SlothActivity;
import com.viroyal.sloth.nucleus.factory.PresenterFactory;
import com.viroyal.sloth.nucleus.factory.ReflectionPresenterFactory;
import com.viroyal.sloth.nucleus.presenter.Presenter;

/**
 * This class is an example of how an activity could controls it's presenter.
 * You can inherit from this class or copy/paste this class's code to
 * create your own view implementation.
 *
 * @param <P> a type of presenter to return with {@link #getPresenter}.
 */
public abstract class NucleusActivity<P extends Presenter> extends SlothActivity implements ViewWithPresenter<P> {

    private static final String PRESENTER_STATE_KEY = "presenter_state";

    private PresenterLifecycleDelegate<P> presenterDelegate =
        new PresenterLifecycleDelegate<>(ReflectionPresenterFactory.<P>fromViewClass(getClass()));
    /**
     * 因为有些页面(比如SplashActivity)在启动另一个页面的时候处于pause状态.此时不应该
     * 释放view资源,要做关闭操作,所以此处加个判断
     */
    protected boolean isNeedFinished;//是否要关闭
    /**
     * Returns a current presenter factory.
     */
    public PresenterFactory<P> getPresenterFactory() {
        return presenterDelegate.getPresenterFactory();
    }

    /**
     * Sets a presenter factory.
     * Call this method before onCreate/onFinishInflate to override default {@link ReflectionPresenterFactory} presenter factory.
     * Use this method for presenter dependency injection.
     */
    @Override
    public void setPresenterFactory(PresenterFactory<P> presenterFactory) {
        presenterDelegate.setPresenterFactory(presenterFactory);
    }

    /**
     * Returns a current attached presenter.
     * This method is guaranteed to return a non-null value between
     * onResume/onPause and onAttachedToWindow/onDetachedFromWindow calls
     * if the presenter factory returns a non-null value.
     *
     * @return a currently attached presenter or null.
     */
    public P getPresenter() {
        return presenterDelegate.getPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            presenterDelegate.onRestoreInstanceState(savedInstanceState.getBundle(PRESENTER_STATE_KEY));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(PRESENTER_STATE_KEY, presenterDelegate.onSaveInstanceState());
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenterDelegate.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenterDelegate.onDropView(isNeedFinished);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenterDelegate.onDestroy(!isChangingConfigurations());
    }
}
