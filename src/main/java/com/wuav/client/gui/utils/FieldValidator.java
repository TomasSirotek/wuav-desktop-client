package com.wuav.client.gui.utils;

@FunctionalInterface
public interface FieldValidator {
    boolean validate(String input);
}