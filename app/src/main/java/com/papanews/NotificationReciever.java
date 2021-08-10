package com.papanews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReciever extends BroadcastReceiver {

    public static final String ACTION_NEXT = "NEXT";
    public static final String ACTION_PREVIOUS = "PREVIOUS";
    public static final String ACTION_PLAY = "PLAY";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "notification recieved", Toast.LENGTH_SHORT).show();


        Intent intent_notif = new Intent(context, DashBoard.class);
        context.startActivity(intent_notif);



        Intent intent1 = new Intent(context, MusicService.class);
        if (intent.getAction() != null){
            switch (intent.getAction())
            {
                case ACTION_PLAY:
                    Toast.makeText(context, "Play", Toast.LENGTH_LONG).show();
                    intent1.putExtra("myActionName",intent.getAction());
                    context.startService(intent1);
                    break;
                case ACTION_NEXT:
                    Toast.makeText(context, "Next", Toast.LENGTH_LONG).show();
                    intent1.putExtra("myActionName",intent.getAction());
                    context.startService(intent1);
                    break;
                case ACTION_PREVIOUS:
                    Toast.makeText(context, "Previous", Toast.LENGTH_LONG).show();
                    intent1.putExtra("myActionName",intent.getAction());
                    context.startService(intent1);
                    break;
            }
        }
    }
}
