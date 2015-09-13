package com.daggerstudy.coffee;

/**
 * ElectricHeater
 *
 * @author Jian Yang
 * @date 9/13/2015
 */
public class ElectricHeater implements Heater {
    boolean heating;

    @Override
    public void on() {
        System.out.println("~ ~ ~ heating ~ ~ ~");
        this.heating = true;
    }

    @Override
    public void off() {
        this.heating = false;
    }

    @Override
    public boolean isHot() {
        return heating;
    }
}
