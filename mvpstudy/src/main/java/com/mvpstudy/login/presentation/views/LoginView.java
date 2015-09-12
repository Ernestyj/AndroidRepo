package com.mvpstudy.login.presentation.views;

/**
 * LoginView
 *
 * @author Jian Yang
 * @date 9/12/2015
 */
public interface LoginView {
    public void showProgress();

    public void hideProgress();

    public void setUsernameError();

    public void setPasswordError();

    public void navigateToHome();
}
