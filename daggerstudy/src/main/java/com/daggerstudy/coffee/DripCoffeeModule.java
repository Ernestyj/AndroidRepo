package com.daggerstudy.coffee;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * DripCoffeeModule
 *
 * @author Jian Yang
 * @date 9/13/2015
 */
@Module(includes = PumpModule.class)
public class DripCoffeeModule {
    @Provides
    @Singleton
    Heater provideHeater() {
        return new ElectricHeater();
    }
}
