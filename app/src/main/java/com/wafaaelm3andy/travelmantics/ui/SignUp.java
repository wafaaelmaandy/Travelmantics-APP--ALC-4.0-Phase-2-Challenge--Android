package com.wafaaelm3andy.travelmantics.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wafaaelm3andy.travelmantics.R;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
    }

    public void save(View view) {
        startActivity(new Intent(this,AddHolidayDealActivity.class));
    }
}
