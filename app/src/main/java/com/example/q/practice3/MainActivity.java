package com.example.q.practice3;

import android.Manifest;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                //activity를 위한 레이아웃  UI를 설정할때 이용
                //activity_main.xml의 레이아웃 UI를 이용하겠다!
                setContentView(R.layout.activity_main);


                //Set a toolbar to replace to action bar
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                // Create the adapter that will return a fragment for each of the three
                // primary sections of the activity.
                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


                // viewpager란 수평으로 view를 좌우로 스크롤 할때 사용하는 클래스
                // viewpager가 표시하는 view는 pageradapter를 통해 공급받는다.
                // pageradapter를 통해 화면에 표시 될 view의 라이프 사이클을 관리 할 수 있다. (listview와 listadapter의 관계와 동일하다.)
                // Set up the ViewPager with the sections adapter.
                mViewPager = (ViewPager) findViewById(R.id.container);
                mViewPager.setAdapter(mSectionsPagerAdapter);

                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

                mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));



                //remove
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
            }
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getApplicationContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        new TedPermission(getApplicationContext())
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission, you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .check();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //onCreateOptionMenu()는 최초에 메뉴키가 눌렸을 때 호출되고
    //onOptionsItemSeleated()는 메뉴 아이템이 클릭되었을때 호출된다.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //deleted PlaceholderFragment class from here

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */

    //Fragment는 Activity 내부에서 UI나 프로세스를 정의할 수 있는 더 작은 단위이고,
    // 하나의 Activity에 다수의 Fragment를 배치하거나 여러 개의 Activity에서 하나의 Fragment를 재사용하는 것이 가능하다.
    // 따라서 Fragment는 Activity를 구성하는 작은 모듈이라고 생각하면 되며, Activity와는 별도의
    // 라이프사이클과 이벤트 또한 따로 처리가 가능하고, 하나의 Activity가 실행되고 있을 때
    // 유동적으로 추가하고 삭제가 가능하도록 되어있다.

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    Tab1Contacts tab1 = new Tab1Contacts();
                    return tab1;
                case 1:
                    Tab2Pictures tab2 = new Tab2Pictures();
                    return tab2;
                case 2:
                    Tab3Anon tab3 = new Tab3Anon();
                    return tab3;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

//        @Override
//        public CharSequence getPageTitle (int position){
//            switch (position) {
//                case 0:
//                    return "CONTACTS";
//                case 1:
//                    return "PICTURES";
//                case 2:
//                    return "ANON";
//            }
//            return null;
//
//            }
    }
}