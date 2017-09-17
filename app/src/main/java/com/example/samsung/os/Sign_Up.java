package com.example.samsung.os;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Sign_Up extends AppCompatActivity {
    EditText inputrealname, inputName, inputPw, inputChkPw, inputId,inputAuthNum;
    RadioButton selectMan, selectWoman, selectEmail, selectPhone;
    Spinner inputY, inputM, inputD;
    Button sendAuthBt, checkAuthBt, signupBt, cancelBt;
    TextView timer;
    LinearLayout authLl;
    int maxTime = 180;
    int time = 0;
    Boolean auth = false;
    int authCkh = 1;
    String authNum = "";
    String authId = "";
    String reciveAuth = "";
    String gender = "";
    String authType = "";
    String y = "";
    String m = "";
    String d = "";
    ArrayList arrayY, arrayM, arrayD;
    ArrayAdapter adapter;

    JSONObject signUpJson = new JSONObject();
    JSONObject sendAuthJson = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);
        //actionbar 가장 왼쪽에 뒤로가기 버튼 설정
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

        // 인증방법 radio event 처리
        selectEmail = (RadioButton)findViewById(R.id.email);
        selectPhone = (RadioButton)findViewById(R.id.phone);
        RadioButton.OnClickListener authTypeListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectEmail.isChecked())
                    authType = "0";
                else if(selectPhone.isChecked())
                    authType = "1";
                Toast.makeText(getApplicationContext(),authType,Toast.LENGTH_SHORT).show();
            }
        };
        selectEmail.setOnClickListener(authTypeListener);
        selectPhone.setOnClickListener(authTypeListener);

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

        // 인증 번호 입력창 만들기
        inputAuthNum = new EditText(this);
        inputAuthNum.setHint("인증번호를 입력하세요");
        inputAuthNum.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        timer = new TextView(this);
        checkAuthBt = new Button(this);
        checkAuthBt.setText("인증번호 확인");
        sendAuthBt = (Button)findViewById(R.id.authBt);
        sendAuthBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputId = (EditText)findViewById(R.id.id);
                authId = inputId.getText().toString();
                if(authId.equals(null) || authId.equals("")) {
                    Toast.makeText(getApplicationContext(),"아이디를 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    sendAuthJson.put("type", authType);
                    sendAuthJson.put("to", authId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Sign_Up.sendAuth sendAuthTask = new Sign_Up.sendAuth();
                try {
                    String result = sendAuthTask.execute(1).get();
                    System.out.println(result);
                    JSONObject sendChk = new JSONObject(result);
                    String auth = sendChk.get("auth").toString();
                    String msg = sendChk.get("msg").toString();
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                    if(!auth.equals("false")) reciveAuth = auth;
                    else return;
                    if(authType.equals("1")) inputAuthNum.setText(reciveAuth);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                authLl = (LinearLayout)findViewById(R.id.authLl);
                authLl.removeAllViewsInLayout();
                authLl.addView(inputAuthNum);
                authLl.addView(timer);
                authLl.addView(checkAuthBt);
                time = maxTime;
                mHandler.removeMessages(0);
                mHandler.sendEmptyMessage(0);
            }
        });

        // 인증 번호 확인
        checkAuthBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authNum = inputAuthNum.getText().toString();
                System.out.println(authNum);
                if(reciveAuth.equals(authNum)) {
                    Toast.makeText(getApplicationContext(),"인증되었습니다.",Toast.LENGTH_SHORT).show();
                    mHandler.removeCallbacksAndMessages(null);
                    auth = true;
                }
                else {
                    Toast.makeText(getApplicationContext(),"인증번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                    auth = false;
                }
            }
        });

        // 회원가입 버튼
        signupBt = (Button)findViewById(R.id.signupBt);
        signupBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputrealname = (EditText)findViewById(R.id.name);
                inputName = (EditText)findViewById(R.id.login_name);
                inputPw = (EditText)findViewById(R.id.login_pw);
                inputChkPw = (EditText)findViewById(R.id.login_pw_chk);
                inputId = (EditText)findViewById(R.id.id);
                signupBt = (Button)findViewById(R.id.signupBt);
                cancelBt = (Button)findViewById(R.id.cancelBt);

                String realname = inputrealname.getText().toString();
                if(realname.equals(null)||realname.equals("")){
                    Toast.makeText(getApplicationContext(),"이름을 입력하세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                String name = inputName.getText().toString();
                if(name.equals(null) || name.equals("")) {
                    Toast.makeText(getApplicationContext(),"닉네임을 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(gender.equals(null) || gender.equals("")) {
                    Toast.makeText(getApplicationContext(),"성별을 선택해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                y = inputY.getSelectedItem().toString();
                m = inputM.getSelectedItem().toString();
                d = inputD.getSelectedItem().toString();
                if(y.equals(null) || y.equals("") || m.equals(null) || m.equals("") || d.equals(null) || d.equals("")){
                    Toast.makeText(getApplicationContext(),"생년월일을 선택해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                String pw = inputPw.getText().toString();
                String chkPw = inputChkPw.getText().toString();
                if(!pw.equals(chkPw)) {
                    Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(pw.equals(null) || pw.equals("")) {
                    Toast.makeText(getApplicationContext(),"비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                String id = inputId.getText().toString();
                if(!id.equals(authId)) {
                    Toast.makeText(getApplicationContext(),"재인증이 필요합니다.",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(authType.equals(null) || authType.equals("")){
                    Toast.makeText(getApplicationContext(),"인증 방법을 선택해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (auth) {
                    try {
                        signUpJson.put("realname",realname);
                        signUpJson.put("name", name);
                        signUpJson.put("gender", gender);
                        if(m.length() == 1) m = "0" + m;
                        if(d.length() == 1) d = "0" + d;
                        signUpJson.put("birth", y+m+d);
                        if(authType.equals("0")){
                            signUpJson.put("phone", null);
                            signUpJson.put("email", id);
                        }
                        else if(authType.equals("1")){
                            signUpJson.put("phone", id);
                            signUpJson.put("email", null);
                        }
                        signUpJson.put("id", authType);
                        signUpJson.put("pw", pw);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println(signUpJson.toString());
                    Sign_Up.signup signupTask = new Sign_Up.signup();
                    try {
                        String result = signupTask.execute(1).get();
                        JSONObject signupChk = new JSONObject(result);
                        String auth = signupChk.get("auth").toString();
                        String msg = signupChk.get("msg").toString();
                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                        if(auth.equals("true")){
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else Toast.makeText(getApplicationContext(), "인증이 완료되지 않았습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 회원가입 취소
        cancelBt = (Button)findViewById(R.id.cancelBt);
        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // 인증 timer
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

    // 회원가입 asyncTask
    public class signup extends AsyncTask<Integer, Void, String> {
        private String Url = "http://52.79.150.239/teamProject/account/signup";
        InputStream is = null;
        String result = "";

        @Override
        protected String doInBackground(Integer... params) {
            try{
                String oss = signUpJson.toString();

                // connection 설정
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

    // 인증 asyncTask
    public class sendAuth extends AsyncTask<Integer, Void, String> {
        private String Url = "http://52.79.150.239/teamProject/account/sendAuth";
        InputStream is = null;
        String result = "";

        @Override
        protected String doInBackground(Integer... params) {
            try{
                String oss = sendAuthJson.toString();

                // connection 설정
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