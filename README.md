# Gemini
[![jCenter](https://api.bintray.com/packages/haroncode/maven/gemini-core/images/download.svg)](https://bintray.com/haroncode/maven/gemini-core/_latestVersion)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![CircleCI](https://circleci.com/gh/HaronCode/Gemini.svg?style=svg)](https://circleci.com/gh/HaronCode/Gemini)

This library is designed to simplify build the MVI architecture in android.

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
- Android helper classes for connection:
```groovy
implementation "com.haroncode.gemini:gemini-android:${latest-version}"
```
- Kotlin dsl to create connection rules:
```groovy
implementation "com.haroncode.gemini:gemini-dsl:${latest-version}"
```
## Getting start
Now we're working for documentation and sample, but if you need sample right now you can find it here https://github.com/HaronCode/WordLearn

## Store working scheme

<img width="500 px" src="/doc/gemini_store_scheme.svg"/>

## Participants
+ idea, code and documentation - Zaltsman Nikita (@HaronCode)
+ review, code, writing unit tests - Kinayatov Dias (@kdk96)

## License
```
MIT License

Copyright (c) 2020 Nikita Zaltsman (@HaronCode) and Kinayatov Dias (@kdk96)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
