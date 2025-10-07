package com.example.videowallpaper.data

import android.content.Context
import android.net.Uri

object WallpaperPreferences {
    private const val PREFS_NAME = "video_wallpaper"
    private const val KEY_URI = "video_uri"

    fun saveVideoUri(context: Context, uri: Uri) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_URI, uri.toString())
            .apply()
    }

    fun getVideoUri(context: Context): Uri? {
        val uriValue = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_URI, null)
        return uriValue?.let(Uri::parse)
    }
}
