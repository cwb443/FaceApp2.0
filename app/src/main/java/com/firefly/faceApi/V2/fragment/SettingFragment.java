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
import com.firefly.faceEngine.dblib.SettingManage;
import com.firefly.faceEngine.dblib.bean.Setting;


import java.util.List;

/**
 * The Settings page
 */
public class SettingFragment extends Fragment {

    private SettingManage settingManage = App.getInstance().getSettingManage();

    public  Boolean flag=true;
    private boolean isCreated=false;

    ToggleButton button1,button2,button3,button4,button5;
    SeekBar seekBar;
    EditText forecastOrders,recommended,jump;
    Button exit;
    TextView save;
    Setting setting;
    Boolean flag1,flag2,flag3,flag4;

    public static Boolean flag5;

    int progressSeekBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view1=LayoutInflater.from(getContext()).inflate(R.layout.fragment_setting,container,false);

        return view1;
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

    /**
     * Initialize the database corresponding to the Settings page and modify the information of the Settings page
     */
    public void flushed(){
        List<Setting> saveInformation = settingManage.getSaveInformation();
        //对setting数据库进行初始化
        if (saveInformation.size() == 0){
            Setting setting = new Setting(null,0,0,0,0,
                    "124.221.187.242:8081","124.221.187.242:8081",0,0,"1");
            settingManage.SaveInformation(setting);
            button1.setChecked(false);
            button2.setChecked(false);
            button3.setChecked(false);
            button4.setChecked(false);
            button5.setChecked(false);
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

            if (setting.getWhite() == 0){
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

            if (setting.getGoodsOpen() == 0){
                button4.setChecked(false);
                flag4 = false;
            }

            else {
                button4.setChecked(true);
                flag4 = true;
            }

            if (setting.getCustomerList()==0){
                button5.setChecked(false);
                flag5 = false;
            }else {
                button5.setChecked(true);
                flag5 = true;
            }

            seekBar.setProgress(setting.getBrightness());
            progressSeekBar = setting.getBrightness();

            if ("".equals(setting.getPredicted()))
                forecastOrders.setHint("default");
            else
                forecastOrders.setText(setting.getPredicted());

            if ("".equals(setting.getRecommended()))
                recommended.setHint("default");
            else
                recommended.setText(setting.getRecommended());
            if ("".equals(setting.getRecommended()))
                jump.setText("1");
            else
                jump.setText(setting.getJumpInterval());
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
        button4=getActivity().findViewById(R.id.button_4);
        button5=getActivity().findViewById(R.id.button_list);

        seekBar=getActivity().findViewById(R.id.led_bar);
        forecastOrders=getActivity().findViewById(R.id.id_forecast_orders);
        recommended=getActivity().findViewById(R.id.id_recommended_products);
        jump = getActivity().findViewById(R.id.id_jump);

        exit=getActivity().findViewById(R.id.exit);
        save = getActivity().findViewById(R.id.save_overlay_view);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag2)
                    setting.setWhite(1);
                else
                    setting.setWhite(0);
                if (flag3)
                    setting.setInfrared(1);
                else
                    setting.setInfrared(0);
                if (flag4)
                    setting.setGoodsOpen(1);
                else
                    setting.setGoodsOpen(0);
                if (flag5)
                    setting.setCustomerList(1);
                else
                    setting.setCustomerList(0);

                setting.setBrightness(progressSeekBar);

                setting.setRecommended(recommended.getText().toString());
                setting.setPredicted(forecastOrders.getText().toString());
                setting.setJumpInterval(jump.getText().toString());
                settingManage.UpdateInformation(setting);
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

                settingManage.UpdateInformation(setting);
                flag1=b;
                button1.setSelected(b);

                if(b)
                    Toast.makeText(getContext(), "Face recognition is open", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "Face recognition is close", Toast.LENGTH_SHORT).show();

            }
        });

        button2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                flag2 = b;
                seekBar.setClickable(flag2);
                seekBar.setEnabled(flag2);
                seekBar.setEnabled(flag2);
                button2.setSelected(b);
            }
        });
        button3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                flag3 = b;
                button3.setSelected(b);
            }
        });

        button4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                flag4 = b;
                button4.setSelected(b);
            }
        });

        button5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                flag5 = b;
                button5.setSelected(b);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
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

















