package com.zukron.animemusicplayer.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.triggertrap.seekarc.SeekArc;
import com.zukron.animemusicplayer.R;
import com.zukron.animemusicplayer.model.DetailMusic;
import com.zukron.animemusicplayer.model.Music;
import com.zukron.animemusicplayer.networking.APIDetailMusic;
import com.zukron.animemusicplayer.service.MusicPlayer;

import java.util.Locale;


public class DetailMusicActivity extends AppCompatActivity implements APIDetailMusic.OnResponse, View.OnClickListener, SeekArc.OnSeekArcChangeListener {
    private CircularImageView civCoverDetailMusic, civPlayPauseDetailMusic;
    private SeekArc saDetailMusic;
    private TextView tvDurationDetailMusic, tvBandNameDetailMusic, tvTitleDetailMusic, tvTitlePostDetailMusic;
    private Toolbar toolbar;
    private ProgressBar pbDetailMusic;
    private APIDetailMusic apiDetailMusic;
    private MediaPlayer mediaPlayer;
    private ObjectAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_music);

        apiDetailMusic = new APIDetailMusic(this, this);

        toolbar = findViewById(R.id.tb_detail_music);
        civCoverDetailMusic = findViewById(R.id.civ_cover_detail_music);
        civPlayPauseDetailMusic = findViewById(R.id.civ_play_pause_detail_music);
        saDetailMusic = findViewById(R.id.sa_detail_music);
        tvDurationDetailMusic = findViewById(R.id.tv_duration_detail_music);
        tvBandNameDetailMusic = findViewById(R.id.tv_band_name_detail_music);
        tvTitleDetailMusic = findViewById(R.id.tv_title_detail_music);
        tvTitlePostDetailMusic = findViewById(R.id.tv_title_post_detail_music);
        pbDetailMusic = findViewById(R.id.pb_detail_music);

        init();
        setData();
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        civPlayPauseDetailMusic.setOnClickListener(this);
        saDetailMusic.setOnSeekArcChangeListener(this);
    }

    private void initMusic(String dataSource) {
        mediaPlayer = MusicPlayer.init(dataSource);

        // ubah logo ketika musik berjalan dan ubah max seek bar
        civPlayPauseDetailMusic.setImageResource(R.drawable.ic_baseline_pause_24);
    }

    private void startRotation(float fromDegrees, float toDegrees) {
        // set rotation
        // View.Rotation is android:rotation
        animator = ObjectAnimator.ofFloat(civCoverDetailMusic, View.ROTATION, fromDegrees, toDegrees);
        animator.setDuration(10000);
        animator.setRepeatCount(Animation.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    private void startSeekBarThread() {
        // use runOnUiThread to avoid this error on SeekBar
        // android.view.ViewRoot$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
        // sleep untuk membatasi eksekusi thread
        Thread seekBarThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (mediaPlayer.isPlaying()) {
                        // seek bar and duration
                        synchronized (this) {
                            wait(1000);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    double progress = ((double) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100;
                                    long minutes = (mediaPlayer.getCurrentPosition() / 1000) / 60;
                                    int seconds = (mediaPlayer.getCurrentPosition() / 1000) % 60;
                                    String duration = String.format(Locale.US, "%02d:%02d", minutes, seconds);

                                    saDetailMusic.setProgress((int) progress);
                                    tvDurationDetailMusic.setText(duration);
                                }
                            });
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        seekBarThread.start();
    }

    private void setData() {
        Music music = getIntent().getParcelableExtra("music");

        if (music != null) {
            tvBandNameDetailMusic.setText(music.getBandName());
            tvTitleDetailMusic.setText(music.getTitle());

            apiDetailMusic.getDetailMusic(music.getId());
        }
    }

    @Override
    public void onClick(View view) {
        if (mediaPlayer != null) {
            // jika state play maka ubah ke pause
            if (mediaPlayer.isPlaying()) {
                pauseMusic();
            } else {
                playMusic();
            }
        }
    }

    void pauseMusic() {
        civPlayPauseDetailMusic.setImageResource(R.drawable.ic_baseline_play_arrow_24);

        mediaPlayer.pause();
        animator.pause();
    }

    void playMusic() {
        civPlayPauseDetailMusic.setImageResource(R.drawable.ic_baseline_pause_24);

        mediaPlayer.start();
        startSeekBarThread();

        // start rotation from current rotation
        // kenapa ditambah 360 ? karena semisal rotasi current ny sudah mendekati 360 contoh 270,
        // maka kecepatan ny akan berkurang maka ny harus ditambah 360 lagi
        startRotation(civCoverDetailMusic.getRotation(), (civCoverDetailMusic.getRotation() + 360));
    }

    @Override
    public void detailMusicResponse(DetailMusic detailMusic) {
        pbDetailMusic.setVisibility(View.INVISIBLE);

        tvTitlePostDetailMusic.setText(detailMusic.getTitlePost());
        Glide.with(this)
                .load(detailMusic.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(civCoverDetailMusic);

        initMusic(detailMusic.getLinkMp3());
        startRotation(0, 360);
        startSeekBarThread();
    }

    @Override
    public void onStartTrackingTouch(SeekArc seekArc) {
        pauseMusic();
    }

    @Override
    public void onStopTrackingTouch(SeekArc seekArc) {
        // seek to
        double seekTo = ((double) seekArc.getProgress() * mediaPlayer.getDuration()) / 100;

        tvDurationDetailMusic.setText(getResources().getText(R.string.init_duration));
        mediaPlayer.seekTo((int) seekTo);
        playMusic();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void errorResponse(VolleyError error) {
        error.printStackTrace();
    }

    @Override
    public void onProgressChanged(SeekArc seekArc, int i, boolean b) {

    }
}