package com.wuav.client.bll.utilities.engines;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import com.google.gson.Gson;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class CodeEngine implements ICodesEngine {

    public byte[] generateEAN13BarcodeImage(String barcodeText, int width, int height) throws Exception {
        EAN13Writer barcodeWriter = new EAN13Writer();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.EAN_13, width, height);

        return convertToBytes(MatrixToImageWriter.toBufferedImage(bitMatrix));
    }
    public byte[] generateQRCodeImage(String barcodeText,int width, int height) throws Exception {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, width, height);

        return convertToBytes(MatrixToImageWriter.toBufferedImage(bitMatrix));
    }

    public ImageView generateQRCodeImageView(int userId, int projectId, int width, int height) throws Exception {
        Map<String, Integer> qrData = new HashMap<>();
        qrData.put("userId", userId);
        qrData.put("projectId", projectId);

         Gson gson = new Gson();
        String jsonString = gson.toJson(qrData);

        byte[] qrCodeImage = generateQRCodeImage(jsonString, width, height);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(qrCodeImage);
        Image image = new Image(inputStream);
        ImageView imageView = new ImageView(image);

        return imageView;
    }


    private byte[] convertToBytes(BufferedImage bufferedImage) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();
        return imageInByte;
    }
}
