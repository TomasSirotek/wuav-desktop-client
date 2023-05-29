package com.wuav.client.gui.utils.enums;

/**
 * The enum I con type.
 */
public enum IConType {
    CLOSE("close.png"),
    OPEN ("close.png"),
    SUCCESS("/dashboardDone.png");

    private final String style;

    /**
     * Instantiates a new I con type.
     *
     * @param style the style
     */
    IConType(String style) {
        this.style = style;
    }

    public String getStyle() {
        return style;
    }
}
