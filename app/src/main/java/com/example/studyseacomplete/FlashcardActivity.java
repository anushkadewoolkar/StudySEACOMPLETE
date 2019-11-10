package com.example.studyseacomplete;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Random;

public class FlashcardActivity extends AppCompatActivity {

    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;

    public int getRandomNumber(int minNumber, int maxNumber) {
        Random rand = new Random();
        return rand.nextInt((maxNumber - minNumber) + 1) + minNumber;
    }
// hi
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();

        if (allFlashcards != null && allFlashcards.size() > 0) {
            int rand = getRandomNumber(0,allFlashcards.size()-1);
            ((TextView) findViewById(R.id.flashcard_question)).setText(allFlashcards.get(rand).getQuestion());
            ((TextView) findViewById(R.id.flashcard_answer1)).setText(allFlashcards.get(rand).getAnswer());
        }

        findViewById(R.id.flashcard_answer1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackground(getResources().getDrawable(R.drawable.background));

            }
        });

        findViewById(R.id.visibilityoff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.flashcard_answer1).setVisibility(View.INVISIBLE);
                v.setVisibility(View.INVISIBLE);
                findViewById(R.id.visibilityon).setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.visibilityon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.flashcard_answer1).setVisibility(View.VISIBLE);
                v.setVisibility(View.INVISIBLE);
                findViewById(R.id.visibilityoff).setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlashcardActivity.this, addflashcard.class);
                FlashcardActivity.this.startActivityForResult(intent,100);
            }
        });

        findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlashcardActivity.this, addflashcard.class);
                intent.putExtra("question",((TextView) findViewById(R.id.flashcard_question)).getText().toString());
                intent.putExtra("answer1",((TextView) findViewById(R.id.flashcard_answer1)).getText().toString());
                intent.putExtra("edit","yeet");
                FlashcardActivity.this.startActivityForResult(intent,100);
            }
        });

        findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (allFlashcards.size() == 0){
                    ((TextView) findViewById(R.id.flashcard_question)).setText("Add a card to get started!");
                    ((TextView) findViewById(R.id.flashcard_answer1)).setText("");
                }else {
                    // set the question and answer TextViews with data from the database
                    int rand = getRandomNumber(0, allFlashcards.size() - 1);
                    ((TextView) findViewById(R.id.flashcard_question)).setText(allFlashcards.get(rand).getQuestion());
                    ((TextView) findViewById(R.id.flashcard_answer1)).setText(allFlashcards.get(rand).getAnswer());
                    }
            }
        });

        findViewById(R.id.deleteBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcardDatabase.deleteCard(((TextView) findViewById(R.id.flashcard_question)).getText().toString());
                allFlashcards = flashcardDatabase.getAllCards();

                if (allFlashcards.size() == 0) {
                    ((TextView) findViewById(R.id.flashcard_question)).setText("Add a card to get started!");
                    ((TextView) findViewById(R.id.flashcard_answer1)).setText("");
                }else{
                    findViewById(R.id.next_button).performClick();
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            try {

                String question = data.getExtras().getString("question");
                String answer1 = data.getExtras().getString("answer1");

                if (data.getExtras().getString("edit") != null) {
                    flashcardDatabase.deleteCard(((TextView) findViewById(R.id.flashcard_question)).getText().toString());
                }

                ((TextView) findViewById(R.id.flashcard_question)).setText(question);
                ((TextView) findViewById(R.id.flashcard_answer1)).setText(answer1);

                flashcardDatabase.insertCard(new Flashcard(question, answer1));
                allFlashcards = flashcardDatabase.getAllCards();
                Snackbar.make(findViewById(R.id.RelativeLayout), "Created card successfully", Snackbar.LENGTH_LONG)
                        .show();
            } catch (Exception e) {

            }
        }
    }

}