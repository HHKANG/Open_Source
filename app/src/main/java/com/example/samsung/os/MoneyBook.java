package com.example.samsung.os;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

public class MoneyBook extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_book);

        CalendarView calendar = (CalendarView)findViewById(R.id.calendar);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth){
                //Toast.makeText(MoneyBook.this, ""+year+"/"+(month+1)+"/"+dayOfMonth, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MoneyBook.this, SelMoneyBook.class);
                intent.putExtra("year",year);
                intent.putExtra("month",(month+1));
                intent.putExtra("dayOfmonth",dayOfMonth);
                startActivity(intent);
            }
        });
    }
}
