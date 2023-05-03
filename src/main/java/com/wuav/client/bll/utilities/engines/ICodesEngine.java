package com.wuav.client.bll.utilities.engines;

import javafx.scene.image.ImageView;

public interface ICodesEngine {

    byte[] generateEAN13BarcodeImage(String barcodeText, int width, int height) throws Exception;

    byte[] generateQRCodeImage(String barcodeText, int width, int height) throws Exception;

    ImageView generateQRCodeImageView(int userId, int projectId, int width, int height) throws Exception;
}
