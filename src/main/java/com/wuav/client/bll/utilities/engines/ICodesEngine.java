package com.wuav.client.bll.utilities.engines;

import javafx.scene.image.ImageView;

/**
 * The interface for the codes engine
 */
public interface ICodesEngine {


    /**
     * Generates a QR code image
     *
     * @param barcodeText the text to generate the QR code for
     * @param width       the width of the QR code
     * @param height      the height of the QR code
     * @return the QR code image
     * @throws Exception if the QR code could not be generated
     */
    byte[] generateQRCodeImage(String barcodeText, int width, int height) throws Exception;

    /**
     * Generates a QR code image view
     *
     * @param userId      the user id
     * @param projectName the project name
     * @param width       the width of the code
     * @param height      the height of the cde
     * @return the QR code image view
     * @throws Exception if the QR code image view could not be generated
     */
    ImageView generateQRCodeImageView(int userId, String projectName, int width, int height) throws Exception;
}
