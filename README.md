[![GitHub issues](https://img.shields.io/github/issues/xcesco/reversi-android.svg)](https://github.com/xcesco/reversi-android/issues)

<img src="https://github.com/xcesco/reversi-android/blob/master/docs/logo.png" alt="logo" width="200"/>

# Reversi for Android
This is the Android version of the project <a href="https://github.com/xcesco/reversi">fmt-reversi</a>. More information
on main project can be found at <a href="https://github.com/xcesco/reversi">reversi github</a> repository. 

## The game
There are different set of rules applied to Reversi. Our project take its from [Wikipedia](https://en.wikipedia.org/wiki/Reversi) 
and [Federazione Nazionale Gioco Othello](http://www.fngo.it/regole.asp).

## How to compile the project
To compile the project you need:

 - Java 8+
 - Android Studio
 
Use Android Studio github integration to clone and import the project.

## Used libraries and frameworks
In addition to the standard libraries, I used the following libraries in this project:

 - [butterknife](https://jakewharton.github.io/butterknife/)

## Store availability
Game is available on [Play store](https://play.google.com/store/apps/details?id=it.fmt.games.reversi.android)

## Some screnshoots
 Just a simple screenshot to view the result without install anything.
 
 <table>
 <row>
 <td><img src="https://github.com/xcesco/reversi-android/blob/master/docs/android_screenshot.png" alt="logo" width="300"/></td>
 </row>
 </table>

 ## Used libraries
 - https://square.github.io/retrofit/
 - https://square.github.io/okhttp/
 dagger

 ### Scarlet
 https://proandroiddev.com/websockets-with-scarlet-7d3e2235dc03

 https://github.com/NaikSoftware/StompProtocolAndroid
 https://stackoverflow.com/questions/41675194/is-it-possible-to-use-stomp-with-okhttp3-websocket

 ## Google Game Service
 https://developers.google.com/games/services/console/enabling

 886691898919-ck0lcchsjt5s90ovr1a3g21afbehqvle.apps.googleusercontent.com

### How to get certifcate sha
https://stackoverflow.com/questions/27609442/how-to-get-the-sha-1-fingerprint-certificate-in-android-studio-for-debug-mode
debug
B1:B0:5B:41:EA:08:48:14:2E:A8:D4:AC:F9:17:F0:A8:04:4B:2E:07
release
42:01:E6:E7:B1:29:63:E0:B0:50:27:87:41:BC:C2:79:E8:D6:61:A7

 ### Firebase Analytics
 ### Firebase Crashlytics
- https://firebase.google.com/docs/crashlytics/get-started?authuser=0&platform=android

#ViewBinding
- https://developer.android.com/topic/libraries/view-binding#java
- https://stackoverflow.com/questions/60664346/what-is-the-right-way-of-android-view-binding-in-the-recyclerview-adapter-class

 ## Logger
 Timber
 - https://levelup.gitconnected.com/timber-a-logging-library-for-android-56c431cd7300
 - https://github.com/JakeWharton/timber
 - https://github.com/JakeWharton/timber/blob/dd94a22/timber-sample/src/main/java/com/example/timber/ExampleApp.java

Timber on JUNIT
- https://github.com/LachlanMcKee/timber-junit-rule