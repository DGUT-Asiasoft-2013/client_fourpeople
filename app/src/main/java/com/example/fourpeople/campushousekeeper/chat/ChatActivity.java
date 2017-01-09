package com.example.fourpeople.campushousekeeper.chat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.api.User;
import com.example.fourpeople.campushousekeeper.chat.util.ScenceHandle;
import com.example.fourpeople.campushousekeeper.chat.view.RecorderAdapter;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;


public class ChatActivity extends Activity {

    private ListView mListView;
    private ArrayAdapter<Data> mAdapter;

    public List<Data> mDatas = new ArrayList<Data>();


    public static String cScene = null;
    public static String inputMessage = null;


    public static EditText input;
    public static Button btn_voice, btn_send;
    ClientThread clientThread;
    public static ScenceHandle myScene;
//	public static GifView gf1;


    Handler myHandler;
    public String myName = null;
    public String hisName = null;
    Bitmap myIcon = null;

    Bitmap hisIcon = null;
    public User mine = null;
    public User him = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_layout);


        myName = getIntent().getStringExtra("myName");
        hisName = getIntent().getStringExtra("hisName");

        mine = (User) getIntent().getSerializableExtra("mine");
        him = (User) getIntent().getSerializableExtra("him");

        if (mine==null&&him==null) {
            myIcon = BitmapFactory.decodeResource(getResources(), R.drawable.person);
            hisIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        }else{
            getUserIcon(him);
            getUserIcon(mine);
        }

        mListView = (ListView) findViewById(R.id.id_listview);


        input = (EditText) findViewById(R.id.ed);
        // btn_voice = (Button) findViewById(R.id.btn_voice);
        btn_send = (Button) findViewById(R.id.btn_send);


        myHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 0x123) {
                    inputMessage = msg.obj.toString();
                    if (inputMessage.length() > 2) {
                        Data data = new AnalysizeString().analysizeString(inputMessage.trim());
                        if (data != null) {
                            mDatas.add(data);
                            mAdapter.notifyDataSetChanged();
                            Log.d("adapter", "handleMessage: " + data.getPosition() + data.getUserName());
                            mListView.setSelection(mDatas.size() - 1);
                            input.setText("");
                            inputMessage = null;
                        }
                    }
                }
                }

        };

        clientThread = new ClientThread(myHandler);
        new Thread(clientThread).start(); //��
        btn_send.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                try {
                    Message msg = new Message();
                    msg.what = 0x345;
                    String name=mine==null?myName:String.valueOf(mine.getId());
                    msg.obj = "(" + name+ ")" + input.getText().toString();
                    clientThread.revHandler.sendMessage(msg);
                    input.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


        mAdapter = new RecorderAdapter(ChatActivity.this, mDatas);
        mListView.setAdapter(mAdapter);

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    //�޸�
    public class Data {
        float time;
        String userName;
        String content;
        Bitmap icon;
        int position;


        public Bitmap getIcon() {
            return icon;
        }

        public void setIcon(Bitmap icon) {
            this.icon = icon;
        }


        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }


        public void setContent(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public float getTime() {
            return time;
        }

        public void setTime(float time) {
            this.time = time;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }


    }

    class AnalysizeString {
        Data analysizeString(String response) {
            Data item = new Data();
            int left = response.indexOf('(');
            int right = response.indexOf(')');
            String userName=null;
            Log.d("anylize", "analysizeString: "+right+left+response.length());
            if (response.length()>right+1) {
                userName = response.substring(left + 1, right);
                item.setUserName(userName);
                item.setContent(response.substring(right + 1));
                item.setTime(0);
            }else{
                userName = response.substring(left + 1, right-1);
                item.setUserName(userName);
                item.setContent(" ");
                item.setTime(0);
            }

            if(mine!=null&&him!=null){
                if (userName.equals(String.valueOf(mine.getId()))) {
                    item.setPosition(0);
                    item.setIcon(myIcon);

                } else if (userName.equals(String.valueOf(him.getId()))) {
                    item.setPosition(1);
                    item.setIcon(hisIcon);
                } else {
                    return null;
                }
            }else{
                if( userName.equals(myName)){
                    item.setPosition(0);
                    item.setIcon(myIcon);
                }else if (userName.equals(hisName)) {
                    item.setPosition(1);
                    item.setIcon(hisIcon);
                } else {
                    return null;
                }

            }


            return item;
        }
    }



    public void getUserIcon(final User user) {

        Request request = new Request.Builder().url(Server.serverAddress + user.getAvatar()).method("get", null).build();

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
                            if (user == him) {
                                hisIcon = bmp;
                            } else {
                                myIcon = bmp;
                            }

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
