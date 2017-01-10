package com.example.fourpeople.campushousekeeper.auction.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.auction.fragment.widget.AuctionPictureInputCellFragment;
import com.example.fourpeople.campushousekeeper.auction.fragment.widget.AuctionSimpleTextCellFragment;
import com.example.fourpeople.campushousekeeper.auction.fragment.widget.MenuFragment;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddAuctionActivity extends Activity {
    private static final String[] m_arr = { "换物", "换钱"};

    AuctionSimpleTextCellFragment itenName;
    AuctionSimpleTextCellFragment itemPrice;
    AuctionSimpleTextCellFragment itemIntroduucton;
    AuctionSimpleTextCellFragment itemOthers;

    AuctionPictureInputCellFragment itemPicture;

    MenuFragment menuFragment = new MenuFragment();
    Spinner itemMethod;
    ImageButton menu;
    Boolean isOpen = false;
    EditText days;
    EditText bidPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auction_activity_add_auction);
        initView();
    }

    void initView() {
        itemMethod = (Spinner) findViewById(R.id.spinner);
        Button commit = (Button) findViewById(R.id.btn_commit);
        Button quit = (Button) findViewById(R.id.btn_quit);
        bidPrice= (EditText) findViewById(R.id.et_addprice);
        itenName = (AuctionSimpleTextCellFragment) getFragmentManager().findFragmentById(R.id.item_name);
        itemPrice = (AuctionSimpleTextCellFragment) getFragmentManager().findFragmentById(R.id.item_price);
        itemIntroduucton = (AuctionSimpleTextCellFragment) getFragmentManager().findFragmentById(R.id.item_introduction);
        itemOthers = (AuctionSimpleTextCellFragment) getFragmentManager().findFragmentById(R.id.item_others);
        itemPicture= (AuctionPictureInputCellFragment) getFragmentManager().findFragmentById(R.id.item_picture);


        days= (EditText) findViewById(R.id.et_days);
        menu = (ImageButton)findViewById(R.id.btn_menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (isOpen == false) {

                    getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_left,
                            R.animator.slide_out_right
                    ).replace(R.id.container, menuFragment).commit();


                  runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((ImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.auction_drag_down));
                        }
                    });
                    //isOpen=true;
                } else {

                 getFragmentManager().beginTransaction().remove(menuFragment).commit();
                   runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((ImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.auction_menu));
                        }
                    });
                    // isOpen=false;
                }
                isOpen = !isOpen;
            }

        });



        ArrayAdapter<CharSequence> ada = ArrayAdapter.createFromResource(this, R.array.auction_method, R.layout.auction_spinner_item);
        ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemMethod.setAdapter(ada);
        itemMethod.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String[] method = getResources().getStringArray(R.array.auction_method);
               // Toast.makeText(AddAuctionActivity.this, method[arg2], Toast.LENGTH_LONG).show();
                arg0.setVisibility(View.VISIBLE);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                //
            }
        });

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AddAuctionActivity.this).setMessage("确定提交？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        commitAuction();
                    }
                }).show();
            }
        });

    }

    //拍卖提交到服务器
    private void commitAuction() {
        String auctionItemName = itenName.getText();
        String auctionItemPrice = itemPrice.getText();
        String auctionItemIntroduction = itemIntroduucton.getText();
        String auctionItemOthers = itemOthers.getText();
        String auctionItemMethod = itemMethod.getSelectedItem().toString();
        String auctionItemDays=days.getText().toString();
        String auctionBidPrice=bidPrice.getText().toString();



        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("itemName", auctionItemName)
                .setType(MultipartBody.FORM)
                .addFormDataPart("price", auctionItemPrice)
                .addFormDataPart("bidPrice",auctionBidPrice)
                .addFormDataPart("others", auctionItemOthers)
                .addFormDataPart("method",auctionItemMethod)
                .addFormDataPart("days",auctionItemDays)
                .addFormDataPart("introduction",auctionItemIntroduction);
        if (itemPicture.getPngData() != null)  //上传照片
        {
           builder .addFormDataPart(
                "picture",
                "picture",
                RequestBody
                        .create(MediaType.parse("image/png"), itemPicture.getPngData()));
        }

        Request request=Server.requestBuildWithAuction("addAuction").method("post",null).post(builder.build()).build();

        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseString=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        new AlertDialog.Builder(AddAuctionActivity.this).setMessage("客官，您的货物已送拍卖咯，请点击查看！").setPositiveButton("有劳", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(AddAuctionActivity.this,AuctionsListActivity.class));
                                finish();
                            }
                        }).show();
                    }
                });

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        itenName.setLabelText("货物名");
        itenName.setHintText("货物名");
        itenName.setPassword(false);

        itemPrice.setLabelText(" 底  价");
        itemPrice.setHintText("最低价格");
        itemPrice.setPassword(false);

        itemIntroduucton.setLabelText(" 简  介 ");
        itemIntroduucton.setHintText("物品介绍");
        itemIntroduucton.setPassword(false);

        itemOthers.setLabelText(" 补  充 ");
        itemOthers.setHintText("其他补充");
        itemOthers.setPassword(false);

        menu.setImageDrawable(getResources().getDrawable(R.drawable.auction_menu));

    }


}
