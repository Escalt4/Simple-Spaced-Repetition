package com.example.simplespacedrepetition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final String DATABASE_FILE_NAME = getResources().getString(R.string.database_file_name);
    final String SCHEDULED_FILE_NAME = getResources().getString(R.string.scheduled_file_name);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle("Simple Intervals");

        ifNotExistCreateFile();
    }

    // если не существует то создание пустого файла базы данных и запланированых слов
    public void ifNotExistCreateFile() {
        File fileDataBase = new File(this.getFilesDir(), DATABASE_FILE_NAME);
        if (!fileDataBase.exists()) {
            try {
                FileOutputStream fos = openFileOutput(DATABASE_FILE_NAME, MODE_PRIVATE);
                fos.write("".getBytes());
                fos.close();
            } catch (Exception ex) {
                logAndToast(ex.getMessage(), true);
            }
        }

        File fileScheduled = new File(this.getFilesDir(), SCHEDULED_FILE_NAME);
        if (!fileScheduled.exists()) {
            try {
                FileOutputStream fos = openFileOutput(SCHEDULED_FILE_NAME, MODE_PRIVATE);
                fos.write("".getBytes());
                fos.close();
            } catch (Exception ex) {
                logAndToast(ex.getMessage(), true);
            }
        }

    }

    // обработка ошибок и сообщений
    public void logAndToast(String text, Boolean itIsError) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

        if (itIsError) {
            Log.e("SimpleIntervals", text);
        } else {
            Log.d("SimpleIntervals", text);
        }
    }

    // Обработка нажатий кнопок
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLearnNew:
                Intent intentLearnNew = new Intent(this, LearnWordActivity.class);
                intentLearnNew.putExtra("mode", false);
                startActivity(intentLearnNew);
                break;

            case R.id.buttonSpacedRepetition:
                Intent intentSpacedRepetition = new Intent(this, LearnWordActivity.class);
                intentSpacedRepetition.putExtra("mode", true);
                startActivity(intentSpacedRepetition);
                break;

            case R.id.buttonWorkingWithDB:
                Intent intentWorkingWithDB = new Intent(this, WorkingWithDBActivity.class);
                startActivity(intentWorkingWithDB);
                break;

            case R.id.buttonSettings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                break;

            case R.id.buttonListOfPlanned:
                Intent intentListOfPlanned = new Intent(this, ListOfPlannedActivity.class);
                startActivity(intentListOfPlanned);
                break;

            default:
                break;
        }
    }
}