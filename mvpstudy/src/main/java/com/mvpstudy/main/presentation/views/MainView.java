package com.mvpstudy.main.presentation.views;

import java.util.List;

/**
 * MainView
 *
 * @author Jian Yang
 * @date 9/12/2015
 */
public interface MainView {

    public void showProgress();

    public void hideProgress();

    public void setItems(List<String> items);

    public void showMessage(String message);
}
