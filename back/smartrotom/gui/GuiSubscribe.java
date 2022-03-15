/*
 * Copyright (C) 2018 BARBOTIN Nicolas
 */

package es.allblue.lizardon.smartrotom.gui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GuiSubscribe {
}
