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


<div>
  <img width="700" alt="Screenshot 1" src="https://github.com/TomassSimko/Private-Movie-Collection/assets/72190589/6234f48d-15b6-45f9-8d74-7d54ac701737">
  <br></br>
<img width="700" alt="Screenshot 2" src="https://github.com/TomassSimko/Private-Movie-Collection/assets/72190589/840e3a78-cce1-469f-9258-241f34826ae7">
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
      <a href="#">Fundaments</a>
      <ul>
        <li><a href="#tech-stack">Tech stack</a></li>
        <li><a href="#core-management">Core management</a></li>
        <li><a href="#email-management">Email management</a></li>
        <li><a href="#pdf-management">PDF management</a></li>
        <li><a href="#http-request-management">HTTP request management</a></li>
        <li><a href="#unit-testing">Unit testing</a></li>
        <li><a href="#password-management">Password management</a></li>
        <li><a href="#style">Style</a></li>
        <li><a href="#style">Resources used for the project design </a></li>
      </ul>
    </li>
    <li>
        <a href="#">About the project</a>
    <ul>
       <li><a href="#color-pallet">Color pallet</a></li>
       <li><a href="#user-interface-desktop-design">User interface desktop design</a></li>
       <li><a href="#user-interface-swift-design">User interface SWIFT design</a></li>
      </ul>
    </li>
 <li>
        <a href="#">Features and requirements</a>
    <ul>
       <li><a href="#pdf-generation">PDF generation</a></li>
       <li><a href="#email-templates">Email templates</a></li>
       <li><a href="#language-model-implementation">Language model implementation</a></li>
       <li><a href="#qr-code-generation">QR code generation</a></li>
      </ul>
    </li>
