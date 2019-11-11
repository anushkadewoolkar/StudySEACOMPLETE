package com.example.studyseacomplete;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
//

public class addflashcard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addflashcard);

        if (getIntent().getStringExtra("question") != null)
        {
            String question = getIntent().getStringExtra("question");
            String answer1 = getIntent().getStringExtra("answer1");

            ((EditText) findViewById(R.id.question)).setText(question);
            ((EditText) findViewById(R.id.answer1)).setText(answer1);
        }

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //makes sure all fields are filled when check image is clicked, else toast is displayed
        findViewById(R.id.check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((EditText) findViewById(R.id.question)).getText().toString().equals("") || ((EditText) findViewById(R.id.answer1)).getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please fill out all information", Toast.LENGTH_LONG).show();
                }
                else
                    {
                    Intent data = new Intent();

                    data.putExtra("question", ((EditText) findViewById(R.id.question)).getText().toString());
                    data.putExtra("answer1", ((EditText) findViewById(R.id.answer1)).getText().toString());
                    data.putExtra("edit", getIntent().getStringExtra("edit"));
                    setResult(RESULT_OK, data);
                    finish();
                    }
            }
        });
    }
}