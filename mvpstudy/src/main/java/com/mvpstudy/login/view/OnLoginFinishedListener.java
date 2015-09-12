package com.mvpstudy.login.view;

/**
 * OnLoginFinishedListener
 *
 * @author Jian Yang
 * @date 9/12/2015
 */
public interface OnLoginFinishedListener {
    public void onUsernameError();

    public void onPasswordError();

    public void onSuccess();
}
