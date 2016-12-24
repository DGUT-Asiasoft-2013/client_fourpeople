package com.example.fourpeople.campushousekeeper.widget;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.entity.Auction;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2016/12/23.
 */

public class AuctionAdapter extends BaseAdapter {

    Context context;
    List<Auction> auctionData;

    public AuctionAdapter(Context context, List<Auction> auctionData){
        this.context=context;
        this.auctionData=auctionData;
    }
    @Override
    public int getCount() {
        return auctionData==null?0:auctionData.size();
    }

    @Override
    public Object getItem(int position) {
        return auctionData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {/*
         ViewHolder holder;
        if (convertView==null) {
           // convertView = View.inflate(context, R.layout.fragment_auction_item, null);
            holder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.fragment_auction_item, null);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.auctionName= (TextView) convertView.findViewById(R.id.tv_auctionners);
        holder.avatarView= (AvatarView) convertView.findViewById(R.id.img_picture);
        holder.introduction= (TextView) convertView.findViewById(R.id.tv_introduction);
        holder.time= (TextView) convertView.findViewById(R.id.tv_time);

        holder.auctionName.setText(auctionData.get(position).getAuctinner().getName());
        holder.introduction.setText(auctionData.get(position).getIntroduction());
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        holder.time.setText(format.format(auctionData.get(position).getCreateDate()));
        holder.avatarView.load(Server.serverAddress+auctionData.get(position).getPicture());
        convertView.setTag(holder);
        Log.d("view", auctionData.get(position).toString());
        return convertView;*/



        View itemview = null;
        // ViewHolder holder;
        if (convertView == null) {
            //convertView = View.inflate(parent.getContext(), R.layout.fragment_auction_item, null);
            //holder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            itemview = layoutInflater.inflate(R.layout.fragment_auction_item, null);
        } else {
            // holder = (ViewHolder) convertView.getTag();
            itemview = convertView;
        }
        TextView auctionName = (TextView) itemview.findViewById(R.id.tv_auctionners);
        com.example.fourpeople.campushousekeeper.widget.AvatarView avatarView = (com.example.fourpeople.campushousekeeper.widget.AvatarView) itemview.findViewById(R.id.img_picture);
        TextView introduction = (TextView) itemview.findViewById(R.id.tv_introduction);
        TextView time = (TextView) itemview.findViewById(R.id.tv_time);
        auctionName.setText(auctionData.get(position).getAuctinner().getName());
        introduction.setText(auctionData.get(position).getIntroduction());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time.setText(format.format(auctionData.get(position).getCreateDate()));
        avatarView.load(Server.serverAddress + auctionData.get(position).getPicture());
        // convertView.setTag(holder);
        Log.d("view", auctionData.get(position).toString());
        return itemview;
    }

    static  class ViewHolder{
        TextView auctionName;
        AvatarView avatarView;
        TextView time;
        TextView introduction;
    }
}
