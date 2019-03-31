package com.pauapps.pau.allrandomizer.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.pauapps.pau.allrandomizer.R;

import java.util.Random;

public class NumbersActivity extends Activity {

    EditText number;
    int num;

    //TODO posar un range de numeros, entre 0 i 100, per exemple, o entre -50 i 50
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numbers);

        number = (EditText) findViewById(R.id.number);

        //TODO Posar iniciador d'anuncis
    }

    public void sum(View v) {
        num = Integer.parseInt(number.getText().toString());
        num++;
        number.setText("" + num);
    }

    public void res(View v) {
        num = Integer.parseInt(number.getText().toString());
        num--;
        if (num < 0) {
            //TODO indicar que no pot ser negtiu
            number.setText("0");
        } else {
            number.setText("" + num);
        }
    }

    public void rand(View v) {
        EditText num = (EditText) findViewById(R.id.number);
        int number = Integer.parseInt(String.valueOf(num.getText()));
        Random r = new Random();
        if (number == 0) {
            num.setText("0");
        } else {
            //TODO mostrar texte
            num.setText("" + r.nextInt(number));
        }
    }
}
