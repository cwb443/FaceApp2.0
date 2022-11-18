package com.firefly.faceApi.V2.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.firefly.faceApi.V2.ChatAdapter;
import com.firefly.faceApi.V2.EditUserActivity;
import com.firefly.faceApi.V2.Event.UseManageAddEventClass;
import com.firefly.faceApi.V2.R;
import com.firefly.faceEngine.App;
import com.firefly.faceEngine.dblib.DBManager;
import com.firefly.faceEngine.dblib.SettingManage;
import com.firefly.faceEngine.dblib.bean.Person;
import com.intellif.YTLFFaceManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户管理页面
 * 对用户增删改查的入口
 */
public class UserManageFragment extends Fragment  implements ListItemClickHelp {

    private YTLFFaceManager YTLFFaceManage = YTLFFaceManager.getInstance();
    private DBManager dbManager = App.getInstance().getDbManager();
    private SettingManage settingManage = App.getInstance().getSettingManage();
    ChatAdapter chatAdapter;
    ListView listView;
    ImageView imageView;
    ImageView delete;
    private ArrayList<String> showList;
    private ArrayList<Person> connectList;
    private int customerList = settingManage.getSaveInformation().get(0).getCustomerList();

    Dialog dialog;

    // SDK
    private YTLFFaceManager YTLFFace = YTLFFaceManager.getInstance();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_user_manage,container,false);
        listView=view.findViewById(R.id.main_list_view);
        chatAdapter=new ChatAdapter(getActivity(),showList, UserManageFragment.this);

        if (customerList==1){
            listView.setAdapter(chatAdapter);
        }

        imageView=view.findViewById(R.id.id_add);
        delete = view.findViewById(R.id.id_del);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showmyDialog(-1,"All user will be deleted");
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
        connectList = new ArrayList<>();
        showList = new ArrayList<>();
        List<Person> personList = dbManager.getPersonList();
        for (Person person: personList) {
            connectList.add(person);
            showList.add(person.getName());
        }
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

    private void showmyDialog(int position,String title) {
        dialog=new UserManageFragment.CustomDialog(getContext(), R.style.mystyle, R.layout.delete_dlalog,position,title);
        dialog.show();
    }

    public void onClick(View item, View widget, int position, int which) {
        switch (which) {
            case R.id.image_delete:   //lv条目中 iv_del
              showmyDialog(position,null);
                break;
            case R.id.image_edit:
                Intent intent=new Intent(getActivity(), EditUserActivity.class);
                Bundle data=new Bundle();
                data.putInt("position",position);
                intent.putExtra("data",data);
                startActivity(intent);
                break;
        }
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
        private TextView deleteTitle;

        private String title;

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
        public CustomDialog(Context context, int theme, int resLayout,int position,String title) {
            super(context, theme);
            this.context = context;
            this.layoutRes = resLayout;
            postion_1=position;
            this .title = title;
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // 指定布局
            this.setContentView(layoutRes);
            // 根据id在布局中找到控件对象
            bt_cancal = (Button) findViewById(R.id.id_dialog_cancel_btn);
            bt_confirm = (Button) findViewById(R.id.id_dialog_comfirm_btn);
            deleteTitle=(TextView) findViewById(R.id.id_dialog_title);

            if (title==null){
                deleteTitle.setText("You will delete "+(postion_1+1)+" th");
            }else {
                deleteTitle.setText(title);
            }



            // 为按钮绑定点击事件监听器
            bt_cancal.setOnClickListener(this);
            bt_confirm.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int id = v.getId();// 确定按钮
            if (id == R.id.id_dialog_comfirm_btn ) {// 修改
                if (postion_1 == -1){
                    dbManager.deletePersonAll();
                    YTLFFaceManage.dataBaseClear();
                    setUserList();
                }else{
                    Person person = connectList.get(postion_1);
                    dbManager.deletePerson(person.getId());
                    YTLFFace.dataBaseDelete(person.getId());
                    showList.remove(postion_1);
                    connectList.remove(postion_1);
                    chatAdapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }else {
                // 取消按钮
                dialog.dismiss();
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            customerList = settingManage.getSaveInformation().get(0).getCustomerList();
            setUserList();
        }
    }

    public void setUserList(){
        if (customerList==1){
            connectList = new ArrayList<>();
            showList = new ArrayList<>();
            List<Person> personList = dbManager.getPersonList();
            for (Person person : personList) {
                connectList.add(person);
                showList.add(person.getName());
            }
            chatAdapter = new ChatAdapter(getActivity(), showList, UserManageFragment.this);
            listView.setAdapter(chatAdapter);
        }else {
            ArrayList<String> arrayList = new ArrayList<>();

            chatAdapter = new ChatAdapter(getActivity(), arrayList, UserManageFragment.this);
            listView.setAdapter(chatAdapter);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        setUserList();
    }
}










