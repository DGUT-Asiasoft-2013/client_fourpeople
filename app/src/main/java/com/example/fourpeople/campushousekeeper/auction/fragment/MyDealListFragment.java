package com.example.fourpeople.campushousekeeper.auction.fragment;


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
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.auction.entity.Transaction;
import com.example.fourpeople.campushousekeeper.auction.fragment.widget.TitleFragment;
import com.example.fourpeople.campushousekeeper.auction.view.AuctionnerView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;

import okhttp3.Request;
import okhttp3.Response;


public class MyDealListFragment extends Fragment {
    View view;
    List<Transaction> transactionData;
    ListView transactionList;
    List<Transaction> auctionData;
    ListView auctionList;
    TitleFragment bidTitle;
    TitleFragment auctionTitle;

    Transaction transaction;
    // AuctionAdapter auctionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.auction_fragment_my_deal, null);
            transactionList = (ListView) view.findViewById(R.id.lv_transaction_list);
            auctionList = (ListView) view.findViewById(R.id.lv_post_auction_list);
            auctionList.setAdapter(auctionAdapter);
            //  auctionAdapter = new AuctionAdapter(getActivity(), auctionData);
            transactionList.setAdapter(dealAdapter);
            bidTitle = (TitleFragment) getFragmentManager().findFragmentById(R.id.deal_title_bid);
            auctionTitle = (TitleFragment) getFragmentManager().findFragmentById(R.id.deal_title_auction);
            bidTitle.setOnClicklistener(new TitleFragment.OnClicklistener() {
                @Override
                public void onClick() {
                    if (transactionList.getVisibility()== View.VISIBLE) {
                        transactionList.setVisibility(View.GONE);
                    }else{
                        transactionList.setVisibility(View.VISIBLE);
                    }

                }
            });

            auctionTitle.setOnClicklistener(new TitleFragment.OnClicklistener() {
                @Override
                public void onClick() {
                    if (auctionList.getVisibility()== View.VISIBLE) {
                        auctionList.setVisibility(View.GONE);
                    }else {
                        auctionList.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        return view;
    }



    private void loadData() {
        Request request = Server.requestBuildWithAuction("findfinishtransaction/").method("get", null).build();
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
                    List<Transaction> transactions = mapper.readValue(responseString, new TypeReference<List<Transaction>>() {
                    });
                    transactionData = transactions;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                onAttach(getActivity());
                if (getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dealAdapter.notifyDataSetChanged();
                        }
                    });
                }



            }
        });
    }

    private void loadAuctionData() {
        Request request = Server.requestBuildWithAuction("findfinishauction/").method("get", null).build();
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
                    List<Transaction> transactions = mapper.readValue(responseString, new TypeReference<List<Transaction>>() {
                    });
                    auctionData = transactions;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                onAttach(getActivity());
                if (getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dealAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
        loadAuctionData();
        bidTitle.setTitleText("竞拍单");
        bidTitle.setImageBitmap(R.drawable.title1);

        auctionTitle.setTitleText("出货卡");
        auctionTitle.setImageBitmap(R.drawable.title2);
    }

    BaseAdapter dealAdapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return transactionData == null ? 0 : transactionData.size();
        }

        @Override
        public Object getItem(int position) {
            return transactionData.get(position);
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
                convertView = layoutInflater.inflate(R.layout.auction_fragment_transaction_item, null);
                holder.auctionName = (TextView) convertView.findViewById(R.id.tv_auction);
                holder.bid = (TextView) convertView.findViewById(R.id.tv_bid);
                holder.auctionnerView = (AuctionnerView) convertView.findViewById(R.id.img_auctionner_picture);
                holder.bidderView = (AuctionnerView) convertView.findViewById(R.id.img_picture);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.auctionName.setText(transactionData.get(position).getBid().getAuction().getAuctionName());
            holder.bid.setText(transactionData.get(position).getBid().getPrice());
            holder.bidderView.load(Server.serverAddress + transactionData.get(position).getBid().getBider().getAvatar());
            holder.auctionnerView.load(Server.serverAddress + transactionData.get(position).getAuctionner().getAvatar());
            convertView.setTag(holder);
            Log.d("view", transactionData.get(position).toString());
            return convertView;
        }


    };

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
                holder = new ViewHolder();
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                convertView = layoutInflater.inflate(R.layout.auction_fragment_transaction_item, null);
                holder.auctionName = (TextView) convertView.findViewById(R.id.tv_auction);
                holder.bid = (TextView) convertView.findViewById(R.id.tv_bid);
                holder.auctionnerView = (AuctionnerView) convertView.findViewById(R.id.img_auctionner_picture);
                holder.bidderView = (AuctionnerView) convertView.findViewById(R.id.img_picture);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.bid.setText(auctionData.get(position).getBid().getAuction().getAuctionName());
            holder.auctionName.setText(auctionData.get(position).getBid().getPrice());
            holder.auctionnerView.load(Server.serverAddress + auctionData.get(position).getBid().getBider().getAvatar());
            holder.bidderView.load(Server.serverAddress + auctionData.get(position).getAuctionner().getAvatar());
            convertView.setTag(holder);
            Log.d("view", auctionData.get(position).toString());
            return convertView;
        }


    };

    static class ViewHolder {
        TextView auctionName;
        TextView bid;
        AuctionnerView auctionnerView;
        AuctionnerView bidderView;
    }

}
