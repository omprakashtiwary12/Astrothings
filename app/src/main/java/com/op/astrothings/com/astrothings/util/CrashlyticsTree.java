package com.op.astrothings.com.astrothings.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import timber.log.Timber;

public class CrashlyticsTree extends Timber.Tree {

    private static final String CRASHLYTICS_KEY_PRIORITY = "priority";
    private static final String CRASHLYTICS_KEY_TAG = "tag";
    private static final String CRASHLYTICS_KEY_MESSAGE = "message";

    @Override
    protected void log(int priority, String tag, @NonNull String msg, Throwable error) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return;
        }

        if (error == null) {
            FirebaseCrashlytics.getInstance().recordException(new Exception(msg));
        } else {
            FirebaseCrashlytics.getInstance().recordException(error);
        }
    }
}