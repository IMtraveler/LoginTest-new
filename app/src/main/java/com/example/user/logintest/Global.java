package com.example.user.logintest;

/**
 * Created by I-Ting on 2017/11/26.
 */
import android.app.Application;
public class Global extends Application {
    private int check=1;

    public void setWord(int word){
        this.check = word;
    }

    public int getWord() {
        return check;
    }
}
