package com.kot32.kshareviewactivitylibrary.ins;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.kot32.kshareviewactivitylibrary.reflect.Reflect;

/**
 * Created by kot32 on 16/1/21.
 */
public class AnimationInstrumentation extends Instrumentation {

    private Reflect  originInstrumentationRef;

    private Activity originActivity;

    public AnimationInstrumentation(Instrumentation origin){
        originInstrumentationRef = Reflect.on(origin);
    }

    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target,
                                            Intent intent, int requestCode, Bundle options) {

        // Log.e("要跳转的Activity 的名字", target.getClass().getSimpleName());

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        ActivityResult result = originInstrumentationRef.call("execStartActivity", who, contextThread, token, target,
                                                              intent, requestCode, options).get();

        return result;
    }
}
