package com.event_bar_easv.bll.utilities.pdf;


import com.event_bar_easv.be.Event;
import com.event_bar_easv.be.SpecialTicketType;
import com.event_bar_easv.be.Ticket;
import com.event_bar_easv.be.TicketType;
import com.event_bar_easv.be.user.AppUser;
import com.event_bar_easv.bll.utilities.engines.CodeEngine;
import com.event_bar_easv.bll.utilities.engines.ICodesEngine;
import com.google.inject.Inject;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class PdfGenerator implements IPdfGenerator {

    private static final String RESOURCE_FOLDER = "src/main/resources/com/event_bar_easv/images/easv_logo.png";
    private static final String OUTPUT_FOLDER = "src/main/resources";

    private static final String FILE_NAME = ".pdf";

    private final ICodesEngine codesEngine;

    private String fileName = "";

    @Inject
    public PdfGenerator(ICodesEngine codesEngine) {
        this.codesEngine = codesEngine;
    }

    private static void writeText(PDPageContentStream contentStream, String text, PDFont font, float leading,
                                  int size, float xPos, float yPos, RenderingMode renderModeL) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, size);
        contentStream.newLineAtOffset(xPos, yPos);
        contentStream.setRenderingMode(renderModeL);
        contentStream.setLeading(leading);
        contentStream.showText(text);
        contentStream.endText();

    }


    @Override
    public String generatePdf(AppUser customer, Event event2, Ticket ticket, SpecialTicketType specialTicketType) {

        try(PDDocument document = new PDDocument()){
            PDPage page = new PDPage(new PDRectangle(600,300));
            document.addPage(page);

            // ADD ANOTHER PAGE IF HAS SPECIALTICKET TYPE

            File file = new File(RESOURCE_FOLDER);
            String absolutePath = file.getAbsolutePath();

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            var barCode = codesEngine.generateEAN13BarcodeImage("123456789012", 300, 300); // must not exceed 13 digits
            var qrCode = codesEngine.generateQRCodeImage(String.valueOf(customer.getId()), 115, 115);

            PDImageXObject pdImage = PDImageXObject.createFromFile(absolutePath, document);
            PDImageXObject bardCode = PDImageXObject.createFromByteArray(document, barCode, "png");
            PDImageXObject qrCodeImg = PDImageXObject.createFromByteArray(document, qrCode, "png");

            PDRectangle pageSize = page.getMediaBox();
            float pageWidth = pageSize.getWidth();
            float pageHeight = pageSize.getHeight();

            float imageWidth = pageWidth / 3;

            // pos
            float firstImageXOffset = -220;
            float firstImageYOffset = -150;

            float secondImageXOffset = pageWidth - imageWidth;
            float secondImageYOffset = 50;

            float qrCodeXOffset = 305;
            float qrCodeYOffset = 25;

            contentStream.drawImage(pdImage, firstImageXOffset, firstImageYOffset, 600, 600);
            contentStream.drawImage(bardCode,secondImageXOffset, secondImageYOffset, imageWidth, pageHeight - 100);
            contentStream.drawImage(qrCodeImg,qrCodeXOffset, qrCodeYOffset, 115, 115);

            float textXOffset = imageWidth - 20;
            float textYOffset = (7 * pageHeight) / 9;

            String eventName = event2.getEventName();
            String eventLocation = event2.getLocation();
            String eventStartEndDate = "Start/End Date: " + event2.getStartDate() + " - " + event2.getEndDate();
            String eventStartEndTime = "Start/End Time: " + event2.getStartTime() + " - " + event2.getEndTime();
            String ticketNumber = "Ticket Number: " + ticket.getNumber();
            String customeName = "Customer Name: " + customer.getName();

            String ticketType = "Ticket type: " + ticket.getTicketType().getType();

            writeText(contentStream, eventName, PDType1Font.TIMES_ROMAN,14.5f, 16, textXOffset, textYOffset + 20, RenderingMode.FILL_STROKE);
            writeText(contentStream, eventLocation, PDType1Font.TIMES_ROMAN,0 ,14, textXOffset , textYOffset -20 , RenderingMode.FILL);
            writeText(contentStream, eventStartEndDate, PDType1Font.TIMES_ROMAN,0 ,14, textXOffset , textYOffset -40 , RenderingMode.FILL);
            writeText(contentStream, eventStartEndTime, PDType1Font.TIMES_ROMAN,0 ,14, textXOffset , textYOffset -60 , RenderingMode.FILL);
            writeText(contentStream, ticketNumber, PDType1Font.TIMES_ROMAN,14.5f ,14, textXOffset , textYOffset -80 , RenderingMode.FILL);

            writeText(contentStream, ticketType, PDType1Font.TIMES_ROMAN,14.5f ,16, textXOffset , textYOffset -100 , RenderingMode.FILL_STROKE);
            writeText(contentStream, customeName, PDType1Font.TIMES_ROMAN,14.5f ,14, textXOffset , textYOffset -115 , RenderingMode.FILL);


            if(specialTicketType != null){
                PDPage specialTicketPage = new PDPage(new PDRectangle(300,300));
                document.addPage(specialTicketPage);

                PDPageContentStream contentStream2 = new PDPageContentStream(document, specialTicketPage);
                float textXOffset2 = 20;
                float textYOffset2 = (7 * pageHeight) / 9;
                float qrCodeXOffset2 =  20;
                float qrCodeYOffset2 = 10;

                String specialTicketTypeText = "Special Ticket Type: " + specialTicketType.getType();
                String specialTicketTypeBenefit = "Description: " + specialTicketType.getBenefit();



                var qrCodeSpecial = codesEngine.generateQRCodeImage(String.valueOf(ticket.getNumber()), 115, 115);
                PDImageXObject qrCodeImg2 = PDImageXObject.createFromByteArray(document, qrCodeSpecial, "png");

                writeText(contentStream2, specialTicketTypeText, PDType1Font.TIMES_ROMAN,14.5f, 16, textXOffset2, textYOffset2 + 20, RenderingMode.FILL_STROKE);
                writeText(contentStream2, specialTicketTypeBenefit, PDType1Font.TIMES_ROMAN,0 ,14, textXOffset2 , textYOffset2 -20 , RenderingMode.FILL);

                contentStream2.drawImage(qrCodeImg2,qrCodeXOffset2, qrCodeYOffset2, 180, 180);

                contentStream2.close();
            }

            contentStream.close();
            // Save the document to a resource folder
            Random random = new Random();

            Path resourceFolder = Paths.get(OUTPUT_FOLDER);
            fileName = random + FILE_NAME;
            Path filePath = resourceFolder.resolve(random + FILE_NAME);
            System.out.println("Saving file");
            try (OutputStream outputStream = Files.newOutputStream(filePath)) {
                document.save(outputStream);
                contentStream.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return fileName;
    }
}
