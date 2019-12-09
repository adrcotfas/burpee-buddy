package com.apps.adrcotfas.burpeebuddy.settings;

import com.apps.adrcotfas.burpeebuddy.common.bl.BuddyApplication;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

public class SettingsHelper {
    public final static String ENABLE_AUTO_LOCK = "pref_auto_lock";
    public final static String ENABLE_SOUND = "pref_enable_sound";
    public final static String ENABLE_SPECIAL_SOUND = "pref_enable_sound_special";
    public final static String SPECIAL_SOUND_INTERVAL = "pref_sound_special_reps";
    public final static String ENABLE_WAKEUP = "pref_enable_wakeup";
    public final static String WAKEUP_INTERVAL = "pref_wakeup_reps";


    public static boolean autoLockEnabled() {
        return getDefaultSharedPreferences(BuddyApplication.getInstance())
                .getBoolean(ENABLE_AUTO_LOCK, false);
    }

    public static boolean soundEnabled() {
        return getDefaultSharedPreferences(BuddyApplication.getInstance())
                .getBoolean(ENABLE_SOUND, true);
    }

    public static boolean specialSoundEnabled() {
        return getDefaultSharedPreferences(BuddyApplication.getInstance())
                .getBoolean(ENABLE_SPECIAL_SOUND, true);
    }

    public static boolean wakeupEnabled() {
        return getDefaultSharedPreferences(BuddyApplication.getInstance())
                .getBoolean(ENABLE_WAKEUP, false);
    }

    public static int getSpecialSoundInterval() {
        return Integer.parseInt(getDefaultSharedPreferences(BuddyApplication.getInstance())
                .getString(SPECIAL_SOUND_INTERVAL, "5"));
    }

    public static int getWakeUpInterval() {
        return Integer.parseInt(getDefaultSharedPreferences(BuddyApplication.getInstance())
                .getString(WAKEUP_INTERVAL, "10"));
    }

}
