package com.example.samsung.os;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Main_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        //actionbar 가장 왼쪽에 뒤로가기 버튼 설정
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        Toast.makeText(getApplicationContext(),key,Toast.LENGTH_SHORT).show();
    }
    //actionbar 우측에 메뉴 설정
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.upload, menu);
        return true;
    }

    //actionbar에서 뒤로가기 버튼을 누를 경우 가장 최근에 열려있는 화면으로 넘어가고 현재 화면은 종료
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.upload:
                Intent intent = new Intent(getApplicationContext(), Upload_Sns.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}