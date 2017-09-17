package com.example.samsung.os;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SelMoneyBook extends AppCompatActivity {

    ImageView iv;
    Button camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_money_book);

        Intent intent = getIntent();
        int year = intent.getExtras().getInt("year");
        int month = intent.getExtras().getInt("month");
        int dayOfmonth = intent.getExtras().getInt("dayOfmonth");
        Log.d("test", year +"/"+ month +"/"+dayOfmonth);
        TextView txt_view;
        txt_view = (TextView)findViewById(R.id.view);
        txt_view.setText(year+"/"+month+"/"+dayOfmonth);


        camera = (Button)findViewById(R.id.camera);

        iv =(ImageView)findViewById(R.id.image);

        camera.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,1);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("uri",data.getData().toString());
        iv.setImageURI(data.getData());
    }
}
