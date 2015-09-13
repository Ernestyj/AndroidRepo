package com.daggerstudy.androidcoffee;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * ForApplication
 *
 * @author Jian Yang
 * @date 9/13/2015
 */
@Qualifier
@Retention(RUNTIME)
public @interface ForApplication {
}
