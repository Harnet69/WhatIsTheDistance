# ![App icon](https://github.com/Harnet69/WhatIsTheDistance/blob/master/app/GitHubFiles/app_ico_sm.png)What Is The Distance? 

## App purposes: 
 - measuring the distance between two rail stations(if its coordinates are known)
 - possibility to work offline
 - showing a list of all stations
## Application installation:
- scan this QR code by an Android phone, download and install the app 
![QR](https://github.com/Harnet69/WhatIsTheDistance/blob/master/app/GitHubFiles/app_QR.png)
- download .apk [WhatIsTheDistance v.1.1 installer](https://drive.google.com/file/d/1katsLEg4kYnWXLj2uN_Dy3eVHuS7BUzy/view?usp=sharing) and run it on Android phone
- clone a project code from this repo to your computer and run it via Android studio or another Android emulator

## Technologies [![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-white.svg)](https://sonarcloud.io/dashboard?id=Harnet69_WhatIsTheDistance)
 - Kotlin as a programming language
 - Google material design as UI pattern
 - MVVM as code architecture pattern
 - Android JetPack library as a contemporary development tool
 - Navigation as a switcher between fragments
 - Fragments as MVVM part
 - DataBinding as OOP principle in frontend
 - Retrofit as a manager of interactions with APIs 
 - SQLite database as local storage
 - Room as a mapper of applications classes to a local database
 - Dagger2 as dependency injection manager
 - SharedPreferences as local storage of application settings
 - Coroutines as substitution of deprecated AsyncTask
 - SonarCloud service as code quality manager
 - LeakCanary as memory leaks checking tool

## Measure fragment
 - get data for autoComplete predictions from third part APIs
 - check Internet connection for "offline" mode
 - "update" timer(astablished to 2 minutes by default) controls sources of data retrieval(switch between API and local database) 
 - measure the distance between two stations(if its coordinates exist) 
 - input text field is autoCompleted by data from "keywords" API
 - click on non input field hide a keyboard  

## Stations list
 - shows all stations info from "stations" API endpoint, sorted by its id
 - swiper for list updating
 - contains progress bar and load error message label
