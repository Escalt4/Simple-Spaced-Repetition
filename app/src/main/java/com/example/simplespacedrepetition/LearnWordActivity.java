package com.example.simplespacedrepetition;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LearnWordActivity extends AppCompatActivity {

    // TODO: выхожу из изучения\повторения - предупредение : количество правильных ответов будет сброшено

    String DATABASE_FILE_NAME;

    public static final Integer NUMBER_OF_SIMULTANEOUSLY_STUDIED_WORDS = 25;
    public static final Integer REQUIRED_QUANTITY_CORRECT_ANSWERS = 3;
    public static final Integer[] INTERVALS = {86400, 172800, 432000, 1123200, 2678400, 6739200, 16848000, 42163200, 105494400, 263692800, 659145600, 1647907200};

    TextView textViewWord;
    TextView textViewKeys;
    TextView textViewIndicators;

    Word[] database;
    Integer[] randomIndexesDB;
    ArrayList<Integer> indexesInUse = new ArrayList<>();
    Integer currentIndex = -1;

    Word history;

    Long unixTime;
    boolean spacedRepetition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_word);

        spacedRepetition = getIntent().getExtras().getBoolean("mode");

        if (spacedRepetition) {
            this.setTitle("Интервальные повторения");
        } else {
            this.setTitle("Изучение новых слов");
        }

        DATABASE_FILE_NAME = getResources().getString(R.string.database_file_name);

        textViewWord = findViewById(R.id.textViewWord);
        textViewKeys = findViewById(R.id.textViewKeys);
        textViewIndicators = findViewById(R.id.textViewIndicators);

        if (importFromDB()) {
            shuffleIndex();
            addInIndexesInUse();
            displayNextWord();
        }
    }


    // импорт слов из базы данных
    boolean importFromDB() {
        try {
            FileInputStream fileInputStream = openFileInput(DATABASE_FILE_NAME);
            byte[] bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes);
            fileInputStream.close();

            String text = new String(bytes);

            if (text.equals("")) {
                logAndToast("База данных пуста", true);
                return false;
            }

            String[] strings = text.split("\n");

            database = new Word[strings.length];

            for (int i = 0; i < strings.length; i++) {
                String[] string = strings[i].split(";");

                database[i] = new Word(string[0],
                        Integer.parseInt(string[1]),
                        Integer.parseInt(string[2]),
                        Arrays.copyOfRange(string, 3, string.length));
            }
            return true;
        } catch (Exception ex) {
            logAndToast(ex.getMessage(), true);
            return false;
        }
    }

    // создать перемешаный список индексов элементов базы данных
    void shuffleIndex() {
        try {
            randomIndexesDB = new Integer[database.length];

            for (int i = 0; i < randomIndexesDB.length; i++) {
                randomIndexesDB[i] = i;
            }

            List intList = Arrays.asList(randomIndexesDB);
            Collections.shuffle(intList);
            intList.toArray(randomIndexesDB);
        } catch (Exception ex) {
            logAndToast(ex.getMessage(), true);
        }
    }

    // много условий вынес в отдельную функцию
    boolean smart_if(Word dbElement, boolean spacedRepetition) {
        // проверка не взято ли это слово уже для изучения
        if (dbElement.getUsageNow()) {
            return false;
        }
        // в зависимости от того изучение новых слов или интервальное повторение
        if (spacedRepetition) {
            return dbElement.getLastDate() != 0 && unixTime - dbElement.getLastDate() > dbElement.getCurInterval();
        } else {
            return dbElement.getLastDate() == 0;
        }
    }

    // добавить слова в список изучаемого
    void addInIndexesInUse() {
        try {
            if (indexesInUse.size() < NUMBER_OF_SIMULTANEOUSLY_STUDIED_WORDS) {
                unixTime = System.currentTimeMillis() / 1000;

                for (int i = 0; i < database.length; i++) {
                    if (smart_if(database[randomIndexesDB[i]], spacedRepetition)) {
                        indexesInUse.add(randomIndexesDB[i]);
                    }
                    if (indexesInUse.size() == NUMBER_OF_SIMULTANEOUSLY_STUDIED_WORDS) {
                        break;
                    }
                }

                if (indexesInUse.size() == 0) {
                    logAndToast("В базе данных нет слов для изучения", true);
                }
            }
        } catch (Exception ex) {
            logAndToast(ex.getMessage(), true);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        export_to_bd();
    }

    // экспорт в базу данных
    void export_to_bd() {
        try {
            FileOutputStream fileOutputStream = openFileOutput(DATABASE_FILE_NAME, MODE_PRIVATE);
            for (int i = 0; i < database.length; i++) {
                fileOutputStream.write(database[i].getAllToSaveDB().getBytes());
            }
            fileOutputStream.close();
        } catch (Exception ex) {
            logAndToast(ex.getMessage(), true);
        }
    }

    // вывод перевода текущего слова на экран
    public void displayKeys() {
        try {
            textViewKeys.setText(database[indexesInUse.get(currentIndex)].getKeysSeparatedLineBreak());
        } catch (Exception ex) {
            logAndToast(ex.getMessage(), true);
        }
    }

    // обработка ответа
    public void ifAnswerIs(boolean correct) {
        textViewKeys.setText("");
        history = database[indexesInUse.get(currentIndex)];
        findViewById(R.id.buttonUndo).setEnabled(true);

        if (correct) {
            // смотрим сколько уже правильных ответов подряд дано
            Integer numberCorrectAnswers = database[indexesInUse.get(currentIndex)].getNumberCorrectAnswers();


            if (numberCorrectAnswers == REQUIRED_QUANTITY_CORRECT_ANSWERS) {
                // если нужное число ответов набрано, ставим текущую дату и следующий интервал
                database[indexesInUse.get(currentIndex)].setQuantityCorrectAnswers(0);
                Integer CurInterval = database[indexesInUse.get(currentIndex)].getCurInterval();
                for (int i = 0; i < INTERVALS.length; i++) {
                    if (INTERVALS[i] > CurInterval) {
                        database[indexesInUse.get(currentIndex)].setCurInterval(INTERVALS[i]);
                        break;
                    }
                }
                database[indexesInUse.get(currentIndex)].setLastDate(Math.round(System.currentTimeMillis() / 1000));
            } else {
                // иначе увеличиваем количество правильных ответов
                database[indexesInUse.get(currentIndex)].setQuantityCorrectAnswers(numberCorrectAnswers + 1);
            }
        } else {
            // если ответ давн неверно сбрасываем количество правильных ответов
            database[indexesInUse.get(currentIndex)].setQuantityCorrectAnswers(0);
        }

        displayNextWord();
    }


    // вывод следующего слова на экран
    public void displayNextWord() {
        if (indexesInUse.size() > 0) {
            currentIndex += 1;
            if (currentIndex == indexesInUse.size()) {
                currentIndex = 0;
            }

            // обновление индикатора номера слова и количества правильных ответов
            StringBuffer toTextView = new StringBuffer();

            toTextView.append(database[indexesInUse.get(currentIndex)].getNumberCorrectAnswers());
            toTextView.append("/");
            toTextView.append(REQUIRED_QUANTITY_CORRECT_ANSWERS);
            toTextView.append("\n");
            toTextView.append(currentIndex + 1);
            toTextView.append("/");
            toTextView.append(indexesInUse.size());

            textViewIndicators.setText(toTextView);

            textViewWord.setText(database[indexesInUse.get(currentIndex)].getWord());
        }
    }

    // обработка ошибок и сообщений
    public void logAndToast(String text, Boolean itIsError) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

        if (itIsError) {
            Log.e("SimpleIntervals", text);
            this.finish();
        } else {
            Log.d("SimpleIntervals", text);
        }
    }

    // делаем кнопки неактивными когда их нельзя использовать
    public void onOffButtons(boolean itIsAnswer) {
        if (itIsAnswer) {
            findViewById(R.id.buttonShowAnswer).setEnabled(false);
            findViewById(R.id.buttonTrue).setEnabled(true);
            findViewById(R.id.buttonFalse).setEnabled(true);
        } else {
            findViewById(R.id.buttonTrue).setEnabled(false);
            findViewById(R.id.buttonFalse).setEnabled(false);
            findViewById(R.id.buttonShowAnswer).setEnabled(true);
        }
    }


    //
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                database[indexesInUse.get(currentIndex)] = (Word) resultData.getSerializableExtra("word");

                textViewWord.setText(database[indexesInUse.get(currentIndex)].getWord());
                if (textViewKeys.getText().toString().length() != 0) {
                    textViewKeys.setText(database[indexesInUse.get(currentIndex)].getKeysSeparatedLineBreak());
                }
            }
        }
    }

    // Обработка нажатий кнопок
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonTrue:
                onOffButtons(false);
                ifAnswerIs(true);
                break;

            case R.id.buttonFalse:
                onOffButtons(false);
                ifAnswerIs(false);
                break;

            case R.id.buttonShowAnswer:
                onOffButtons(true);
                displayKeys();
                break;

            case R.id.buttonUndo:
                findViewById(R.id.buttonUndo).setEnabled(false);
                onOffButtons(false);
                textViewKeys.setText("");
                currentIndex -= 1;
                database[indexesInUse.get(currentIndex)] = history;
                currentIndex -= 1;

                displayNextWord();
                break;

            case R.id.buttonSkip:
                onOffButtons(false);
                textViewKeys.setText("");
                history = database[indexesInUse.get(currentIndex)];
                findViewById(R.id.buttonUndo).setEnabled(true);
                displayNextWord();
                break;

            case R.id.buttonEdit:
                Intent intentEdit = new Intent(this, EditWordActivity.class);
                intentEdit.putExtra("word", database[indexesInUse.get(currentIndex)]);
                startActivityForResult(intentEdit, 0);
                break;

            default:
                break;
        }
    }
}

