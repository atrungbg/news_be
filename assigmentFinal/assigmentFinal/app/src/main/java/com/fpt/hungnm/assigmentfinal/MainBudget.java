package com.fpt.hungnm.assigmentfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class MainBudget extends AppCompatActivity {
    private static final String TAG ="Hungnm";
    private LinearLayout llCreateBudget;
    private Spinner spMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_budget);
            bindingView();
            bindingAction();
        }catch (Exception ex){
            Log.e(TAG, "MainBudget - onCreate - " + ex.getMessage());
        }
    }

    private void bindingAction() {
        try{

        }catch (Exception ex){
            Log.e(TAG, "MainBudget - bindingAction - " + ex.getMessage());
        }
    }

    private void bindingView() {
//        try{
//            spMonth = findViewById(R.id.sp_budget_month);
//            llCreateBudget = findViewById(R.id.ll_create_budget);
//        }catch (Exception ex){
//            Log.e(TAG, "MainBudget - bindingView - " + ex.getMessage());
//        }
    }
}