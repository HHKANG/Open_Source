package com.example.samsung.os;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Find_ID_Password extends AppCompatActivity {
    LinearLayout layout;
    Button find_pass, find_id;
    RadioButton selectMan, selectWoman;
    String gender;
    ArrayList arrayY, arrayM, arrayD;
    ArrayAdapter adapter;
    Spinner inputY, inputM, inputD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find__id__password);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // 성별 radio event 처리
        selectMan = (RadioButton)findViewById(R.id.man);
        selectWoman = (RadioButton)findViewById(R.id.woman);
        RadioButton.OnClickListener genderListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectMan.isChecked())
                    gender = "M";
                else if(selectWoman.isChecked())
                    gender = "F";
                Toast.makeText(getApplicationContext(),gender,Toast.LENGTH_SHORT).show();
            }
        };
        selectMan.setOnClickListener(genderListener);
        selectWoman.setOnClickListener(genderListener);

        // spinner 설정에 필요한 올해 연도 가져오기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        String currentY = "2017";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        currentY = sdf.format(date);

        // spinner 연도 설정 (현재연도 ~ 1900)
        arrayY = new ArrayList();
        for(int i = Integer.parseInt(currentY);i >= 1900;i--)
            arrayY.add(i);
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayY);
        inputY = (Spinner)findViewById(R.id.year);
        inputY.setAdapter(adapter);

        // spinner 월 설정
        arrayM = new ArrayList();
        for(int i = 1;i <= 12;i++)
            arrayM.add(i);
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayM);
        inputM = (Spinner)findViewById(R.id.month);
        inputM.setAdapter(adapter);

        // spinner 일 설정 (월별 다른 최대 일 수 변경, 윤달 X)
        inputD = (Spinner)findViewById(R.id.day);
        arrayD = new ArrayList();
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayD);
        inputM.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int maxD;
                String tempM = inputM.getSelectedItem().toString();
                switch (tempM){
                    case "1":
                    case "3":
                    case "5":
                    case "7":
                    case "8":
                    case "10":
                    case "12":
                        maxD = 31;
                        break;
                    case "2":
                        maxD = 28;
                        break;
                    case "4":
                    case "6":
                    case "9":
                    case "11":
                        maxD = 30;
                        break;
                    default:
                        maxD = 31;
                        break;
                }
                arrayD.clear();
                for(int i = 1;i <= maxD;i++)
                    arrayD.add(i);
                inputD.setAdapter(adapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        find_id = (Button)findViewById(R.id.find_id);
        find_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout = (LinearLayout)findViewById(R.id.enter_id);
                layout.setVisibility(View.GONE);
            }
        });
        // 아이디 입력창 만들기
        find_pass = (Button)findViewById(R.id.find_pass);
        find_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout = (LinearLayout)findViewById(R.id.enter_id);
                layout.setVisibility(View.VISIBLE);
            }
        });
    }

    //Action Bar에 메뉴를 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.signup_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.signup:
                Intent intent = new Intent(Find_ID_Password.this, Sign_Up.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
