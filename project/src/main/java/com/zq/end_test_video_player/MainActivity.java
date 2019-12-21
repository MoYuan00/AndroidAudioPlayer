package com.zq.end_test_video_player;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.*;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.*;

import java.util.*;

public class MainActivity extends AppCompatActivity {
    // 用来获取音乐的相关信息

    private static final String TAG = "MainActivity";
    private TextView textViewNowPro;
    private TextView textViewAllPro;
    private ImageView imageViewArtist;
    private TextView textViewArtist;
    private TextView textViewAlbum;
    private TextView textViewTitle;
    private ImageView imageViewReturn;

    Button btnPlay;
    SeekBar seekBar;
    private Song song;
    private boolean isChanging = false;
    ObjectAnimator animator;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getSupportActionBar() != null)
            getSupportActionBar().hide();
        // 获取传来的歌曲
        song = (Song) getIntent().getExtras().getSerializable("song");
        textViewNowPro = findViewById(R.id.textViewNowPro);
        textViewAllPro = findViewById(R.id.textViewAllPro);
        textViewAlbum = findViewById(R.id.textViewAlbum);
        textViewArtist = findViewById(R.id.textViewArtist);
        imageViewArtist = findViewById(R.id.imageViewArtist);
        textViewTitle = findViewById(R.id.textViewTitle);
        btnPlay = findViewById(R.id.buttonPlay);
        seekBar = findViewById(R.id.seekBar);
        imageViewReturn = findViewById(R.id.imageViewReturn);
        // 设置旋转动画
        animator = ObjectAnimator.ofFloat(imageViewArtist, "rotation", 0f, 360f);
        animator.setDuration(50000);// 持续时间 - 决定了播放的快慢
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
        // 监听进度条更新事件
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewNowPro.setText(getTime(progress));
                if (isChanging) {// 如果在拖动
                    Player.moveTo(progress);
                    if (progress == seekBar.getMax()) {//  拖动到 播放完了
                        pause();
                        btnPlay.setText("replay");
                    }
                    return;
                }
                if (progress == seekBar.getMax()) {//  播放完了
                    pause();
                    btnPlay.setText("replay");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {// 点击进度条时 开始拖动前
                isChanging = true;
                pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {// 放开进度条后 开始拖动后
                isChanging = false;
                play();
            }
        });
        // 监听按钮点击
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Player.isPlaying())// 是否在播放
                    play();
                else
                    pause();
            }
        });
        new Timer().schedule(new TimerTask() {// 不间断的更新播放进度
            @Override
            public void run() {
                if (!isChanging)
                    seekBar.setProgress(Player.getCurrentPosition());// 实时更新进度
            }
        }, 0, 10);

        imageViewReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回选择列表
                startActivity(new Intent(MainActivity.this, MusicListActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setSong(song);
        play();
    }

    public String getTime(int progress) {// 将毫秒转换成00:00
        int sec = progress / 1000; // 获取秒
        int min = sec / 60; // 获取分
        sec = sec % 60;// 获取剩余秒数 (分:秒)
        return String.format("%02d:%02d", min, sec);
    }

    public void pause() {// 暂停
        Player.pause();
        if (!isChanging)// 拖动的时候仍然播放动画
            animator.pause();
        btnPlay.setText("play");
    }

    public void play() {// 播放
        Player.play();
        if (!animator.isStarted())
            animator.start();
        else
            animator.resume();
        btnPlay.setText("pause");
    }

    /**
     * 设置音乐文件
     *
     * @param song
     */
    public void setSong(Song song) {
        seekBar.setMax(song.getDuration());
        seekBar.setProgress(0);
        textViewAllPro.setText(getTime(seekBar.getMax()));
        // 获取音乐相关信息
        textViewTitle.setText(song.getTitle());
        textViewAlbum.setText(song.getAlbum());
        textViewArtist.setText(song.getArtist());
        if (song.getBitmap() != null)
            imageViewArtist.setImageBitmap(song.getBitmap());
    }

}
