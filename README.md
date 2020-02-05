# Gemini

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
