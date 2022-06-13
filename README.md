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

## Screenshots
### While Charging
![image](https://user-images.githubusercontent.com/35669235/173282400-f93a2aac-9461-4a92-b677-f470289a78ce.png)

### While on Battery Power
![image](https://user-images.githubusercontent.com/35669235/173282195-63ef904c-8b10-4ac0-87b4-2e2ade209a48.png)


