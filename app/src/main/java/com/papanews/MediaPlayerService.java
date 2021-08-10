package com.papanews;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MediaPlayerService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra("notificationId", 1);
        Log.e("pleaseplease", String.valueOf(notificationId));

        global.mediplayer_Setting = 1;
        // if you want cancel notification
        global.mMediaPlayer.stop();
        global.mMediaPlayerInfluence.stop();
        global.mMediaPlayerInternational.stop();
        global.mMediaPlayerMiscellaneous.stop();
        global.mMediaPlayerSports.stop();
        global.mMediaPlayerStartup.stop();
        global.mMediaPlayerBusiness.stop();
        global.mMediaPlayerEntertaintment.stop();
        global.mMediaPlayerPolitics.stop();
        global.mMediaPlayerTech.stop();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);

    }
}