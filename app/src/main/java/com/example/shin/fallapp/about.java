package com.example.shin.fallapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import static android.view.View.OnClickListener;

/**
 * Created by Shin on 2015/4/24.
 */
public class about extends Activity {

    private ImageButton btnAboutBack;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutabout);

        btnAboutBack = (ImageButton)findViewById(R.id.btnAboutBack);

        btnAboutBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(about.this,fall.class);
                startActivity(intent);
                about.this.finish();
            }
        });
    }
}
