package com.example.studyseacomplete;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class  TaskDatabase extends SQLiteOpenHelper {
    //task table
    static final String TABLE_ALLTASKS = "alltasks";
    static final String COLUMN_ID = "_id"; //?
    static final String COLUMN_CLASS = "class"; //?
    static final String COLUMN_TASK = "task";
    static final String COLUMN_FINISHED = "finished";
    static final String COLUMN_DATE_DUE = "due";
    static final String COLUMN_DURATION_TIME = "duration";
    static final String COLUMN_TIME = "reminder";
    static final String COLUMN_HOURS = "reminder_hours";
    static final String COLUMN_DAYS = "reminder_days";
    static final String COLUMN_DURATION_INPUT = "duration_input";
    static final String COLUMN_REMINDER_INPUT = "reminder_input";
    static final String COLUMN_IMPORTANCE = "importance";

    static final String TABLE_COLUMNS[] = {
            COLUMN_DATE_DUE,
            COLUMN_IMPORTANCE,
            COLUMN_CLASS,
            COLUMN_ID,
            COLUMN_TASK,
            COLUMN_DURATION_TIME,
            COLUMN_TIME,
            COLUMN_HOURS,
            COLUMN_DAYS,
            COLUMN_DURATION_INPUT,
            COLUMN_REMINDER_INPUT,
            COLUMN_FINISHED
    };

    //database values
    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 12;

    private static final TaskDatabase instance = new TaskDatabase();
    private final SQLiteDatabase db;

    private TaskDatabase() {
        super(HomeworkPlanner.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    public static SQLiteDatabase getDatabase() {
        return instance.db;
    }

    public static Cursor getUnfinishedTaskCursor() {
        // Sort algorithm: importance / 10 * ( 100 - duration / 30 - EXP(0.001 * time_to_deadline_in_minutes))
        return instance.db.query(true, TABLE_ALLTASKS, null, COLUMN_FINISHED + " = 0", null, null, null,
                COLUMN_IMPORTANCE + " * 0.1 * ( 100 - 60*1000*" + COLUMN_DURATION_TIME + " / 30 - 1.06)", null);
    }

    public static Cursor selectTaskById(long id) {
        return instance.db.query(true, TABLE_ALLTASKS, null, COLUMN_ID + " = " + new Long(id).toString(),
                null, null, null, null, null);
    }

    // This is where we need to write create table statements.
    // This is called when database is created.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "
                + TABLE_ALLTASKS + " ( "

                + COLUMN_ID + " integer primary key autoincrement default 0, "
                + COLUMN_CLASS + " string, "
                + COLUMN_TASK + " string, "
                + COLUMN_IMPORTANCE + " long, "
                + COLUMN_TIME + " long, "
                + COLUMN_DAYS + " long, "
                + COLUMN_HOURS + " long, "
                + COLUMN_DURATION_TIME + " long, "
                + COLUMN_REMINDER_INPUT + " long, "
                + COLUMN_DURATION_INPUT + " long, "
                + COLUMN_DATE_DUE + " long, "
                + COLUMN_FINISHED + " long DEFAULT 0 );");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        // TODO
        db.execSQL("DROP TABLE tasks;");
        onCreate(db);
    }
}
