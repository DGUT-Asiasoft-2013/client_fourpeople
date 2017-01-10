package com.example.fourpeople.campushousekeeper.auction.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.auction.entity.Auction;
import com.example.fourpeople.campushousekeeper.auction.entity.Bid;
import com.example.fourpeople.campushousekeeper.auction.entity.Transaction;
import com.example.fourpeople.campushousekeeper.auction.fragment.ExchangeFragment;
import com.example.fourpeople.campushousekeeper.auction.fragment.widget.TitleFragment;
import com.example.fourpeople.campushousekeeper.auction.view.AuctionnerView;
import com.example.fourpeople.campushousekeeper.chat.ChatActivity;
import com.example.fourpeople.campushousekeeper.mall.view.GoodsAvatar;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class ExchangeActivity extends Activity {

    Auction auction ;
    AuctionnerView picture;
    TextView price;
    Button agree;
    ExchangeFragment exchangeFragment = new ExchangeFragment();
    Bid bid;
    Transaction transaction;
    TextView bidCount;

    TextView biderName;
    TextView bidPrice;
    TextView bidRelation;
    TextView bidAddress;
    TitleFragment biderNameTitle;
    TitleFragment bidPriceTitle;
    TitleFragment bidRelationTitle;
    TitleFragment bidAddressTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auction = (Auction) getIntent().getSerializableExtra("auctionItem");
        loadData();
        reload();
        if (auction.getStateInfo().equals("正在拍卖")) {
            setContentView(R.layout.auction_activity_exchange);
            getFragmentManager().beginTransaction().replace(R.id.container, exchangeFragment).commit();
            picture = (AuctionnerView) findViewById(R.id.bidder_price_picture);
            price = (TextView) findViewById(R.id.tv_price);
            agree = (Button) findViewById(R.id.btn_agree);

            GoodsAvatar auctionPicture = (GoodsAvatar) findViewById(R.id.img_auction_picture);
            auctionPicture.load(auction.getPicture());

            TextView auctionName = (TextView) findViewById(R.id.et_auction_name);
            auctionName.setText(auction.getAuctionName());

            bidCount = (TextView) findViewById(R.id.et_counts);

            TextView time = (TextView) findViewById(R.id.et_time);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            time.setText(format.format(auction.getCreateDate()));

            TextView introduction = (TextView) findViewById(R.id.tv_introduction);
            introduction.setText("\u3000\u3000" + auction.getIntroduction());


            agree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(ExchangeActivity.this).setTitle("交换信息").setMessage("客官确定交换此物？").setPositiveButton("是的，此物对我有用", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            postTransaction(bid);
                        }
                    }).setNegativeButton("我再考虑一下吧！", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
                }
            });

        } else {
            setContentView(R.layout.auction_activity_exchange_step_two);
            picture = (AuctionnerView) findViewById(R.id.bidder_price_picture);
            price = (TextView) findViewById(R.id.tv_price);
            agree = (Button) findViewById(R.id.btn_agree);

            GoodsAvatar auctionPicture = (GoodsAvatar) findViewById(R.id.img_auction_picture);
            auctionPicture.load(auction.getPicture());

            TextView auctionName = (TextView) findViewById(R.id.et_auction_name);
            auctionName.setText(auction.getAuctionName());

            bidCount = (TextView) findViewById(R.id.et_counts);

            TextView time = (TextView) findViewById(R.id.et_time);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            time.setText(format.format(auction.getCreateDate()));

            TextView introduction = (TextView) findViewById(R.id.tv_introduction);
            introduction.setText("\u3000\u3000" + auction.getIntroduction());

            biderName = (TextView) findViewById(R.id.tv_bider_name);
            bidAddress = (TextView) findViewById(R.id.tv_bider_address);
            bidPrice = (TextView) findViewById(R.id.tv_bid_price);
            bidRelation = (TextView) findViewById(R.id.tv_bider_phone);

            bidAddressTitle = (TitleFragment) getFragmentManager().findFragmentById(R.id.fra_title_address);
            biderNameTitle = (TitleFragment) getFragmentManager().findFragmentById(R.id.fra_title_name);
            bidPriceTitle = (TitleFragment) getFragmentManager().findFragmentById(R.id.fra_title_price);
            bidRelationTitle = (TitleFragment) getFragmentManager().findFragmentById(R.id.fra_title_phone);
            biderNameTitle.setTitleText("用户");
            bidAddressTitle.setTitleText("联系地址");
            bidPriceTitle.setTitleText("出价");
            bidRelationTitle.setTitleText("联系方式");


          bidRelation.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + bid.getBider().getTel()));
                  startActivity(intent);
              }
          });
            findViewById(R.id.btn_chat).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(ExchangeActivity.this, ChatActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("mine",auction.getAuctinner());
                    bundle.putSerializable("him",bid.getBider());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            findViewById(R.id.btn_finish).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bid.getAuction().getStateInfo().equals("交易中")) {
                        if (transaction==null) {
                            finishTransaction(auction.getId());
                            reload();
                        }

                    }

                }
            });

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!auction.getStateInfo().equals("拍卖过期")&&!auction.getStateInfo().equals("正在拍卖")) {
            biderNameTitle.setTitleText("用户");
            bidAddressTitle.setTitleText("联系地址");
            bidPriceTitle.setTitleText("出价");
            bidRelationTitle.setTitleText("联系方式");
        }
        loadData();
    }

    private void loadData() {
        Request request = Server.requestBuildWithAuction("bid/" + auction.getId()).method("get", null).build();
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
                    bid = mapper.readValue(responseString, Bid.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (auction.getStateInfo().equals("交易中") || auction.getStateInfo().equals("正在交易") || auction.equals("交易完成")) {

                                bidAddress.setText(bid.getBider().getAddress());
                                bidPrice.setText(bid.getPrice());
                                bidRelation.setText(bid.getBider().getTel());
                                biderName.setText(bid.getBider().getName());
                                /*biderNameTitle.setTitleText("用户");
                                bidAddressTitle.setTitleText("联系地址");
                                bidPriceTitle.setTitleText("出价");
                                bidRelationTitle.setTitleText("联系方式");*/
                            } else {
                                picture.load(Server.serverAddress + bid.getBider().getAvatar());
                                price.setText(bid.getPrice());
                            }

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void postTransaction(Bid bidItem) {

        final MultipartBody.Builder builder = new MultipartBody.Builder();
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Intent intent = new Intent(ExchangeActivity.this, MyAuctionActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("transaction", transaction);
                            startActivity(intent);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
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
                    runOnUiThread(new Runnable() {
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


    void reload() {
        Request request = Server.requestBuildWithAuction("bid/counts/" + auction.getId()).method("get", null).build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                try {
                    String responseString = response.body().string();
                    Log.d("count", "onResponse: " + responseString);
                    ObjectMapper mapper = new ObjectMapper();
                    final String count = mapper.readValue(responseString, String.class);

                    if (!count.equals("")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bidCount.setText(count);
                                bidCount.invalidate();
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
