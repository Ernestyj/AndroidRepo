package com.daggerstudy.androidcoffee;

import android.app.Application;
import android.location.LocationManager;

import com.daggerstudy.androidcoffee.ui.HomeActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;

/**
 * DemoApplication
 *
 * @author Jian Yang
 * @date 9/13/2015
 */
public class DemoApplication extends Application {
    @Singleton
    @Component(modules = AndroidModule.class)
    public interface ApplicationComponent {
        void inject(DemoApplication application);
        void inject(HomeActivity homeActivity);
        void inject(DemoActivity demoActivity);
    }

    @Inject
    LocationManager locationManager; // for some reason.

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerDemoApplication_ApplicationComponent.builder()
                .androidModule(new AndroidModule(this))
                .build();
        component().inject(this); // As of now, LocationManager should be injected into this.
    }

    public ApplicationComponent component() {
        return component;
    }
}
