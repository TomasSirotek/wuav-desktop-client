package com.wuav.client.gui.utils.validations;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextInputControl;


/**
 * The type Form field.
 */
public class FormField {
    private final Node control;
    private final String errorMessage;
    private final FieldValidator validationFunction;
    private final String errorValidationMessage;

    /**
     * Instantiates a new Form field.
     *
     * @param control      the control
     * @param errorMessage the error message
     */
    public FormField(Node control, String errorMessage) {
        this(control, errorMessage, null, null);
    }

    /**
     * Instantiates a new Form field.
     *
     * @param control                the control
     * @param errorMessage           the error message
     * @param validationFunction     the validation function
     * @param errorValidationMessage the error validation message
     */
    public FormField(Node control, String errorMessage, FieldValidator validationFunction, String errorValidationMessage) {
        this.control = control;
        this.errorMessage = errorMessage;
        this.validationFunction = validationFunction;
        this.errorValidationMessage = errorValidationMessage;
    }

    /**
     * Gets text.
     *
     * @return the text
     */
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

    /**
     * Get error message string.
     *
     * @return the string
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Gets validation function.
     *
     * @return the validation function
     */
    public FieldValidator getValidationFunction() {
        return validationFunction;
    }


}
