package edu.neu.babyfoto;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.onRecyclerViewItemClickListener {

    private static final int IMAGE_REQUEST_CODE = 0;
    private int[] image = {R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4,
            R.drawable.image5,R.drawable.image6,R.drawable.image7,R.drawable.image8};
    private RecyclerView recyclerview;

    //Added
    private static int REQUEST_CAMERA_2 = 2;
    private String mFilePath;
    private String fileName;
    private ImageView picture;
    //Added

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean  onOptionsItemSelected(MenuItem item) {
        int i ;
        Random random = new Random();
        switch (item.getItemId()){
            case R.id.add_itemc: openCamera_2(); break;
            case R.id.add_itemb: openAlbum(); break;
            case R.id.remove_item:
                i = random.nextInt(getImagePathFromSD().size()-1);
                deleteSingleFile(getImagePathFromSD(),i);
                break;
    }
    return true;}



    private void initView() {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        //新建一个RecyclerView的适配器，并传入数据
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this, getImagePathFromSD());
        //将适配器设置给recyclerview控件
        recyclerview.setAdapter(recyclerViewAdapter);
        //新建一个StaggeredGridLayoutManager布局管理器，设置参数：1.显示的列数   2.显示布局的方向（水平或垂直）
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //将布局管理器设置给recyclerveiw控件
        recyclerview.setLayoutManager(staggeredGridLayoutManager);
        //给适配器添加我们暴露的监听方法
        recyclerViewAdapter.setOnRecyclerViewItemClickListener((RecyclerViewAdapter.onRecyclerViewItemClickListener) this);



    }

    //实现我们的监听接口里的方法，在这里获得数据，对数据进行操作

    public void onItemClick(View view, int img) {
        //创建一个intent，指明跳转目标类
        Intent intent = new Intent(this, ImageDetail.class);
        //拿到数据传给intent
        intent.putExtra("image", img);
        //启动Activity
        startActivity(intent);
    }

    /**
     * 从sd卡获取图片资源
     * @return
     */
    private List<String> getImagePathFromSD() {
        // 图片列表
        List<String> imagePathList = new ArrayList<String>();


        // 得到sd卡内image文件夹的路径   File.separator(/)
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Pictures";

          //      String filePath = "/storage/emulated/0/Pictures/";

               // /sdcard/Pictures/IMG_1650582826732.jpg
        Log.e("XXX",filePath);
        Boolean y = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        // 得到该路径文件夹下所有的文件
        File fileAll = new File(filePath);
        File[] files = fileAll.listFiles();

        Log.e("XXX1", String.valueOf(files.length));

        // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (checkIsImageFile(file.getPath())) {

                imagePathList.add(file.getPath());
            }
          // Log.e("XXXS",file.getPath());
        }
        // 返回得到的图片列表
        return imagePathList;
    }

    /**
     * 检查扩展名，得到图片格式的文件
     * @param fName  文件名
     * @return
     */
    @SuppressLint("DefaultLocale")
    private boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif")
                || FileEnd.equals("jpeg")|| FileEnd.equals("bmp") ) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;
    }

    /**
     * 打开相机并拍照
     *
     */
    private void openCamera_2() {

        //File fileDir = new File(Environment.getExternalStorageDirectory(),"Pictures");
        File fileDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()  ,"/Babies");
        Log.e("ZZZ1", String.valueOf(fileDir));
        if (!fileDir.exists()) {
            fileDir.mkdir();
            Log.e("XXXL2", "new");
        }
        fileName = "IMG_" + System.currentTimeMillis() + ".jpg";

        mFilePath = fileDir.getAbsolutePath()+"/"+ fileName;


        Log.e("XXXLS",mFilePath);

        Uri uris = null;
        ContentValues contentValues = new ContentValues();
        //设置文件名
        Log.e("XXXLS2","XL");
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            //contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/Pictures");
            contentValues.put(MediaStore.Images.Media.DATA, mFilePath);
        }else {

            contentValues.put(MediaStore.Images.Media.DATA, mFilePath);
        }
        //contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE,  mFilePath);

        uris = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uris);

        startActivityForResult(intent, REQUEST_CAMERA_2);
        Log.e("XXXLS3","X3");
    }

    private void openAlbum() {
        /**
         *      启动相册程序
         */
        //在这里跳转到手机系统相册里面
       // Intent intent = new Intent(
         //       Intent.ACTION_PICK,
           //     MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        //  该函数表示要查找文件的mime类型（如*/*），这个和组件在manifest里定义的相对应，但在源代码里
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {      //  如果是在4.4及以 上 系统的手机就调用该方法来处理图片
                        handleImageOnKitKat(data);
                    }
                }

        }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagPath = null;
        Uri uri = data.getData();
        String filename = "IMG_"+String.valueOf(uri).substring(String.valueOf(uri).length()-10, String.valueOf(uri).length());
        Log.e("1-0", String.valueOf(uri));
        Log.e("2-0", filename);
        final File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Pictures/"+filename);

        FileOutputStream fos = null;

        copyFile(this, uri, f);

    }

    public void copyFile(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            Log.e("input", String.valueOf(srcUri));
            Log.e("Output", dstFile.getPath());
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            copyStream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }}
    public static boolean copyStream(InputStream src, OutputStream dest) {
        byte[] buffer = new byte[2048];

        try {/*w  ww .j a v a 2  s  .  com*/
            int size;

            while ((size = src.read(buffer)) != -1) {
                dest.write(buffer, 0, size);
            }
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    private boolean deleteSingleFile(List<String> image,int position) {
        String filePath$Name = image.get(position);
        Log.e("delete", filePath$Name);
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            Log.e("delete1", file.getPath());
            if (file.delete()) {
                Log.e("--Method--", "Copy_Delete.deleteSingleFile: 删除单个文件" + filePath$Name + "成功！");
                Log.e("delete12", "success");
                return true;
            } else {
                Toast.makeText(getApplicationContext(), "删除单个文件" + filePath$Name + "失败！", Toast.LENGTH_SHORT).show();
                Log.e("delete13", "fail1");
                return false;
            }
        } else {
            Toast.makeText(getApplicationContext(), "删除单个文件失败：" + filePath$Name + "不存在！", Toast.LENGTH_SHORT).show();
            Log.e("delete14", "fail1");
            return false;
        }
    }


    }














