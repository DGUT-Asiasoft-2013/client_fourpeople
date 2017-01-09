package com.example.fourpeople.campushousekeeper.chat.view;

import java.util.List;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.chat.ChatActivity.Data;
import com.example.fourpeople.campushousekeeper.chat.util.ViewHolder;


public class RecorderAdapter extends ArrayAdapter<Data> {

    private List<Data> voiceDatas;

    private int minItemWidth;
    private int maxItemWidth;


    View mAnimView;
    private Context mContext;




    public RecorderAdapter(Context context, List<Data> vDatas) {
        super(context, -1, vDatas);
        voiceDatas = vDatas;
        mContext = context;

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        maxItemWidth = (int) (outMetrics.widthPixels * 0.65f);
        minItemWidth = (int) (outMetrics.widthPixels * 0.15f);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // boolean flag=voiceDatas.get(position).getPosition();
        ViewHolder holder = null;
        View convertView1=null,convertView2=null;
        boolean flag=(voiceDatas.get(position).getPosition()==0?true:false);
        Log.d("bool", voiceDatas.get(position).getUserName()+flag);
        if(flag){

            convertView1=inflater.inflate(R.layout.chat_item_mine,null);
            holder=new ViewHolder();
            holder.chatting_time_tv=(TextView) convertView1.findViewById(R.id.id_time);
            holder.chatting_content_itv=(TextView)convertView1.findViewById(R.id.id_content);
            holder.chatting_avatar_iv=(ImageView)convertView1.findViewById(R.id.id_icon);
            convertView1.setTag(holder);

            convertView=convertView1;
        }else{
            convertView2=inflater.inflate(R.layout.chat_item_him, null);
            holder=new ViewHolder();
            holder.chatting_time_tv=(TextView) convertView2.findViewById(R.id.id_time);
            holder.chatting_content_itv=(TextView)convertView2.findViewById(R.id.id_content);
            holder.chatting_avatar_iv=(ImageView)convertView2.findViewById(R.id.id_icon);
            convertView2.setTag(holder);
            convertView=convertView2;
        }
        holder.chatting_time_tv.setText(voiceDatas.get(position).getUserName());
        holder.chatting_content_itv.setText(voiceDatas.get(position).getContent());
        holder.chatting_avatar_iv.setVisibility(View.VISIBLE);
        return convertView;

    }
    private class ViewHolder{
        TextView chatting_time_tv;
        TextView chatting_content_itv;
        ImageView chatting_avatar_iv;

    }


}
