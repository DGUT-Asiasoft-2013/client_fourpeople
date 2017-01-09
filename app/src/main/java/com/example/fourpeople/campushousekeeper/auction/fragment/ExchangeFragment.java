package com.example.fourpeople.campushousekeeper.auction.fragment;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Page;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.auction.activity.ExchangeActivity;
import com.example.fourpeople.campushousekeeper.auction.activity.ShowAuctionActivity;
import com.example.fourpeople.campushousekeeper.auction.entity.Auction;
import com.example.fourpeople.campushousekeeper.auction.entity.Bid;
import com.example.fourpeople.campushousekeeper.auction.entity.Transaction;
import com.example.fourpeople.campushousekeeper.auction.view.AuctionnerView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;


public class ExchangeFragment extends Fragment {
    View view;
    List<Bid> exchangeData;
    ListView exchangeList;
    Transaction transaction;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.auction_fragment_exchange, null);
            exchangeList = (ListView) view.findViewById(R.id.lv_bid_list);
            //  auctionAdapter = new AuctionAdapter(getActivity(), auctionData);
            exchangeList.setAdapter(auctionAdapter);
            exchangeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    onClick(i);
                }
            });
        }
        //loadData();

        return view;
    }

    private void onClick(final int index) {
        new AlertDialog.Builder(getActivity()).setTitle("交换信息").setMessage("客官确定交换此物？").setPositiveButton("是的，此物对我有用", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                postTransaction(exchangeData.get(index));

            }
        }).setNegativeButton("我再考虑一下吧！", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

    private void loadData(Integer id) {
        Request request = Server.requestBuildWithAuction("mybid/" + id).method("get", null).build();
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
                    List<Bid> bid = mapper.readValue(responseString, new TypeReference<List<Bid>>() {
                    });
              /*  for (Bid item : bid) {
                        if (item.getMethodIsPrice()!=true) {
                            exchangeData.add(item);
                            Log.d("item", "onResponse: "+item.getPrice());
                        }
                    }*/
                    exchangeData = bid;
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
        Auction auctionItem = (Auction) getActivity().getIntent().getSerializableExtra("auctionItem");
        loadData(auctionItem.getId());
    }

    BaseAdapter auctionAdapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return exchangeData == null ? 0 : exchangeData.size();
        }

        @Override
        public Object getItem(int position) {
            return exchangeData.get(position);
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
                //convertView = View.inflate(parent.getContext(), R.layout.auction_fragment_auction_item, null);
                holder = new ViewHolder();
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                convertView = layoutInflater.inflate(R.layout.auction_fragment_bid_item, null);
                holder.auctionName = (TextView) convertView.findViewById(R.id.tv_auctionners);
                holder.auctionnerView = (AuctionnerView) convertView.findViewById(R.id.img_picture);
                holder.introduction = (TextView) convertView.findViewById(R.id.tv_price);
            } else {
                holder = (ViewHolder) convertView.getTag();
                //itemview = convertView;
            }

            holder.auctionName.setText(exchangeData.get(position).getBider().getName());
            holder.introduction.setText(exchangeData.get(position).getPrice());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //   holder.time.setText(format.format(exchangeData.get(position).getCreateDate()));
            holder.auctionnerView.load(Server.serverAddress + exchangeData.get(position).getBider().getAvatar());
            convertView.setTag(holder);
            Log.d("view", exchangeData.get(position).toString());
            return convertView;
        }


    };

    static class ViewHolder {
        TextView auctionName;
        AuctionnerView auctionnerView;
        //TextView time;
        TextView introduction;
    }

    public void postTransaction(Bid bidItem) {

        Log.d("post", "postTransaction: "+bidItem.getId());
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("bidId", bidItem.getId().toString()).setType(MultipartBody.FORM);
        Request request = Server.requestBuildWithAuction("transaction/").method("post", null).post(builder.build()).build();
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
                    transaction = mapper.readValue(responseString, Transaction.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(getActivity()).setMessage(responseString.toString()).show();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
