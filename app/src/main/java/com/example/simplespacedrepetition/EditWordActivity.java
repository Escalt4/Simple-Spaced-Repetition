package com.example.simplespacedrepetition;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditWordActivity extends AppCompatActivity {
    EditText editTextWord;
    EditText editTextKeys;

    Word word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word);

        word = (Word) getIntent().getExtras().getSerializable("word");

        editTextWord = findViewById(R.id.editTextWord);
        editTextKeys = findViewById(R.id.editTextKeys);

        editTextWord.setText(word.getWord());
        editTextKeys.setText(word.getKeysSeparatedLineBreak());
    }


    void saveAndExit() {
        word.setWord(editTextWord.getText().toString());
        word.setKeys(editTextKeys.getText().toString().split("\n"));

        Intent intent = new Intent();
        intent.putExtra("word", word);
        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }

    // Обработка нажатий кнопок
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSave:
                saveAndExit();
                break;

            default:
                break;
        }
    }

}