package com.example.to_dolistapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText taskInput;
    private Button addButton;
    private LinearLayout taskContainer;
    private List<String> tasks;

    private static final String FILE_NAME = "tasks.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskInput = findViewById(R.id.taskInput);
        addButton = findViewById(R.id.addButton);
        taskContainer = findViewById(R.id.taskContainer);

        tasks = new ArrayList<>();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = taskInput.getText().toString().trim();
                if (!task.isEmpty()) {
                    tasks.add(task);
                    saveTasks();
                    addTaskView(task);
                    taskInput.setText("");
                }
            }
        });

        loadTasks();
        displayTasks();
    }

    private void addTaskView(final String task) {
        View taskView = LayoutInflater.from(this).inflate(R.layout.tasklayout, null);
        CheckBox checkBox = taskView.findViewById(R.id.checkBox);
        checkBox.setText(task);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasks.remove(task);
                saveTasks();
                taskContainer.removeView((View) v.getParent());
            }
        });

        taskContainer.addView(taskView);
    }

    private void loadTasks() {
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            String tasksString = new String(buffer);
            String[] taskArray = tasksString.split("\n");
            for (String task : taskArray) {
                tasks.add(task);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveTasks() {
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            for (String task : tasks) {
                fos.write((task + "\n").getBytes());
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayTasks() {
        for (String task : tasks) {
            addTaskView(task);
        }
    }
}
