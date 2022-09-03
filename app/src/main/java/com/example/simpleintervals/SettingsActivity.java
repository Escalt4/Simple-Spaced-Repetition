package com.example.simpleintervals;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public class SettingsActivity extends AppCompatActivity  {
    public static final String DATABASE_FILE_NAME = "DATABASE";
    public static final String SCHEDULED_FILE_NAME = "SCHEDULED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();

        this.setTitle("Настройки");
    }


    // добавление новых слов в базу данных
    // выбор файла со словами
    private void openFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, 0);
    }

    // чтение выбраного файла
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                Uri uri = resultData.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);

                    byte[] buffer = new byte[inputStream.available()];
                    inputStream.read(buffer);
                    inputStream.close();
                    String text = new String(buffer);

                    addWordsAtDB(text);
                } catch (Exception ex) {
                    logAndToast(ex.getMessage(), true);
                }
            }
        }
    }

    // добавление в базу данных полученых из файла слов
    public void addWordsAtDB(String text) {
        try {
            FileOutputStream fos = openFileOutput(DATABASE_FILE_NAME, MODE_APPEND);

            String[] strings = text.split("\n");

            for (int i = 0; i < strings.length; i++) {
                String[] string = strings[i].split(";");
                // формат бд: слово;дата последнего изменения интервала;длина текущего интервала;значение слова
                fos.write((string[0] + ";0;0;").getBytes());
                for (int j = 1; j < string.length; j++) {
                    fos.write((string[j].replace("\n", "").replace("\r", "") + ";").getBytes());
                }
                fos.write(("\n").getBytes());
            }
            fos.close();

            logAndToast("Слова успешно импортированы", false);
        } catch (Exception ex) {
            logAndToast(ex.getMessage(), true);
        }
    }


//    // это для функции редактирования бд
//    // вывод содержания базы данных на экран
//    public void openText() {
//        try {
//            String text;
//            String[] Keys;
//            for (int i = 0; i < database.length; i++) {
//                text = database[i].getWord() + " " +
//                        database[i].getLastDate() + " " +
//                        database[i].getCurInterval() + " " +
//                        database[i].getNumberCorrectAnswers() + " ";
//
//                Keys = database[i].getKeys_as_array();
//
//                for (int j = 0; j < Keys.length; j++) {
//                    text += Keys[j] + " ";
//                }
//
//                Log.d("SimpleIntervals", text);
//            }
//        } catch (Exception ex) {
//            logAndToast(ex.getMessage(), true);
//        }
//    }

    // очистка базы данных
    public void cleanupDB() {
        File file_db = new File(this.getFilesDir(), DATABASE_FILE_NAME);

        if (file_db.length() == 0) {
            logAndToast("База данных пуста", false);
        } else {
            try {
                FileOutputStream fos = openFileOutput(DATABASE_FILE_NAME, MODE_PRIVATE);
                fos.write("".getBytes());
                fos.close();
                logAndToast("База данных очищена", false);
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
//            case R.id.button_1:
//                openFile();
//                break;
//
//            case R.id.button_2:
//                break;
//
//            case R.id.button_3:
//                cleanupDB();
//                break;
//
//            case R.id.button_4:
//                break;

            default:
                break;
        }
    }

}