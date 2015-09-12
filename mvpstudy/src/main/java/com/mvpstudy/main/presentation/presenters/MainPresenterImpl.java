package com.mvpstudy.main.presentation.presenters;

import android.os.Handler;

import com.mvpstudy.main.presentation.views.MainView;
import com.mvpstudy.main.view.OnFinishedListener;

import java.util.Arrays;
import java.util.List;

/**
 * MainPresenterImpl
 *
 * @author Jian Yang
 * @date 9/12/2015
 */
public class MainPresenterImpl implements MainPresenter, OnFinishedListener {
    private MainView mainView;

    public MainPresenterImpl(MainView mainView) {
        this.mainView = mainView;
    }

    private List<String> createArrayList() {
        return Arrays.asList(
                "Item 1",
                "Item 2",
                "Item 3",
                "Item 4",
                "Item 5",
                "Item 6",
                "Item 7",
                "Item 8",
                "Item 9",
                "Item 10"
        );
    }

    @Override
    public void onResume() {
        mainView.showProgress();
        findItems(this);
    }

    @Override
    public void onItemClicked(int position) {
        mainView.showMessage(String.format("Position %d clicked", position + 1));
    }

    @Override
    public void findItems(final OnFinishedListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                listener.onFinished(createArrayList());
            }
        }, 2000);
    }

    @Override
    public void onFinished(List<String> items) {
        mainView.setItems(items);
        mainView.hideProgress();
    }
}
