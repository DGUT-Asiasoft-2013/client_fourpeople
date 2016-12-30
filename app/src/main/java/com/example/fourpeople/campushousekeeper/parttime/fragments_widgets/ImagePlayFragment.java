package com.example.fourpeople.campushousekeeper.parttime.fragments_widgets;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.example.fourpeople.campushousekeeper.R.drawable.part_1;

/**
 * Created by Administrator on 2016/12/30.
 */

public class ImagePlayFragment extends Fragment{
    View view;
     int[] imageResIds;
     String[] titles;
     List imageViews;
     List dots;
     TextView tv_title;
     ViewPager viewPager;
     ScheduledExecutorService scheduledExecutorService;
     int currentItem = 0;//当前页面
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.part_fragment_image_play,null);
        imageResIds=new int[]{R.drawable.part_1,R.drawable.part_2,R.drawable.part_3};
        //滑动的图片
        imageViews = new ArrayList<ImageView>();
        for(int i = 0; i < imageResIds.length; i++){
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(imageResIds[i]);
            imageViews.add(imageView);
        }
        //图片所对应的圆点
        dots = new ArrayList<View>();
        dots.add(view.findViewById(R.id.v_dot0));
        dots.add(view.findViewById(R.id.v_dot1));
        dots.add(view.findViewById(R.id.v_dot2));
        dots.add(view.findViewById(R.id.v_dot3));
        dots.add(view.findViewById(R.id.v_dot4));
        viewPager = (ViewPager) view.findViewById(R.id.vp);
        //给viewpager添加数据
        viewPager.setAdapter(MyAdapter);
        //页面改变监听
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int oldPosition = 0;
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                currentItem=i;
                //改变圆点(焦点)
                dots.get(oldPosition);
                dots.get(i);

                oldPosition = i;

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        return view;

    }
    PagerAdapter MyAdapter=new PagerAdapter() {
        @Override
        public int getCount() {
            return imageResIds.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //将每个图片加入到ViewPager里
            ((ViewPager)container).addView((View) imageViews.get(position));
            return imageViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //将每个图片在ViewPager里释放掉
            ((ViewPager)container).removeView((View)object);
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            //view 和 Object 是不是一个对象
            return view == o;
        }

    };

    @Override
    public void onStart() {
        //用一个定时器  来完成图片切换
        //Timer 与 ScheduledExecutorService 实现定时器的效果

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        //通过定时器 来完成 每2秒钟切换一个图片
        //经过指定的时间后，执行所指定的任务
        //scheduleAtFixedRate(command, initialDelay, period, unit)
        //command 所要执行的任务
        //initialDelay 第一次启动时 延迟启动时间
        //period  每间隔多次时间来重新启动任务
        //unit 时间单位
        scheduledExecutorService.scheduleAtFixedRate(new ViewPagerTask(), 1, 5, TimeUnit.SECONDS);
        super.onStart();
    }

    @Override
    public void onStop() {
        //停止图片切换
        scheduledExecutorService.shutdown();

        super.onStop();
    }
    //用来完成图片切换的任务
    private class ViewPagerTask implements Runnable{

        public void run() {
            //实现我们的操作
            //改变当前页面
            currentItem = (currentItem + 1) % imageViews.size();
            //Handler来实现图片切换
            handler.obtainMessage().sendToTarget();
        }
    }
    Handler handler=new Handler()
    {
        public void handleMessage(Message msg) {
            //设定viewPager当前页面
            viewPager.setCurrentItem(currentItem);
        }
    };

}
