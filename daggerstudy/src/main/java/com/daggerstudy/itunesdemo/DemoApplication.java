package com.daggerstudy.itunesdemo;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.daggerstudy.itunesdemo.component.DaggerDemoApplicationComponent;
import com.daggerstudy.itunesdemo.component.DemoApplicationComponent;
import com.daggerstudy.itunesdemo.module.ApplicationModule;

/**
 * DemoApplication
 *
 * @author Jian Yang
 * @date 9/16/2015
 */
public class DemoApplication extends Application {
    private DemoApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mComponent = DaggerDemoApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        mComponent.inject(this);
    }

    public DemoApplicationComponent getComponent() {
        return mComponent;
    }

    /**
     * Extracts application from support context types.
     *
     * @param context Source context.
     * @return Application instance or {@code null}.
     */
    public static DemoApplication from(@NonNull Context context) {
        return (DemoApplication) context.getApplicationContext();
    }
}
