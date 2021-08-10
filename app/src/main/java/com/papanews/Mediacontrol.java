package com.papanews;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;

import static com.papanews.global.mMediaPlayernew;

public class Mediacontrol {
    Context context;


    public Mediacontrol(Context context) {
        this.context = context;
    }

    public void media_load(String url) {
//        mMediaPlayernew.stop();
        try {
            mMediaPlayernew.reset();
            mMediaPlayernew.setDataSource(url);
            mMediaPlayernew.prepare();
        } catch (IOException e) {
            Log.e("exception_media", String.valueOf(e));
            e.printStackTrace();
        }

    }

    public void media_play() throws IOException {
        mMediaPlayernew.start();
    }
    public void media_pause() throws IOException {
        mMediaPlayernew.pause();
    }

    public boolean media_is_playing() throws IOException {
        return mMediaPlayernew.isPlaying();
    }

    @RequiresApi
            (api = Build.VERSION_CODES.M)
    public void media_playing_speed(float speed) throws IOException {
        mMediaPlayernew.setPlaybackParams(mMediaPlayernew.getPlaybackParams().setSpeed(speed));
    }





//    public void media_pause() throws IOException {
//        mMediaPlayernew.pause();
//    }

}
