package com.firefly.faceApi.V2.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.firefly.faceApi.V2.Event.FaceDetectEventClass;
import com.firefly.faceApi.V2.R;
//import com.firefly.faceApi.V2.utils.GoodsUtils;
import com.firefly.faceEngine.App;
import com.firefly.faceEngine.dblib.DBManager;
import com.firefly.faceEngine.dblib.bean.Person;
import com.firefly.faceEngine.utils.Tools;
import com.intellif.YTLFFaceManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    ImageView imageView;
//    GoodsUtils goodsUtils = new GoodsUtils();

//    // 在线获取授权 API_KEY
//    public final String API_KEY = "xrZEJz51qfiBI3FB";
//
//    // 指定本地SD卡目录，用于存放models和license公钥等文件
//    public static String FACE_PATH = "/sdcard/firefly/";
//
//    // SDK
//    private YTLFFaceManager YTLFFace = YTLFFaceManager.getInstance().initPath(FACE_PATH);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        SettingFragment settingFragment=new SettingFragment();

        Boolean flag = settingFragment.getFlag();
        if (flag){
            Runnable runnable = new Runnable() {

                @Override
                public void run() {

                }
            };
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getFaceDetectEvent(FaceDetectEventClass event){
        if(event == null){
            return;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_pho,null);
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_home,container,false);

    }


//    public void getGoods(){
//
////        String allGoods = goodsUtils.getAllGoods();
//
//    }
//
//
//    // 检测环境，并运行
//    private void runOnFaceSdkReady(Runnable runnable) {
//        if (isFaceSdkReady()) {
//            if (runnable != null) {
//                runnable.run();
//            }
//        } else {
//            initSdk(runnable);
//        }
//    }
//
//    // 是否SDK是否已可以
//    private boolean isFaceSdkReady() {
//        return YTLFFaceManager.isSDKRuning && YTLFFaceManager.isLoadDB;
//    }
//
//    // 初始化SDK
//    private void initSdk(Runnable runnable) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    if (YTLFFace.initIntellif(getContext()) && initLicenseBySecret() && startFaceSDK() && loadDB()) {
//                        Tools.debugLog("initSDK finish");
//                    }
//
//                    if (isFaceSdkReady()) {
//                        if (runnable != null) {
//                            runnable.run();
//                        }
//                    } else {
//                        if (!Tools.isNetWorkConnect() && !YTLFFace.checkLicense()) {
//                            Tools.toast(com.firefly.arcterndemo.R.string.ytlf_dictionaries43);
//                        }
//                    }
//
//                } catch (Exception e) {
//                    Tools.printStackTrace(e);
//                } finally {
//                    Tools.dismissLoadingProgress();
//                }
//            }
//        }).start();
//    }
//
//    // 加载授权license
//    public boolean initLicenseBySecret() {
//        return YTLFFace.initLicense(API_KEY);
//        //return YTLFFace.initLicenseBySecret();
//    }
//
//
//    // 启动FaceSDK
//    public boolean startFaceSDK() {
//        if (!YTLFFaceManager.isSDKRuning) {
//            int flag = YTLFFace.startFaceSDK();
//            if (flag != 0) {
//                YTLFFaceManager.isSDKRuning = false;
//            } else {
//                YTLFFaceManager.isSDKRuning = true;
//            }
//        }
//        return YTLFFaceManager.isSDKRuning;
//    }
//
//    // 加载人脸库
//    public boolean loadDB() {
//        DBManager dbManager = App.getInstance().getDbManager();
//        List<Person> personList = dbManager.getPersonList();
//        if (personList.size() <= 0) {
//            Tools.debugLog("initSDK DB is empty");
//            YTLFFaceManager.isLoadDB = true;
//        } else {
//            int size = personList.size();
//            ArrayList<Long> idList  = new ArrayList<>();
//            ArrayList<byte[]> featureList  = new ArrayList<>();
//
//            for (int i = 0; i < size; i++) {
//                try {
//                    Person person = personList.get(i);
//                    if (idList.contains(person.getId()) || featureList.contains(person.getFeature()) ||
//                            person.getFeature()== null || person.getFeature().length < 1) {
//                        Tools.debugLog("person fail : ", person.toString());
//                        continue;
//                    }
//
//                    idList.add(person.getId());
//                    featureList.add(person.getFeature());
//                } catch (Exception e) {
//                    Tools.printStackTrace(e);
//                }
//            }
//
//            long[] ids = new long[idList.size()];
//            byte[][] features = new byte[idList.size()][];
//            for (int i = 0; i < idList.size(); i++) {
//                ids[i] = idList.get(i);
//                features[i] = featureList.get(i);
//            }
//
//            YTLFFace.dataBaseAdd(ids, features);
//            Tools.debugLog("initSDK loadDB size=%s", ids.length);
//            YTLFFaceManager.isLoadDB = true;
//        }
//
//        return YTLFFaceManager.isLoadDB;
//    }



}
