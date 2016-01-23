package com.kot32.kshareviewactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.kot32.kshareviewactivitylibrary.manager.KShareViewActivityManager;

public class SecondActivity extends AppCompatActivity {

    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

    }

    @Override
    public void onBackPressed() {
        KShareViewActivityManager.getInstance(SecondActivity.this).finish(SecondActivity.this);
    }


}
