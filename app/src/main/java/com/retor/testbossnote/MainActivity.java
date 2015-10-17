package com.retor.testbossnote;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.retor.testbossnote.ui.worker.UIWorker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UIWorker.getInstance(this).fillStartScreen();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
