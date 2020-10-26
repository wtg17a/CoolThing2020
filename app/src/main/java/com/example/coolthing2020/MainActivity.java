package com.example.coolthing2020;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String guess;
    private String[] passwords;
    private String[][] clues;
    private String[] levelTitles;
    private String packageName;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefEdit;
    private int currentLevel;
    private int highestLevelCleared;

    private TextView levelTitle;
    private TextView clue1;
    private TextView clue2;
    private TextView levelCounter;
    private EditText guessField;
    private Button submitButton;
    private Button prevButton;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        packageName = getApplicationContext().getPackageName();
        prefs = getSharedPreferences("myPrefs", 0);
        prefEdit = prefs.edit();
        highestLevelCleared = prefs.getInt("highScore", 0);
        levelTitle = findViewById(R.id.level_label);
        levelCounter = findViewById(R.id.level_counter);
        clue1 = findViewById(R.id.clue_text_1);
        clue2 = findViewById(R.id.clue_text_2);
        guessField = findViewById(R.id.edit_text_guess);

        submitButton = findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comparePassword();
            }
        });

        prevButton = findViewById(R.id.button_prev);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initLevel(currentLevel-1);
            }
        });

        nextButton = findViewById(R.id.button_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentLevel <= highestLevelCleared)
                {
                    initLevel(currentLevel+1);
                }
            }
        });

        passwords = new String[10];
        clues = new String[10][2];
        levelTitles = new String[10];
        fillArrays();
        initLevel(1);
    }

    private void fillArrays()
    {
        for(int i = 0; i < getResources().getInteger(R.integer.max_level); ++i)
        {
            int id = this.getResources().getIdentifier("password" + (i+1), "string", packageName);
            passwords[i] = getString(id);

            id = this.getResources().getIdentifier("clue" + (i+1) + "_" + 1, "string", packageName);
            clues[i][0] = getString(id);

            id = this.getResources().getIdentifier("clue" + (i+1) + "_" + 2, "string", packageName);
            clues[i][1] = getString(id);

            id = this.getResources().getIdentifier("level" + (i+1), "string", packageName);
            levelTitles[i] = getString(id);
        }
    }

    private void initLevel(int level)
    {
        if(level < 1 || level > passwords.length)
        {
            return;
        }

        levelTitle.setText(levelTitles[level-1]);
        clue1.setText(clues[level-1][0]);
        clue2.setText(clues[level-1][1]);
        currentLevel = level;
        int cap = Math.min(highestLevelCleared+1, getResources().getInteger(R.integer.max_level));
        String counterText = currentLevel + "/" + cap;
        levelCounter.setText(counterText);
    }

    private void comparePassword()
    {
        if(guessField.getText().toString().equals(passwords[currentLevel-1]))
        {
            if(currentLevel > highestLevelCleared)
            {
                highestLevelCleared = currentLevel;
                prefEdit.putInt("highScore", highestLevelCleared);
                prefEdit.commit();
            }

            ++currentLevel;
            initLevel(currentLevel);
            Toast toast = Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT);
            toast.show();
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Incorrect...", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}