package com.example.fourpeople.campushousekeeper.auction.fragment;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.api.User;
import com.example.fourpeople.campushousekeeper.auction.entity.Transaction;
import com.example.fourpeople.campushousekeeper.auction.view.AuctionnerView;
import com.example.fourpeople.campushousekeeper.chat.ChatActivity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class MyTransactionFragment extends Fragment {
    View view;
    List<Transaction> transactionData;
    ListView transactionList;
    Transaction transaction;
    Transaction item;
    // AuctionAdapter auctionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.auction_fragment_my_transaction, null);
            transactionList = (ListView) view.findViewById(R.id.lv_transaction_list);
            //  auctionAdapter = new AuctionAdapter(getActivity(), auctionData);
            transactionList.setAdapter(transactionAdapter);
            transactionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("index", "onItemClick: " + i);
                    onClick(i);

                }
            });


        }
        return view;
    }


    private void onClick(final int index) {
        {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    item=transactionData.get(index);
                    String state = item.getState();
                    if (state.equals("交易中")) {
                        new AlertDialog.Builder(getActivity()).setMessage("您已取得交易权，现在是否进行交易联系？").setPositiveButton("好的",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //    beginTransaction(transactionData.get(index).getId());
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                new AlertDialog.Builder(getActivity()).setMessage("交易地点" + item.getAuctionner().getAddress() + "\n" + "联系方式：" + item.getAuctionner().getTel()).setPositiveButton("拨打电话", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.getAuctionner().getTel()));
                                                        getActivity().startActivity(intent);
                                                    }
                                                }).setNegativeButton("私信", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent intent=new Intent(getActivity(), ChatActivity.class);
                                                        Bundle bundle=new Bundle();
                                                        bundle.putSerializable("mine",item.getBid().getBider());
                                                        bundle.putSerializable("him",item.getAuctionner());
                                                        intent.putExtras(bundle);
                                                        startActivity(intent);
                                                    }
                                                }).setNeutralButton("完成交易", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        finishTransaction(item.getBid().getAuction().getId());
                                                    }
                                                }).show();
                                            }
                                        });
                                    }
                                }).setNegativeButton("不，我再想想", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setNeutralButton("我还是不要了吧", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
                    } else if (state.equals("正在交易")) {
                        User auctionner = transactionData.get(index).getAuctionner();
                        new AlertDialog.Builder(getActivity()).setMessage("是否同意交易？").setPositiveButton("同意", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                beginTransaction(transactionData.get(index).getId());
                            }
                        }).show();
                    }

                }
            });
        }
    }

    private void finishTransaction(Integer id) {

        //Log.d("post", "postTransaction: "+bidItem.getId());
        MultipartBody.Builder builder = new MultipartBody.Builder();
        // builder.addFormDataPart("bidId", bidItem.getId().toString()).setType(MultipartBody.FORM);
        Request request = Server.requestBuildWithAuction("finishtransaction/" + id).method("get", null).build
                ();
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
                            //  new AlertDialog.Builder(getActivity()).setMessage(responseString.toString()).show();
                            loadData();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void beginTransaction(int id) {

        //Log.d("post", "postTransaction: "+bidItem.getId());
        MultipartBody.Builder builder = new MultipartBody.Builder();
        // builder.addFormDataPart("bidId", bidItem.getId().toString()).setType(MultipartBody.FORM);
        Request request = Server.requestBuildWithAuction("begintransaction/" + id).method("get", null).build
                ();
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
                            //  new AlertDialog.Builder(getActivity()).setMessage(responseString.toString()).show();
                            Log.d("transactionbegin", "run: "+responseString);
                            loadData();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void loadData() {
        Request request = Server.requestBuildWithAuction("findtransaction/").method("get", null).build();
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
                            transactionAdapter.notifyDataSetChanged();
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
    }

    BaseAdapter transactionAdapter = new BaseAdapter() {

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
                //itemview = convertView;
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

    static class ViewHolder {
        TextView auctionName;
        TextView bid;
        AuctionnerView auctionnerView;
        AuctionnerView bidderView;
    }

}
