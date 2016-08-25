# CircularImageView

[![Release](https://jitpack.io/v/manjunathc23/CircularImageView.svg)](https://jitpack.io/#manjunathc23/CircularImageView)
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
![SDK](https://img.shields.io/badge/SDK-15%2B-green.svg)
![Release](https://img.shields.io/badge/release-v1.0.4-green.svg)
[![Build Status](https://travis-ci.org/manjunathc23/CircularImageView.svg?branch=master)](https://api.travis-ci.org/manjunathc23/CircularImageView)
[![CircleCI](https://circleci.com/gh/manjunathc23/CircularImageView.svg?style=svg)](https://circleci.com/gh/manjunathc23/CircularImageView)

--

![pjimage](https://cloud.githubusercontent.com/assets/1502341/17903751/376af04e-6932-11e6-9ca3-6a5766e639b6.jpeg)

Usage
-----
```xml
    <com.github.manjunathc23.views.CircularImageView
        android:id="@+id/image_view_1"
        android:layout_width="160dp"
        android:layout_height="160dp"
        android:layout_gravity="center"
        android:layout_margin="@dimen/default_6x_padding_margin"
        app:civ_border_color="@color/zeta_white"
        app:civ_border_width="@dimen/circular_image_view_boarder" />
```

## Install

You can install using gradle:

```
	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```

```
	dependencies {
	        compile 'com.github.manjunathc23:CircularImageView:v1.0.0'
	}
	
```

License
-------

    Copyright Manjunath Chandrashekar 2016

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
