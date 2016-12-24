package com.example.fourpeople.campushousekeeper.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Page;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.entity.Auction;
import com.example.fourpeople.campushousekeeper.widget.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;


public class AuctionsListFragment extends Fragment {
    View view;
    List<Auction> auctionData;
    ListView auctionList;
    // AuctionAdapter auctionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_auctions_list, null);
            auctionList = (ListView) view.findViewById(R.id.lv_auctions_list);
            //  auctionAdapter = new AuctionAdapter(getActivity(), auctionData);
            auctionList.setAdapter(auctionAdapter);
        }
        //loadData();
        return view;
    }

    private void loadData() {
        Request request = Server.requestBuildWithAuction("auctions").method("get", null).build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    final String responseString = response.body().string();
                    ObjectMapper mapper = new ObjectMapper();
                    Page<Auction> auction = mapper.readValue(responseString, new TypeReference<Page<Auction>>() {
                    });
                    auctionData = auction.getContent();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        auctionAdapter.notifyDataSetChanged();
                    }
                });


            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    BaseAdapter auctionAdapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return auctionData == null ? 0 : auctionData.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
           // View itemview = null;
            ViewHolder holder;
            if (convertView == null) {
                //convertView = View.inflate(parent.getContext(), R.layout.fragment_auction_item, null);
                holder = new ViewHolder();
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                convertView = layoutInflater.inflate(R.layout.fragment_auction_item, null);
                holder.auctionName = (TextView) convertView.findViewById(R.id.tv_auctionners);
                holder.avatarView = (com.example.fourpeople.campushousekeeper.widget.AvatarView) convertView.findViewById(R.id.img_picture);
                holder.introduction = (TextView) convertView.findViewById(R.id.tv_introduction);
                holder.time = (TextView) convertView.findViewById(R.id.tv_time);
            } else {
                holder = (ViewHolder) convertView.getTag();
                //itemview = convertView;
            }

            holder.auctionName.setText(auctionData.get(position).getAuctinner().getName());
            holder.introduction.setText(auctionData.get(position).getIntroduction());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            holder.time.setText(format.format(auctionData.get(position).getCreateDate()));
            holder.avatarView.load(Server.serverAddress + auctionData.get(position).getPicture());
             convertView.setTag(holder);
            Log.d("view", auctionData.get(position).toString());
            return convertView;
        }


    };
    static  class ViewHolder{
        TextView auctionName;
        com.example.fourpeople.campushousekeeper.widget.AvatarView avatarView;
        TextView time;
        TextView introduction;
    }
}
