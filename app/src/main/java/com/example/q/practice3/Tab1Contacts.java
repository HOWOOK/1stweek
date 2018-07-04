package com.example.q.practice3;

import android.Manifest;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;

public class Tab1Contacts extends Fragment {

    private ListView lv;
    private ArrayList<Tab1Contacts_item> data = new ArrayList<>();


    //Fragment가 UI를 처음으로 그리고자할 때에 호출된다.
    //Fragment를 통해 UI를 그리고자 한다면 이 함수의 결과로 Fragment layout의 루트에 해당하는 View를 리턴해야한다.
    //만약 null을 리턴한다면 Fragment는 UI를 제공하지 않는다.

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.tab1contacts, container, false);

            super.onCreate(savedInstanceState);

            lv = (ListView) rootView.findViewById(R.id.listview);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(rootView.getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                Cursor c;
                c = rootView.getContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null,
                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " asc");

                while (c.moveToNext()) {
                    // 연락처 id 값
                    String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                    // 연락처 대표 이름
                    String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));

                    // ID로 전화 정보 조회
                    Cursor phoneCursor = getActivity().getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                            null, null);

                    // 데이터가 있는 경우
                    if (phoneCursor.moveToFirst()) {

                        String photoUri = phoneCursor.getString(phoneCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

                        String number = phoneCursor.getString(phoneCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                        Tab1Contacts_item member = new Tab1Contacts_item(photoUri, name, number);
                        data.add(member);
                    }

                    phoneCursor.close();
                }// end while
                c.close();
                ListviewAdapter adapter = new ListviewAdapter(rootView.getContext(), R.layout.tab1contacts_item, data);
                lv.setAdapter(adapter);
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(rootView.getContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        new TedPermission(rootView.getContext())
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission, you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_CONTACTS)
                .check();



        return rootView;
    }
}


