package com.mvpstudy.main.presentation.presenters;

import com.mvpstudy.main.view.OnFinishedListener;

/**
 * MainPresenter
 *
 * @author Jian Yang
 * @date 9/12/2015
 */
public interface MainPresenter {

    public void onResume();

    public void onItemClicked(int position);

    public void findItems(OnFinishedListener listener);
}
