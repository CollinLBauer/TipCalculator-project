package com.example.tipcalculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.Locale;

public class TipActivity extends AppCompatActivity {

    Context context;
    Button buttonBack;
    TextView displayTipLabel;
    TextView displayTip;
    TextView displaySumBase;
    TextView displaySumTip;
    TextView displaySumFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);

        // get views
        displayTip =        findViewById(R.id.display_tip);
        displaySumBase =    findViewById(R.id.sum_base);
        displaySumTip =     findViewById(R.id.sum_tip);
        displaySumFinal =   findViewById(R.id.sum_final);
        displayTipLabel =   findViewById(R.id.display_tip_label);
        //buttonBack =    findViewById(R.id.tip_button_back);  // broken atm

        // get intent contents
        Intent intentIn = getIntent();
        double basePrice = intentIn.getDoubleExtra("basePrice",0);
        double tipAmount = intentIn.getDoubleExtra("tipAmount", 0);
        double tip = basePrice * 0.01 * tipAmount;
        int numPeople = intentIn.getIntExtra("numPeople", 1);


        displayTipLabel.setText(String.format(Locale.getDefault(), getResources().getString(R.string.tip_label), numPeople));
        setTextDecimal(displayTip, tip/numPeople);
        setTextDecimal(displaySumBase, basePrice);
        setTextDecimal(displaySumTip, tip);
        setTextDecimal(displaySumFinal, basePrice + tip);
    }

    private void setTextDecimal(TextView view, double value) {
        DecimalFormat df = new DecimalFormat("#,###,###,###.00");
        view.setText(df.format(value));
    }

}
