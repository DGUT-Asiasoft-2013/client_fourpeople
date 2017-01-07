package com.example.fourpeople.campushousekeeper.parttime.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.parttime.fragments_widgets.ImagePlayFragment;

/**
 * Created by Administrator on 2017/1/4.
 */

public class MyParttimeActivity extends Activity{
    ImagePlayFragment imagePlayFragment=new ImagePlayFragment();
    ImageButton myorder,my_job,my_employ,mycolection,btn_myrelease;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.part_activity_myparttime);
        imagePlayFragment = (ImagePlayFragment) getFragmentManager().findFragmentById(R.id.image_play);
        my_employ=(ImageButton)findViewById(R.id.my_employ);
        myorder=(ImageButton)findViewById(R.id.myorder);
        my_job=(ImageButton)findViewById(R.id.my_job);
        mycolection=(ImageButton)findViewById(R.id.mycolection);
        btn_myrelease=(ImageButton)findViewById(R.id.btn_myrelease);
        my_employ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyParttimeActivity.this,MyEmployActivity.class);
                startActivity(intent);
            }
        });
        my_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyParttimeActivity.this,MyWantJobActivity.class);
                startActivity(intent);
            }
        });


    }
}
