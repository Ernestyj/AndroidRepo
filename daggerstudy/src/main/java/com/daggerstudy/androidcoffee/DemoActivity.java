package com.daggerstudy.androidcoffee;

import android.app.Activity;
import android.os.Bundle;

/**
 * DemoActivity
 *
 * @author Jian Yang
 * @date 9/13/2015
 */
public abstract class DemoActivity extends Activity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Perform injection so that when this call returns all dependencies will be available for use.
        ((DemoApplication) getApplication()).component().inject(this);
    }
}