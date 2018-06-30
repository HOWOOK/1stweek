package com.example.q.practice3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Tab1Contacts extends Fragment{


    //Fragment가 UI를 처음으로 그리고자할 때에 호출된다.
    //Fragment를 통해 UI를 그리고자 한다면 이 함수의 결과로 Fragment layout의 루트에 해당하는 View를 리턴해야한다.
    //만약 null을 리턴한다면 Fragment는 UI를 제공하지 않는다.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1contacts, container, false);



        return rootView;
    }
}
