package com.daggerstudy.androidcoffee.ui;

import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.daggerstudy.androidcoffee.DemoActivity;
import com.daggerstudy.androidcoffee.DemoApplication;

import javax.inject.Inject;

/**
 * HomeActivity
 *
 * @author Jian Yang
 * @date 9/13/2015
 */
public class HomeActivity extends DemoActivity {
    @Inject
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((DemoApplication) getApplication()).component().inject(this);

        // TODO do something with the injected dependencies here!
        Log.i("HomeActivity", locationManager.toString());
    }
}
