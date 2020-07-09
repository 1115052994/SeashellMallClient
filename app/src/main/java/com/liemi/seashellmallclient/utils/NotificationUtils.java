package com.liemi.seashellmallclient.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import com.liemi.seashellmallclient.R;
import com.netmi.baselibrary.ui.MApplication;

/*
 * 状态栏消息相关
 * */
public class NotificationUtils {
    //消息种类，目前只有普通消息和注册消息
    public static final int MESSAGE_NORMAL = 100000;//TODO:普通消息，信鸽发送的普通消息
    public static final int MESSAGE_REGISTER = 100001;//注册消息，用户注册成功以后会发送一条消息

    private static final int messageId = 1000;//默认消息id
    private static final String notificationId = "channel_message";
    private static final String name = "三享读书";

    //发送消息
    public static void sendNotification(String title, String content, PendingIntent pi) {
        sendNotification(MESSAGE_NORMAL, messageId, title, content, pi);
    }

    //发送消息
    public static void sendNotification(int type, int id, String title, String content, PendingIntent pi) {
        Context context = MApplication.getAppContext();
        //获取NotificationManager实例
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(notificationId, name, NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(context)
                    .setChannelId(notificationId)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(R.mipmap.app_logo)
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    .setContentIntent(pi)
                    .build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.app_logo)
                    .setChannelId(notificationId)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    .setContentIntent(pi);
            notification = notificationBuilder.build();
        }
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        if (notificationManager != null) {
            notificationManager.notify("pushMessage", id, notification);
        }
    }
}
