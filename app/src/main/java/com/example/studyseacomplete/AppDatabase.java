package com.example.studyseacomplete;


import androidx.room.Database;
import androidx.room.RoomDatabase;

//https://medium.com/@ajaysaini.official/building-database-with-room-persistence-library-ecf7d0b8f3e9
//developer.android.com/reference/android/arch/persistence/room/Database
//https://stackoverflow.com/questions/54386798/how-does-it-work-passing-an-abstract-class-as-parameter-in-room-database-builde
//Use an abstract class because the implementation of the Dao methods will be provided by Room itself
@Database(entities = {Flashcard.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FlashcardDao flashcardDao();
}
