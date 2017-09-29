package com.example.samsung.os;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class sns_list extends AppCompatActivity{

    ImageView select_picture;
    TextView textView;
    EditText plus_text;
    Button plus;
    ListView select_text;
    ArrayList<listview_text> arrayList = new ArrayList<listview_text>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sns_list);

        select_picture = (ImageView)findViewById(R.id.select_picture);
        textView = (TextView)findViewById(R.id.textView);
        plus_text = (EditText)findViewById(R.id.plus_text);
        plus = (Button)findViewById(R.id.plus);
        select_text = (ListView)findViewById(R.id.select_text);

        final listview_text_adapter adapter = new listview_text_adapter(this, R.layout.activity_listview_text, arrayList);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.add(new listview_text((plus_text.getText().toString())));
                select_text.setAdapter(adapter);
            }
        });
    }
}