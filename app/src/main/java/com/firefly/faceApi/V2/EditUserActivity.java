package com.firefly.faceApi.V2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firefly.faceEngine.App;
import com.firefly.faceEngine.dblib.DBManager;
import com.firefly.faceEngine.dblib.bean.Person;

import java.util.ArrayList;
import java.util.List;

public class EditUserActivity extends AppCompatActivity {

    private DBManager dbManager = App.getInstance().getDbManager();
    EditText newName;
    Integer position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        Intent intent=getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        position = bundle.getInt("position");
        newName = findViewById(R.id.name_text_edit);


    }

    public void confirmClick(View view){
        changeName();

    }

    public void changeName(){

        List<Person> personList = dbManager.getPersonList();//从数据库中拉取
        List<Person> personArrayList = new ArrayList<>();//展现的与拉取的id匹配
        List<String> dataList = new ArrayList<>();//展现
        for (Person person : personList) {
            personArrayList.add(person);
            dataList.add(person.getName());
        }

        Person person = personArrayList.get(position);

        String name=newName.getText().toString();

        String s = removeSpace(name);

        if (s.equals("")||s==null){
            Toast.makeText(this,"Name Can't Be Null",Toast.LENGTH_LONG).show();
            newName.setText("");
            newName.setHint("Please input name");
        }else {
            dbManager.updatePersonById(person.getId(),s);
            finish();
        }

//
//        personArrayList = new ArrayList<>();
//        dataList = new ArrayList<>();
//        personList = dbManager.getPersonList();
//        for (Person p: personList) {
//            personArrayList.add(p);
//            dataList.add(p.getName());
//        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){

            changeName();

        }
        return super.onKeyDown(keyCode, event);
    }

    public String removeSpace(String str){
        if ((str.equals("")||str == null)||(str.charAt(0) != ' '&&str.charAt(str.length()-1)!=' '))
            return str;
        String trim = str.trim();
        return removeSpace(trim);
    }
}