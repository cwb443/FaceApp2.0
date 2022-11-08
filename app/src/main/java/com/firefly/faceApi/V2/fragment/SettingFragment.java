package com.firefly.faceApi.V2.fragment;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.firefly.faceApi.V2.R;
import com.firefly.faceEngine.App;
import com.firefly.faceEngine.dblib.SaveInfo;
import com.firefly.faceEngine.dblib.SettingManage;
import com.firefly.faceEngine.dblib.bean.Setting;


import java.util.List;


public class SettingFragment extends Fragment {

    private SettingManage settingManage = App.getInstance().getSettingManage();

    public  Boolean flag=true;
    private boolean isCreated=false;

    ToggleButton button1,button2,button3;
    SeekBar seekBar;
    EditText forecastOrders,recommended;
    Button exit;
    TextView save;
    Setting setting;
    Boolean flag1,flag2,flag3;
    int progressSeekBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // View view=inflater.inflate(R.layout.fragment_news,parent ,false);
        View view1=LayoutInflater.from(getContext()).inflate(R.layout.fragment_setting,container,false);

        return view1;
//        button1.is
    }


    @Override
    public void onStart() {
        super.onStart();
        flushed();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden)
            flushed();
    }

    public void flushed(){
        List<Setting> saveInformation = settingManage.getSaveInformation();
        if (saveInformation.size() == 0){
            Setting setting = new Setting(null,0,0,0,0,"124.221.187.242:8081","124.221.187.242:8081");
            settingManage.SaveInformation(setting);
            button1.setChecked(false);
            button2.setChecked(false);
            button3.setChecked(false);
            seekBar.setProgress(0);
        }else {

            setting = saveInformation.get(0);
            Log.e("TAG", "flushed: "+setting );
            if (setting.getRecognition() == 0){
                button1.setChecked(false);
                flag1 = false;
            }

            else{
                button1.setChecked(true);
                flag1 = true;
            }

            if (setting.getRed() == 0){
                button2.setChecked(false);
                flag2=false;
            }

            else{
                button2.setChecked(true);
                flag2 = true;
            }

            seekBar.setClickable(flag2);
            seekBar.setEnabled(flag2);
            seekBar.setEnabled(flag2);

            if (setting.getInfrared() == 0){
                button3.setChecked(false);
                flag3=false;
            }

            else{
                button3.setChecked(true);
                flag3 = true;
            }


            seekBar.setProgress(setting.getBrightness());
            progressSeekBar = setting.getBrightness();

            if ("".equals(setting.getForecast()))
                forecastOrders.setHint("default");
            else
                forecastOrders.setText(setting.getForecast());

            if ("".equals(setting.getProducts()))
                recommended.setHint("default");
            else
                recommended.setText(setting.getProducts());
        }
    }

    public Boolean getFlag() {
        return flag;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        button1=getActivity().findViewById(R.id.button_1);
        button2=getActivity().findViewById(R.id.button_2);
        button3=getActivity().findViewById(R.id.button_3);
        seekBar=getActivity().findViewById(R.id.led_bar);
        forecastOrders=getActivity().findViewById(R.id.id_forecast_orders);
        recommended=getActivity().findViewById(R.id.id_recommended_products);

        exit=getActivity().findViewById(R.id.exit);
        save = getActivity().findViewById(R.id.save_overlay_view);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (flag1)
//                    setting.setRecognition(1);
//                else
//                    setting.setRecognition(0);
                if (flag2)
                    setting.setRed(1);
                else
                    setting.setRed(0);
                if (flag3)
                    setting.setInfrared(1);
                else
                    setting.setInfrared(0);

                setting.setBrightness(progressSeekBar);

                setting.setProducts(recommended.getText().toString());
                setting.setForecast(forecastOrders.getText().toString());
                settingManage.SaveInformation(setting);
                Toast.makeText(getContext(),"Save Success!",Toast.LENGTH_LONG).show();
            }
        });

        button1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b)
                    setting.setRecognition(1);
                else
                    setting.setRecognition(0);

                SaveInfo.UpdateInformation(getContext(),setting);
                flag1=b;
                button1.setSelected(b);

            }
        });

        button2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

//                if (b)
//                    setting.setRed(1);
//                else
//                    setting.setRed(0);
                flag2 = b;
//                SaveInfo.UpdateInformation(getContext(),setting);
                seekBar.setClickable(flag2);
                seekBar.setEnabled(flag2);
                seekBar.setEnabled(flag2);


                button2.setSelected(b);
            }
        });
        button3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

//                if (b)
//                    setting.setInfrared(1);
//                else
//                    setting.setInfrared(0);
                flag3 = b;
//                SaveInfo.UpdateInformation(getContext(),setting);

                button3.setSelected(b);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });



        forecastOrders.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                setting.setForecast(forecastOrders.getText().toString());
////                setting.setProducts(recommended.getText().toString());
////                SaveInfo.UpdateInformation(getContext(),setting);
            }
        });

        recommended.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//
//                setting.setProducts(recommended.getText().toString());
////                SaveInfo.UpdateInformation(getContext(),setting);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                setting.setBrightness(progress);
//                SaveInfo.UpdateInformation(getContext(),setting);
                if (flag1&&flag2){
                    Toast.makeText(getContext(),"The brightness range is 1 to 8, brightness is "+progress,Toast.LENGTH_SHORT).show();
                }
                progressSeekBar = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TAG", "onDestroy: qqq");




    }
}

















