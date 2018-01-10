package com.example.nttr.csvtest;

/**
 * Created by nttr on 2018/01/10.
 */

public class csvread {
    String [] Read = new String[10];

    public  void setdata(String data[]){
        int n = data.length;
        if (n>10) n=10;

        for (int i=0;i<n;i++) Read[i] = data[i];



    }


}
