package com.example.q.practice3;

import android.app.ProgressDialog;
import android.content.Context;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Tab2Pictures extends Fragment {


    private String htmlPageUrl = "http://www.10000img.com/ran.php"; //파싱할 홈페이지의 URL주소
    private ImageView imageviewHtmlDocument;
    private String htmlContentInStringFormat="";


    //Glide.with(getView()).load(htmlContentInStringFormat).into(imageviewHtmlDocument);


    int cnt=0;
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
    final ArrayList<String> items = new ArrayList<String>() ;
    final GridAdapter adapter = new GridAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab2pictures, container, false);


        //list = imageReader(Environment.getExternalStorageDirectory());

        gv = (GridView) rootView.findViewById(R.id.gridView);
        gv.setAdapter(adapter);



        Button htmlTitleButton = (Button) rootView.findViewById(R.id.special);
        htmlTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println( (cnt+1) +"번째 파싱");
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();
                cnt++;
            }
        });


        return rootView;
    }

    class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.tab2pictures_custom, parent ,false);

            //ImageView iv = convertView.findViewById(R.id.icons);

           // String uri = getItem(position).toString();
            //iv.setImageURI( Uri.parse(getItem(position).toString()));

            //Glide.with(convertView).load(uri).into(iv);

            ////////////////////////////////////////////////////////////////HTML에서 가져오기

            imageviewHtmlDocument = convertView.findViewById(R.id.icons);
            Glide.with(convertView).load(items.get(position)).into(imageviewHtmlDocument);

            return convertView;
        }

    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(getContext());


        @Override
        protected void onPreExecute() {

            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("최대한 빠르게 불러오고 있으니 ㄱㄷ");
            asyncDialog.show();

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(htmlPageUrl).get();

                //System.out.println("전체문서는 다음과 같다 : \n" + doc);

                Element imageURI= doc.select("center img[src]").first();
                String uriStr = imageURI.toString();
                int target_num;
                target_num = uriStr.indexOf("=") + 2;

                //trim == 앞뒤 공백제거!
                //System.out.println("결과는1: " + uriStr);
                String data = "http://10000img.com/";
                data += uriStr.substring(target_num, uriStr.substring(target_num).indexOf(" ")+target_num-1);
                //System.out.println("결과는1: " + data);

                htmlContentInStringFormat = data;
                //System.out.println("결과는2: " + htmlContentInStringFormat);

//                //list.add(htmlContentInStringFormat);
//
//                for (int i = 0; i<list.size(); i++){
//                    System.out.println("지금들어있는 리스트는 ["+i+"]"+list.get(i)+"\n");
//                }



            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //Asynctask는 비동기적으로 실행되기 때문에 doInBackground의 일이 끝나면 이 함수가 불린다!

                items.add(htmlContentInStringFormat);
                adapter.notifyDataSetChanged();
                asyncDialog.dismiss();
        }
    }




//    ArrayList<File> imageReader(File root){
//        ArrayList<File> a = new ArrayList<>();
//
//        File[] files = root.listFiles();
//        for (int i = 0; i<files.length; i++){
//            if (files[i].isDirectory()){
//                a.addAll(imageReader(files[i]));
//            }else{
//                if(files[i].getName().endsWith(".jpg")){
//                    a.add(files[i]);
//                }
//            }
//        }
//        return a;
//    }





}
