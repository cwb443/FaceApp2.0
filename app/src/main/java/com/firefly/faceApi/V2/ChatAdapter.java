package com.firefly.faceApi.V2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firefly.faceApi.V2.fragment.ListItemClickHelp;

import java.util.ArrayList;
import java.util.List;


public class ChatAdapter extends BaseAdapter  {

  public ArrayList<String> data = new ArrayList();

    private  List<String>         dataList;
    ImageView image_edit,image_delete;
    private  View.OnClickListener listener;
    private ListItemClickHelp callback;
    private Context contxet;

    public ChatAdapter(Context context, ArrayList<String> dataList, ListItemClickHelp callback) {
        //获得布局解析器
        this.callback=callback;
        data=dataList;
        this.contxet=context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder=new ViewHolder();
        if (convertView==null){
            convertView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_list,null);
            viewHolder.friendName=  convertView.findViewById(R.id.name_text_view);
            viewHolder.delete=convertView.findViewById(R.id.image_delete);
            viewHolder.edit=convertView.findViewById(R.id.image_edit);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        final View view = convertView;
        viewHolder.friendName.setText((String) data.get(i));
        int one=viewHolder.delete.getId();
        int two=viewHolder.edit.getId();
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(view, viewGroup, i, one);
            }
        });
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(view, viewGroup, i, two);
            }
        });
        viewHolder.delete.setTag(i);
        return view;
    }

    public class ViewHolder{

        ImageView delete;
        ImageView edit;
        private TextView friendName;

    }

}
