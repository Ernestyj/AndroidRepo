package com.daggerstudy.coffee;

import javax.inject.Singleton;

import dagger.Component;

/**
 * CoffeeApp
 *
 * @author Jian Yang
 * @date 9/13/2015
 */
public class CoffeeApp {
    @Singleton
    @Component(modules = { DripCoffeeModule.class })
    public interface Coffee {
        CoffeeMaker maker();
    }

    public static void main(String[] args) {
        Coffee coffee = DaggerCoffeeApp_Coffee.builder().build();
        coffee.maker().brew();
    }
}
