package com.example.studyseacomplete;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.text.DateFormat.getDateTimeInstance;

public class AddTaskActivity extends AppCompatActivity implements Serializable {
    static final TypedArray reminderTimes = HomeworkPlanner.getContext().getResources().obtainTypedArray(R.array.ReminderTimes);
    static final TypedArray reminderHours = HomeworkPlanner.getContext().getResources().obtainTypedArray(R.array.ReminderHours);
    static final TypedArray reminderDays = HomeworkPlanner.getContext().getResources().obtainTypedArray(R.array.ReminderDays);
    static final String durationTimes[] = { // TODO: move to strings.xml
            "0:05 minutes",
            "0:10 minutes",
            "0:15 minutes",
            "0:20 minutes",
            "0:30 minutes",
            "0:45 minutes",
            "1:00 hour",
            "1:15 hours",
            "1:30 hours",
            "1:45 hours",
            "2:00 hours",
            "2:30 hours",
            "3:00 hours",
            "3:30 hours",
            "4:00 hours",
            "5:00 hours",
            "6:00 hours",
            "7:00 hours",
            "8:00 hours",
            "9:00 hours",
            "10:00 hours"
    };
    Task newTask;
    EditText Task;
    EditText Class;
    EditText DueInMilliseconds;
    EditText DateDue;
    SeekBar TimeForTaskEntered;
    SeekBar ImportanceOfTask;
    TextView TimeForTaskDisplay;
    Spinner TaskReminder;
    TextView DisplayReminderDays;
    TextView DisplayReminderHours;
    Spinner ChooseReminderDays;
    Spinner ChooseReminderHours;
    String reminder = "None";
    ArrayAdapter<CharSequence> reminderHourAdapter;
    ArrayAdapter<CharSequence> reminderDayAdapter;
    ArrayAdapter<CharSequence> reminderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        long id = getIntent().getLongExtra("task", -1);
        if (id != -1)
            newTask = new Task(id);

        Task = (EditText) findViewById(R.id.etTask);
        Class = (EditText) findViewById(R.id.etClass);
        TimeForTaskEntered = (SeekBar) findViewById(R.id.sbDuration);
        DueInMilliseconds = (EditText) findViewById(R.id.etDueMillis);
        DateDue = (EditText) findViewById(R.id.etDue);
        TimeForTaskDisplay = (TextView) findViewById(R.id.tvDuration);
        ImportanceOfTask = (SeekBar) findViewById(R.id.sbImportance);
        TaskReminder = (Spinner) findViewById(R.id.sReminder);
        DisplayReminderDays = (TextView) findViewById(R.id.tvReminderDays);
        DisplayReminderHours = (TextView) findViewById(R.id.tvReminderHours);
        ChooseReminderDays = (Spinner) findViewById(R.id.sReminderDays);
        ChooseReminderHours = (Spinner) findViewById(R.id.sReminderHours);

        DisplayReminderDays.setVisibility(View.INVISIBLE);
        DisplayReminderHours.setVisibility(View.INVISIBLE);
        ChooseReminderDays.setVisibility(View.INVISIBLE);
        ChooseReminderHours.setVisibility(View.INVISIBLE);

        reminderDayAdapter = ArrayAdapter.createFromResource(this, R.array.ReminderDays, android.R.layout.simple_spinner_item);
        reminderDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ChooseReminderDays.setAdapter(reminderDayAdapter);

        reminderHourAdapter = ArrayAdapter.createFromResource(this, R.array.ReminderHours, android.R.layout.simple_spinner_item);
        reminderHourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ChooseReminderHours.setAdapter(reminderHourAdapter);



