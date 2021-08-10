package com.papanews;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MusicService extends Service {

    private IBinder mBinder = new MyBinder();
    public static final String ACTION_NEXT = "NEXT";
    public static final String ACTION_PREVIOUS = "PREVIOUS";
    public static final String ACTION_PLAY = "PLAY";

    ActionPlaying actionPlaying;
    public static String news_name="";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

//    public static void news_namegetter(String nname){
//        news_name = nname;
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String actionName = intent.getStringExtra("myActionName");
        if (actionName != null) {
            switch (actionName) {
                case ACTION_PLAY:
                    if (actionPlaying != null) {
                        Log.e("musicservics","play - "+news_name);
                        actionPlaying.playClicked(news_name);
                    }
                    break;
                case ACTION_NEXT:
                    if (actionPlaying != null) {
                        Log.e("musicservics","next - "+news_name);
                        actionPlaying.nextClicked(news_name);
                    }
                    break;
                case ACTION_PREVIOUS:
                    if (actionPlaying != null) {
                        Log.e("musicservics","pre - "+news_name);
                        actionPlaying.previousClicked(news_name);
                    }
                    break;
            }
        }

        return START_STICKY;
    }

    public void setCallBack(ActionPlaying actionPlaying){
        this.actionPlaying = actionPlaying;
    }
}
