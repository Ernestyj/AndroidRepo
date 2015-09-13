package com.daggerstudy.coffee;

import javax.inject.Inject;

/**
 * Thermosiphon
 *
 * @author Jian Yang
 * @date 9/13/2015
 */
public class Thermosiphon implements Pump {
    private final Heater heater;

    @Inject
    Thermosiphon(Heater heater) {
        this.heater = heater;
    }

    @Override
    public void pump() {
        if (heater.isHot()) {
            System.out.println("=> => pumping => =>");
        }
    }
}
