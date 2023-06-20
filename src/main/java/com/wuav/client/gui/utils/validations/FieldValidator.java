package com.wuav.client.gui.utils.validations;

/**
 * The interface Field validator.
 * contains exactly one abstract method.
 * Its to tell the compiler to generate an error if the interface contains more than one abstract method and does not satisfy the requirements of a functional interface.
 * FieldValidator notEmptyValidator = input -> !input.isEmpty(); (example)
 * boolean isValid = notEmptyValidator.validate("Hello, world!");  // true (example
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