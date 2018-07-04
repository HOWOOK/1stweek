package com.example.q.practice3;

import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Tab1Contacts extends Fragment {

    private ListView lv;
    private String searchKeyword;
    private ArrayList<Tab1Contacts_item> data;
    private  ListviewAdapter adapter;

    public Tab1Contacts_item getItem(int position){
        return data.get(position);
    }

    private static String getContactIDFromName(ContentResolver contactHelper, String display_name){
        Uri contactUri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[] {ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME};
        String where = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "='" +display_name+"'";
        String[] whereParams =null;
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC";
        Cursor cursor = null;

        try {
            cursor = contactHelper.query(contactUri, projection, where, whereParams, sortOrder);
            if(cursor.moveToFirst()){
            return cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));}
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor!=null){
                cursor.close();
                cursor = null;
            }
        }
        return null;
    }

    private ArrayList<Tab1Contacts_item> getList() throws Exception {
        ArrayList<Tab1Contacts_item> data = new ArrayList<>();
        Cursor c;
        c = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
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


                if (data == null) {
                    throw new NullPointerException("data가 null 입니다.");
                }

                boolean isAdd = false;
                if (searchKeyword != null && "".equals(searchKeyword.trim()) == false) {
                    String iniName = HangulUtils.getHangulInitialSound(name,
                            searchKeyword);
                    String numberWithoutBar = number.replaceAll("[^0-9]", "");

                    if (numberWithoutBar.contains(searchKeyword) || iniName.contains(searchKeyword)) {
                        isAdd = true;
                }
                } else {
                    isAdd = true;
                }
                if (isAdd) {
                    Tab1Contacts_item member = new Tab1Contacts_item(photoUri, name, number);
                    data.add(member);
                }
            }
            phoneCursor.close();
        }// end while
        c.close();

        return data;
    }

    private void displayList() throws Exception {

        data = null;

        data = getList();

        adapter = new ListviewAdapter(getActivity(), R.layout.tab1contacts_item, data);
        lv.setAdapter(adapter);
    }


    //Fragment가 UI를 처음으로 그리고자할 때에 호출된다.
    //Fragment를 통해 UI를 그리고자 한다면 이 함수의 결과로 Fragment layout의 루트에 해당하는 View를 리턴해야한다.
    //만약 null을 리턴한다면 Fragment는 UI를 제공하지 않는다.

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.tab1contacts, container, false);

            super.onCreate(savedInstanceState);

            lv = (ListView) rootView.findViewById(R.id.listview);

        EditText searchBox = (EditText) rootView.findViewById(R.id.search_box);

        try {
            searchBox.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable arg0) {
                    // ignore
                }

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                    // ignore
                }

                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {

                    try {
                        searchKeyword = s.toString();
                        displayList();
                    } catch (Exception e) {
                        Log.e("", e.getMessage(), e);
                    }
                }

            });
            displayList();
        } catch (Exception e) {
            Log.e("", e.getMessage(), e);
        }

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fab:

                        LayoutInflater inflater = getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.tab1_contacts_add, null);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

                        // 제목셋팅
                        alertDialogBuilder.setTitle("연락처 추가");
                        alertDialogBuilder.setView(dialogView);

                        EditText mEditPhone = (EditText) dialogView.findViewById(R.id.editText2);
                        mEditPhone.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
                        mEditPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("저장",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                EditText editName = (EditText) dialogView.findViewById(R.id.editText1);
                                                EditText editPhone = (EditText) dialogView.findViewById(R.id.editText2);
                                                String name = editName.getText().toString();
                                                String phone = editPhone.getText().toString();

                                                ArrayList<ContentProviderOperation> list = new ArrayList<>();
                                                try{
                                                    list.add(
                                                            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                                                                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                                                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                                                                    .build()
                                                    );

                                                    list.add(
                                                            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                                                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                                                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)   //이름

                                                                    .build()
                                                    );

                                                    list.add(
                                                            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                                                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                                                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)           //전화번호
                                                                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE  , ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)   //번호타입(Type_Mobile : 모바일)

                                                                    .build()
                                                    );
                                                    rootView.getContext().getContentResolver().applyBatch(ContactsContract.AUTHORITY, list);  //주소록추가
                                                    list.clear();   //리스트 초기화

                                                }catch(RemoteException e){
                                                    e.printStackTrace();
                                                }catch(OperationApplicationException e){
                                                    e.printStackTrace();
                                                }

                                                Toast.makeText(rootView.getContext(), "성공적으로 저장되었습니다.",Toast.LENGTH_SHORT).show();
                                                try{
                                                displayList();}
                                                catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        })
                                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });


                        // 다이얼로그 보여주기
                        alertDialogBuilder.show();
                        break;

                    default:
                        break;
                }
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, final int position, long l) {
                Tab1Contacts_item curItem = getItem(position);
                final String curName = curItem.getName();
                final String curPhone = curItem.getPhone();

                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.tab1_contacts_detail, null);
                TextView nameView = dialogView.findViewById(R.id.name);
                TextView phoneView = dialogView.findViewById(R.id.phone);
                nameView.setText(curName);
                phoneView.setText(curPhone);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());


                alertDialogBuilder.setTitle("연락처 정보");
                alertDialogBuilder.setView(dialogView);
                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                builder.setMessage("정말 삭제하겠습니까?");
                                builder.setCancelable(false)
                                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                String id = getContactIDFromName(getContext().getContentResolver(), curName);
                                                getContext().getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts._ID+"="+ id+"", null);

                                                Toast.makeText(getContext(), "성공적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                                try{
                                                    displayList();}
                                                catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        })
                                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });
                                builder.show();
                            }
                        })
                        .setNegativeButton("수정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                LayoutInflater inflater = getLayoutInflater();
                                final View dialogView = inflater.inflate(R.layout.tab1_contacts_add, null);
                                EditText nameView = dialogView.findViewById(R.id.editText1);
                                EditText phoneView = dialogView.findViewById(R.id.editText2);
                                nameView.setText(curName);
                                phoneView.setText(curPhone);

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

                                // 제목셋팅
                                alertDialogBuilder.setTitle("연락처 수정");
                                alertDialogBuilder.setView(dialogView);

                                EditText mEditPhone = (EditText) dialogView.findViewById(R.id.editText2);
                                mEditPhone.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
                                mEditPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

                                alertDialogBuilder
                                        .setCancelable(false)
                                        .setPositiveButton("저장",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                        EditText editName = (EditText) dialogView.findViewById(R.id.editText1);
                                                        EditText editPhone = (EditText) dialogView.findViewById(R.id.editText2);
                                                        String name = editName.getText().toString();
                                                        String phone = editPhone.getText().toString();

                                                        ArrayList<ContentProviderOperation> list = new ArrayList<>();
                                                        try{
                                                            list.add(
                                                                    ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                                                                            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                                                            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                                                                            .build()
                                                            );

                                                            list.add(
                                                                    ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                                                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                                                            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                                                            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)   //이름

                                                                            .build()
                                                            );

                                                            list.add(
                                                                    ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                                                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                                                            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                                                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)           //전화번호
                                                                            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE  , ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)   //번호타입(Type_Mobile : 모바일)

                                                                            .build()
                                                            );
                                                            rootView.getContext().getContentResolver().applyBatch(ContactsContract.AUTHORITY, list);  //주소록추가
                                                            list.clear();   //리스트 초기화

                                                        }catch(RemoteException e){
                                                            e.printStackTrace();
                                                        }catch(OperationApplicationException e){
                                                            e.printStackTrace();
                                                        }

                                                        String id = getContactIDFromName(getContext().getContentResolver(), curName);
                                                        getContext().getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts._ID+"="+ id+"", null);

                                                        Toast.makeText(rootView.getContext(), "성공적으로 수정되었습니다.",Toast.LENGTH_SHORT).show();
                                                        try{
                                                            displayList();}
                                                        catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                })
                                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });


                                // 다이얼로그 보여주기
                                alertDialogBuilder.show();
                            }
                        });


                // 다이얼로그 보여주기
                alertDialogBuilder.show();
                //Toast.makeText(getContext(),"Selected: " + curName + curPhone, Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }
}


