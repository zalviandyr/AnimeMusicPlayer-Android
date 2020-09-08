package com.zukron.animemusicplayer.ui.activity

import android.animation.ObjectAnimator
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.*
import com.google.android.exoplayer2.util.Util
import com.google.android.material.snackbar.Snackbar
import com.triggertrap.seekarc.SeekArc
import com.zukron.animemusicplayer.R
import com.zukron.animemusicplayer.networking.InternetConnectionListener
import com.zukron.animemusicplayer.service.ExoBuilderBuilder
import com.zukron.animemusicplayer.service.Utilities.convertTime
import com.zukron.animemusicplayer.ui.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail_music.*
import java.io.File

/**
 * Project name is AnimeMusicPlayer
 * Created by Zukron Alviandy R on 9/7/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class DetailMusicActivity : AppCompatActivity(), Player.EventListener, View.OnClickListener, SeekArc.OnSeekArcChangeListener, InternetConnectionListener {
    companion object {
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var simpleExoPlayer: SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_music)

        // toolbar
        setSupportActionBar(tb_detail_music)

        // view model
        detailViewModel = ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory(application)
        ).get(DetailViewModel::class.java)

        val id = intent.getIntExtra(EXTRA_ID, 0)
        detailViewModel.setMusicId(id)

        detailViewModel.getMusicDetail.observe(this) {
            pb_detail_music.visibility = View.GONE

            tv_band_name_detail_music.text = it.bandName
            tv_title_detail_music.text = it.title
            tv_title_post_detail_music.text = it.titlePost
            Glide.with(this)
                    .load(it.image)
                    .placeholder(R.drawable.icons8_no_image_100)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(civ_cover_detail_music)
        }

        detailViewModel.setInternetConnectionListener(this)

        // exo player
        simpleExoPlayer = ExoBuilderBuilder.getInstance(this)
        prepareExoPlayer()
        simpleExoPlayer.addListener(this)

        // button listener
        civ_play_pause_detail_music.setOnClickListener(this)

        // seek arc listener
        sa_detail_music.setOnSeekArcChangeListener(this)

        // image rotation
        setImageRotation()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        // set button icon
        setButtonIcon(playWhenReady)

        if (playbackState == Player.STATE_READY) {
            val duration = simpleExoPlayer.duration
            tv_duration_detail_music.text = duration.convertTime()

            val handler = Handler(simpleExoPlayer.applicationLooper)
            val runnable = object : Runnable {
                override fun run() {
                    val curDuration = (simpleExoPlayer.duration - simpleExoPlayer.currentPosition).convertTime()
                    val progress = (simpleExoPlayer.currentPosition * 100.0) / simpleExoPlayer.duration

                    tv_duration_detail_music.text = curDuration
                    sa_detail_music.progress = progress.toInt()

                    handler.postDelayed(this, 1000)
                }
            }

            handler.postDelayed(runnable, 0)
        }
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        Snackbar.make(relative_layout, R.string.no_internet, Snackbar.LENGTH_INDEFINITE).show()
    }

    override fun onClick(view: View?) {
        simpleExoPlayer.playWhenReady = !simpleExoPlayer.playWhenReady
    }

    override fun onProgressChanged(seekArc: SeekArc?, i: Int, b: Boolean) {
        if (b) {
            val seekTo = (seekArc!!.progress * simpleExoPlayer.duration) / 100
            simpleExoPlayer.seekTo(seekTo)
        }
    }

    override fun onStartTrackingTouch(seekArc: SeekArc?) {
        simpleExoPlayer.playWhenReady = false
    }

    override fun onStopTrackingTouch(seekArc: SeekArc?) {
        simpleExoPlayer.playWhenReady = true
    }

    override fun onInternetUnavailable() {
        Snackbar.make(relative_layout, R.string.no_internet, Snackbar.LENGTH_SHORT)
                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                .show()
    }

    private fun setImageRotation() {
        // start rotation from current rotation
        // kenapa ditambah 360 ? karena semisal rotasi current ny sudah mendekati 360 contoh 270,
        // maka kecepatan ny akan berkurang maka ny harus ditambah 360 lagi
        val fromDegrees = civ_cover_detail_music.rotation
        val toDegrees = civ_cover_detail_music.rotation + 360

        // object animator
        val objectAnimator = ObjectAnimator.ofFloat(
                civ_cover_detail_music,
                View.ROTATION,
                fromDegrees,
                toDegrees
        )

        objectAnimator.duration = 25000 // 25 seconds
        objectAnimator.repeatCount = Animation.INFINITE
        objectAnimator.interpolator = LinearInterpolator()
        objectAnimator.start()

    }

    private fun prepareExoPlayer() {
        detailViewModel.getMusicDetail.observe(this) {
            val userAgent = Util.getUserAgent(this, getString(R.string.app_name))

            // specify cache folder
            val cacheFolder = File(applicationContext.cacheDir, "media")
            // specify cache size
            val cacheEvictor = LeastRecentlyUsedCacheEvictor(50 * 1024 * 1024) // 50mb
            // build cache
            val databaseProvider = ExoDatabaseProvider(this)
            val cache = SimpleCache(cacheFolder, cacheEvictor, databaseProvider)
            // Build data source factory with cache enabled, if data is available in cache it will return immediately, otherwise it will open a new connection to get the data.
            val cacheDataSourceFactory = CacheDataSourceFactory(cache, DefaultHttpDataSourceFactory(userAgent))

            val replaceWhiteSpace = it.url.replace("\\s+".toRegex(), "%20")
            val uri = Uri.parse(replaceWhiteSpace)
            val mediaSource = ProgressiveMediaSource
                    .Factory(cacheDataSourceFactory)
                    .createMediaSource(uri)

            simpleExoPlayer.prepare(mediaSource)
            simpleExoPlayer.playWhenReady = true
        }
    }

    private fun setButtonIcon(isPlay: Boolean) {
        if (isPlay) {
            civ_play_pause_detail_music.setImageResource(R.drawable.ic_baseline_pause_24)
        } else {
            civ_play_pause_detail_music.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }
    }
}