        reminderAdapter = ArrayAdapter.createFromResource(this, R.array.ReminderTimes, android.R.layout.simple_spinner_item);
        reminderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TaskReminder.setAdapter(reminderAdapter);
        TaskReminder.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (((String) parent.getItemAtPosition(pos)).compareTo("Custom") == 0) {
                    DisplayReminderDays.setVisibility(View.VISIBLE);
                    DisplayReminderHours.setVisibility(View.VISIBLE);

                    ChooseReminderDays.setVisibility(View.VISIBLE);
                    ChooseReminderHours.setVisibility(View.VISIBLE);

                } else {
                    DisplayReminderDays.setVisibility(View.INVISIBLE);
                    DisplayReminderHours.setVisibility(View.INVISIBLE);

                    ChooseReminderDays.setVisibility(View.INVISIBLE);
                    ChooseReminderHours.setVisibility(View.INVISIBLE);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        DateDue.setOnFocusChangeListener(new EditText.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus)
                    onDateClick(view);
            }
        });

        if (newTask == null) {
            setTitle("New Task");
            DueInMilliseconds.setText(Long.toString(Calendar.getInstance().getTimeInMillis()));
            findViewById(R.id.bDelete).setVisibility(View.INVISIBLE);
            TimeForTaskEntered.setProgress(4);
            TimeForTaskDisplay.setText(durationTimes[4]);
        }

        else {
            setTitle("Edit Task");
            ((Button) findViewById(R.id.bSubmit)).setText("Save");

            // fill inputs with task properties
            Task.setText(newTask.get(TaskDatabase.COLUMN_TASK));
            Class.setText(newTask.get(TaskDatabase.COLUMN_CLASS));
            setDateTime(Long.parseLong(newTask.get(TaskDatabase.COLUMN_DATE_DUE)));

            TimeForTaskEntered.setProgress(Integer.parseInt(newTask.get(TaskDatabase.COLUMN_DURATION_INPUT)));
            TimeForTaskDisplay.setText(durationTimes[Integer.parseInt(newTask.get((TaskDatabase.COLUMN_DURATION_INPUT)))]);

            TaskReminder.setSelection(Integer.parseInt(newTask.get(TaskDatabase.COLUMN_REMINDER_INPUT)));
            ChooseReminderHours.setSelection(Integer.parseInt(newTask.get(TaskDatabase.COLUMN_REMINDER_HOURS)));
            ChooseReminderDays.setSelection(Integer.parseInt(newTask.get(TaskDatabase.COLUMN_REMINDER_DAYS)));

            ImportanceOfTask.setProgress(Integer.parseInt(newTask.get(TaskDatabase.COLUMN_IMPORTANCE)));
        }

        TimeForTaskEntered.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TimeForTaskDisplay.setText(durationTimes[progress]);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    // callback for the datetime picker that's triggered by onDateClick
    public void setDateTime(int year, int month, int dayOfMonth, int hour, int minute) {
        Calendar datetime = new GregorianCalendar(year, month, dayOfMonth, hour, minute);

        DueInMilliseconds.setText(Long.toString(datetime.getTimeInMillis()));
        DateDue.setText(getDateTimeInstance().format(datetime.getTime()));
    }

    public void setDateTime(long millis) {
        DueInMilliseconds.setText(Long.toString(millis));
        DateDue.setText(getDateTimeInstance().format(new Date(millis)));
    }

    // display the date & time picker dialogs
    public void onDateClick(View v) {
        new DateTimePicker(this, Long.parseLong("" + DueInMilliseconds.getText())).show();
    }

    // convert string displayed above duration slider/seekbar to miliseconds (as string)
    private String getDuration() throws InvalidParameterException {
        Pattern p = Pattern.compile("^([0-9]+):([0-9]+)");
        Matcher match = p.matcher(durationTimes[TimeForTaskEntered.getProgress()]);
        match.find();

        Integer hours = new Integer(match.group(1));
        Integer minutes = new Integer(match.group(2));
        return new Integer(hours * 60 * 60 * 1000 + minutes * 60 * 1000).toString(); // calculate miliseconds and return
    }

    // return the amount of time before the task is due to display a reminder
    private String getReminderTime() throws InvalidParameterException {
        switch ((String) TaskReminder.getSelectedItem()) {
            case "Custom":
                Integer days = new Integer(ChooseReminderDays.getSelectedItem().toString());
                Integer hours = new Integer(ChooseReminderHours.getSelectedItem().toString());

                return new Integer(days * 24 * 60 * 60 * 1000 + days * 60 * 60 * 1000).toString();
            case "None":
                return "0";
            default:
                Pattern pattern = Pattern.compile("^(\\d+) (\\w+[^s])s?$");
                Matcher match = pattern.matcher((String) TaskReminder.getSelectedItem());
                match.find();

                Integer num = new Integer(match.group(1));
                switch (match.group(2)) {
                    case "minute":
                        return new Integer(num * 60 * 1000).toString();
                    case "hour":
                        return new Integer(num * 60 * 60 * 1000).toString();
                    case "day":
                        return new Integer(num * 24 * 60 * 60 * 1000).toString();
                    case "week":
                        return new Integer(num * 7 * 24 * 60 * 60 * 1000).toString();
                    default:
                        throw new InvalidParameterException();
                }
        }
    }

    // create the new task (called when "add task" button is pressed)
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void submit(View v) throws Exception {
        // TODO: better error handling
        if (Task.getText().length() == 0)
            Snackbar.make(findViewById(android.R.id.content), "Please enter a task name", Snackbar.LENGTH_LONG)
                    .show();

        else if (Class.getText().length() == 0)
            Snackbar.make(findViewById(android.R.id.content), "Please enter a class name", Snackbar.LENGTH_LONG)
                    .show();

        else {
            HashMap<String, String> properties = new HashMap<>();

            // load existing task id if editing a task
            if (newTask != null)
                properties.put(TaskDatabase.COLUMN_ID, newTask.getId().toString());

            properties.put(TaskDatabase.COLUMN_TASK, Task.getText().toString());
            properties.put(TaskDatabase.COLUMN_CLASS, Class.getText().toString());
            properties.put(TaskDatabase.COLUMN_DATE_DUE, DueInMilliseconds.getText().toString());
            properties.put(TaskDatabase.COLUMN_DURATION_TIME, getDuration());
            properties.put(TaskDatabase.COLUMN_IMPORTANCE, Integer.toString(ImportanceOfTask.getProgress()));
            properties.put(TaskDatabase.COLUMN_REMINDER_TIME, getReminderTime());
            properties.put(TaskDatabase.COLUMN_DURATION_INPUT, Integer.toString(TimeForTaskEntered.getProgress()));
            properties.put(TaskDatabase.COLUMN_REMINDER_INPUT, Long.toString(TaskReminder.getSelectedItemId()));
            properties.put(TaskDatabase.COLUMN_REMINDER_DAYS, ChooseReminderDays.getSelectedItem().toString());
            properties.put(TaskDatabase.COLUMN_REMINDER_HOURS, ChooseReminderHours.getSelectedItem().toString());

            if (newTask == null) {
                newTask = new Task(properties);

                // add new task to database
                Long id = new Long(newTask.insertTask());
                newTask.set(TaskDatabase.COLUMN_ID, id.toString());
            } else {
                newTask = new Task(properties);

                // cancel old alarms
                Alarm.cancelOverdueAlarm(newTask.getId());
                Alarm.cancelReminderAlarm(newTask.getId());

                // update task in database
                newTask.updateTask();
            }

            long now = Calendar.getInstance().getTimeInMillis();

            // create an overdue alarm for the task
            if (now < Long.parseLong(newTask.get(TaskDatabase.COLUMN_DATE_DUE)))
                Alarm.setOverdueAlarm(newTask);

            // create a reminder alarm for the task
            if (newTask.get(TaskDatabase.COLUMN_REMINDER_TIME) != "0")
                Alarm.setReminderAlarm(newTask);

            setResult(RESULT_OK, null);
            finish();
        }
    }

    // called when delete button is clicked
    public void delete(final View view) {
        new AlertDialog.Builder(view.getContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to permanently delete this task?")   // TODO: move to strings.xml
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newTask.delete();
                        setResult(RESULT_OK, null);
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
