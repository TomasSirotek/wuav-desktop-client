<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->

# WUAV-DESKTOP-CLIENT

:school_satchel: Final-Exam | 2st Semester | SDE & SCO

---

<!-- PROJECT LOGO -->
<br />
<p align="center">
 <a href="https://github.com/TomassSimko/Private-Movie-Collection/assets/72190589/0dfec5c5-db98-4bf6-b558-c5e2a6bb3a12">
    <img src="https://github.com/TomassSimko/Private-Movie-Collection/assets/72190589/0dfec5c5-db98-4bf6-b558-c5e2a6bb3a12" alt="Logo" width="250">
  </a>
  <p align="center">
    WUAV Desktop Management Application
    <br />
    <a href="https://github.com/TomassSimko/Private-Movie-Collection"><strong>Explore the docs »</strong></a>
    <br />
  </p>

![final_mock](https://github.com/TomassSimko/wuav-desktop-client/assets/72190589/6c338654-4584-4588-847a-99f92821495e)


## The Brief

The WUAV Mobile Technician Documentation System is a desktop application designed to assist the 7 mobile technicians of WUAV in documenting their work during customer service visits. The system aims to ensure that each installation is thoroughly documented, providing customers with essential documentation for their own reference and future use by technicians.

This was my second semester with duration of 3 weeks in my computer science studies and should represent all thought materials from past two semesters. I wanted to challenge my self and take this project to another level therefore I have created the required desktop application in JavaFX and also created a mobile application in Swift. Both applications are connected to the same database and are using the same RestAPI. Both repositories can be found down below.

This GitHub project will include the development of the web-based application, documentation on how to set up and use the system, and ongoing maintenance and updates to ensure its reliability and effectiveness in supporting WUAV's mobile technicians.

Please let me know if you need any further information or if there's anything specific you would like to add to the project description!
## Product Mock

<div style="display: flex; align-items: center;">
  <div style="display: inline-block; text-align: center;">
    <img width="500" alt="Screenshot 2023-05-23 at 14 32 26" src="https://github.com/TomassSimko/Private-Movie-Collection/assets/72190589/6234f48d-15b6-45f9-8d74-7d54ac701737">
  </div>
  <div style="display: inline-block; text-align: center;">
    <img width="500" alt="Screenshot 2023-05-23 at 14 22 45" src="https://github.com/TomassSimko/Private-Movie-Collection/assets/72190589/840e3a78-cce1-469f-9258-241f34826ae7">
  </div>
</div>


## Minimal RestAPI 
This repository is a minimal RestAPI that helped me for temporary storing the images data from the Swift application and receive them in the desktop application

Key features:
* Handling post request
* Handling image data
* Storing temp data in dictionary 
* Base64 images 
* Delete request of the temp 

This repo also has basic authentication for the RestAPI, however it was just to play around and discover more RestAPI 
Also the API implements Dapper and Service-Repository pattern just to show my understanding of the design patterns.

https://github.com/TomassSimko/wuav-api
## Swift app 
This repository is a mobile application that I have created in Swift for the WUAV company. This application served as a mobile application for the technicians to document their work during customer service visits. This application handles scanning QR code from the desktop app and send post request to the RestAPI with the image data.
It was my first time using SWIFT and building mobile application and it was definitely a challenge. I have learned a lot about the mobile development and the Swift language. I have also learned how to use the Xcode IDE and how to use the storyboard to create the UI.
Key features: 
* Scanning QR code
* Sending post request to the RestAPI
* Handling image data
* Handling JSON data
* Handling user experience
* Quick unfinished AR kit demo 
  * This was just a concept of a AR installation guide for technicians to scan rooms and via AR manipulate Devices in real-world environment. This was not finished due to time constraints and most probably future budgeting.
  * Includes AR kit library and AR kit demo with another library to scan and recognize placement of the device with i cognitive menu of device that can be placed into the real-world environment  

https://github.com/TomassSimko/wuav-app
  


<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary><h2 style="display: inline-block">Table of Contents</h2></summary>
  <ol>
    <li>
      <a href="#">About The Project</a>
      <ul>
        <li><a href="#tech-stack">Tech stack</a></li>
        <li><a href="#style">Style</a></li>
      </ul>
    </li>
    <li><a href="#features-and-requirements">Features</a></li>
    <li><a href="#application-design">Application design</a></li>
    <li><a href="#database-design">Database design</a></li>
    <li><a href="#uml-diagram">UML diagram</a></li>
    <li><a href="#application-design-patterns">Application design patterns</a></li>
    <li><a href="#application-interface">Application interface</a></li>
    <li><a href="#licence">License</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

## Tech stack

* [Java](https://www.java.com/en/)
* [Liberica 19](https://bell-sw.com/libericajdk/)
* [JavaFX](https://openjfx.io/)

## Core management
* [Guava](https://github.com/google/guava)
* [Guice-DI](https://github.com/google/guice)
* [MyBatis](https://mybatis.org/mybatis-3/)
* [Logback-classic](https://logback.qos.ch/)
* [SQLServer](https://www.microsoft.com/en-us/sql-server/)
* [SLF4J](https://www.slf4j.org/)
* [Jsoup](https://jsoup.org/)

## Email management
* [Google-api-client](https://github.com/googleapis/google-api-java-client)
* [Google-oauth-client](https://github.com/googleapis/google-api-java-client)
* [Google-oauth-services-gmail](https://github.com/googleapis/google-api-java-client)
* [Sun-mail](https://javaee.github.io/javamail/)
* [Thymeleaf](https://github.com/thymeleaf/thymeleaf)

## PDF management
* [ItextPDF](https://itextpdf.com/en)
* [Apache-pdfbox](https://pdfbox.apache.org/)
* [Apache-commons-io](https://commons.apache.org/proper/commons-io/)
* [PDFBox-tools](https://pdfbox.apache.org/download.cgi#20x)

# Image management
* [Azure-storage-blob](https://docs.microsoft.com/en-us/azure/storage/blobs/storage-quickstart-blobs-java-v10)
## QR code management
* [ZXing](https://github.com/zxing/zxing/)
## HTTP request management
* [OkHttp](https://square.github.io/okhttp/)
## Unit testing
* [Mockito](https://www.slf4j.org/)
* [JUnit](https://junit.org/junit5/)
* [AssertJ](https://assertj.github.io/doc/)

## Password management
* [Spring-security](https://spring.io/projects/spring-security)
## Style
* [CSS](https://developer.mozilla.org/en-US/docs/Web/CSS/Reference)
* [MaterialFX](https://github.com/palexdev/MaterialFX)
* [AnimateFx](https://github.com/Typhon0/AnimateFX)


## Resources used for the project design 
* [FlatIcon](https://www.flaticon.com/)
* [Icons8](https://icons8.com/)
* [Dribble](https://dribbble.com/)
* [Pinterest](https://www.pinterest.com/)
* [Behance](https://www.behance.net/)
* [Apple](https://www.apple.com/)
* [Figma](https://www.figma.com/)
* [GmailAPI-video-that-helped-me](https://www.youtube.com/watch?v=xtZI23hxetw&ab_channel=SebastianDaschner)
<!-- ABOUT THE PROJECT -->


## About The Project
## Color pallet 
This is the primary color pallet for the overall product design

<img width="1112" src="https://github.com/TomassSimko/Private-Movie-Collection/assets/72190589/cd36ce8c-b9d5-4cf4-9729-a0861dac0381">

## User interface desktop design

Login page
<img width="1112" alt="Screenshot 2023-05-23 at 19 25 29" src="https://github.com/TomassSimko/Private-Movie-Collection/assets/72190589/119d84ef-9df3-4782-b3bf-e64ed7f63900">

Dashboard page
<img width="1112" alt="Screenshot 2023-05-23 at 19 25 23" src="https://github.com/TomassSimko/Private-Movie-Collection/assets/72190589/8f4bc390-18ac-45ba-b2a4-5f27092e4f0b">

Project creation stepper
<img width="1068" alt="Screenshot 2023-05-23 at 19 30 53" src="https://github.com/TomassSimko/Private-Movie-Collection/assets/72190589/b08e8ead-1ed6-471e-a2b1-dfc21ea55077">

## User interface SWIFT design

<div style="display: flex;">
  <img width="250" alt="Screenshot 1" src="https://github.com/TomassSimko/Private-Movie-Collection/assets/72190589/e7d436d3-f373-4db9-8058-17e7adb4664b">
  <img width="250" alt="Screenshot 2" src="https://github.com/TomassSimko/Private-Movie-Collection/assets/72190589/4266d941-ed91-48ab-9336-4e2b8313387f">
  <img width="250" alt="Screenshot 3" src="https://github.com/TomassSimko/Private-Movie-Collection/assets/72190589/224331c7-6806-44dd-a50b-77966462efb3">
</div>



## Features and requirements

## PDF generation
One of the requirements was to be able to generate pdf files from the data that is stored in the database. I have used the ItextPDF library to generate the pdf files. 
This is the quick result of generated pdf file.


## Email templates 
Application is able to send emails to the users. 

I have used the Google API to send emails. I have used the Thymeleaf library to generate the email templates.
Build a template for the email that is sent to the user. This is the result of the email template. They are different types of the emails

Installation confirmation,password reset email and email with password when new user is created and password is generated for him.

## QR code generation
When creating new project in the process of creating new project the QR code is generated for the project.SWIFT application is required to scan the QR code and it will open the project in the application and user can go and take pictures from the phone and upload them to the desktop application.
Generated imageView is presented to the user interface and read from the application as JSON holding the data about the project and user id.
``` java
@Override
  public byte[] generateQRCodeImage(String barcodeText,int width, int height) throws Exception {
    QRCodeWriter barcodeWriter = new QRCodeWriter();
      BitMatrix bitMatrix =
        barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, width, height);
        return convertToBytes(MatrixToImageWriter.toBufferedImage(bitMatrix));
 }

@Override
  public ImageView generateQRCodeImageView(int userId, String projectName, int width, int height) throws Exception {
    Map<String, Object> qrData = new HashMap<>();

    qrData.put("userId", userId);
    qrData.put("projectName", projectName);

    Gson gson = new Gson();
    String jsonString = gson.toJson(qrData);

    byte[] qrCodeImage = generateQRCodeImage(jsonString, width, height);
    ByteArrayInputStream inputStream = new ByteArrayInputStream(qrCodeImage);
    Image image = new Image(inputStream);
    ImageView imageView = new ImageView(image);

    return imageView;
 }
```

# Simplified class diagram

![Screenshot 2023-05-23 at 13 36 50](https://github.com/TomassSimko/Private-Movie-Collection/assets/72190589/3b9c8258-c1a9-4894-80f3-374cdc3db2b6)

# Database diagram

MSSQL Database diagram

<img width="1044" alt="Screenshot 2023-05-23 at 13 34 21" src="https://github.com/TomassSimko/Private-Movie-Collection/assets/72190589/f9591967-6285-47c1-89c5-be0cc09345f3">


## Application design patterns

- [x] Singleton pattern
- [x] Factory pattern
- [x] Strategy pattern
- [x] Inversion of control pattern
- [x] Model-view-controller pattern
- [x] Service-Repository pattern
- [x] Facade pattern
- [x] Builder pattern



## Licence

Distributed under the MIT License. See LICENSE for more information.

Team: Just me :) <br>
2023 SDE & SCO cs project posted here on GitHub. <br>
Hope you will like it! <br>
Thank you for your attention!
TTT :black_nib:

## Contact

Tomas Simko - [@twitter](https://twitter.com/TomasSimko_) -simko.t@email.cz - [@linkedIn](https://www.linkedin.com/in/tomas-simko/)

Project Link: 


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[contributors-shield]: https://contrib.rocks/image?repo=TomassSimko/Private-Movie-Collection

[contributors-url]: https://github.com/TomassSimko/Private-Movie-Collection/graphs/contributors

