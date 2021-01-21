# What Is The Distance? [![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-white.svg)](https://sonarcloud.io/dashboard?id=Harnet69_WhatIsTheDistance)

## App purposes:
![QR](https://github.com/Harnet69/WhatIsTheDistance/blob/master/app/GitHubFiles/app_ico_sm.png)
 - measuring the distance between two rail stations
 - working offline
 - showing a list of all stations

## Technologies
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
 - SharedPreferences as local storage of application settings
 - Coroutines as substitution of deprecated AsyncTask
 - SonarCloud service as code quality manager

## Measure fragment
 - get data for autoComplete predictions from third part APIs
 - check Internet connection for "offline" mode
 - "update" timer(2 minutes by default) controls sources of data retrieval(switch between API and local database) 
 - measure the distance between two stations(if its coordinates exist) 
 - input text field is autoCompleted by data from "keywords" API

## Stations list
 - shows all stations info from "stations" API endpoint, sorted by its id
 - contains progress bar and load error message label
