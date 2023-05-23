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

![final_mock](https://github.com/TomassSimko/wuav-desktop-client/assets/72190589/6c338654-4584-4588-847a-99f92821495e)
<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://user-images.githubusercontent.com/72190589/207565741-1867a0a5-7bd6-46c8-a985-6e0ac64e4cac.png">

  </a>
  <p align="center">
    WUAV Desktop Management Application
    <br />
    <a href="https://github.com/TomassSimko/Private-Movie-Collection"><strong>Explore the docs »</strong></a>
    <br />
  </p>


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

<!-- ABOUT THE PROJECT -->

# Wuav Management Application


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

