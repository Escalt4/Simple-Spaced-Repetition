package com.example.simpleintervals;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class ListOfPlanned extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_planned);
    }


    // Обработка нажатий кнопок
    public void onClick(View view) {
        switch (view.getId()) {
            //
            case R.id.buttonImport:
                break;

            //
            case R.id.buttonExport:
                break;

            default:
                break;
        }
    }

}