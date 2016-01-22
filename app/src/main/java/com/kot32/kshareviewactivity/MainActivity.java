package com.kot32.kshareviewactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kot32.kshareviewactivitylibrary.manager.KShareViewActivityManager;

public class MainActivity extends AppCompatActivity {

    private Button    b;

    private ImageView img;

    private TextView  title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b = (Button) findViewById(R.id.button);
        img = (ImageView) findViewById(R.id.img);
        title = (TextView) findViewById(R.id.title);

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                KShareViewActivityManager.getInstance().startActivity(MainActivity.this, SecondActivity.class,
                                                                      R.layout.activity_second, img, title);
            }
        });
    }

}
