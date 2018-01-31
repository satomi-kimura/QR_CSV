package com.example.nttr.csvtest;

public class Content {

    int num_con = 5;
    String[] content = new String[num_con];

    public Content(String[] adddata) {
        int n = adddata.length;
        if (n > num_con) n = num_con;

        for (int i = 0; i < n; i++) content[i] = adddata[i];
        if (n < num_con) {
            for (int i = n; i < num_con; i++) content[i] = String.valueOf(0);
        }
    }

    public void Content() {
    }
}