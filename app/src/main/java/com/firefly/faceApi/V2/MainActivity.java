package com.firefly.faceApi.V2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.firefly.faceApi.V2.Event.UseManageAddEventClass;
import com.firefly.faceApi.V2.Keep.KeepLiveManager;
import com.firefly.faceApi.V2.fragment.SettingFragment;
import com.firefly.faceApi.V2.fragment.UserManageFragment;
import com.firefly.faceEngine.App;
import com.firefly.faceEngine.activity.BaseActivity;
import com.firefly.faceEngine.activity.FaceDetectActivity;
import com.firefly.faceEngine.dblib.DBManager;
import com.firefly.faceEngine.dblib.bean.Person;
import com.firefly.faceEngine.other.Debug;
import com.firefly.faceEngine.utils.Constants;
import com.firefly.faceEngine.utils.GlideImageLoader;
import com.firefly.faceEngine.utils.SPUtil;
import com.firefly.faceEngine.utils.Tools;
import com.intellif.YTLFFaceManager;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener{
    //Face recognition part++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Get Authorized Online API_KEY
    public final String API_KEY = "xrZEJz51qfiBI3FB";

    // Specifies the local SD card directory where the models and license public keys will be stored
    public static String FACE_PATH = "/sdcard/firefly/";

    // SDK
    private YTLFFaceManager YTLFFace = YTLFFaceManager.getInstance().initPath(FACE_PATH);
    //Face recognition part-------------------------------------------------------------------------------

//    HomeFragment homeFragment=new HomeFragment();
    UserManageFragment userManageFragment=new UserManageFragment();
    SettingFragment settingFragment=new SettingFragment();

    LinearLayout  Home,User,Setting;
    //ImageView add;
    MainActivity thisMainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        thisMainActivity = this;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        // 1.获取管理类
        this.getSupportFragmentManager().beginTransaction()
                .add(R.id.container_content,userManageFragment)
                .add(R.id.container_content,settingFragment)
                .hide(settingFragment)
                .commit();
        initView();
        //Face recognition part+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        initSetting();
        if (!Tools.isCameraCanUse()) {
            Tools.toast("Camera occupied or unavailable");
            finish();
        }

        //Pixel and transparent activities prioritize the App process
        KeepLiveManager.getInstance().registerKeepLiveReceiver(this);
        //Pixel and transparent activities prioritize the App process-------------------------------------------------------------------------------
        if (settingFragment.getFlag()){
            thisMainActivity.DetectRun();
        }
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
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        KeepLiveManager.getInstance().unregisterKeepLiveReceiver(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUseManageAddEvent(UseManageAddEventClass event){

        if(event == null){
            return;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, AddUserFeatureActivity.class);//DBActivity.class);
                startActivity(intent);
            }
        };

        runOnFaceSdkReady(runnable);
    }


    public void initView(){
        Home=(LinearLayout) this.findViewById(R.id.menu_home);
        User=(LinearLayout) this.findViewById(R.id.menu_user);
        Setting=(LinearLayout) this.findViewById(R.id.menu_setting);

        Home.setOnClickListener(this);
        User.setOnClickListener(this);
        Setting.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Home.setSelected(false);
        User.setSelected(true);
        Setting.setSelected(false);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.menu_user://图片
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .show(userManageFragment)
                        .hide(settingFragment)

                        .commit();
                Home.setSelected(false);
                User.setSelected(true);
                Setting.setSelected(false);
                break;
            case  R.id.menu_home://home
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .show(userManageFragment)
                        .hide(settingFragment)

                        .commit();
                Home.setSelected(false);
                User.setSelected(true);
                Setting.setSelected(false);
                DetectRun();
                break;
            case R.id.menu_setting://
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .hide(userManageFragment)
                        .show(settingFragment)
                        .commit();
                Home.setSelected(false);
                User.setSelected(false);
                Setting.setSelected(true);

                break;

            default:break;

        }
    }


    //Face recognition part+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Yes or no SDK is available
    private boolean isFaceSdkReady() {
        return YTLFFaceManager.isSDKRuning && YTLFFaceManager.isLoadDB;
    }

    // Initializing the SDK
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

    // Detect the environment and run it
    private void runOnFaceSdkReady(Runnable runnable) {
        if (isFaceSdkReady()) {
            if (runnable != null) {
                runnable.run();
            }
        } else {
            initSdk(runnable);
        }
    }

    // Loading the license
    public boolean initLicenseBySecret() {
        return YTLFFace.initLicense(API_KEY);
        //return YTLFFace.initLicenseBySecret();
    }

    // Start FaceSDK
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

    //Loading the face library
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

    public void DetectRun(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, FaceDetectActivity.class);
                startActivity(intent);
            }
        };

        runOnFaceSdkReady(runnable);
    }

    public void RegisterRun(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, AddUserFeatureActivity.class);
                startActivity(intent);
            }
        };

        runOnFaceSdkReady(runnable);
    }

    public void test3(View view) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Debug.doSearch(context, "/sdcard/firefly/图1.jpg");

                Tools.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Debug.doSearch(context, "/sdcard/firefly/图2.jpg");
                    }
                }, 3000);
            }
        };

        runOnFaceSdkReady(runnable);
    }

    public void test5(View view) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Debug.getFaceAttrs(context, "/sdcard/firefly/图1.jpg");

                Tools.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Debug.getFaceAttrs(context, "/sdcard/firefly/图2.jpg");
                    }
                }, 3000);
            }
        };

        runOnFaceSdkReady(runnable);
    }

    //初始化ImagePicker，拍照或选图
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setShowCamera(true);
        imagePicker.setCrop(true);
        imagePicker.setSaveRectangle(true);
        imagePicker.setMultiMode(false);
        imagePicker.setSelectLimit(1);
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        imagePicker.setFocusWidth(800);
        imagePicker.setFocusHeight(800);
        imagePicker.setOutPutX(1000);
        imagePicker.setOutPutY(1000);
    }

    private void initSetting() {
        initImagePicker();
        Constants.recognition_overturn_rgbcamera = SPUtil.readCameraRgb();
        Constants.recognition_overturn_ircamera = SPUtil.readCameraIr();
        Constants.face_frame_mirror = SPUtil.readFaceFrameMirror();
        Constants.face_frame_reverse = SPUtil.readFaceFrameReverse();

        Constants.recognition_overturn_rgbcamera = true;

        Constants.select_screen_rotate_rgbcamera = SPUtil.readScreenRotateRgbCamera();
        Constants.select_screen_rotate_ircamera = SPUtil.readScreenRotateIrCamera();

        Tools.debugLog("Signature=%s", YTLFFace.getSignature());
    }

    private void run(Runnable... runnables) {
        int index = 1;
        for (Runnable item : runnables) {
            Tools.runOnUiThread(item, index++ * 500);
        }
    }
    //Face recognition part-------------------------------------------------------------------------------
}