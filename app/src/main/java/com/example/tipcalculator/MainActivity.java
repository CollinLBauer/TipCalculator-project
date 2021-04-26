package com.example.tipcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.tipcalculator.helpers.DecimalDigitsInputFilter;

public class MainActivity extends AppCompatActivity {

    Context context;
    EditText basePriceEdit;
    RadioGroup radioGroup;
    EditText customTipEdit;
    Button calculateButton;
    Button historyButton;
    EditText personEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();


        // get views
        basePriceEdit = findViewById(R.id.edit_base_price);
        radioGroup = findViewById(R.id.radio_group);
        customTipEdit = findViewById(R.id.edit_custom);
        calculateButton = findViewById(R.id.button_calculate);
        historyButton = findViewById(R.id.button_history);
        personEdit = findViewById(R.id.edit_people);
        basePriceEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);



        // setup EditText input types and filters
        basePriceEdit.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(9,2)});
        customTipEdit.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(3,2)});
        customTipEdit.setInputType(InputType.TYPE_NULL);


        // customTipEdit can only be edited when selected
        InputMethodManager inm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_custom) {
                customTipEdit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            } else {
                customTipEdit.setInputType(InputType.TYPE_NULL);
                View focus = getCurrentFocus();
                if (focus != null) {
                    inm.hideSoftInputFromWindow(focus.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        //click listener for calculate
        calculateButton.setOnClickListener(v -> {
            Intent intentOut = new Intent(context, TipActivity.class);

            // get base price
            double basePrice;
            try {
                basePrice = Float.parseFloat(basePriceEdit.getText().toString());
            } catch (Exception e) {
                Toast.makeText(context, "Please enter a price.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (basePrice < 1) {
                Toast.makeText(context, "Price must be at least $1.", Toast.LENGTH_SHORT).show();
                return;
            }

            intentOut.putExtra("basePrice", basePrice);

            // get tip amount
            if (radioGroup.getCheckedRadioButtonId() == R.id.radio_10) {
                intentOut.putExtra("tipAmount", (double) 10);
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.radio_15) {
                intentOut.putExtra("tipAmount", (double) 15);
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.radio_20) {
                intentOut.putExtra("tipAmount", (double) 20);
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.radio_custom) {

                // make sure the custom tip is valid before continuing
                double tipAmount;
                try {
                    tipAmount = Float.parseFloat(customTipEdit.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(context, "Please enter a tip amount.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (tipAmount < 1) {
                    Toast.makeText(context, "Tip amount must be at least 1%.", Toast.LENGTH_SHORT).show();
                    return;
                }

                intentOut.putExtra("tipAmount", tipAmount);

            } else {
                Toast.makeText(context, "No tip amount selected.", Toast.LENGTH_SHORT).show();
                return;
            }

            // get the number of people
            int numPeople;
            try {
                numPeople = Integer.parseInt(personEdit.getText().toString());
            } catch (Exception e) {
                Toast.makeText(context, "Missing people count.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (numPeople < 1) {
                Toast.makeText(context, "Must be at least 1 person.", Toast.LENGTH_SHORT).show();
                return;
            }
            intentOut.putExtra("numPeople", numPeople);

            // start the tip activity
            startActivity(intentOut);

        });


    }
}