package com.example.simpleintervals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String DATABASE_FILE_NAME = "DATABASE";
    public static final String SCHEDULED_FILE_NAME = "SCHEDULED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            //
            case R.id.buttonLearnNew:
                Intent intentLearnNew = new Intent(this, LearnWord.class);
                intentLearnNew.putExtra("mode", false);
                startActivity(intentLearnNew);
                break;
            //
            case R.id.buttonIntervalRepetition:
                Intent intentIntervalRepetition = new Intent(this, LearnWord.class);
                intentIntervalRepetition.putExtra("mode", true);
                startActivity(intentIntervalRepetition);
                break;
            //
            case R.id.buttonSettings:
//                Intent intentSettings = new Intent(this, Settings.class);
//                startActivity(intentSettings);
                break;
            //
//            case R.id.buttonStatistics:
//                break;
            //
            case R.id.buttonListOfPlanned:
                Intent intentListOfPlanned = new Intent(this, ListOfPlanned.class);
                startActivity(intentListOfPlanned);
                break;

            default:
                break;
        }
    }
}