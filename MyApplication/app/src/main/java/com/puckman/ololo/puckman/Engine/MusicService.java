package com.puckman.ololo.puckman.Engine;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.puckman.ololo.puckman.R;

import java.io.IOException;

import static android.media.session.PlaybackState.ACTION_PAUSE;
import static android.media.session.PlaybackState.ACTION_PLAY;
import static android.media.session.PlaybackState.ACTION_PLAY_PAUSE;

/**
 * Created by Ololo on 05.06.2017.
 */

//Заготовки на будущее

public class MusicService extends Service  {
    private int length = 0;
    MediaPlayer mp = null;
    private final static int MAX_VOLUME = 100;
    Context context;
    AudioManager.OnAudioFocusChangeListener afChangeListener;

    MyBinder binder = new MyBinder();

    public class MyBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action.equals(ACTION_PLAY)){
            playMusic(R.raw.puckman_second);
        }
        if (action.equals(ACTION_PAUSE)){
            pauseMusic();
        }if (action.equals(ACTION_PLAY_PAUSE)){
            resumeMusic();
        }
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            try {
                mp.stop();
                mp.release();
            } finally {
                mp = null;
            }
        }
    }

    public void onTaskRemoved(Intent rootIntent) {
        stopSelf();
    }


    public void playMusic(int musicFile) {
        //System.out.println("$@!$");
        if (mp != null) {
            if (mp.isPlaying()) {
                try {
                    mp.stop();
                    mp.release();
                    mp = MediaPlayer.create(this, musicFile);

                    AudioManager am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                    int result = am.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

                    if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                        // Start playback.
                        mp.setLooping(true);
                        final float volume = (float) (1 - (Math.log(MAX_VOLUME - 85) / Math.log(MAX_VOLUME)));
                        mp.setVolume(volume, volume);
                        mp.start();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    mp = MediaPlayer.create(this, musicFile);

                    AudioManager am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                    int result = am.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

                    if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                        // Start playback.
                        mp.setLooping(true);
                        final float volume = (float) (1 - (Math.log(MAX_VOLUME - 85) / Math.log(MAX_VOLUME)));
                        mp.setVolume(volume, volume);
                        mp.prepare();
                        mp.start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else {
            try {
              //  System.out.println("$@!$ffffff");
                mp = MediaPlayer.create(this, musicFile);

                AudioManager am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                int result = am.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Start playback.
                    mp.setLooping(true);
                    final float volume = (float) (1 - (Math.log(MAX_VOLUME - 85) / Math.log(MAX_VOLUME)));
                    mp.setVolume(volume, volume);
                    mp.start();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
    * MediaPlayer methods
    * */

    public void pauseMusic() {
        if (mp.isPlaying()) {
            mp.pause();
            length = mp.getCurrentPosition();

        }
    }

    public void resumeMusic() {
        if (mp.isPlaying() == false) {
            mp.seekTo(length);
            mp.start();
        }
    }

    public void stopMusic() {
        mp.stop();
        mp.release();
        mp = null;
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (mp != null) {
            try {
                mp.stop();
                mp.release();
            } finally {
                mp = null;
            }
        }
        return false;
    }

}