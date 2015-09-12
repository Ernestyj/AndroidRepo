package com.mvpstudy.login.presentation.presenters;

import com.mvpstudy.login.interactor.LoginInteractor;
import com.mvpstudy.login.interactor.LoginInteractorImpl;
import com.mvpstudy.login.presentation.views.LoginView;
import com.mvpstudy.login.view.OnLoginFinishedListener;

/**
 * LoginPresenterImpl
 *
 * @author Jian Yang
 * @date 9/12/2015
 */
public class LoginPresenterImpl implements LoginPresenter, OnLoginFinishedListener {
    private LoginView loginView;
    private LoginInteractor loginInteractor;

    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        this.loginInteractor = new LoginInteractorImpl();
    }
    @Override
    public void validateCredentials(String username, String password) {
        loginView.showProgress();
        loginInteractor.login(username, password, this);
    }

    @Override
    public void onUsernameError() {
        loginView.setUsernameError();
        loginView.hideProgress();
    }

    @Override
    public void onPasswordError() {
        loginView.setPasswordError();
        loginView.hideProgress();
    }

    @Override
    public void onSuccess() {
        loginView.navigateToHome();
    }
}
