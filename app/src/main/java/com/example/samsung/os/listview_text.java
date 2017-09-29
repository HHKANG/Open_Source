package com.example.samsung.os;

import android.support.v7.app.AppCompatActivity;

public class listview_text extends AppCompatActivity {
    private String text;

    public listview_text(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
