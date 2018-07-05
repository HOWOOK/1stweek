package com.example.q.practice3;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class Tab3Anon extends Fragment {

    private final int CAMERA_CODE = 1111;
    private final int GALLERY_CODE = 1112;
    private Uri photoUri;
    private String currentPhotoPath;//실제 사진 파일 경로
    String mImageCaptureName;//이미지 이름

    ImageView ivImage;
    CanvasView cvImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab3anon, container, false);

        //ivImage = (ImageView) rootView.findViewById(R.id.water);
        cvImage = (CanvasView) rootView.findViewById(R.id.water);

        Button photoAction = (Button) rootView.findViewById(R.id.photoAction);
        photoAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });

        Button GalleryAction = (Button) rootView.findViewById(R.id.galleryAction);
        GalleryAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGallery();
            }
        });

        Button clearAction = (Button) rootView.findViewById(R.id.clearAction);
        clearAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCanvas(rootView);
            }
        });

        Button saveAction = (Button) rootView.findViewById(R.id.saveAction);
        saveAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakeCache();
            }
        });
        return rootView;
    }

    public void MakeCache(){
        String StoragePath =
                Environment.getExternalStorageDirectory().getAbsolutePath();
        String savePath = StoragePath + "/DCIM/Camera";

        File f = new File(savePath);

        if (!f.isDirectory()) f.mkdirs();

        getView().findViewById(R.id.water).buildDrawingCache(); //캡처할 뷰를 지정
        Bitmap bitmap = getView().findViewById(R.id.water).getDrawingCache();
        if(bitmap==null){
            System.out.println("이미지 파일을 불러오지 못했슈");
        }
        FileOutputStream fos =null;

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String strFilePath = savePath+"/"+mImageCaptureName+"_"+timeStamp+".jpeg";
        File fileCacheItem = new File(strFilePath);

        System.out.println("파일 이름은 : "+mImageCaptureName+"_"+timeStamp+".jpeg");
        System.out.println("파일 저장경로는 : "+strFilePath);

        try{
            fos = new FileOutputStream(fileCacheItem);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100, fos);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            getContext().sendBroadcast(new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(fileCacheItem)) );
            getView().findViewById(R.id.water).destroyDrawingCache();
            Toast.makeText(getContext(), "사진을 저장했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

//    public void picCapture(View v ){   // 버튼 onClick 리스너
//
//        String strFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + CAPTURE_PATH;
//        File folder = new File(strFolderPath);
//        if(!folder.exists()) {  // 해당 폴더 없으면 만들어라
//            folder.mkdirs();
//        }
//
//
//
//        String strFilePath = strFolderPath + "/" + System.currentTimeMillis() + ".png";
//        File fileCacheItem = new File(strFilePath);
//
//        try {
//            fos = new FileOutputStream(fileCacheItem);
//            captureView.compress(Bitmap.CompressFormat.PNG, 100, fos);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            Toast.makeText(this, "영상을 캡쳐했습니다", Toast.LENGTH_SHORT).show();
//        }
//    }
//

    public void  clearCanvas(View v){
        cvImage.clearCanvas();
    }

    private void selectPhoto() {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (intent.resolveActivity(getContext().getPackageManager()) != null) {  //fragment로 인해 변경

                File photoFile = null;
                try {
                    photoFile = createImageFile();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                if (photoFile != null) {

                    ////fragment로 인해 변경
                    photoUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName(), photoFile);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);


                    startActivityForResult(intent, CAMERA_CODE);

                }
            }

        }
    }

    private void selectGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }




    private File createImageFile() throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mImageCaptureName = timeStamp + ".png";

        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/DCIM/Camera/"
                + mImageCaptureName);
        currentPhotoPath = storageDir.getAbsolutePath();
        return storageDir;

    }

    private void getPictureForPhoto() {
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(currentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation;
        int exifDegree;

        if (exif != null) {
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exifDegree = exifOrientationToDegrees(exifOrientation);
        } else {
            exifDegree = 0;
        }
        //ivImage.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기


        int target_num1, target_num2;
        target_num1 = currentPhotoPath.lastIndexOf("/") + 1;
        target_num2 = currentPhotoPath.substring(target_num1).lastIndexOf(".");
        mImageCaptureName = currentPhotoPath.substring(target_num1, target_num1 + target_num2  );
        cvImage.setImageBitmap(rotate(bitmap, exifDegree));//캔버스 뷰에 비트맵 넣기
    }

    private void sendPicture(Uri imgUri) {

        String imagePath = getRealPathFromURI(imgUri); // path 경로
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
        //ivImage.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기

        int target_num1, target_num2;
        target_num1 = imagePath.lastIndexOf("/") + 1;
        target_num2 = imagePath.substring(target_num1).lastIndexOf(".");
        mImageCaptureName = imagePath.substring(target_num1, target_num1 + target_num2  );
        cvImage.setImageBitmap(rotate(bitmap, exifDegree));//캔버스 뷰에 비트맵 넣기

    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case GALLERY_CODE:
                    sendPicture(data.getData()); //갤러리에서 가져오기
                    //System.out.println("파일 이름은 : "+mImageCaptureName);
                    break;
                case CAMERA_CODE:
                    getPictureForPhoto(); //카메라에서 가져오기
                    //System.out.println("파일 이름은 : "+mImageCaptureName);
                    break;

                default:
                    System.out.println("불가능한 접근\n");
                    break;
            }

        }
    }


    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }


    private Bitmap rotate(Bitmap src, float degree) {
        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }



}