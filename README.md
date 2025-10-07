# 循環動態桌布（Looping Video Wallpaper）

這是一款 Android 應用程式，協助你將手機內的影片設定為可循環播放的動態桌布。使用者可以從裝置或雲端儲存空間選擇影片，立即預覽效果，並開啟系統的桌布設定介面完成套用。

## 功能特色

- 📁 選擇任何支援的影片檔作為桌布來源（透過系統檔案選擇器，支援雲端與本機檔案）。
- 🔁 影片會在桌布上自動循環播放，並自動靜音避免干擾。
- 👀 內建預覽播放器，選擇影片後立即檢視效果。
- 🔒 使用 `takePersistableUriPermission` 儲存影片授權，下次開啟仍可直接使用。

## 專案結構

```
- app
  ├── src/main
  │   ├── AndroidManifest.xml              # 活動與動態桌布服務宣告
  │   ├── java/com/example/videowallpaper
  │   │   ├── MainActivity.kt              # 影片選擇與預覽邏輯
  │   │   ├── data/WallpaperPreferences.kt # 儲存影片 URI 的偏好設定
  │   │   └── wallpaper/VideoWallpaperService.kt # 以 MediaPlayer 播放桌布影片
  │   └── res
  │       ├── layout/activity_main.xml     # UI 佈局
  │       ├── values/strings.xml, colors.xml
  │       └── xml/video_wallpaper.xml      # 桌布服務描述
  └── build.gradle                         # 模組設定
- build.gradle                             # 專案層級設定
- settings.gradle                          # 模組註冊
```

## 建置與執行

1. 以 Android Studio (Giraffe 以上) 開啟專案資料夾。
2. 讓 IDE 自動下載相依套件並同步 Gradle。
3. 將應用程式部署至 Android 7.0 (API 24) 以上的裝置。
4. 在應用程式中選擇影片並依提示進入桌布設定流程。

> **提示**：如果選擇的是雲端檔案（如 Google Drive），請確保已完成離線下載或擁有穩定的網路，以便桌布服務能夠持續存取影片。

## 注意事項

- 由於系統權限限制，應用程式僅會要求必要的檔案存取授權，並透過持久化 URI 保留讀取權限。
- 影片播放採用 `MediaPlayer`，若影片格式不被裝置支援，可能無法播放。
- 若需要重設桌布為靜態圖片，可回到系統桌布設定或移除應用程式所授予的影片權限。

## 授權

本專案以 MIT 授權釋出，歡迎自由使用與修改。
