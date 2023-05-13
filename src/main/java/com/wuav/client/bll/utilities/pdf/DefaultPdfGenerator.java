package com.wuav.client.bll.utilities.pdf;

import com.wuav.client.be.CustomImage;
import com.wuav.client.be.Project;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.cache.ImageCache;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class DefaultPdfGenerator {
    private AppUser appUser;
    private Project project;
    private String fileName;
    private boolean includeDescription;
    private boolean includeTechnicians;
    private boolean includeDevices;
    private boolean includeImages;

    private static final String RESOURCE_FOLDER = "src/main/resources/com/wuav/client/images/wuav-logo.png";


    private DefaultPdfGenerator(Builder builder) {
        this.appUser = builder.appUser;
        this.project = builder.project;
        this.fileName = builder.fileName;
        this.includeDescription = builder.includeDescription;
        this.includeTechnicians = builder.includeTechnicians;
        this.includeDevices = builder.includeDevices;
        this.includeImages = builder.includeImages;
    }

    public ByteArrayOutputStream generatePdf() {
        // Implement the PDF generation logic here
        // ...
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



            if(includeTechnicians){
                // technician info
                PDRectangle techInfoRect = new PDRectangle(PDRectangle.A4.getWidth() / 2, PDRectangle.A4.getHeight() - 800, PDRectangle.A4.getWidth() / 2 - 50, 400);

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
            }


            if(includeDescription){
                // Description Text area
                PDRectangle descriptionRect = new PDRectangle(50, PDRectangle.A4.getHeight() - 800, PDRectangle.A4.getWidth(), 200);
                // Description Text
            contentStream.beginText();
            contentStream.setFont(fontHeader, 14);
          //  contentStream.setNonStrokingColor(blackColor);
            contentStream.newLineAtOffset(descriptionRect.getLowerLeftX(), descriptionRect.getLowerLeftY() + 300);
            contentStream.showText("Description");
            contentStream.endText();

            // Lorem Ipsum Text
           // String loremIpsum = DESCR_TEST;

            contentStream.beginText();
            contentStream.setFont(font, 12);
         //   contentStream.setNonStrokingColor(blackColor);
            contentStream.newLineAtOffset(descriptionRect.getLowerLeftX(), descriptionRect.getLowerLeftY() + 250);

            // Break lorem ipsum text into lines and show them
            List<String> loremLines = breakTextIntoLines(project.getDescription(), 500, font, 12);
            for (String line : loremLines) {
                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -20);
            }
            contentStream.endText();
            }


            if (includeImages) {
                // Add a new page for the images
                PDPage imagePage = new PDPage();
                document.addPage(imagePage);
                PDPageContentStream imageContentStream = new PDPageContentStream(document, imagePage);

                // Draw the images
                addImages(imageContentStream,document, project.getProjectImages());

                imageContentStream.close();
            }


            contentStream.close(); // DONT REMOVE !

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


    private void addImages(PDPageContentStream contentStream, PDDocument document, List<CustomImage> projectImagesList) throws IOException {
        // Calculate the dimensions for the images
        float imageWidth = PDRectangle.A4.getWidth() - 100; // Full width, minus padding
        float imageHeight = (PDRectangle.A4.getHeight() - 200) / 2; // Half height, minus padding

        // Calculate the positions for the images
        float imageX = 50; // Padding
        float firstImageY = PDRectangle.A4.getHeight() - 100 - imageHeight; // Top padding plus image height
        float secondImageY = 50; // Bottom padding

        // Retrieve the non-main images
        List<CustomImage> nonMainImages = projectImagesList.stream()
                .filter(image -> !image.isMainImage())
                .collect(Collectors.toList());

        // Check if there are at least two non-main images
        if (nonMainImages.size() >= 2) {
            // Load the first non-main image
            CustomImage firstImage = nonMainImages.get(0);
            Image fxImage1 = ImageCache.getImage(firstImage.getId());
            BufferedImage bufferedImage1 = SwingFXUtils.fromFXImage(fxImage1, null);
            PDImageXObject image1 = LosslessFactory.createFromImage(document, bufferedImage1);

            // Load the second non-main image
            CustomImage secondImage = nonMainImages.get(1);
            Image fxImage2 = ImageCache.getImage(secondImage.getId());
            BufferedImage bufferedImage2 = SwingFXUtils.fromFXImage(fxImage2, null);
            PDImageXObject image2 = LosslessFactory.createFromImage(document, bufferedImage2);

            // Draw the first image on the top half of the page
            contentStream.drawImage(image1, imageX, firstImageY, imageWidth, imageHeight);

            // Draw the second image on the bottom half of the page
            contentStream.drawImage(image2, imageX, secondImageY, imageWidth, imageHeight);
        }
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

    public static class Builder {
        private AppUser appUser;
        private Project project;
        private String fileName;
        private boolean includeDescription;
        private boolean includeTechnicians;
        private boolean includeDevices;
        private boolean includeImages;

        public Builder(AppUser appUser, Project project, String fileName) {
            this.appUser = appUser;
            this.project = project;
            this.fileName = fileName;
        }

        public Builder includeDescription(boolean includeDescription) {
            this.includeDescription = includeDescription;
            return this;
        }

        public Builder includeTechnicians(boolean includeTechnicians) {
            this.includeTechnicians = includeTechnicians;
            return this;
        }

        public Builder includeDevices(boolean includeDevices) {
            this.includeDevices = includeDevices;
            return this;
        }

        public Builder includeImages(boolean includeImages) {
            this.includeImages = includeImages;
            return this;
        }

        public DefaultPdfGenerator build() {
            return new DefaultPdfGenerator(this);
        }
    }
}

