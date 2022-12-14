package com.firefly.faceEngine.activity;

import static com.firefly.faceEngine.App.getContext;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;

import com.firefly.arcterndemo.R;
import com.firefly.faceEngine.App;
import com.firefly.faceEngine.dblib.SettingManage;
import com.firefly.faceEngine.dblib.bean.Person;
import com.firefly.faceEngine.dblib.bean.Setting;
import com.firefly.faceEngine.other.FaceInfo;
import com.firefly.faceEngine.utils.MatrixYuvUtils;
import com.firefly.faceEngine.utils.Tools;
import com.firefly.faceEngine.view.FaceView;
import com.firefly.faceEngine.view.GrayInterface;
import com.firefly.faceEngine.view.GraySurfaceView;
import com.firefly.faceEngine.view.LivingInterface;
import com.firefly.faceEngine.view.LivingListener;
import com.intellif.YTLFFaceManager;
import com.intellif.arctern.base.ArcternAttribute;
import com.intellif.arctern.base.ArcternImage;
import com.intellif.arctern.base.ArcternRect;
import com.intellif.arctern.base.AttributeCallBack;
import com.intellif.arctern.base.ExtractCallBack;
import com.intellif.arctern.base.SearchCallBack;
import com.intellif.arctern.base.TrackCallBack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FaceDetectActivity extends BaseActivity implements TrackCallBack, AttributeCallBack, SearchCallBack, ExtractCallBack {
    private ArcternImage irImage = null;
    private ArcternImage rbgImage = null;
    private TextView txt1, txt2, txt3, texttxt;
    private FaceView faceView;
    private ImageView imgLandmark;
    private GraySurfaceView grayInterface;
    private Map<Long, Person> mMapPeople = new HashMap<>();
    private CountDownTimer mCountDownTimer;
    private YTLFFaceManager YTLFFace = YTLFFaceManager.getInstance();
    private SettingManage settingManage = App.getInstance().getSettingManage();
    private ExecutorService executorService;
    private Future future;
    private FaceInfo faceInfo;

    private Long ID;

    private int view_width, view_height;
    private int frame_width, frame_height;
    private long lastOnAttributeCallBackTime;
    private long lastOnSearchCallBackTime;

    private ImageView closeImage;
    private TextView closeTextWel,closeTextName;

    private ImageView backSetting;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detect);

        initView();
        getViewWH();
        startCountDownTimer();
        setInfraredFillLight(true); //?????????
        ActionBar actionBar = getSupportActionBar(); if (actionBar != null) { actionBar.hide(); }

        closeImage=findViewById(R.id.close_goods_image);
        closeTextWel=findViewById(R.id.close_goods_text_wel);
        closeTextName=findViewById(R.id.close_goods_text_name);

        backSetting = findViewById(R.id.back_setting);

        getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);

        backSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backSetting.setClickable(false);
                showmyDialog();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        noFace();
        backSetting.setClickable(true);
    }

    public void noFace(){
        List<Setting> saveInformation = settingManage.getSaveInformation();
        Integer recognition = saveInformation.get(0).getRecognition();
        closeImage.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.close_defaultpeople));
    }

    private void initView() {
        texttxt = findViewById(R.id.test);
        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        txt3 = findViewById(R.id.txt3);
        faceView = findViewById(R.id.faceview);
        imgLandmark = findViewById(R.id.img_landmark);

        grayInterface = findViewById(R.id.grayInterface);

        grayInterface.getHolder().setFormat(PixelFormat.TRANSLUCENT);

        LivingInterface.getInstance().init(this);
        LivingInterface.getInstance().setLivingCallBack(rgbLivingListener);

        GrayInterface.getInstance().init(this);
        GrayInterface.getInstance().setLivingCallBack(irLivingListener);

        YTLFFace.setOnTrackCallBack(this);
        YTLFFace.setOnSearchCallBack(this);
        YTLFFace.setOnAttributeCallBack(this);
    }



    @Override
    protected void onResume() {
        super.onResume();
        List<Person> mPeople = App.getInstance().getDbManager().getPersonList();
        for (Person person : mPeople) {
            mMapPeople.put(person.getId(), person);
        }
    }

    @Override
    public void onExtractFeatureListener(ArcternImage arcternImage, byte[][] features, ArcternRect[] arcternRects) {
    }

    @Override
    public void onTrackListener(ArcternImage arcternImage, long[] trackIds, ArcternRect[] arcternRects) {
        if (arcternRects != null) {
            faceView.setFaces(arcternRects, frame_width, frame_height, view_width, view_height);
        }
    }

    //Face belongs to the listener callback
    @Override
    public void onAttributeListener(ArcternImage arcternImage, long[] trackIds, ArcternRect[] arcternRects, ArcternAttribute[][] arcternAttributes, int[] landmarks) {
        ArcternAttribute[] attributes = arcternAttributes[0];
        if (attributes.length == 0) {
            return;
        }

        lastOnAttributeCallBackTime = System.currentTimeMillis();
        faceInfo = new FaceInfo(arcternImage, arcternAttributes);
        handleAttribute();
        refreshLogTextView();
    }

    @Override
    public void onSearchListener(ArcternImage arcternImage, long[] trackIds, ArcternRect[] arcternRects, long[] searchIds, int[] landmarks, float[] socre) {
        if (searchIds.length <= 0 || arcternImage == null ||
                faceInfo == null || faceInfo.getFrameId() != arcternImage.frame_id) {
            return;
        }

        lastOnSearchCallBackTime = System.currentTimeMillis();
        faceInfo.setSearchId(searchIds[0]);
        try {
            handlePerson();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        handleLandmark(arcternImage, landmarks);
    }

    LivingListener rgbLivingListener = new LivingListener() {
        @Override
        public void livingData(ArcternImage arcternImage) {
            rbgImage = arcternImage;
            frame_width = rbgImage.width;
            frame_height = rbgImage.height;
            if (irImage != null) {
                doDelivery(rbgImage, irImage);
            }
        }
    };

    LivingListener irLivingListener = new LivingListener() {
        @Override
        public void livingData(ArcternImage image) {
            irImage = image;
        }
    };

    private void doDelivery(final ArcternImage rbgImage, final ArcternImage irImage) {
        if (future != null && !future.isDone()) {
            return;
        }

        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor();
        }

        future = executorService.submit(new Runnable() {
            @Override
            public void run() {
                LivingInterface.rotateYUV420Degree90(rbgImage); //??????90???
                LivingInterface.rotateYUV420Degree90(irImage); //??????90???
                MatrixYuvUtils.mirrorForNv21(rbgImage.gdata, rbgImage.width, rbgImage.height);  //rbg ??????????????????
                Tools.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        List<Setting> saveInformation = settingManage.getSaveInformation();
                        Integer red = saveInformation.get(0).getInfrared();
                        if (red==0)
                            YTLFFace.doDelivery(rbgImage, rbgImage);
                        else
                            YTLFFace.doDelivery(rbgImage, irImage);
                    }
                });
            }
        });
    }

    int isNewUser = 0;//0 Initial 1 New user 2 old user
    int isUserId = 0;
    // Handling personnel information
    private void handlePerson() throws InterruptedException {

        List<Setting> saveInformation = settingManage.getSaveInformation();
        Integer recognition = saveInformation.get(0).getRecognition();

        Boolean flag;
        Boolean goodsFlag;
        saveInformation.get(0).getJumpInterval();
        int time = (int) (1000*Double.parseDouble(saveInformation.get(0).getJumpInterval()));

        if (recognition == 0)
            flag = false;
        else
            flag = true;

        if (flag){
            Person person = mMapPeople.get(faceInfo.getSearchId());
            if (person != null) {
                faceView.isRed = false;
                Long userId = person.getId();
                String name = person.getName();

                if(isNewUser != 2||isUserId!=userId) {
                    isNewUser = 2;
                    isUserId = Math.toIntExact(userId);
                    //??????????????????????????????Title????????????
                    setTitle(userId,name,false);
                    sleep(time);

                    Bundle bundle = new Bundle();
                    bundle.putLong("userId",userId);
                    Intent intent = new Intent(context,GoodsActivity.class);
                    intent.putExtra("bundle",bundle);

                    context.startActivity(intent);
                    finish();

                }
            } else {
                faceView.isRed = true;
                if(isNewUser != 1) {
                        isNewUser = 1;
                        setTitle(0l,"New Customer",false);
                }
            }
        }
    }

    // Processing face attribute information
    private void handleAttribute() {
        ArcternAttribute[] attributes = faceInfo.getAttributes()[0];

        for (int i = 0; i < attributes.length; i++) {
            ArcternAttribute item = attributes[i];
            switch (i) {
                case ArcternAttribute.ArcternFaceAttrTypeEnum.QUALITY://????????????
                    faceInfo.setFaceQualityConfidence(item.confidence);
                    break;

                case ArcternAttribute.ArcternFaceAttrTypeEnum.LIVENESS_IR: //??????
                    faceInfo.setLiveLabel(item.label);
                    faceInfo.setLivenessConfidence(item.confidence);
                    break;

                case ArcternAttribute.ArcternFaceAttrTypeEnum.FACE_MASK: //??????
                    faceInfo.setFaceMask(item.label == ArcternAttribute.LabelFaceMask.MASK);
                    break;

                case ArcternAttribute.ArcternFaceAttrTypeEnum.GENDER: //??????
                    Tools.debugLog("??????:%s", item.toString());
                    faceInfo.setGender(item.label);
                    faceInfo.setGenderConfidence(item.confidence);
                    break;

                case ArcternAttribute.ArcternFaceAttrTypeEnum.AGE: //??????
                    Tools.debugLog("??????:%s", item.toString());
                    faceInfo.setAge((int) item.confidence);
                    break;
            }
        }
    }

    // Processing the face key coordinates
    private void handleLandmark(ArcternImage arcternImage, int[] landmarks) {
        try {
            Bitmap bitmap = Tools.bgr2Bitmap(arcternImage.gdata, arcternImage.width, arcternImage.height);
            Bitmap landmarksBitmap = Tools.drawPointOnBitmap(bitmap, landmarks);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        imgLandmark.setImageBitmap(landmarksBitmap);
                        imgLandmark.setVisibility(View.VISIBLE);
                    } catch (Throwable e) {
                        Tools.printStackTrace(e);
                    }
                }
            });
        } catch (Throwable e) {
            Tools.printStackTrace(e);
        }
    }

    private void refreshLogTextView(){
        StringBuilder attribute = new StringBuilder();

        if (faceInfo.isFaceMask()) {
            attribute.append(getString(R.string.ytlf_dictionaries8))
                    .append("\n");

        } else {
            attribute.append(getString(R.string.ytlf_dictionaries9))
                    .append("\n");

            attribute.append(getString(R.string.ytlf_dictionaries21))
                    .append(faceInfo.getFaceQualityConfidence())
                    .append("\n");

            if (faceInfo.isLiveness()) {
                attribute.append(getString(R.string.ytlf_dictionaries19))
                        .append(":")
                        .append(faceInfo.getLivenessConfidence())
                        .append("\n");

            } else if (faceInfo.isNotLiveness()) {
                attribute.append(getString(R.string.ytlf_dictionaries20))
                        .append("\n");
                faceView.isRed = true;
                showText(txt2, "--");
                setVisibility(imgLandmark, View.GONE);
            }

            attribute.append(getString(R.string.ytlf_dictionaries45))
                    .append(faceInfo.getGenderString())
                    .append("\n");

            attribute.append(getString(R.string.ytlf_dictionaries46))
                    .append(faceInfo.getAgeString())
                    .append("\n");
        }

        if (!faceInfo.isFaceMask() && faceInfo.getFaceQualityConfidence() < 0.4) {
            faceView.isRed = false;
            showText(txt1, "--");
            return;
        }

        showText(txt1, attribute);
    }

    protected void showText(TextView txt, CharSequence msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txt.setText(msg);
            }
        });
    }

    protected void setVisibility(View view, int visibility) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(visibility);
            }
        });
    }

    public static String getTimeShort() {
        return new SimpleDateFormat("HH:mm:ss:SSS ").format(new Date());
    }

    private int times=0;
    private void saveBitmap2Jpeg(Bitmap bitmap){
        if(times > 10){
            return;
        }

        times ++;
        String path = YTLFFaceManager.getInstance().getYTIFFacthPath() + "img/" + System.currentTimeMillis() + ".jpg";
        boolean result = Tools.saveBitmap2Jpeg(bitmap, path);
        Tools.debugLog("result=%s, path=%s", result, path);
    }

    private void getViewWH() {
        ViewTreeObserver vto = faceView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                faceView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                view_width = faceView.getWidth();
                view_height = faceView.getHeight();
            }
        });
    }


    private void startCountDownTimer() {
        if (mCountDownTimer != null) {
            return;
        }

        mCountDownTimer = new CountDownTimer(Long.MAX_VALUE, 10000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (lastOnAttributeCallBackTime + 3000 < System.currentTimeMillis()) {
                    showText(txt1, "--");
                    faceView.isRed = false;
                }

                if (lastOnSearchCallBackTime + 3000 < System.currentTimeMillis()) {
                    showText(txt2, "--");
                    setVisibility(imgLandmark, View.GONE);
                }
            }
            @Override
            public void onFinish() {
                cancel();
            }
        };

        mCountDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            if (mCountDownTimer != null) {
                mCountDownTimer.onFinish();
            }
            setInfraredFillLight(false);
        } catch (Exception e) {
            Tools.printStackTrace(e);
        }
    }

    public void setTitle(Long userId,String name,Boolean goodsOpen){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (userId == 0l){
                    closeImage.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.close_bluepeople));
                    closeTextWel.setText("Welcome");
                    closeTextName.setText(name);
                }else if (userId == -1l){
                    closeImage.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.close_defaultpeople));
                    closeTextWel.setText("");
                    closeTextName.setText(name);
                }else {
                    closeImage.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.close_greenpeople));
                    closeTextWel.setText("Welcome");
                    closeTextName.setText(name);
                }
            }
        });
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
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){

            showmyDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    Dialog dialog;

    Context context = getContext();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dialog=new CustomDialog(this,R.style.mystyle,R.layout.dialog2);
        dialog.show();
    }

    private void showmyDialog() {

        dialog=new CustomDialog(this,R.style.mystyle,R.layout.dialog2);
        dialog.show();
    }
    //Dialog
    public class CustomDialog extends Dialog implements
            View.OnClickListener {

        /**
         * layout file
         **/
        int layoutRes;

        /**
         * Context object
         **/
        Context context;

        /**
         * cancel button
         **/
        private Button bt_cancal;

        /**
         * Button OK
         **/
        private Button bt_confirm;

        /**
         * Harvest address id
         */
        private int postion_1;
        private EditText password;

        public CustomDialog(Context context) {
            super(context);
            this.context = context;
        }

        /**
         * A custom layout constructor
         *
         * @param context
         * @param resLayout
         */
        public CustomDialog(Context context, int resLayout) {
            super(context);
            this.context = context;
            this.layoutRes = resLayout;
        }

        /**
         * Custom theme and layout constructors
         *
         * @param context
         * @param theme
         * @param resLayout
         */
        public CustomDialog(Context context, int theme, int resLayout) {
            super(context, theme);
            this.context = context;
            this.layoutRes = resLayout;
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            this.setContentView(layoutRes);
            bt_cancal = (Button) findViewById(R.id.id_cancel_btn);
            bt_confirm = (Button) findViewById(R.id.id_comfirm_btn);
            password=(EditText)findViewById(R.id.id_password);
            bt_cancal.setOnClickListener(this);
            bt_confirm.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.id_comfirm_btn ) {

                if (password.getText().toString().equals("123456")){
                    dialog.dismiss();
                    finish();
                }else{
                    Toast.makeText(context,"Wrong Password",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }else {
                dialog.dismiss();
            }
        }
    }

}
