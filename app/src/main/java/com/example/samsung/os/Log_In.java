package com.example.samsung.os;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.concurrent.ExecutionException;

public class Log_In extends AppCompatActivity {
    Button Login, Find;
    EditText Login_id, Login_password;
    JSONObject loginJson = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

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
                Login_id = (EditText)findViewById(R.id.login_id);
                Login_password = (EditText)findViewById(R.id.login_password);
                String id = Login_id.getText().toString();
                String pw = Login_password.getText().toString();

                try {
                    loginJson.put("id", id);
                    loginJson.put("pw", pw);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                loginChk loginTask = new loginChk();
                try {
                    String result = loginTask.execute(1).get();
                    JSONObject loginChk = new JSONObject(result);
                    String auth = loginChk.get("auth").toString();
                    String key = loginChk.get("key").toString();
                    String msg = loginChk.get("msg").toString();
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                    if(auth.equals("true")){
                        Intent intent = new Intent(getApplicationContext(), sns_list.class);
                        intent.putExtra("key",key);
                        startActivity(intent);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                Intent intent = new Intent(Log_In.this, Sign_Up.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class loginChk extends AsyncTask<Integer, Void, String> {
        private String Url = "http://52.79.150.239/teamProject/account/login";
        InputStream is = null;
        String result = "";

        @Override
        protected String doInBackground(Integer... params) {
            try{
                String oss = loginJson.toString();

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