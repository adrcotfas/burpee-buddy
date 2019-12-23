package com.apps.adrcotfas.burpeebuddy.common.soundplayer;

import android.content.Context;
import android.content.ContextWrapper;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

import timber.log.Timber;

public class SoundPlayer extends ContextWrapper {

    private static final String TAG = "SoundPlayer";
    private MediaPlayer mMediaPlayer;

    public SoundPlayer(Context base) {
        super(base);
    }

    public void init() {
        mMediaPlayer = new MediaPlayer();
    }

    public void play(int sound) {
        play(sound, false);
    }

    public void play(int sound, boolean release) {
        try {
            final Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + sound);
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(this, uri);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
            mMediaPlayer.prepare();

            if (release) {
                mMediaPlayer.setOnCompletionListener(mp -> mMediaPlayer.release());
            }

            mMediaPlayer.setOnPreparedListener(mp -> {
                mMediaPlayer.start();
            });

        } catch (SecurityException | IOException e) {
            Timber.tag(TAG).wtf(e.getMessage());
            mMediaPlayer.release();
        }
    }

    public void stop() {
        mMediaPlayer.release();
    }
}
