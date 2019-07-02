package com.example.superslidelayout.helper;

import android.app.Activity;
import android.content.Intent;

public class ActivityHelpter {

    /**
     * 跳转到目标activity
     *
     * @param targetActivity
     */
    public static void jumpTarget(Activity activity, Class<?> targetActivity) {
        activity.startActivity(new Intent(activity, targetActivity));
    }
}
