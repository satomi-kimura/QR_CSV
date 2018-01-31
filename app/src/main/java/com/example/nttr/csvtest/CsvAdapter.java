package com.example.nttr.csvtest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


/**
 * Created by nttr on 2018/01/17.
 */
public class CsvAdapter extends ArrayAdapter<Content> {

    int num_con = 5;
    LayoutInflater layoutInflater = null;

    public CsvAdapter(@NonNull Context context) {
        super(context, R.layout.list_item);
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.content[0] = ((TextView) view.findViewById(R.id.contents1));
            viewHolder.content[1] = ((TextView) view.findViewById(R.id.contents2));
            viewHolder.content[2]= ((TextView) view.findViewById(R.id.contents3));
            viewHolder.content[3]= ((TextView) view.findViewById(R.id.contents4));
            viewHolder.content[4]= ((TextView) view.findViewById(R.id.contents5));
//            viewHolder.content[5]= ((TextView) view.findViewById(R.id.contents6));
//            viewHolder.content[6]= ((TextView) view.findViewById(R.id.contents7));
//            viewHolder.content[7]= ((TextView) view.findViewById(R.id.contents8));
//            viewHolder.content[8]= ((TextView) view.findViewById(R.id.contents9));
//            viewHolder.content[9]= ((TextView) view.findViewById(R.id.contents10));
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        for (int i = 0; i < num_con; i++) {
            viewHolder.content[i].setText(getItem(position).content[i]);
        }
        return view;
    }

    private static class ViewHolder {
        TextView[] content = new TextView[10];
    }
}
