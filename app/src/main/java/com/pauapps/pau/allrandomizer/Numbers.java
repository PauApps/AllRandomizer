package com.pauapps.pau.allrandomizer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Numbers extends Activity {

    EditText number;
    int num;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numbers);

        number = (EditText) findViewById(R.id.number);

        //TODO Posar iniciador d'anuncis
    }

    public void sum(View v) {
        num = Integer.parseInt(number.getText().toString());
        num++;
        number.setText(num);
    }

    public void res(View v) {
        num = Integer.parseInt(number.getText().toString());
        num--;
        number.setText(num);
    }
}
