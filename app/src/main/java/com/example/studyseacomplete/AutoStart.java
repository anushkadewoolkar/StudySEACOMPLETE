package com.example.studyseacomplete;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Calendar;

/**
 * Created by austin on 10/26/17.
 */

public class AutoStart extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Cursor taskCursor = TaskDatabase.getUnfinishedTaskCursor();

            long now = Calendar.getInstance().getTimeInMillis();

            // create an alarm for each uncompleted task
            while (!taskCursor.isAfterLast()) {
                Task task = new Task(taskCursor);
                Long reminder = Long.parseLong(task.get(TaskDatabase.COLUMN_TIME));
                Long due = Long.parseLong(task.get(TaskDatabase.COLUMN_DATE_DUE));

                Alarm.setOverdueAlarm(task);

                if (now < due - reminder)
                    Alarm.setReminderAlarm(task);

                taskCursor.moveToNext();
            }

            taskCursor.close();
        }
    }
}