<li>
        <a href="#">Architecture</a>
    <ul>
       <li><a href="#project-class-diagram">Project Class diagram</a></li>
       <li><a href="#database-diagram">Database diagram</a></li>
      </ul>
    </li>
       <li><a href="#application-design-patterns">Application design patterns</a></li>
 <li>
        <a href="#">Initialization steps</a>
    <ul>
       <li><a href="#java-desktop">Java desktop</a></li>
       <li><a href="#minimal-rest-api-c-sharp">Minimal RestAPI C#</a></li>
       <li><a href="#swift-application">SWIFT application</a></li>
      </ul>
    </li>
 <li><a href="#license">License</a></li>
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
* [DiceBear](https://avatars.dicebear.com/)
* [GmailAPI-video-that-helped-me](https://www.youtube.com/watch?v=xtZI23hxetw&ab_channel=SebastianDaschner)
<!-- ABOUT THE PROJECT -->


## About The Project

## Figma file preview 

[Figma preview ](https://www.figma.com/file/CTTtzNBrhDf45AX4cuBWF0/Wuav-app?type=design&node-id=553%3A5918&t=APpocZkOaKy2m3Wb-1)
## Color pallet 
This is the primary color pallet for the overall product design

<img width="1112" src="https://github.com/TomassSimko/Private-Movie-Collection/assets/72190589/cd36ce8c-b9d5-4cf4-9729-a0861dac0381">

## User interface desktop design

<div style="display: grid; grid-template-columns: repeat(3, 1fr); grid-gap: 5px;">
  <div>
    <img src="https://github.com/TomassSimko/Private-Movie-Collection/assets/72190589/119d84ef-9df3-4782-b3bf-e64ed7f63900" alt="Login page" width="400">
  </div>
  <div>
    <img src="https://github.com/TomassSimko/Private-Movie-Collection/assets/72190589/8f4bc390-18ac-45ba-b2a4-5f27092e4f0b" alt="Dashboard page" width="400">
  </div>
  <div>
    <img src="https://github.com/TomassSimko/wuav-app/assets/72190589/6f3a87dd-527d-426a-9f08-23cedee21b53" alt="User profile" width="400">
  </div>
  <div>
    <img src="https://github.com/TomassSimko/Private-Movie-Collection/assets/72190589/b08e8ead-1ed6-471e-a2b1-dfc21ea55077" alt="Project creation stepper" width="400">
  </div>
  <div>
    <img src="https://github.com/TomassSimko/wuav-desktop-client/assets/72190589/7b2782fd-53e1-4ed7-ab2f-e17d5abc892f" alt="Project details Tab 1" width="400">
  </div>
  <div>
    <img src="https://github.com/TomassSimko/wuav-desktop-client/assets/72190589/f57a637e-c056-487a-8e9e-8ed41057a610" alt="Project details Tab 2" width="400">
  </div>
  <div>
    <img src="https://github.com/TomassSimko/wuav-desktop-client/assets/72190589/9999cd2f-77cb-4191-ad06-bff73e0043ba" alt="Project details Edit project" width="400">
  </div>
</div>


## User interface SWIFT design

<div style="display: flex;">
  <img width="250" alt="Screenshot 1" src="https://github.com/TomassSimko/Private-Movie-Collection/assets/72190589/e7d436d3-f373-4db9-8058-17e7adb4664b">
  <img width="250" alt="Screenshot 2" src="https://github.com/TomassSimko/Private-Movie-Collection/assets/72190589/4266d941-ed91-48ab-9336-4e2b8313387f">
  <img width="250" alt="Screenshot 3" src="https://github.com/TomassSimko/Private-Movie-Collection/assets/72190589/224331c7-6806-44dd-a50b-77966462efb3">
</div>

# Features and requirements

## PDF generation
One of the requirements was to be able to generate pdf files from the data that is stored in the database. I have used the ItextPDF library to generate the pdf files. 
This is the quick result of generated pdf file.

<div style="display: flex;">
  <img width="400" alt="Screenshot 1" src="https://github.com/TomassSimko/wuav-desktop-client/assets/72190589/36d26d2f-fe25-4739-a322-73942c77711e">
  <img width="400" alt="Screenshot 2" src="https://github.com/TomassSimko/wuav-desktop-client/assets/72190589/28fd1389-01e8-460b-9fe7-9cdecb496da2">
</div>


## Email templates 
Application is able to send emails to the users. 

I have used the Google API to send emails. I have used the Thymeleaf library to generate the email templates.
Build a template for the email that is sent to the user. This is the result of the email template. They are different types of the emails

Installation confirmation,password reset email and email with password when new user is created and password is generated.

<div >
  <img alt="Screenshot 1" src="https://github.com/TomassSimko/wuav-desktop-client/assets/72190589/3026bf80-f018-4516-be64-c76a59e2abf6">
  <img  alt="Screenshot 2" src="https://github.com/TomassSimko/wuav-desktop-client/assets/72190589/c1b69fc6-c0da-4a33-9cee-81d729b99a3e">
</div>


## Language model implementation
I have added an extra feature to the device creation where Technicians can create devices and in case of the emergency they can go and ask relevant question to the API that I have called as a Wuav chatbot. 
This is the result of the chatbot while creating the devices to the project.

I have used this free API to create the chatbot.

https://free.churchless.tech/v1/chat/completions

<div style="display: flex;">
  <img  alt="Screenshot 1" src="https://github.com/TomassSimko/wuav-desktop-client/assets/72190589/0e53daad-9235-4692-a450-794553f48d22">
</div>

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

# Architecture

# Project Class diagram

![Screenshot 2023-05-23 at 13 36 50](https://github.com/TomassSimko/Private-Movie-Collection/assets/72190589/3b9c8258-c1a9-4894-80f3-374cdc3db2b6)

# Database diagram

MSSQL Database diagram

<img width="1044" alt="Screenshot 2023-05-23 at 13 34 21" src="https://github.com/TomassSimko/Private-Movie-Collection/assets/72190589/f9591967-6285-47c1-89c5-be0cc09345f3">


# Application design patterns

- [x] Singleton pattern
- [x] Factory pattern
- [x] Strategy pattern
- [x] Inversion of control pattern
- [x] Model-view-controller pattern
- [x] Service-Repository pattern
- [x] Facade pattern
- [x] Builder pattern

# Initialization steps 
These steps are for academy purpose only. You can still go and play with the code however it won't run without the database and the server or the credentials.

Credentials: 
- [x] Users emails
  - User - technician: tech@hotmail.com
  - User - admin: admin@hotmail.com
  - User - product manager: pm@hotmail.com
  - User - sales: sales@hotmail.com
- [x] Passwords
  - Password for all users: 123456

Note: Every new created user by admin in the system will receive email (to new user email) with the autogenerated password.

Passwords cannot be changed by the users themselves. Only admin can recover the password for the user by sending the email with the new autogenerated password. 
### Java desktop

1. Clone the repository 
   ```sh
   git clone https://github.com/TomassSimko/wuav-desktop-client
    ```
   or
    ```sh
   Open the provided zip file with the project
   ```

2. Open the project in your IDE
3. Run Maven install
4. Make sure you include .cfg and .json token files in the resources root folder (for academy!)
5. Make sure that root folder includes tokens folder with credentials for OAuth (Gmail)
6. VM options for the application
   ```sh
   --add-modules jdk.httpserver --add-exports=jdk.internal.httpserver/jdk.internal.http
   ```
   or
   1. Navigate to run configurations
   2. Edit configuration modify options add VM options
   3. Add VM option
    ```sh
    --add-modules jdk.httpserver --add-exports=jdk.internal.httpserver/jdk.internal.http
    ```
7. Add ENV paths 
    ```sh
    CONFIG_BLOB = {$path to the config file}
    GMAIL_PATH= {$path to the gmail config file}
    ```
8. Run the application

### Minimal RestAPI C#

1. Clone the repository 
   ```sh
   git clone https://github.com/TomassSimko/wuav-api
    ```
2. Make sure you have installed .NET SDK
3. Open the project in your IDE
4. Run the application
    ```sh
    dotnet watch run
    ```

### SWIFT application
Normally I would use .plist file to store the API url however for the academy purpose I have decided to use the port number as a variable that can be changed in the code.

1. Clone the repository 
   ```sh
   git clone https://github.com/TomassSimko/wuav-app
    ```
2. Open the project in XCode IDE
3. Enter correct port number for the server in UploadView.swift file
    ``` swift
   @State private var portAPI: String = "http://{$port_number}:5000"
   ```
   Note: this {$port_number} has to be the same port number as the one you are running the simulator on (your IP address) otherwise it won't work with running API. 
4. Run the application on your device (Require setting up the developer account etc...)

## Licence

Distributed under the MIT License. See LICENSE for more information.

I have created this project for learning purposes and for exam project with passion and having lots of fun !

If by any change you will find any of the code useful for you please do feel free to use it or distribute it.

Team: Just me :) <br>
2023 SDE & SCO cs project posted here on GitHub. <br>
Hope you will like it! <br>
Thank you for your time and attention!
TTT :black_nib:

## Contact

Tomas Simko - [@twitter](https://twitter.com/TomasSimko_) -simko.t@email.cz - [@linkedIn](https://www.linkedin.com/in/tomas-simko/)

Project Link: https://github.com/TomassSimko/wuav-desktop-client


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->


