package com.kot32.kshareviewactivitylibrary.actions;

import android.view.View;

/**
 * Created by kot32 on 16/1/22.
 */
public interface KShareViewActivityAction {

    void onAnimatorStart();

    void onAnimatorEnd();

    void changeViewProperty(View view);
}
