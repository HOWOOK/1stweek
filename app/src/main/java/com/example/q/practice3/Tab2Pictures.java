package com.example.q.practice3;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class Tab2Pictures extends Fragment {

    GridView gridView;

    String letterList[]={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P"
            ,"Q","R","S","T","U","V","W","X","Y","Z"};

    int lettersIcon[] = {R.drawable.lettera, R.drawable.letterb,
            R.drawable.letterc, R.drawable.letterd, R.drawable.lettere,
            R.drawable.letterf, R.drawable.letterg, R.drawable.letterh,
            R.drawable.letteri, R.drawable.letterj, R.drawable.letterk,
            R.drawable.letterl, R.drawable.letterm, R.drawable.lettern,
            R.drawable.lettero, R.drawable.letterp, R.drawable.letterq,
            R.drawable.letterr, R.drawable.letters, R.drawable.lettert,
            R.drawable.letteru, R.drawable.letterv, R.drawable.letterw,
            R.drawable.letterx, R.drawable.lettery, R.drawable.letterz};


    //    @Override
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.tab2pictures);
//
//        gridView = (GridView) findViewById(R.id.gridView);
//
//        Tap2GridAdapter adapter = new Tap2GridAdapter(Tab2Pictures.this, lettersIcon, letterList);
//
//        gridView.setAdapter(adapter);
//
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                Toast.makeText(Tap2Pictures.this, "Clicked Letter :" +letterList[position], Toast.LENGTH_SHORT ).show();
//            }
//        });
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab2pictures, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridView);

        Tab2GridAdapter adapter = new Tab2GridAdapter(rootView.getContext(), lettersIcon, letterList);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(rootView.getContext(), "Clicked Letter :" +letterList[position], Toast.LENGTH_SHORT ).show();
            }
        });

        return rootView;
    }
}
