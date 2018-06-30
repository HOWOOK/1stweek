package com.example.q.practice3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Tap2GridAdapter extends BaseAdapter{

    private int icons[];
    private String letters[];
    private Context context;
    private LayoutInflater inflater;

    public Tap2GridAdapter(Context context, int icons[], String letters[]){
        this.context=context;
        this.icons=icons;
        this.letters=letters;
    }

    @Override
    public int getCount() {
        return letters.length;
    }

    @Override
    public Object getItem(int position) {
        return letters[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridView = convertView;
        if (convertView == null){
            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.tap2custom_layout,null);
        }

        ImageView icon = gridView.findViewById(R.id.icons);
        TextView letter = gridView.findViewById(R.id.letters);

        icon.setImageResource(icons[position]);
        letter.setText(letters[position]);


        return gridView;
    }
}
