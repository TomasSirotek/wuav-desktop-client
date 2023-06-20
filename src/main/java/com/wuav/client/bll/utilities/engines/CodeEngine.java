package com.wuav.client.bll.utilities.engines;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.google.gson.Gson;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * The engine for the codes
 */
public class CodeEngine implements ICodesEngine {

    /**
     * Generates a QR code image
     *
     * @param barcodeText the text to generate the QR code for
     * @param width       the width of the QR code
     * @param height      the height of the QR code
     * @return the QR code image
     * @throws Exception if the QR code could not be generated
     */
    @Override
    public byte[] generateQRCodeImage(String barcodeText, int width, int height) throws Exception {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, width, height);

        return convertToBytes(MatrixToImageWriter.toBufferedImage(bitMatrix));
    }

    /**
     * Generates a QR code image view
     *
     * @param userId      the user id
     * @param projectName the project name
     * @param width       the width
     * @param height      the height
     * @return the QR code image view
     * @throws Exception if the QR code image view could not be generated
     */
    @Override
    public ImageView generateQRCodeImageView(int userId, String projectName, int width, int height) throws Exception {
        // A new HashMap is created. This will be used to store the data that will be encoded into the QR code.
        Map<String, Object> qrData = new HashMap<>();

        // The user's ID and the project name are put into the HashMap with keys "userId" and "projectName", respectively.
        qrData.put("userId", userId);
        qrData.put("projectName", projectName);

        // A new Gson object is created. Gson is a library that can be used to convert Java Objects into their JSON representation and vice versa.
        Gson gson = new Gson();
        // The HashMap is converted into a JSON string using the Gson object.
        String jsonString = gson.toJson(qrData);

        // The JSON string is then converted into a QR code image. The width and height parameters specify the dimensions of the QR code image.
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
