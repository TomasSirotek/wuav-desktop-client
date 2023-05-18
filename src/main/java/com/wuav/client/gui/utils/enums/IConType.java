package com.wuav.client.gui.utils.enums;

public enum IConType {
    CLOSE("close.png"),
    OPEN ("close.png");



    private final String style;

    IConType(String style) {
        this.style = style;
    }

    public String getStyle() {
        return style;
    }
}
