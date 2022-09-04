package com.example.simplespacedrepetition;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class ListOfPlannedActivity extends AppCompatActivity {
    String SCHEDULED_FILE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_planned);

        this.setTitle("Список запланированого");

        SCHEDULED_FILE_NAME = getResources().getString(R.string.scheduled_file_name);
    }


    // Обработка нажатий кнопок
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonImport:
                break;

            case R.id.buttonExport:
                break;

            default:
                break;
        }
    }

}