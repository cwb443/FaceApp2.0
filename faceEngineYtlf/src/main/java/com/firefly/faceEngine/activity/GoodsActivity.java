package com.firefly.faceEngine.activity;

import static com.firefly.faceEngine.App.getContext;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firefly.arcterndemo.R;
import com.firefly.faceEngine.App;
import com.firefly.faceEngine.dblib.DBManager;
import com.firefly.faceEngine.dblib.SettingManage;
import com.firefly.faceEngine.dblib.bean.Person;
import com.firefly.faceEngine.goods.GoodsMessage;
import com.firefly.faceEngine.goods.GoodsUtils;
import com.firefly.faceEngine.goods.MyJsonParser;
import com.firefly.faceEngine.goods.bean.Goods;
import com.firefly.faceEngine.utils.Tools;
import com.intellif.YTLFFaceManager;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GoodsActivity extends BaseActivity implements View.OnClickListener {

    private long userId;

    //Face recognition part++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Get Authorized Online API_KEY
    public final String API_KEY = "xrZEJz51qfiBI3FB";

    // Specifies the local SD card directory where the models and license public keys will be stored
    public static String FACE_PATH = "/sdcard/firefly/";

    // SDK
    private YTLFFaceManager YTLFFace = YTLFFaceManager.getInstance().initPath(FACE_PATH);
    //Face recognition part-------------------------------------------------------------------------------

    private DBManager dbManager = App.getInstance().getDbManager();

    private ImageView goods1,goods2,goods3,goods4,goods5,goods6;
    private TextView Name1,Name2,Name3,Name4,Name5,Name6;
    private TextView recPrice4,recPrice5,recPrice6;
    private TextView description1,description2,description3;
    private TextView query1,query2,query3;
    private TextView prePrice1,prePrice2,prePrice3;
    private ImageView imageview;
    private TextView imageText,imageWel;
    private Button backHome;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

        Intent intent = getIntent();
        Bundle bundle =  intent.getBundleExtra("bundle");
        userId = bundle.getLong("userId");


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

        backHome = findViewById(R.id.back_home);

        Person person = dbManager.getPersonById(userId);

        if (person == null){
            setTitle(0l,"new");
            setForecastGoods(userId);
            setRecommendGoods(userId);
        }else {
            String name = person.getName();
            setTitle(userId,name);
            setForecastGoods(userId);
            setRecommendGoods(userId);
        }


        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backHome.setClickable(false);
                backFace();
            }
        });

        getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);

    }

    @Override
    protected void onStart() {
        super.onStart();
        backHome.setClickable(true);
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
                        // 隐藏导航栏和状态栏
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


    //Modify the header information
    public void setTitle(Long userId,String name){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (userId==0l){
                    imageview.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.bluepeople));
                    imageWel.setText("Welcome");
                    imageText.setText(name);
                }else {
                    imageview.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.greenpeople));
                    imageWel.setText("Welcome");
                    imageText.setText(name);
                }
            }
        });
    }

    //Get the Predicted Orders product
    public void setForecastGoods(Long userId){
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

    //Get the Recommended Products data
    public void setRecommendGoods(Long userId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Goods> recommendGoods = getRecommendGoods(userId);
                    for (int i=0;i<recommendGoods.size();i++){
                        int q = i;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
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

    //Render the home page products
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

    //Loading product images
    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.connect();
            InputStream is = conn.getInputStream();
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    //Get recommended products
    public ArrayList<Goods> getRecommendGoods(Long userId) throws Exception {
        String recommendGoods = GoodsUtils.getRecommendGoods(userId);
        if (recommendGoods.equals("Network connection failure"))
            Toast.makeText(getContext(),"Network connection failure!",Toast.LENGTH_LONG).show();
        return MyJsonParser.getGoods(recommendGoods);
    }

    //Getting predicted products
    public ArrayList<Goods> getPredict(Long userId) throws Exception {
        String predictGoods = GoodsUtils.getPredictGoods(userId);

        if (predictGoods.equals("Network connection failure"))
            Toast.makeText(this,"Network connection failure!",Toast.LENGTH_LONG).show();
        return MyJsonParser.getGoods(predictGoods) ;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            backFace();
        }
        return super.onKeyDown(keyCode, event);
    }


    public void backFace(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(context, FaceDetectActivity.class);
                startActivity(intent);
            }
        };

        runOnFaceSdkReady(runnable);
        finish();
    }


    @Override
    public void onClick(View v) {

    }

    private void runOnFaceSdkReady(Runnable runnable) {
        if (isFaceSdkReady()) {
            if (runnable != null) {
                runnable.run();
            }
        } else {
            initSdk(runnable);
        }
    }

    private boolean isFaceSdkReady() {
        return YTLFFaceManager.isSDKRuning && YTLFFaceManager.isLoadDB;
    }

    private void initSdk(Runnable runnable) {
        Tools.showLoadingProgress(this, false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (YTLFFace.initIntellif(context) && initLicenseBySecret() && startFaceSDK() && loadDB()) {
                        Tools.debugLog("initSDK finish");
                    }

                    if (isFaceSdkReady()) {
                        if (runnable != null) {
                            runnable.run();
                        }
                    } else {
                        if (!Tools.isNetWorkConnect() && !YTLFFace.checkLicense()) {
                            Tools.toast(com.firefly.arcterndemo.R.string.ytlf_dictionaries43);
                        }
                    }

                } catch (Exception e) {
                    Tools.printStackTrace(e);
                    finish();
                } finally {
                    Tools.dismissLoadingProgress();
                }
            }
        }).start();
    }

    public boolean initLicenseBySecret() {
        return YTLFFace.initLicense(API_KEY);
        //return YTLFFace.initLicenseBySecret();
    }

    public boolean startFaceSDK() {
        if (!YTLFFaceManager.isSDKRuning) {
            int flag = YTLFFace.startFaceSDK();
            if (flag != 0) {
                YTLFFaceManager.isSDKRuning = false;
                showShortToast(com.firefly.arcterndemo.R.string.app_name);
            } else {
                YTLFFaceManager.isSDKRuning = true;
            }
        }
        return YTLFFaceManager.isSDKRuning;
    }

    public boolean loadDB() {
        DBManager dbManager = App.getInstance().getDbManager();
        List<Person> personList = dbManager.getPersonList();
        if (personList.size() <= 0) {
            Tools.debugLog("initSDK DB is empty");
            YTLFFaceManager.isLoadDB = true;
        } else {
            int size = personList.size();
            ArrayList<Long> idList  = new ArrayList<>();
            ArrayList<byte[]> featureList  = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                try {
                    Person person = personList.get(i);
                    if (idList.contains(person.getId()) || featureList.contains(person.getFeature()) ||
                            person.getFeature()== null || person.getFeature().length < 1) {
                        Tools.debugLog("person fail : ", person.toString());
                        continue;
                    }

                    idList.add(person.getId());
                    featureList.add(person.getFeature());
                } catch (Exception e) {
                    Tools.printStackTrace(e);
                }
            }

            long[] ids = new long[idList.size()];
            byte[][] features = new byte[idList.size()][];
            for (int i = 0; i < idList.size(); i++) {
                ids[i] = idList.get(i);
                features[i] = featureList.get(i);
            }

            YTLFFace.dataBaseAdd(ids, features);
            Tools.debugLog("initSDK loadDB size=%s", ids.length);
            YTLFFaceManager.isLoadDB = true;
        }

        return YTLFFaceManager.isLoadDB;
    }

}