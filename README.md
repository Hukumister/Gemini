# Gemini
[![jCenter](https://api.bintray.com/packages/haroncode/maven/gemini-core/images/download.svg)](https://bintray.com/haroncode/maven/gemini-core/_latestVersion)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)  

This library is designed to simplify build the mvi architecture (a link to the articles by Hanes Dormann 
http://hannesdorfmann.com/android/mosby3-mvi-1), in android.

## Installing
Available through bintray.com.

Add the maven repo to your root `build.gradle`

```groovy
allprojects {
    repositories {
        maven { url 'https://dl.bintray.com/haroncode/maven' }
    }
}
```

Add the dependencies:
- Core classes:
```groovy
implementation "com.haroncode.gemini:gemini-core:${latest-version}"
```
