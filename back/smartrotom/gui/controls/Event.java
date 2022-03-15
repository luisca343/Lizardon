/*
 * Copyright (C) 2018 BARBOTIN Nicolas
 */

package es.allblue.lizardon.smartrotom.gui.controls;

public abstract class Event<T extends Control> {

    protected T source;

    public T getSource() {
        return source;
    }

}
