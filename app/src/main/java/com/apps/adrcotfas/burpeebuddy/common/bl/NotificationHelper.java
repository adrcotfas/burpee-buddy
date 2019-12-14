package com.apps.adrcotfas.burpeebuddy.common.bl;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.apps.adrcotfas.burpeebuddy.R;
import com.apps.adrcotfas.burpeebuddy.common.utilities.TimerFormat;

public class NotificationHelper extends ContextWrapper {

    public static final int WORKOUT_NOTIFICATION_ID = 42;
    private static final String BUDDY_NOTIFICATION = "buddy.notification";

    private final NotificationManager mManager;
    private final NotificationCompat.Builder mBuilder;

    public NotificationHelper(Context context) {
        super(context);
        mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            initChannel();
        }

        final PendingIntent pendingIntent = new NavDeepLinkBuilder(context)
                .setGraph(R.navigation.navigation_graph)
                .setDestination(R.id.workoutFragment)
                .createPendingIntent();

        mBuilder = new NotificationCompat.Builder(this, BUDDY_NOTIFICATION)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent)
                .setShowWhen(false)
                .setOngoing(true)
                .setOnlyAlertOnce(true);
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void initChannel() {
        NotificationChannel c = new NotificationChannel(
                BUDDY_NOTIFICATION, "Buddy workout notification", //TODO: extract string
                NotificationManager.IMPORTANCE_LOW);
        c.setBypassDnd(true);
        c.setShowBadge(true);
        c.setSound(null, null);
        mManager.createNotificationChannel(c);
    }

    public NotificationCompat.Builder getBuilder() {
        return mBuilder;
    }

    public void setReps(int reps) {
        mManager.notify(WORKOUT_NOTIFICATION_ID,
                getBuilder()
                        .setOnlyAlertOnce(true)
                        //TODO: extract string, consider plurals
                        .setContentTitle(reps + " reps")
                        .build());
    }

    public void setElapsedTime(long elapsed) {
        mManager.notify(WORKOUT_NOTIFICATION_ID,
                getBuilder()
                        .setOnlyAlertOnce(true)
                        .setContentText(TimerFormat.secondsToTimerFormat(elapsed))
                        .build());
    }

    public void setTitle(String value) {
        mManager.notify(WORKOUT_NOTIFICATION_ID, getBuilder().setContentTitle(value).build());
    }

    public void clearNotification() {
        mManager.cancelAll();
    }
}
