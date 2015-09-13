package com.daggerstudy.coffee;

import dagger.Module;
import dagger.Provides;

/**
 * PumpModule
 *
 * @author Jian Yang
 * @date 9/13/2015
 */
@Module
public class PumpModule {
    @Provides
    Pump providePump(Thermosiphon pump) {
        return pump;
    }
}
