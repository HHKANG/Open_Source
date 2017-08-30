package com.example.samsung.os;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static java.security.AccessController.getContext;

public class Sign_Up extends AppCompatActivity {
    String id = null;
    EditText inputName, inputPw, inputChkPw, inputId;
    RadioButton selectGender, selectAuth;
    Spinner inputY, inputM, inputD;
    Button sendAuthBt, checkAuthBt, signupBt, cancelBt;
    LinearLayout authLl;
    EditText inputAuthNum;
    TextView timer;
    Button authBt;
    int maxTime = 180;
    int time = 0;
    Boolean auth = false;
    int authCkh = 1;
    String authNum;

    JSONObject signUpJson = new JSONObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);
        //actionbar 가장 왼쪽에 뒤로가기 버튼 설정
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        inputAuthNum = new EditText(this);
        inputAuthNum.setHint("인증번호를 입력하세요");
        inputAuthNum.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        timer = new TextView(this);
        authBt = new Button(this);
        authBt.setText("인증번호 확인");
        sendAuthBt = (Button)findViewById(R.id.authBt);
        sendAuthBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authLl = (LinearLayout)findViewById(R.id.authLl);
                authLl.removeAllViewsInLayout();
                authLl.addView(inputAuthNum);
                authLl.addView(timer);
                authLl.addView(authBt);
                time = maxTime;
                mHandler.removeMessages(0);
                mHandler.sendEmptyMessage(0);
            }
        });

        authBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authNum = inputAuthNum.getText().toString();
                System.out.println(authNum);
                auth = true;
            }
        });

        signupBt = (Button)findViewById(R.id.signupBt);
        signupBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auth) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else
                    Toast.makeText(getApplicationContext(), "인증이 완료되지 않았습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        cancelBt = (Button)findViewById(R.id.cancelBt);
        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            time--;
            timer.setText(time/60 + ":" + time%60);

            if(time == 0){
                Toast.makeText(getApplicationContext(),"시간이 만료되었습니다.",Toast.LENGTH_SHORT).show();
                authLl.removeAllViewsInLayout();
                return;
            }
            else mHandler.sendEmptyMessageDelayed(0,1000);
        }
    };

    //actionbar에서 뒤로가기 버튼을 누를 경우 가장 최근에 열려있는 화면으로 넘어가고 현재 화면은 종료
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class idChk extends AsyncTask<Integer, Void, String> {
        private String Url = "http://52.79.150.239/teamProject/account/login";
        InputStream is = null;
        String result = "";

        @Override
        protected String doInBackground(Integer... params) {
            try{
                URL url = new URL(Url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-type", "application/json");

                // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
                conn.setDoOutput(true);
                // InputStream으로 서버로 부터 응답을 받겠다는 옵션.
                conn.setDoInput(true);

                OutputStream os = conn.getOutputStream();
                os.write(id.getBytes("UTF-8"));
                os.flush();
                // receive response as inputStream
                try {
                    is = conn.getInputStream();
                    // convert inputstream to string
                    if(is != null){
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String line;
                        StringBuffer response = new StringBuffer();
                        while((line = br.readLine()) != null) {
                            response.append(line);
                            response.append('\r');
                        }
                        br.close();

                        result = response.toString();
                        System.out.println(result);
                    }
                    else
                        result = "Did not work!";
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    conn.disconnect();
                }

            }catch(MalformedURLException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) { super.onProgressUpdate(values); }

        @Override
        protected void onPostExecute(String s) { super.onPostExecute(s); }
    }
    public class signUp extends AsyncTask<Integer, Void, String> {
        private String Url = "http://52.79.150.239/teamProject/account/login";
        InputStream is = null;
        String result = "";

        @Override
        protected String doInBackground(Integer... params) {
            try{
                String oss = signUpJson.toString();

                URL url = new URL(Url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-type", "application/json");

                // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
                conn.setDoOutput(true);
                // InputStream으로 서버로 부터 응답을 받겠다는 옵션.
                conn.setDoInput(true);

                OutputStream os = conn.getOutputStream();
                os.write(oss.getBytes("UTF-8"));
                os.flush();
                // receive response as inputStream
                try {
                    is = conn.getInputStream();
                    // convert inputstream to string
                    if(is != null){
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String line;
                        StringBuffer response = new StringBuffer();
                        while((line = br.readLine()) != null) {
                            response.append(line);
                            response.append('\r');
                        }
                        br.close();

                        result = response.toString();
                        System.out.println(result);
                    }
                    else
                        result = "Did not work!";
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    conn.disconnect();
                }

            }catch(MalformedURLException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) { super.onProgressUpdate(values); }

        @Override
        protected void onPostExecute(String s) { super.onPostExecute(s); }
    }
}