package com.example.samsung.os;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Find_ID_Password extends AppCompatActivity {
    LinearLayout layout;
    Button find_pass, find_id, okay, cancel;
    RadioButton selectMan, selectWoman;
    String gender, new_pw;
    ArrayList arrayY, arrayM, arrayD;
    ArrayAdapter adapter;
    Spinner inputY, inputM, inputD;
    EditText name_text, id_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find__id__password);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        name_text = (EditText)findViewById(R.id.name);
        id_text = (EditText)findViewById(R.id.id);

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

        okay = (Button)findViewById(R.id.okay);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name_text.getText().toString().equals("") || name_text.getText().toString().equals(null)){
                    Toast.makeText(getApplicationContext(),"이름을 입력해 주세요",Toast.LENGTH_SHORT).show();
                }
                else if(!selectMan.isChecked() && !selectWoman.isChecked()){
                    Toast.makeText(getApplicationContext(),"성별을 선택해 주세요",Toast.LENGTH_SHORT).show();
                }

                //입력한 조건이 서버에 없는 경우
                /*
                else if(){
                }
                */

                else
                    Show_find_id();
            }
        });

        find_id = (Button)findViewById(R.id.find_id);
        find_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout = (LinearLayout)findViewById(R.id.enter_id);
                layout.setVisibility(View.GONE);

                okay = (Button)findViewById(R.id.okay);
                okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(name_text.getText().toString().equals("") || name_text.getText().toString().equals(null)){
                            Toast.makeText(getApplicationContext(),"이름을 입력해 주세요",Toast.LENGTH_SHORT).show();
                        }
                        else if(!selectMan.isChecked() && !selectWoman.isChecked()){
                            Toast.makeText(getApplicationContext(),"성별을 선택해 주세요",Toast.LENGTH_SHORT).show();
                        }

                        //입력한 조건이 서버에 없는 경우
                        /*
                        else if(){
                        }
                        */

                        else
                            Show_find_id();
                    }
                });
            }
        });
        // 아이디 입력창 만들기
        find_pass = (Button)findViewById(R.id.find_pass);
        find_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout = (LinearLayout)findViewById(R.id.enter_id);
                layout.setVisibility(View.VISIBLE);

                okay = (Button)findViewById(R.id.okay);
                okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(name_text.getText().toString().equals("") || name_text.getText().toString().equals(null)){
                            Toast.makeText(getApplicationContext(),"이름을 입력해 주세요",Toast.LENGTH_SHORT).show();
                        }
                        else if(id_text.getText().toString().equals("") || id_text.getText().toString().equals(null)){
                            Toast.makeText(getApplicationContext(),"아이디를 입력해 주세요",Toast.LENGTH_SHORT).show();
                        }
                        else if(!selectMan.isChecked() && !selectWoman.isChecked()){
                            Toast.makeText(getApplicationContext(),"성별을 선택해 주세요",Toast.LENGTH_SHORT).show();
                        }

                        //입력한 조건이 서버에 없는 경우
                        /*
                        else if(){
                        }
                        */
                        else
                            Change_password_dialog();
                    }
                });
            }
        });

        cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Show_find_id(){
        AlertDialog.Builder id_dialog = new AlertDialog.Builder(this);
        LayoutInflater id_inflater = getLayoutInflater();
        View id_view = id_inflater.inflate(R.layout.find_id_dialog, null);
        id_dialog.setView(id_view);
        id_dialog.setTitle("아이디 찾기");
        id_dialog.setPositiveButton("Okay", null);
        final TextView dialog_id = (TextView)id_view.findViewById(R.id.dialog_id);
        dialog_id.setText("서버와 연동하기");
        id_dialog.show();
    }

    //비밀번호 찾기 완료시 비밀번호를 바꾸도록 하는 Dilog 화면
    private void Change_password_dialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.change_password, null);
        dialog.setView(view);
        dialog.setTitle("Change Password");
        dialog.setPositiveButton("change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final EditText change_pass = (EditText)((AlertDialog)dialogInterface).findViewById(R.id.change_pass);
                final EditText check_pass = (EditText)((AlertDialog)dialogInterface).findViewById(R.id.check_pass);
                final String change, check;

                change = change_pass.getText().toString();
                check = check_pass.getText().toString();

                if(change.equals("") || change.equals(null) || check.equals("") || check.equals(null)){
                    Toast.makeText(getApplicationContext(),"비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else if(change.equals(check)){
                    new_pw = change_pass.getText().toString();

                    Toast.makeText(getApplicationContext(),"비밀번호가 변경되었습니다.(서버와 연동 구현하기)", Toast.LENGTH_SHORT).show();
                    //비밀번호 변경 서버 주소
                    //String url = "";

                    //서버에 데이터를 보냄
                    /*
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Intent intent = new Intent(Find_ID_Password.this, Log_In.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }, new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                        }
                    });

                    RequestQueue postrequestQueue = Volley.newRequestQueue(Simpleinfo.this);
                    postrequestQueue.add(stringRequest);*/
                }
                else
                    Toast.makeText(getApplicationContext(),"Password not match", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setNegativeButton("cancel", null);
        dialog.show();
    }
}