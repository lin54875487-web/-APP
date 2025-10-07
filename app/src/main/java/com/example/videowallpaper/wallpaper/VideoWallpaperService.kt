package com.example.videowallpaper.wallpaper

import android.media.MediaPlayer
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import com.example.videowallpaper.data.WallpaperPreferences

class VideoWallpaperService : WallpaperService() {

    override fun onCreateEngine(): Engine = VideoWallpaperEngine()

    private inner class VideoWallpaperEngine : Engine(),
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

        private var mediaPlayer: MediaPlayer? = null
        private var surfaceHolder: SurfaceHolder? = null

        override fun onSurfaceCreated(holder: SurfaceHolder) {
            super.onSurfaceCreated(holder)
            surfaceHolder = holder
            playVideo()
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            surfaceHolder = null
            releasePlayer()
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            if (visible) {
                mediaPlayer?.start() ?: playVideo()
            } else {
                mediaPlayer?.pause()
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            releasePlayer()
        }

        private fun playVideo() {
            val holder = surfaceHolder ?: return
            val uri = WallpaperPreferences.getVideoUri(applicationContext) ?: return

            releasePlayer()

            val player = MediaPlayer()
            mediaPlayer = player
            try {
                player.setSurface(holder.surface)
                player.setOnPreparedListener(this)
                player.setOnErrorListener(this)
                player.setOnCompletionListener(this)
                player.setVolume(0f, 0f)
                player.setDataSource(applicationContext, uri)
                player.prepareAsync()
            } catch (error: Exception) {
                releasePlayer()
            }
        }

        private fun releasePlayer() {
            mediaPlayer?.setSurface(null)
            mediaPlayer?.setOnPreparedListener(null)
            mediaPlayer?.setOnErrorListener(null)
            mediaPlayer?.setOnCompletionListener(null)
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
        }

        override fun onPrepared(mp: MediaPlayer) {
            mp.isLooping = true
            mp.start()
        }

        override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
            releasePlayer()
            return true
        }

        override fun onCompletion(mp: MediaPlayer) {
            mp.start()
        }
    }
}
