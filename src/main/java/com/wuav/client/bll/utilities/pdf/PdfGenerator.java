package com.wuav.client.bll.utilities.pdf;


import com.google.inject.Inject;
import com.wuav.client.be.CustomImage;
import com.wuav.client.be.Customer;
import com.wuav.client.be.Project;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.cache.ImageCache;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;

import java.awt.image.BufferedImage;
import java.io.*;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;

public class PdfGenerator implements IPdfGenerator {

    private static final String RESOURCE_FOLDER = "src/main/resources/com/wuav/client/images/wuav-logo.png";
    private static final String OUTPUT_FOLDER = "src/main/resources";
    private static final String FILE_EXTENSION = ".pdf";

    @Override
    public ByteArrayOutputStream generatePdf(AppUser appUser, Project project, String fileName) {
        var outputStream = new ByteArrayOutputStream();
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Set font
            PDType1Font font = PDType1Font.HELVETICA;
            PDType1Font fontHeader = PDType1Font.HELVETICA_BOLD;
            contentStream.setFont(font, 12);

           /// MAIN RECTANGLES FOR THE PDF ///

            // Logo Image area
            PDRectangle logoRect = new PDRectangle(50, PDRectangle.A4.getHeight() - 150, PDRectangle.A4.getWidth() / 4, 75);

            // Full Width Image area (with padding around it)
            PDRectangle imageRect = new PDRectangle(50, PDRectangle.A4.getHeight() - 350, PDRectangle.A4.getWidth() - 100, 160);

            // Customer Info List area (right under the full-width image)
            PDRectangle infoRect = new PDRectangle(50, PDRectangle.A4.getHeight() - 800, PDRectangle.A4.getWidth() - 100, 400);

            // technician info
            PDRectangle techInfoRect = new PDRectangle(PDRectangle.A4.getWidth() / 2, PDRectangle.A4.getHeight() - 800, PDRectangle.A4.getWidth() / 2 - 50, 400);

            // Description Text area
            PDRectangle descriptionRect = new PDRectangle(50, PDRectangle.A4.getHeight() - 800, PDRectangle.A4.getWidth(), 200);

            /// END MAIN RECTANGLES FOR THE PDF ///


            // Logo Image
            PDImageXObject logoImage = PDImageXObject.createFromFile(RESOURCE_FOLDER, document);
            contentStream.drawImage(logoImage, logoRect.getLowerLeftX(), logoRect.getLowerLeftY(), logoRect.getWidth(), logoRect.getHeight());

            // Full Width Image

            var filePlan = retrieveInstallationPlanAsFile(project.getProjectImages());
            PDImageXObject image = PDImageXObject.createFromFile(filePlan.getAbsolutePath(), document);

            filePlan.delete();

            // Calculate the scaling factor based on the original image size and the desired display size
            float originalWidth = image.getWidth();
            float originalHeight = image.getHeight();
            float displayWidth = imageRect.getWidth() - 40;
            float displayHeight = imageRect.getHeight();

            float scaleX = displayWidth / originalWidth;
            float scaleY = displayHeight / originalHeight;
            float scale = Math.min(scaleX, scaleY);

            // Draw the resized image within the desired area
            float imageWidth = originalWidth * scale;
            float imageHeight = originalHeight * scale;
            float imageX = imageRect.getLowerLeftX() + (displayWidth - imageWidth) / 2;
            float imageY = imageRect.getLowerLeftY() + (displayHeight - imageHeight) / 2;

            contentStream.drawImage(image, imageX, imageY, imageWidth, imageHeight);

            // Draw a border around the full-width image area
            contentStream.addRect(imageRect.getLowerLeftX(), imageRect.getLowerLeftY(), imageRect.getWidth(), imageRect.getHeight());
            contentStream.setLineWidth(1);
            contentStream.setStrokingColor(java.awt.Color.BLACK);
            contentStream.stroke();

            // Customer Info List
            String customerInfo = "Customer Info";

            contentStream.beginText();
            contentStream.setFont(fontHeader, 14);
            contentStream.newLineAtOffset(infoRect.getLowerLeftX(), infoRect.getUpperRightY() + 20);
            contentStream.showText(customerInfo);
            // add margin on the bottom
            contentStream.newLineAtOffset(0, -20);
            contentStream.endText();

            List<String> customerInfoList = List.of("Customer Name: " + project.getCustomer().getName(),
                    "Customer Email: " + project.getCustomer().getEmail(),
                    "Customer Phone: " + project.getCustomer().getPhoneNumber());

            contentStream.beginText();
            // set padding to the top
            contentStream.newLineAtOffset(0, -5);
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(infoRect.getLowerLeftX(), infoRect.getUpperRightY());

            for (String line : customerInfoList) {
                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -20);
            }
            contentStream.endText();




