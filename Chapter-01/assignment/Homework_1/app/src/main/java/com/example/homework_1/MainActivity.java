package com.example.homework_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_1 = findViewById(R.id.button_1);
        final TextView text_1 = findViewById(R.id.text_1);
        final EditText editText_1 = findViewById(R.id.editText_1);

        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_1.setText(editText_1.getText());
            }
        });

        final Switch switch_1 = findViewById(R.id.switch_1);
        final ImageView imageView_1 = findViewById(R.id.imageView_1);

        switch_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    imageView_1.setVisibility(View.VISIBLE);
                }
                else {
                    imageView_1.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
