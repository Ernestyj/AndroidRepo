package com.daggerstudy.itunesdemo.module;

import android.content.Context;

import com.daggerstudy.itunesdemo.DemoApplication;
import com.daggerstudy.itunesdemo.rest.ITunesService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

/**
 * ApplicationModule
 *
 * @author Jian Yang
 * @date 9/16/2015
 */
@Module
public class ApplicationModule {
    private DemoApplication mApplication;
    private ITunesService mITunesService;

    public ApplicationModule(DemoApplication application) {
        this.mApplication = mApplication;
        mITunesService = new RestAdapter.Builder()
                .setEndpoint("https://itunes.apple.com")
                .build()
                .create(ITunesService.class);
    }

    @Provides @Singleton
    Context provideApplicationContext(){
        return mApplication;
    }

    @Provides @Singleton
    ITunesService provideITunesService(){
        return mITunesService;
    }
}
