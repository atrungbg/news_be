package com.fpt.hungnm.assigmentfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.fpt.hungnm.assigmentfinal.Dal.MyFbContext;

public class MainActivity extends AppCompatActivity {
    private MyFbContext myFbContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myFbContext = new MyFbContext();
    }
}