# README

## Quick start if have secrets.properties and scom cc-android-sdk-1.6.0.aar

this files are outside repo, to fast startup copy below files and sync/build project to generate BuildConfig

./solidarynetwork-mobile-flutter-scom-citizencard-starter/android/secrets.properties
./solidarynetwork-mobile-flutter-scom-citizencard-starter/android/app/libs/cc-android-sdk-1.6.0.aar

BuildConfig is generated when build project
String encodedLicense = network.solidary.mobile.BuildConfig.encodedLicense;

sync project, and use in project with, and build
