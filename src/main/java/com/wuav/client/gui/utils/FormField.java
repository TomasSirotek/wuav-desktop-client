package com.wuav.client.gui.utils;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextInputControl;

public class FormField {
    private final Node control;
    private final String errorMessage;
    private final FieldValidator validationFunction;
    private final String errorValidationMessage;

    public FormField(Node control, String errorMessage) {
        this(control, errorMessage, null, null);
    }

    public FormField(Node control, String errorMessage, FieldValidator validationFunction, String errorValidationMessage) {
        this.control = control;
        this.errorMessage = errorMessage;
        this.validationFunction = validationFunction;
        this.errorValidationMessage = errorValidationMessage;
    }

    public String getText() {
        if (control instanceof TextInputControl) {
            return ((TextInputControl) control).getText();
        } else if (control instanceof ChoiceBox) {
            Object value = ((ChoiceBox<?>) control).getValue();
            return value != null ? value.toString() : "";
        } else {
            throw new UnsupportedOperationException("Control type not supported: " + control.getClass().getName());
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public FieldValidator getValidationFunction() {
        return validationFunction;
    }

    public String getErrorValidationMessage() {
        return errorValidationMessage;
    }
}
