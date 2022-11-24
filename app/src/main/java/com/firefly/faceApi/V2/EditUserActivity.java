package com.firefly.faceApi.V2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firefly.faceEngine.App;
import com.firefly.faceEngine.activity.BaseActivity;
import com.firefly.faceEngine.dblib.DBManager;
import com.firefly.faceEngine.dblib.bean.Person;
import com.intellif.YTLFFaceManager;
import com.intellif.arctern.base.ArcternImage;
import com.intellif.arctern.base.ArcternRect;
import com.intellif.arctern.base.ExtractCallBack;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * 对用户信息进行修改
 */
public class EditUserActivity extends BaseActivity implements ExtractCallBack {

    private String mBitmapPath = "";
    private byte[] bitmapFeature = null;

    private DBManager dbManager = App.getInstance().getDbManager();
    private YTLFFaceManager YTLFFace = YTLFFaceManager.getInstance();

    private Bitmap faceBitmap;
    EditText newName;
    Integer position;

    TextView confirm;

    TextView takePho;

    ImageView image;

    Person person;//The currently modified user

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        Intent intent=getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        position = bundle.getInt("position");
        newName = findViewById(R.id.name_text_edit);
        confirm = findViewById(R.id.confirm_btn);
        takePho = findViewById(R.id.take_pho);
        image = findViewById(R.id.pho_btn);

        //Get the current user
        List<Person> personList = dbManager.getPersonList();//Pull from the database
        List<Person> personArrayList = new ArrayList<>();//Associating showList with the order of personList matches the id in the database
        List<String> showList = new ArrayList<>();//Display in the order of the user on the page
        for (Person person : personList) {
            personArrayList.add(person);
            showList.add(person.getName());
        }

        person = personArrayList.get(position);

        newName.setText(person.getName());

        takePho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(view);
            }

        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePerson();
            }
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //Hide the status bar

        init();

    }

    public void init(){
        EditText hint = (EditText) findViewById(R.id.name_text_edit);
        SpannableString ss = new SpannableString("Enter your name");
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(20,true);
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        hint.setHint(new SpannedString(ss));
    }

    //Hide status bar
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            hideSystemUI();
        }
    }

    //Hide status bar
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        //Hide the navigation and status bars
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    //Changing users
    public void changePerson(){
        String name=newName.getText().toString();

        if (name.equals(person.getName())){
            finish();
        }else {
            String s = removeSpace(name);
            int result =1;
            String v = null;
            if (s.equals("")||s==null) {
                if (bitmapFeature == null){
                    finish();
                }else {
                    long id = dbManager.updatePersonById(person.getId(), person.getName(), bitmapFeature);
                    //载入内存
                    YTLFFace.dataBaseDelete(id);
                    result = YTLFFace.dataBaseAdd(id, bitmapFeature);
                    v = result == 0 ? "Modify successfully" : "fail to Modify";
                }
            }

            else if (bitmapFeature == null){
                long id = dbManager.updatePersonById(person.getId(),s);
                v = "Modify successfully";
            }

            else if (bitmapFeature != null) {
                long id = dbManager.updatePersonById(person.getId(), s, bitmapFeature);
                YTLFFace.dataBaseDelete(id);

                result = YTLFFace.dataBaseAdd(id, bitmapFeature);
                v = result == 0 ? "Modify successfully" : "fail to Modify";
            }
            showShortToast(v);
            if (result == 0) {
                newName.setText("");
                newName.setHint("Enter your name");
                image.setImageResource(R.drawable.photo);

                bitmapFeature = null;
                mBitmapPath = "";
            }
            finish();
        }
    }

    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
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

    private int getFeature(String bitmapPath) {
        return YTLFFace.doFeature(bitmapPath, this);
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

                    image.setImageBitmap(bitmap);
                    faceBitmap = bitmap;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int result = getFeature(mBitmapPath);

                        }
                    }).start();
                }
            }
        }
    }


    @Override
    public void onExtractFeatureListener(ArcternImage arcternImage, byte[][] features, ArcternRect[] arcternRects) {
        Log.i("", "onExtractFeatureListener: "+"qqq");
        if (features.length > 0) {
            bitmapFeature = features[0];
        }
    }
}