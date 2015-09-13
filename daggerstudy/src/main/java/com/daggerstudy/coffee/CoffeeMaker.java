package com.daggerstudy.coffee;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * CoffeeMaker
 *
 * @author Jian Yang
 * @date 9/13/2015
 */
public class CoffeeMaker {
    private final Lazy<Heater> heater; // Create a possibly costly heater only when we use it.
    private final Pump pump;

    @Inject
    CoffeeMaker(Lazy<Heater> heater, Pump pump) {
        this.heater = heater;
        this.pump = pump;
    }

    public void brew() {
        heater.get().on();
        pump.pump();
        System.out.println(" [_]P coffee! [_]P ");
        heater.get().off();
    }
}
