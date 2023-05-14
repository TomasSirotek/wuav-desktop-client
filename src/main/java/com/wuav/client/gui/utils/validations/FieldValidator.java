package com.wuav.client.gui.utils.validations;

@FunctionalInterface
public interface FieldValidator {
    boolean validate(String input);
}