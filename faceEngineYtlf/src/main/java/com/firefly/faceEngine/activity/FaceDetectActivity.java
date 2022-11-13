package com.firefly.faceEngine.activity;

import static com.firefly.faceEngine.App.getContext;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
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
import com.firefly.faceEngine.goods.GoodsMessage;
import com.firefly.faceEngine.goods.GoodsUtils;
import com.firefly.faceEngine.goods.MyJsonParser;
import com.firefly.faceEngine.goods.bean.Goods;
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

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    private ImageView imageview;

    private ImageView goods1,goods2,goods3,goods4,goods5,goods6;
    private TextView Name1,Name2,Name3,Name4,Name5,Name6;
    private TextView recPrice4,recPrice5,recPrice6;
    private TextView description1,description2,description3;
    private TextView query1,query2,query3;
    private ImageView qrCode;
    private TextView tileText;

    private ImageView back;

    private TextView prePrice1,prePrice2,prePrice3;

    private TextView imageText,imageWel;



    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detect);

        initView();
        getViewWH();
        startCountDownTimer();
        setInfraredFillLight(true); //补光灯
        ActionBar actionBar = getSupportActionBar(); if (actionBar != null) { actionBar.hide(); }

        closeImage=findViewById(R.id.close_goods_image);
        closeTextWel=findViewById(R.id.close_goods_text_wel);
        closeTextName=findViewById(R.id.close_goods_text_name);

        goods1=findViewById(R.id.goods1);
        goods2=findViewById(R.id.goods2);
        goods3=findViewById(R.id.goods3);
        goods4=findViewById(R.id.goods4);
        goods5=findViewById(R.id.goods5);
        goods6=findViewById(R.id.goods6);

        Name1=findViewById(R.id.name_text1);
        Name2=findViewById(R.id.name_text2);
        Name3=findViewById(R.id.name_text3);
        Name4=findViewById(R.id.name_text4);
        Name5=findViewById(R.id.name_text5);
        Name6=findViewById(R.id.name_text6);

        prePrice1=findViewById(R.id.price1);
        prePrice2=findViewById(R.id.price2);
        prePrice3=findViewById(R.id.price3);
        recPrice4=findViewById(R.id.price4);
        recPrice5=findViewById(R.id.price5);
        recPrice6=findViewById(R.id.price6);

        description1 = findViewById(R.id.tv_describe1);
        description2 = findViewById(R.id.tv_describe2);
        description3 = findViewById(R.id.tv_describe3);

        query1=findViewById(R.id.edit_query1);
        query2=findViewById(R.id.edit_query2);
        query3=findViewById(R.id.edit_query3);

        imageview = findViewById(R.id.image_view);
        imageWel=findViewById(R.id.image_wel);

        imageText = findViewById(R.id.image_text);

        getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noFace();
    }

    public void noFace(){
        List<Setting> saveInformation = settingManage.getSaveInformation();
        Integer recognition = saveInformation.get(0).getRecognition();
        Integer goodsOpen = saveInformation.get(0).getGoodsOpen();

        Log.e("TAG", "noFace: "+goodsOpen );

        if (goodsOpen == 1){
            if (recognition == 0){
                setForecastGoods(0l);
                setRecommendGoods(0l);
            }
        }else{
            closeImage.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.close_defaultpeople));
        }
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

    //人脸属监听回调
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
        handlePerson();
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

    // 用线程池
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
                LivingInterface.rotateYUV420Degree90(rbgImage); //旋转90度
                LivingInterface.rotateYUV420Degree90(irImage); //旋转90度
                MatrixYuvUtils.mirrorForNv21(rbgImage.gdata, rbgImage.width, rbgImage.height);  //rbg 数据左右镜像
                Tools.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        List<Setting> saveInformation = settingManage.getSaveInformation();
                        Integer red = saveInformation.get(0).getRed();
                        if (red==0)
                            YTLFFace.doDelivery(rbgImage, rbgImage);
                        else
                            YTLFFace.doDelivery(rbgImage, irImage);
                    }
                });
            }
        });
    }

    int isNewUser = 0;//0 初始 1 新用户 2 老用户
    int isUserId = 0;
    // 处理人员信息
    private void handlePerson() {

        List<Setting> saveInformation = settingManage.getSaveInformation();
        Integer recognition = saveInformation.get(0).getRecognition();

        Boolean flag;
        Boolean goodsFlag;

        if (recognition == 0)
            flag = false;
        else
            flag = true;

        if (saveInformation.get(0).getGoodsOpen()==0)
            goodsFlag = false;
        else
            goodsFlag = true;

        if (flag){
            Person person = mMapPeople.get(faceInfo.getSearchId());
            if (person != null) {
                faceView.isRed = false;
                Long userId = person.getId();
                String name = person.getName();

                if(isNewUser != 2||isUserId!=userId) {
                    isNewUser = 2;
                    isUserId = Math.toIntExact(userId);

                    if (goodsFlag){
                        setTitle(userId,name,true);
                        setForecastGoods(userId);
                        setRecommendGoods(userId);
                    }else {
                        //如果关闭商品推荐只对Title进行修改
                        setTitle(userId,name,false);
                    }
                }
            } else {
                faceView.isRed = true;
                if(isNewUser != 1) {

                    if (goodsFlag){
                        setForecastGoods(0l);
                        setRecommendGoods(0l);
                        isNewUser = 1;
                        setTitle(0l,"New Customer",true);
                    }else{
                        isNewUser = 1;
                        setTitle(0l,"New Customer",false);
                    }

                }
            }
        }
    }

    // 处理人脸属性信息
    private void handleAttribute() {
        ArcternAttribute[] attributes = faceInfo.getAttributes()[0];

        for (int i = 0; i < attributes.length; i++) {
            ArcternAttribute item = attributes[i];
            switch (i) {
                case ArcternAttribute.ArcternFaceAttrTypeEnum.QUALITY://人脸质量
                    faceInfo.setFaceQualityConfidence(item.confidence);
                    break;

                case ArcternAttribute.ArcternFaceAttrTypeEnum.LIVENESS_IR: //活体
                    faceInfo.setLiveLabel(item.label);
                    faceInfo.setLivenessConfidence(item.confidence);
                    break;

                case ArcternAttribute.ArcternFaceAttrTypeEnum.FACE_MASK: //口罩
                    faceInfo.setFaceMask(item.label == ArcternAttribute.LabelFaceMask.MASK);
                    break;

                case ArcternAttribute.ArcternFaceAttrTypeEnum.GENDER: //性别
                    Tools.debugLog("性别:%s", item.toString());
                    faceInfo.setGender(item.label);
                    faceInfo.setGenderConfidence(item.confidence);
                    break;

                case ArcternAttribute.ArcternFaceAttrTypeEnum.AGE: //年龄
                    Tools.debugLog("年龄:%s", item.toString());
                    faceInfo.setAge((int) item.confidence);
                    break;
            }
        }
    }

    // 处理人脸关键坐标
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

    // 显示log
    private void refreshLogTextView(){
        StringBuilder attribute = new StringBuilder();

        if (faceInfo.isFaceMask()) {    //口罩时，不处理人脸质量和活体
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

        if (!faceInfo.isFaceMask() && faceInfo.getFaceQualityConfidence() < 0.4) {//无口罩且质量 < 0.4
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
    //保存bitmap本地图片 10次
    private void saveBitmap2Jpeg(Bitmap bitmap){
        if(times > 10){
            return;
        }

        times ++;
        String path = YTLFFaceManager.getInstance().getYTIFFacthPath() + "img/" + System.currentTimeMillis() + ".jpg";
        boolean result = Tools.saveBitmap2Jpeg(bitmap, path);
        Tools.debugLog("result=%s, path=%s", result, path);
    }

    //获得容器的高度
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

    //刷新
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

    /**
     * 对头部信息进行修改
     * @param userId 用户id
     * @param name 用户名或New User
     * @param goodsOpen 判断是否打开商品的获取
     */
    public void setTitle(Long userId,String name,Boolean goodsOpen){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (goodsOpen){
                    if (userId==0l){
                        imageview.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.bluepeople));
                    }else {
                        imageview.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.greenpeople));
                    }
                    imageWel.setText("Welcome!");
                    imageText.setText(name);
                }else {

                    if (userId == 0l){
                        closeImage.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.close_bluepeople));
                    }else {
                        closeImage.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.close_greenpeople));
                    }
                    closeTextWel.setText("Welcome");