            // Technician Info List
            String technicianInfo = "Technician Info";
            contentStream.beginText();
            contentStream.setFont(fontHeader, 14);
            contentStream.newLineAtOffset(techInfoRect.getLowerLeftX(), techInfoRect.getUpperRightY() + 20);
            contentStream.showText(technicianInfo);
            contentStream.newLineAtOffset(0, -20);
            contentStream.endText();

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMMM dd yyyy");
            String formattedDate = dateFormat.format(project.getCreatedAt());

            List<String> technicianInfoList = List.of("Technician Name: " + appUser.getName(),
                    "Technician Email: " +  appUser.getEmail(),
                    "Installation Date : " + formattedDate
                   ) ;

            contentStream.beginText();
            contentStream.newLineAtOffset(0, -5);
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(techInfoRect.getLowerLeftX(), techInfoRect.getUpperRightY());


            for (String line : technicianInfoList) {
                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -20);
            }
            contentStream.endText();
//
//
//            // Description Text
//            contentStream.beginText();
//            contentStream.setFont(fontHeader, 14);
//            contentStream.setNonStrokingColor(blackColor);
//            contentStream.newLineAtOffset(descriptionRect.getLowerLeftX(), descriptionRect.getLowerLeftY() + 300);
//            contentStream.showText("Description");
//            contentStream.endText();
//
//            // Lorem Ipsum Text
//           // String loremIpsum = DESCR_TEST;
//
//            contentStream.beginText();
//            contentStream.setFont(font, 12);
//            contentStream.setNonStrokingColor(blackColor);
//            contentStream.newLineAtOffset(descriptionRect.getLowerLeftX(), descriptionRect.getLowerLeftY() + 250);
//
//            // Break lorem ipsum text into lines and show them
//            List<String> loremLines = breakTextIntoLines(project.getDescription(), 500, font, 12);
//            for (String line : loremLines) {
//                contentStream.showText(line);
//                contentStream.newLineAtOffset(0, -20);
//            }
//            contentStream.endText();
//
//            // close the stream
           contentStream.close();


            Path resourceFolder = Paths.get(OUTPUT_FOLDER);
            Path filePath = resourceFolder.resolve(fileName + FILE_EXTENSION);


            try {
                document.save(outputStream);
                contentStream.close();
                document.close();

                return outputStream;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return outputStream;
    }

    private File retrieveInstallationPlanAsFile(List<CustomImage> projectImagesList) throws IOException {
        CustomImage mainImage = projectImagesList.stream()
                .filter(CustomImage::isMainImage)
                .findFirst()
                .orElse(null);

        File tempFile = null;

        if (mainImage != null) {
            // Retrieve the main image using the ImageCache class
            Image image = ImageCache.getImage(mainImage.getId());

            // Convert the Image to a BufferedImage
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

            // Save the main image to a temporary file
            tempFile = File.createTempFile("mainImage", ".png");
            try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                ImageIO.write(bufferedImage, "png", outputStream);
            }
        }

        return tempFile;
    }

    public static List<String> breakTextIntoLines(String text, float maxWidth, PDFont font, float fontSize) throws IOException {
        List<String> lines = new ArrayList<>();
        int lastSpace = -1;

        while (text.length() > 0) {
            int spaceIndex = text.indexOf(' ', lastSpace + 1);
            if (spaceIndex < 0) {
                spaceIndex = text.length();
            }
            String subString = text.substring(0, spaceIndex);
            float size = fontSize * font.getStringWidth(subString) / 1000;

            if (size > maxWidth) {
                if (lastSpace < 0) {
                    lastSpace = spaceIndex;
                }
                subString = text.substring(0, lastSpace);
                lines.add(subString);
                text = text.substring(lastSpace).trim();
                lastSpace = -1;
            } else if (spaceIndex == text.length()) {
                lines.add(text);
                text = "";
            } else {
                lastSpace = spaceIndex;
            }
        }
        return lines;
    }


}





