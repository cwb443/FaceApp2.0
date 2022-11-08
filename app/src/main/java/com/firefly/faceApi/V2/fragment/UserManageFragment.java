package com.firefly.faceApi.V2.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.firefly.faceApi.V2.ChatAdapter;
import com.firefly.faceApi.V2.EditUserActivity;
import com.firefly.faceApi.V2.Event.UseManageAddEventClass;
import com.firefly.faceApi.V2.R;
import com.firefly.faceEngine.App;
import com.firefly.faceEngine.dblib.DBManager;
import com.firefly.faceEngine.dblib.bean.Person;
import com.intellif.YTLFFaceManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class UserManageFragment extends Fragment  implements ListItemClickHelp {

    private YTLFFaceManager YTLFFaceManage = YTLFFaceManager.getInstance();
    private DBManager dbManager = App.getInstance().getDbManager();
    ChatAdapter chatAdapter;
    ListView listView;
    ImageView imageView;
    ImageView delete;
    private ArrayList<String> dataList;
    private ArrayList<Person> personArrayList;

////    private GoodsUtils goodsUtils = new GoodsUtils();
//    // 在线获取授权 API_KEY
//    public final String API_KEY = "xrZEJz51qfiBI3FB";
//
//    // 指定本地SD卡目录，用于存放models和license公钥等文件
//    public static String FACE_PATH = "/sdcard/firefly/";

    // SDK
    private YTLFFaceManager YTLFFace = YTLFFaceManager.getInstance();
//    private YTLFFaceManager YTLFFace = YTLFFaceManager.getInstance().initPath(FACE_PATH);

    Dialog dialog;
    //  private CustomDialog dialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_user_manage,container,false);
        listView=view.findViewById(R.id.main_list_view);
        chatAdapter=new ChatAdapter(getActivity(),dataList, UserManageFragment.this);
        listView.setAdapter(chatAdapter);

        imageView=view.findViewById(R.id.id_add);
        delete = view.findViewById(R.id.id_del);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                personArrayList = new ArrayList<>();
                dataList = new ArrayList<>();
                List<Person> personList = dbManager.getPersonList();


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("All user will be deleted");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbManager.deletePersonAll();
                        YTLFFaceManage.dataBaseClear();
                        personArrayList = new ArrayList<>();
                        dataList = new ArrayList<>();
                        List<Person> personList = dbManager.getPersonList();
                        for (Person person : personList) {
                            personArrayList.add(person);
                            dataList.add(person.getName());
//            Log.e("", "name: "+person.getName()+" id："+person.getId() );
                        }

                        chatAdapter = new ChatAdapter(getActivity(), dataList, UserManageFragment.this);
                        listView.setAdapter(chatAdapter);
                        imageView.setClickable(true);
                    }
                });
                builder.show();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UseManageAddEventClass useManageAddEventClass = new UseManageAddEventClass();
                useManageAddEventClass.setRunStr("run");
                EventBus.getDefault().post(useManageAddEventClass);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
        personArrayList = new ArrayList<>();
        dataList = new ArrayList<>();
        List<Person> personList = dbManager.getPersonList();
        for (Person person: personList) {
            personArrayList.add(person);
            dataList.add(person.getName());
            Log.e("", "name: "+person.getName()+" id："+person.getId() );
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().unregister(this);
//        }
    }
    /**
     * 删除ListView中的数据
     *
     * @param postion item的位置
     */
    private void deleteItem(int postion) {
        chatAdapter.data.remove(postion);
        chatAdapter.notifyDataSetChanged();
    }



    public void onClick(View item, View widget, int position, int which) {
        switch (which) {
            case R.id.image_delete:   //lv条目中 iv_del
                //final int position = (int) v.getTag(); //获取被点击的控件所在item 的位置，setTag 存储的object，所以此处要强转

                //点击删除按钮之后，给出dialog提示
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("The data in position "+(position+1) +" will be deleted");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Person person = personArrayList.get(position);
                        dbManager.deletePerson(person.getId());

                        YTLFFace.dataBaseDelete(person.getId());

                        dataList.remove(position);
                        personArrayList.remove(position);
                        chatAdapter.notifyDataSetChanged();
                    }
                });
                builder.show();
                break;
            case R.id.image_edit:
         //showmyDialog(position);
                Intent intent=new Intent(getActivity(), EditUserActivity.class);
                Bundle data=new Bundle();
                data.putInt("position",position);
                intent.putExtra("data",data);
                startActivity(intent);
                break;
        }

    }

    private void showmyDialog(int position) {

        dialog=new CustomDialog(getContext(),R.style.mystyle,R.layout.dialog,position);
        dialog.show();
    }
    class CustomDialog extends Dialog implements
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
        private EditText newName;

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
         * @param postion
         */
        public CustomDialog(Context context, int theme, int resLayout,
                            int postion) {
            super(context, theme);
            this.context = context;
            this.layoutRes = resLayout;
            this.postion_1 = postion;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // 指定布局
            this.setContentView(layoutRes);
            // 根据id在布局中找到控件对象
            bt_cancal = (Button) findViewById(R.id.id_cancel_btn);
            bt_confirm = (Button) findViewById(R.id.id_comfirm_btn);
            newName=(EditText)findViewById(R.id.id_new_name);

            // 为按钮绑定点击事件监听器
            bt_cancal.setOnClickListener(this);
            bt_confirm.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                // 确定按钮
                case R.id.id_comfirm_btn:
                    // 修改

                    Person person = personArrayList.get(postion_1);
                    String str=newName.getText().toString();
                    dbManager.updatePersonById(person.getId(),str);

                    personArrayList = new ArrayList<>();
                    dataList = new ArrayList<>();
                    List<Person> personList = dbManager.getPersonList();
                    for (Person p: personList) {
                        personArrayList.add(p);
                        dataList.add(p.getName());
//                        Log.e("", "name: "+person.getName()+" id："+person.getId() );
                        Log.e("TAG," ,"onClick: ");
                    }

                    listView.setAdapter(new ChatAdapter(getActivity(),dataList, UserManageFragment.this));
                    dialog.dismiss();
                    Toast.makeText(getContext(),"Succeed!",Toast.LENGTH_LONG).show();
                    break;

                // 取消按钮
                case R.id.id_cancel_btn:
                    dialog.dismiss();

                default:
                    break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();


        personArrayList = new ArrayList<>();
        dataList = new ArrayList<>();
        List<Person> personList = dbManager.getPersonList();
        for (Person person : personList) {
            personArrayList.add(person);
            dataList.add(person.getName());
//            Log.e("", "name: "+person.getName()+" id："+person.getId() );
        }

        chatAdapter = new ChatAdapter(getActivity(), dataList, UserManageFragment.this);
        listView.setAdapter(chatAdapter);
        imageView.setClickable(true);


    }



}










