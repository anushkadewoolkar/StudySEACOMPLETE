package com.example.studyseacomplete;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao //DAO (Data Access Object) defines the methods that access the database
//https://developer.android.com/reference/android/arch/persistence/room/Dao
//https://medium.com/androiddevelopers/7-pro-tips-for-room-fbadea4bfbd1
//https://medium.com/@ajaysaini.official/building-database-with-room-persistence-library-ecf7d0b8f3e9
public interface FlashcardDao {
    //Query requests data
    @Query("SELECT * FROM flashcard") //https://developer.android.com/reference/android/arch/persistence/room/Query.html
    //gets all flashcards
    List<Flashcard> getAll();

    @Insert
    //inserts all flashcards into database
    void insertAll(Flashcard... flashcards); //https://developer.android.com/reference/android/arch/persistence/room/Insert

    @Delete
    //deletes flashcard from database
    void delete(Flashcard flashcard); //https://developer.android.com/reference/android/arch/persistence/room/Delete

    @Update(onConflict = OnConflictStrategy.REPLACE) //https://developer.android.com/reference/android/arch/persistence/room/Update
   //updates flashcard info in database
    void update(Flashcard flashcard);
}
