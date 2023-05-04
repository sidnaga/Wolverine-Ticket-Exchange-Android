package com.example.snapchatclone;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, Indiv_Thread.class);
        intent1.putExtra("other_user",intent.getStringExtra("Replier"));
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);
        NotificationCompat.Builder mbuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context,"ID")
                .setSmallIcon(R.drawable.wolverine)
                .setContentTitle(intent.getStringExtra("Title"))
                .setContentText(intent.getStringExtra("Reply"))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0,mbuilder.build());
    }
}
