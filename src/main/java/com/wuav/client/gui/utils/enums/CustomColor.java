package com.wuav.client.gui.utils.enums;

public enum CustomColor {
    ERROR("-fx-background-color: rgb(211, 47, 47);"),
    SUCCESS("-fx-background-color: rgb(56, 142, 60);"),
    INFO("-fx-background-color: rgb(2, 136, 209);"),
    WARNING("-fx-background-color: yellow;"),

    TRANSPARENT("-fx-background-color: transparent;"),

    DIMMED("-fx-background-color: rgba(0, 0, 0, 0.2);"),
    HIGHLIGHTED("-fx-background-color: rgba(234, 234, 234, 0.8);");


    private final String style;

    CustomColor(String style) {
        this.style = style;
    }

    public String getStyle() {
        return style;
    }
}
