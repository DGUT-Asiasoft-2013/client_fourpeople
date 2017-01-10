package com.example.fourpeople.campushousekeeper.auction.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.api.User;
import com.example.fourpeople.campushousekeeper.auction.entity.Auction;
import com.example.fourpeople.campushousekeeper.auction.entity.Bid;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.SimpleFormatter;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

import static java.security.AccessController.getContext;

public class ShowAuctionActivity extends Activity {

    TextView auctionName;
    TextView auctionIntroduction;
    TextView endDate;
    TextView auctionOthers;
    TextView auctionPrice;
    TextView bidCount;
    TextView auctinonner;

    EditText price;
    ImageView auctionImage;
    Bid bidItem;

    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                price.setText("");
            }

        }
    };
    Bitmap bitmap;
    Auction auction;
    Button bid;
    Button bidPrice;
    Boolean isMy;
    TextView maxPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auction_activity_show_auction);

        auction = (Auction) getIntent().getSerializableExtra("auctionItem");
        isMy = getIntent().getBooleanExtra("isMy", false);
        initView();
        initData();
        reload();
    }

    private void initData() {
        Request request = new Request.Builder().url(Server.serverAddress + auction.getPicture()).method("get", null).build();

        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    byte[] bytes = response.body().bytes();
                    final Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            auctionImage.setScaleType(ImageView.ScaleType.FIT_XY);
                            auctionImage.setImageBitmap(bmp);
                            auctionImage.invalidate();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView() {
        auctionName = (TextView) findViewById(R.id.tv_auction_name);
        auctionIntroduction = (TextView) findViewById(R.id.tv_auction_introduction);
        auctionOthers = (TextView) findViewById(R.id.tv_auction_others);
        auctionPrice = (TextView) findViewById(R.id.tv_auction_price);
        endDate = (TextView) findViewById(R.id.tv_auction_endDate);
        auctionImage = (ImageView) findViewById(R.id.img_auction_picture);
        bid = (Button) findViewById(R.id.btn_bid);
        price = (EditText) findViewById(R.id.et_price);
        bidCount = (TextView) findViewById(R.id.tv_count);
        maxPrice = (TextView) findViewById(R.id.tv_max_price);
        bidPrice = (Button) findViewById(R.id.btn_bid_price);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.bid_menu);

        bidPrice.setText("+"+auction.getBidPrice());
        auctionName.setText(auction.getAuctionName());
        auctionPrice.setText("￥" + auction.getPrice());
        auctinonner= (TextView) findViewById(R.id.tv_auctionner_name);
        auctinonner.setText(auction.getAuctinner().getName());
        auctionIntroduction.setText("\u3000\u3000" + "" + auction.getIntroduction());
        auctionOthers.setText(auction.getOthers());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        endDate.setText(format.format(auction.getCreateDate()));
        Boolean isAuctioning = auction.getIsAuctioning();
        Log.d("bidPrice", "onResponse: " + auction.getBidPrice());
        if (isMy) {
            relativeLayout.setVisibility(View.GONE);
            bidPrice.setVisibility(View.GONE);
        }


        if (!isAuctioning) {
            bid.setBackgroundColor(Color.GRAY);
            bid.setEnabled(false);
            price.setEnabled(false);
            price.setBackgroundColor(Color.GRAY);
        }
        bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String priceString = price.getText().toString().trim();
                postBid(priceString, 1);

            }
        });
        bidPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String priceString = price.getText().toString().trim();
                postBid(priceString, 2);
            }
        });

    }

    void postBid(final String price, int type) {
        MultipartBody multipartBody;
        //换物
        if (type == 1) {
            multipartBody = new MultipartBody.Builder()
                    .addFormDataPart("type", "1")
                    .addFormDataPart("price", price)
                    .addFormDataPart("auctionId", auction.getId().toString())
                    .build();
        } else {
            //换钱
            multipartBody = new MultipartBody.Builder()
                    .addFormDataPart("price", price)
                    .addFormDataPart("type", "2")
                    .addFormDataPart("auctionId", auction.getId().toString())
                    .build();
        }

        Request request = Server.requestBuildWithAuction("bid").method("post", null).post(multipartBody).build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseString = response.body().string();
                    ObjectMapper mapper = new ObjectMapper();
                    final Bid bid = mapper.readValue(responseString, Bid.class);
                    if (bid.getBider() != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(ShowAuctionActivity.this).setMessage("出价成功").setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        reload();
                                        loadData();
                                        Message msg = new Message();
                                        msg.what = 0x123;
                                        myHandler.sendMessage(msg);
                                    }
                                }).show();

                            }
                        });

                    } else {
                        Log.d("bid", "onResponse: " + bid.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(ShowAuctionActivity.this).setMessage(bid.toString()).show();
                            }
                        });
                    }
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(ShowAuctionActivity.this).setMessage(e.toString()).show();
                        }
                    });
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
                    bidItem = mapper.readValue(responseString, Bid.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            maxPrice.setText(bidItem.getPrice());

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
