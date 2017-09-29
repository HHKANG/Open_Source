package com.example.samsung.os;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Upload_Sns extends AppCompatActivity {

    ImageView upload_image;
    EditText upload_text;
    Button upload_okay, upload_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__sns);

        upload_image = (ImageView)findViewById(R.id.upload_image);
        upload_text = (EditText)findViewById(R.id.upload_text);
        upload_okay = (Button)findViewById(R.id.upload_okay);
        upload_cancel = (Button)findViewById(R.id.upload_cancel);
    }
}
