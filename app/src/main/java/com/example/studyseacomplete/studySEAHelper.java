package com.example.studyseacomplete;


import android.content.Context;

import androidx.room.Room;

import com.example.studyseacomplete.AppDatabase;
import com.example.studyseacomplete.Flashcard;

import java.util.List;

public class studySEAHelper {
    private final AppDatabase studySEAHelper;

    studySEAHelper(Context context) {
        //How to use Room Database: https://developer.android.com/reference/android/arch/persistence/room/RoomDatabase.Builder
        //use DAO instead of accessing data directly because it might change if edited
        studySEAHelper = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "StudySEAHelper")
                .allowMainThreadQueries() //Disables the main thread query check (queries are used to find specific data by filtering specific criteria) for Room
                .fallbackToDestructiveMigration() //Recreates the database instead of crashing
                .build(); //Creates the databases and initializes it.
    }

    //deletes flashcard from database
    public void deleteCard(String flashcardQuestion)
    {
        List<Flashcard> TotalCards = studySEAHelper.flashcardDao().getAll();
        for (Flashcard f : TotalCards)
        {
            if (f.getQuestion().equals(flashcardQuestion))
            {
                studySEAHelper.flashcardDao().delete(f); //defined in DAO
            }
        }
    }

    //inserts all flashcards into database
    public void insertCard(Flashcard flashcard)
    {
        studySEAHelper.flashcardDao().insertAll(flashcard); //defined in DAO
    }

    //gets all flashcards
    public List<Flashcard> getTotalCards()
    {
        return studySEAHelper.flashcardDao().getAll(); //defined in DAO
    }

    ///updates flashcard info in database
    public void updateCard(Flashcard flashcard)
    {
        studySEAHelper.flashcardDao().update(flashcard); //defined in DAO
    }
}
