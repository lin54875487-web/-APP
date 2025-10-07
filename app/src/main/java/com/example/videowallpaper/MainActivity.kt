package com.example.videowallpaper

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.example.videowallpaper.data.WallpaperPreferences
import com.example.videowallpaper.wallpaper.VideoWallpaperService
import com.google.android.material.button.MaterialButton

class MainActivity : ComponentActivity() {

    private lateinit var previewView: VideoView
    private lateinit var selectButton: MaterialButton
    private lateinit var applyButton: MaterialButton

    private val openDocument = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let { persistAndPreviewVideo(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        previewView = findViewById(R.id.videoPreview)
        selectButton = findViewById(R.id.selectVideoButton)
        applyButton = findViewById(R.id.setWallpaperButton)

        selectButton.setOnClickListener { selectVideo() }
        applyButton.setOnClickListener { openWallpaperSettings() }

        WallpaperPreferences.getVideoUri(this)?.let { uri ->
            previewVideo(uri)
        }
    }

    override fun onStop() {
        super.onStop()
        if (previewView.isPlaying) {
            previewView.pause()
        }
    }

    private fun selectVideo() {
        openDocument.launch(arrayOf("video/*"))
    }

    private fun persistAndPreviewVideo(uri: Uri) {
        try {
            releasePersistedPermissions()
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            WallpaperPreferences.saveVideoUri(this, uri)
            previewVideo(uri)
            Toast.makeText(this, R.string.video_ready, Toast.LENGTH_SHORT).show()
        } catch (error: SecurityException) {
            Toast.makeText(this, R.string.video_persist_error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun releasePersistedPermissions() {
        contentResolver.persistedUriPermissions.forEach { permission ->
            try {
                contentResolver.releasePersistableUriPermission(
                    permission.uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) {
                // Ignore failures when permission was already revoked.
            }
        }
    }

    private fun previewVideo(uri: Uri) {
        previewView.stopPlayback()
        previewView.setVideoURI(uri)
        previewView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
            previewView.start()
        }
        previewView.setOnErrorListener { _, _, _ ->
            Toast.makeText(this, R.string.video_persist_error, Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun openWallpaperSettings() {
        val savedUri = WallpaperPreferences.getVideoUri(this)
        if (savedUri == null) {
            Toast.makeText(this, R.string.video_not_selected, Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(this, R.string.video_set_instructions, Toast.LENGTH_LONG).show()
        val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
            putExtra(
                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                ComponentName(this@MainActivity, VideoWallpaperService::class.java)
            )
        }
        startActivity(intent)
    }
}
