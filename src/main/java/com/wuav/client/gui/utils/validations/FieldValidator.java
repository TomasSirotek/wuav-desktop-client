package com.wuav.client.gui.utils.validations;

/**
 * The interface Field validator.
 */
@FunctionalInterface
public interface FieldValidator {
    /**
     * Validate boolean.
     *
     * @param input the input
     * @return the boolean
     */
    boolean validate(String input);
}