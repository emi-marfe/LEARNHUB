//package com.adminsurfacetech.my_learn_hub;
//
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.support.v4.app.NotificationCompat;
//
//public class AlarmReceiver extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        long when = System.currentTimeMillis();
//        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        Intent notificationIntent = new Intent(context, MainActivity.class);
//        notificationIntent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
//                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentTitle("Learn hub")
//                .setContentText("Hey! Try the new set of question here today")
//                .setSound(alarmSound)
//                .setAutoCancel(true)
//                .setWhen(when)
//                .setContentIntent(pendingIntent)
//                .setVibrate(new long[]{1000,1000,1000,1000,1000});
//        notificationManager.notify(0,builder.build());
//
//    }
//}