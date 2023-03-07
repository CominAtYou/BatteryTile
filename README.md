# BatteryTile
An Android Quick Settings tile that displays the current battery state and percentage, and a long-press action to open the battery settings.


## Reasoning
The "Battery Saver" tile in Android 12 in below, when long-pressed, would open the battery settings page in Android Settings. With the introduction of Android 13,
it no longer presents the battery settings page. Instead, it now opens the Battery Saver settings, removing this very useful shortcut. This app aims to restore said
functionality with a custom Quick Settings tile.

## Compatibility
This app is compatible with API 29+ devices (Android 10 or higher), as Quick Settings tile subtitles were added in that release. 

Theoretically, compatibility could be extended to cover API 24+ as well (Android 7 and up) if references to 
[Tile#setSubtitle](https://developer.android.com/reference/android/service/quicksettings/Tile#setSubtitle(java.lang.CharSequence))
were removed. However, this would defeat one of the purposes of the tile, but the option to do so is available to anyone who wishes to compile it themselves.

## Download

[<img src="https://gitlab.com/IzzyOnDroid/repo/-/raw/master/assets/IzzyOnDroid.png"
     alt="Get it on IzzyOnDroid"
     height="80">](https://apt.izzysoft.de/fdroid/index/apk/com.cominatyou.batterytile)

[Download the latest APK from the Releases Section](https://github.com/CominAtYou/BatteryTile/releases/latest)

## Screenshots
### While Charging
![](https://user-images.githubusercontent.com/35669235/214475667-29f97f1c-0731-41e6-9af4-e2966f61d47c.png)

### While on Battery Power
![](https://user-images.githubusercontent.com/35669235/214475766-8a7b21dc-36e5-47ee-8939-1a4fcbab132e.png)
