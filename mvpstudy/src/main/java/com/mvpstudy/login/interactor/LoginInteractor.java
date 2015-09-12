package com.mvpstudy.login.interactor;

import com.mvpstudy.login.view.OnLoginFinishedListener;

/**
 * LoginInteractor
 *
 * @author Jian Yang
 * @date 9/12/2015
 */
public interface LoginInteractor {
    public void login(String username, String password, OnLoginFinishedListener listener);
}
