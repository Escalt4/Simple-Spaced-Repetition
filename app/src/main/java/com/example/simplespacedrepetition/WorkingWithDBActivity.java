package com.example.simplespacedrepetition;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class WorkingWithDBActivity extends AppCompatActivity {

    // TODO: подтверждение очистки бд / обнуления прогресса

    String DATABASE_FILE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_with_db);

        this.setTitle("Работа с базой данных");

        DATABASE_FILE_NAME = getResources().getString(R.string.database_file_name);
    }

    // запрос на открытие файла
    public void openFile(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, requestCode);
    }

    // запрос на сохранение файла
//    public void saveFile(int requestCode) {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("*/*");
//        startActivityForResult(intent, requestCode);
//    }

    // получение результата
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        if (resultData != null && resultCode == Activity.RESULT_OK) {
            Uri uri = resultData.getData();

            switch (requestCode) {
                case 0:
                    addWordsAtDB(uri);
                    return;

                case 1:
                    importDB(uri);
                    return;

                default:
                    return;
            }
        }
    }

    // получение из файла слов и добавление их в базу данных
    public void addWordsAtDB(Uri uri) {
        try {
            FileOutputStream fos = openFileOutput(DATABASE_FILE_NAME, MODE_APPEND);

            InputStream inputStream = getContentResolver().openInputStream(uri);

            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String text = new String(buffer);

            String[] strings = text.split("\n");

            if (!strings[0].contains("IT_IS_VALID_FILE")) {
                logAndToast("Это не файл со словами или", true);
                logAndToast("в начале файла нет строки IT_IS_VALID_FILE", true);
                return;
            }

            for (int i = 1; i < strings.length; i++) {
                String[] string = strings[i].split(";");

                // формат бд: слово;дата последнего изменения интервала;длина текущего интервала;значение слова
                fos.write((string[0].replace("\n", "").replace("\r", "") + ";0;0;").getBytes());
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

    // импорт файла базы данных
    public void importDB(Uri uri) {
        return;
    }

    // экспорт файла базы данных
    public void exportDB() {
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
    public void cleanDB() {
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
            case R.id.buttonExportDB:
//                saveFile(3);
                break;

            case R.id.buttonImportDB:
                openFile(1);
                break;

            case R.id.buttonCleanupDB:
                cleanDB();
                break;

            case R.id.buttonEditDB:
                break;

            case R.id.buttonAddWords:
                openFile(0);
                break;

            default:
                break;
        }
    }
}

