package com.wuav.client.gui.utils;

import animatefx.animation.FadeInDown;
import animatefx.animation.FadeOutUp;
import com.wuav.client.gui.utils.enums.CustomColor;
import javafx.animation.PauseTransition;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * The type Animation util.
 */
public class AnimationUtil {

    /**
     * Animate in out.
     *
     * @param pane                     the pane
     * @param pauseDurationInSeconds  the pause duration in seconds
     * @param style                    the style
     */
    public static void animateInOut(Pane pane, double pauseDurationInSeconds, CustomColor style) {
        pane.setVisible(true);
        pane.setStyle(style.getStyle());
        FadeInDown fadeInDown = new FadeInDown(pane);
        fadeInDown.setOnFinished(event -> {
            PauseTransition pause = new PauseTransition(Duration.seconds(pauseDurationInSeconds));
            pause.setOnFinished(event2 -> {
                FadeOutUp fadeOutUp = new FadeOutUp(pane);
                fadeOutUp.setOnFinished(event3 -> {
                    pane.setVisible(false);
                    pane.setStyle("");
                });
                fadeOutUp.play();
            });
            pause.play();
        });
        fadeInDown.play();
    }
}
