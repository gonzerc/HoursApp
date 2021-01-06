package com.example.tanyaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    EditText sessionCategoryText;
    EditText startTimeText;
    Button startTimePeriodButton;
    EditText endTimeText;
    Button endTimePeriodButton;
    Button addButton;
    TextView listView;


    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("hh:mm a");
    HashMap<String, Float> hours;
    StringBuilder listString;
    boolean startIsAM;
    boolean endIsAM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionCategoryText = findViewById(R.id.sessionCategoryText);
        startTimeText = findViewById(R.id.startTime);
        startTimePeriodButton = findViewById(R.id.startTimePeriod);
        endTimeText = findViewById(R.id.endTime);
        endTimePeriodButton = findViewById(R.id.endTimePeriod);
        addButton = findViewById(R.id.addButton);
        listView = findViewById(R.id.listView);

        hours = new HashMap<>();
        listString = new StringBuilder();
        startIsAM = true;
        endIsAM = true;


        startTimePeriodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startIsAM = !startIsAM;

                if(!startIsAM){
                    startTimePeriodButton.setText("PM");
                }
                else{
                    startTimePeriodButton.setText("AM");
                }
            }
        });

        endTimePeriodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endIsAM = !endIsAM;

                if(!endIsAM){
                    endTimePeriodButton.setText("PM");
                }
                else{
                    endTimePeriodButton.setText("AM");
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sessionCategory;
                String startTime;
                String endTime;

                sessionCategory = sessionCategoryText.getText().toString();
                startTime = startTimeText.getText().toString() + " " + startTimePeriodButton.getText().toString();
                endTime = endTimeText.getText().toString() + " " + endTimePeriodButton.getText().toString();

                SendAlert(sessionCategory, startTime, endTime);

            }
        });
    }

    private void SendAlert(final String sessionCategory, final String startTime, final String endTime){
        StringBuilder message = new StringBuilder();

        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog alertDialog = alert.create();

        message.append("Create Session " + sessionCategory + " ?\n" +
                startTime + " - " + endTime);


        alert.setMessage(message);

        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AddSession(sessionCategory, startTime, endTime);
            }
        });

        alert.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing
            }
        });

        AlertDialog goodAlert = alert.create();
        goodAlert.show();
    }

    private void AddSession(String sessionCategory, String startTime, String endTime){

        float time = 0.0f;

        try {

            time = CalculateTime(startTime, endTime);

            if(hours.isEmpty() || !hours.containsKey(sessionCategory)){
                hours.put(sessionCategory, time);
            }
            else{
                float currentTime = hours.get(sessionCategory);
                float newTime = currentTime + time;
                hours.put(sessionCategory, newTime);
            }

            listString.setLength(0);
            listString.append("Total Hours:\n");
            for(Map.Entry<String, Float> entry : hours.entrySet()){
                listString.append(entry.getKey()).append(" : ").append(String.format("%.2f", entry.getValue())).append("\n");
            }

            listView.setText(listString.toString());

            ClearText();

        }catch (Exception e){
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private float CalculateTime(String start, String end) throws Exception{

        Date startTime = mSimpleDateFormat.parse(start);
        Date endTime = mSimpleDateFormat.parse(end);

        float difference = endTime.getTime() - startTime.getTime();
        float minutes = difference / 1000 / 60;
        float timeDif = minutes / 60;

        return timeDif;
    }

    private void ClearText(){
        sessionCategoryText.setText("");
        startTimeText.setText("");
        endTimeText.setText("");
    }
}
