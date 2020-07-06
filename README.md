[![GitHub issues](https://img.shields.io/github/issues/xcesco/reversi-android.svg)](https://github.com/xcesco/reversi-android/issues)

<img src="https://github.com/xcesco/reversi-android/blob/master/docs/logo.png" alt="logo" width="200"/>

# Reversi for Android
This is the Android version of the project <a href="https://github.com/xcesco/reversi">fmt-reversi</a>. More information
on main project can be found at <a href="https://github.com/xcesco/reversi">reversi github</a> repository.

This client supports network games! It's possible to play with your friends.. everyone from his device!

## The game
There are different set of rules applied to Reversi. Our project take its from [Wikipedia](https://en.wikipedia.org/wiki/Reversi) 
and [Federazione Nazionale Gioco Othello](http://www.fngo.it/regole.asp).

## How to compile the project
To compile the project you need:

 - Java 8+
 - Android Studio
 
Use Android Studio github integration to clone and import the project.

## Store availability
Game is available on [Play store](https://play.google.com/store/apps/details?id=it.fmt.games.reversi.android)

## Some screnshoots
 Just a simple screenshot to view the result without install anything.
 
 <table>
 <row>
 <td><img src="https://github.com/xcesco/reversi-android/blob/master/docs/android_screenshot.png" alt="logo" width="300"/></td>
 </row>
 </table>

 ### How to get certifcate sha
How to extract shar1 from certificate in Android Studio
 https://stackoverflow.com/questions/27609442/how-to-get-the-sha-1-fingerprint-certificate-in-android-studio-for-debug-mode

 ## Used libraries
 - Retrofit: https://square.github.io/retrofit/
 - Okhttp: https://square.github.io/okhttp/
 - Websocket & STOMP protocol: https://github.com/NaikSoftware/StompProtocolAndroid
 - ViewBinding:
    - https://developer.android.com/topic/libraries/view-binding#java
    - https://stackoverflow.com/questions/60664346/what-is-the-right-way-of-android-view-binding-in-the-recyclerview-adapter-class
 - Daggger: https://github.com/google/dagger
 - Navigation component:
    - https://developer.android.com/guide/navigation/navigation-pass-data
    - https://www.raywenderlich.com/6014-the-navigation-architecture-component-tutorial-getting-started#toc-anchor-012
 - ModelView & Live data components: https://developer.android.com/topic/libraries/architecture/livedata
 - Timber:
    - https://levelup.gitconnected.com/timber-a-logging-library-for-android-56c431cd7300
    - https://github.com/JakeWharton/timber
    - https://github.com/JakeWharton/timber/blob/dd94a22/timber-sample/src/main/java/com/example/timber/ExampleApp.java
    - https://github.com/LachlanMcKee/timber-junit-rule
 - Firebase: https://firebase.google.com/docs/crashlytics/get-started?authuser=0&platform=android