//                    Log.e("TAG", "run: "+name);
                    closeTextName.setText(name);
                }
            }
        });
    }

    /**
     * 获取Predicted Orders商品
     * @param userId 用户id
     */
    public void setForecastGoods(Long userId){

        Log.e("", "setForecastGoods: " );

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Goods> forecastGoods = getPredict(userId);

                    for (int i = 0;i<forecastGoods.size();i++){
                        int q = i;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap bmp = getURLimage(forecastGoods.get(q).getPicture());
                                Message msg = new Message();
                                GoodsMessage goodsMessage = new GoodsMessage(bmp,forecastGoods.get(q).getName(),forecastGoods.get(q).getPrice(),forecastGoods.get(q).getDescription(),forecastGoods.get(q).getQuantity());
                                msg.what = q+1;
                                msg.obj = goodsMessage;
                                handle.sendMessage(msg);
                            }
                        }).start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 获取Recommended Products商品数据
     * @param userId 用户id
     */
    public void setRecommendGoods(Long userId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Goods> recommendGoods = getRecommendGoods(userId);
                    for (int i=0;i<recommendGoods.size();i++){
                        Log.e("TAG", "onClick: 2" );
                        int q = i;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("TAG", "setRecommendGoods: " );
                                Bitmap bmp = getURLimage(recommendGoods.get(q).getPicture());
                                Message msg = new Message();
                                GoodsMessage goodsMessage = new GoodsMessage(bmp,recommendGoods.get(q).getName(),recommendGoods.get(q).getPrice(),recommendGoods.get(q).getDescription(),recommendGoods.get(q).getQuantity());
                                msg.what = q+4;
                                msg.obj = goodsMessage;
                                handle.sendMessage(msg);
                            }
                        }).start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 对首页商品进行实时渲染
     */
    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            GoodsMessage goodsMessage = (GoodsMessage)msg.obj;
            switch (msg.what) {
                case 1:
                    goods1.setImageBitmap(goodsMessage.getBitmap());
                    Name1.setText(goodsMessage.getName());
                    description1.setText(goodsMessage.getDescription());
                    prePrice1.setText("$"+goodsMessage.getPrice());
                    query1.setText("Qty:"+goodsMessage.getQuantity());
                    break;
                case 2:
                    goods2.setImageBitmap(goodsMessage.getBitmap());
                    Name2.setText(goodsMessage.getName());
                    description2.setText(goodsMessage.getDescription());
                    prePrice2.setText("$"+goodsMessage.getPrice());
                    query2.setText("Qty:"+goodsMessage.getQuantity());
                    break;
                case 3:
                    goods3.setImageBitmap(goodsMessage.getBitmap());
                    Name3.setText(goodsMessage.getName());
                    description3.setText(goodsMessage.getDescription());
                    prePrice3.setText("$"+goodsMessage.getPrice());
                    query3.setText("Qty:"+goodsMessage.getQuantity());
                    break;
                case 4:
                    goods4.setImageBitmap(goodsMessage.getBitmap());
                    Name4.setText(goodsMessage.getName());
                    recPrice4.setText("$"+goodsMessage.getPrice().toString());
                    break;
                case 5:
                    goods5.setImageBitmap(goodsMessage.getBitmap());
                    Name5.setText(goodsMessage.getName());
                    recPrice5.setText("$"+goodsMessage.getPrice().toString());
                    break;
                case 6:
                    goods6.setImageBitmap(goodsMessage.getBitmap());
                    Name6.setText(goodsMessage.getName());
                    recPrice6.setText("$"+goodsMessage.getPrice().toString());
                    break;

            }
        };

    };

    //加载图片
    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);//读取图像数据
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    //获取推荐商品
    public ArrayList<Goods> getRecommendGoods(Long userId) throws Exception {
        String recommendGoods = GoodsUtils.getRecommendGoods(userId);
        if (recommendGoods.equals("Network connection failure"))
            Toast.makeText(getContext(),"Network connection failure!",Toast.LENGTH_LONG).show();
        return MyJsonParser.getGoods(recommendGoods);
    }

    //获取预测商品
    public ArrayList<Goods> getPredict(Long userId) throws Exception {
        String predictGoods = GoodsUtils.getPredictGoods(userId);

        if (predictGoods.equals("Network connection failure"))
            Toast.makeText(FaceDetectActivity.this,"Network connection failure!",Toast.LENGTH_LONG).show();
        return MyJsonParser.getGoods(predictGoods) ;
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

    public void back(View view){
        showmyDialog();
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
    //弹窗
    public class CustomDialog extends Dialog implements
            View.OnClickListener {

        /**
         * 布局文件
         **/
        int layoutRes;

        /**
         * 上下文对象
         **/
        Context context;

        /**
         * 取消按钮
         **/
        private Button bt_cancal;

        /**
         * 按钮确定
         **/
        private Button bt_confirm;

        /**
         * 收获地址id
         */
        private int postion_1;
        private EditText password;

        public CustomDialog(Context context) {
            super(context);
            this.context = context;
        }

        /**
         * 自定义布局的构造方法
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
         * 自定义主题及布局的构造方法
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

            // 指定布局
            this.setContentView(layoutRes);
            // 根据id在布局中找到控件对象
            bt_cancal = (Button) findViewById(R.id.id_cancel_btn);
            bt_confirm = (Button) findViewById(R.id.id_comfirm_btn);
            password=(EditText)findViewById(R.id.id_password);

            // 为按钮绑定点击事件监听器
            bt_cancal.setOnClickListener(this);
            bt_confirm.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int id = v.getId();// 确定按钮
            if (id == R.id.id_comfirm_btn ) {// 修改

                if (password.getText().toString().equals("123456")){
                    dialog.dismiss();
                    finish();
                }else{
                    Toast.makeText(context,"Wrong Password",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }


            }else {
                // 取消按钮

                dialog.dismiss();

            }


        }
    }

}
