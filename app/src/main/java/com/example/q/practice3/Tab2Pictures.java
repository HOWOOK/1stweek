package com.example.q.practice3;

import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

public class Tab2Pictures extends Fragment {

//    GridView gridView;
//
//    String letterList[]={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P"
//            ,"Q","R","S","T","U","V","W","X","Y","Z"};
//
//    int lettersIcon[] = {R.drawable.lettera, R.drawable.letterb,
//            R.drawable.letterc, R.drawable.letterd, R.drawable.lettere,
//            R.drawable.letterf, R.drawable.letterg, R.drawable.letterh,
//            R.drawable.letteri, R.drawable.letterj, R.drawable.letterk,
//            R.drawable.letterl, R.drawable.letterm, R.drawable.lettern,
//            R.drawable.lettero, R.drawable.letterp, R.drawable.letterq,
//            R.drawable.letterr, R.drawable.letters, R.drawable.lettert,
//            R.drawable.letteru, R.drawable.letterv, R.drawable.letterw,
//            R.drawable.letterx, R.drawable.lettery, R.drawable.letterz};
//

    GridView gv;
    ArrayList<File> list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab2pictures, container, false);

        list = imageReader(Environment.getExternalStorageDirectory());

        gv = (GridView) rootView.findViewById(R.id.gridView);
        gv.setAdapter(new GridAdapter());

        return rootView;
    }

    class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.tab2pictures_custom, parent ,false);
            ImageView iv = convertView.findViewById(R.id.icons);

            String uri = getItem(position).toString();
            //iv.setImageURI( Uri.parse(getItem(position).toString()));

            Glide.with(convertView).load(uri).into(iv);
            return convertView;
        }
    }


    ArrayList<File> imageReader(File root){
        ArrayList<File> a = new ArrayList<>();

        File[] files = root.listFiles();
        for (int i = 0; i<files.length; i++){
            if (files[i].isDirectory()){
                a.addAll(imageReader(files[i]));
            }else{
                if(files[i].getName().endsWith(".jpg")){
                    a.add(files[i]);
                }
            }
        }
        return a;
    }


}
