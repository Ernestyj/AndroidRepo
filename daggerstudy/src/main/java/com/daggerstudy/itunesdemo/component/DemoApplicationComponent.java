package com.daggerstudy.itunesdemo.component;

import com.daggerstudy.itunesdemo.AlbumSearchActivity;
import com.daggerstudy.itunesdemo.DemoApplication;
import com.daggerstudy.itunesdemo.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * DemoApplicationComponent
 *
 * @author Jian Yang
 * @date 9/16/2015
 */
@Component(modules = ApplicationModule.class) @Singleton
public interface DemoApplicationComponent {
    void inject(DemoApplication application);
    void inject(AlbumSearchActivity activity);
}
