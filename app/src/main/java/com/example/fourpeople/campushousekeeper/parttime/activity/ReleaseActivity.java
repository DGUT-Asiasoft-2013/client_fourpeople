package com.example.fourpeople.campushousekeeper.parttime.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fourpeople.campushousekeeper.R;

/**
 * Created by Administrator on 2016/12/20.
 */

public class ReleaseActivity extends Activity {
    Button releaseJob,releaseResume;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.part_fragment_release);
        releaseJob=(Button) findViewById(R.id.btn_advertise);
        releaseJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goNewJob();
            }
        });
        releaseResume=(Button) findViewById(R.id.btn_sendResume);
        releaseResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goResume();
            }
        });

    }
    void goNewJob()
    {
        Intent itnt=new Intent(this,NewJobsActivity.class);
        startActivity(itnt);
    }
    void goResume()
    {
        Intent intent=new Intent(this,NewResumeActivity.class);
        startActivity(intent);

    }
}
