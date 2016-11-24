package com.example.hu.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.hu.myapplication.widget.SwitchView;

/**
 * Created by hu on 2016/11/21.
 */

public class SwitchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.switch_layout);
        SwitchView switchView= (SwitchView) findViewById(R.id.switchView);
        switchView.setCurrentState(SwitchView.OFF);
    }
}
