package com.daggerstudy.coffee;

/**
 * Heater
 *
 * @author Jian Yang
 * @date 9/13/2015
 */
interface Heater {
    void on(); //加热器打开
    void off();//加热器关闭
    boolean isHot();//加热是否完毕
}
