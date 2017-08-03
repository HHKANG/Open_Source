package com.example.samsung.os;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button Login, Find;
    TextView Login_id, Login_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //pull test
        //아이디/비밀번호 찾기를 눌렀을 경우 해당 페이지로 이동
        Find = (Button)findViewById(R.id.find);
        Find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Find_ID_Password.class);
                startActivity(intent);
            }
        });

        //로그인 버튼을 눌렀을 경우 로그인 성공 여부 확인 후 첫 화면으로 이동
        Login = (Button)findViewById(R.id.login_button);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Main_page.class);
                startActivity(intent);
            }
        });
    }

    //actionbar 우측에 회원가입 메뉴 설정
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.signup_menu, menu);
        return true;
    }

    //actionbar에 있는 회원가입을 누를 경우 해당 페이지로 이동
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signup:
                Intent intent = new Intent(MainActivity.this, Sign_Up.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}