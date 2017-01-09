package com.example.fourpeople.campushousekeeper.auction.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.fourpeople.campushousekeeper.auction.view.AuctionnerView;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class ExchangeActivity extends Activity {

    Auction auction;
    AuctionnerView picture;
    TextView price;
    Button agree;
    ExchangeFragment exchangeFragment = new ExchangeFragment();
    Bid bid;
    Transaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auction_activity_exchange);
        getFragmentManager().beginTransaction().replace(R.id.container, exchangeFragment).commit();

        auction = (Auction) getIntent().getSerializableExtra("auctionItem");
        picture = (AuctionnerView) findViewById(R.id.bidder_price_picture);
        price = (TextView) findViewById(R.id.tv_price);
        agree = (Button) findViewById(R.id.btn_agree);

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
        Log.d("auctionddd", "onCreate: " + auction);
        loadData();

    }


    @Override
    protected void onResume() {
        super.onResume();
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
                            picture.load(Server.serverAddress + bid.getBider().getAvatar());
                            price.setText(bid.getPrice());

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

                            Intent intent=new Intent(ExchangeActivity.this,MyAuctionActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("transaction",transaction);
                            startActivity(intent);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
