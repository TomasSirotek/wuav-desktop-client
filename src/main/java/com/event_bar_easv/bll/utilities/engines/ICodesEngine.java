package com.event_bar_easv.bll.utilities.engines;

public interface ICodesEngine {

    byte[] generateEAN13BarcodeImage(String barcodeText, int width, int height) throws Exception;

    byte[] generateQRCodeImage(String barcodeText, int width, int height) throws Exception;
}
