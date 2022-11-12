package com.firefly.faceApi.V2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.firefly.faceEngine.App;
import com.firefly.faceEngine.activity.BaseActivity;
import com.firefly.faceEngine.dblib.DBManager;
import com.firefly.faceEngine.dblib.SettingManage;
import com.firefly.faceEngine.dblib.bean.Person;
import com.intellif.YTLFFaceManager;
import com.intellif.arctern.base.ArcternImage;
import com.intellif.arctern.base.ArcternRect;
import com.intellif.arctern.base.ExtractCallBack;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AddUserFeatureActivity extends BaseActivity implements ExtractCallBack {


    private YTLFFaceManager YTLFFace = YTLFFaceManager.getInstance();
    private ImageView image;
    private EditText et_name;
    private TextView take_pho;

//    private Button btn_register;
    private DBManager dbManager = App.getInstance().getDbManager();
    private Bitmap faceBitmap;
    private String mBitmapPath = "";
    private byte[] bitmapFeature = null;


    TextView confirm_btn;
//    EditText name;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_add_user_featur);
        et_name=(EditText)findViewById(R.id.name_text);
//        cancel_btn=findViewById(R.id.cancel_btn);
        confirm_btn=findViewById(R.id.confirm_btn);
        image=findViewById(R.id.pho_btn);
        take_pho = findViewById(R.id.take_pho);
//        cancel_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("", "onClick: "+"12234454");
                onRegister(view);

            }
        });

        take_pho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(view);
            }

        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏、

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY //(修改这个选项，可以设置不同模式)
                        //使用下面三个参数，可以使内容显示在system bar的下面，防止system bar显示或
                        //隐藏时，Activity的大小被resize。
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // 隐藏导航栏和状态栏
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


    public void selectImage(View view) {
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, IMAGE_PICKER_ONE);
    }

    public String removeSpace(String str){
        if ((str.equals("")||str == null)||(str.charAt(0) != ' '&&str.charAt(str.length()-1)!=' '))
            return str;
        String trim = str.trim();
        return removeSpace(trim);
    }

    public void onRegister(View view) {
        String name = et_name.getText().toString();
        name = removeSpace(name);
        if (bitmapFeature == null&& (name.equals("")||name==null)){
            Toast.makeText(this,"Please enter the content and try again",Toast.LENGTH_LONG).show();
        }else if (bitmapFeature  == null){
            Toast.makeText(this,"Don't hava pictures",Toast.LENGTH_LONG).show();
        }else {

            if (name.equals("")||name==null){
                Toast.makeText(this,"Name Can't Be Null",Toast.LENGTH_LONG).show();
                et_name.setText("");
//            et_name.setHint("Please input name");
            }else{
                if (bitmapFeature != null) {
                    long searchId = YTLFFace.doSearch(bitmapFeature);
                    if (searchId > 0) {
                        showShortToast("The user is registered");
                        return;
                    }
                }

                if (bitmapFeature != null) {
//                Person person = new Person();
//                person.setFeature(bitmapFeature);
//                person.setName(name);
                    long id = dbManager.insertPerson(name,bitmapFeature,mBitmapPath);
                    //载入内存
                    int result = YTLFFace.dataBaseAdd(id, bitmapFeature);

                    String s = result == 0 ? "registered successfully" : "fail to register";
                    showShortToast(s);
                    if (result == 0) {
                        //全部置空
                        et_name.setText("");
                        image.setImageResource(R.drawable.photo);

                        bitmapFeature = null;
                        mBitmapPath = "";
                    }
                }
                finish();
            }
        }
    }



    //获取人脸特征值
    private int getFeature(String bitmapPath) {
        return YTLFFace.doFeature(bitmapPath, this);
    }

    private int getFeature(Bitmap bitmap) {
        return YTLFFace.doFeature(bitmap, this);
    }

    @Override
    public void onExtractFeatureListener(ArcternImage arcternImage, byte[][] features, ArcternRect[] arcternRects) {
        Log.i("", "onExtractFeatureListener: "+"qqq");
        if (features.length > 0) {
            bitmapFeature = features[0];
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("", "onActivityResult: ");
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER_ONE) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null && images.size() > 0) {
                    mBitmapPath = images.get(0).path;
                    Bitmap bitmap = BitmapFactory.decodeFile(mBitmapPath);
//                    Bitmap bm = BitmapFactory.decodeFile(mBitmapPath);
//                    saveBitmap(bm);
                    image.setImageBitmap(bitmap);
                    faceBitmap = bitmap;
//                    Tools.debugLog("bitmap path:%s", mBitmapPath);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int result = getFeature(mBitmapPath);
//                            Tools.debugLog("result: %s", result);
                        }
                    }).start();
                }
            }
        }
    }
}