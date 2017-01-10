package com.example.fourpeople.campushousekeeper.auction.fragment;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.auction.entity.AdDomain;
import com.example.fourpeople.campushousekeeper.auction.entity.Auction;
import com.example.fourpeople.campushousekeeper.auction.util.ADAdapter;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class ADFragment extends Fragment {
    public static String IMAGE_CACHE_PATH = "imageloader/Cache"; // 图片缓存路径

    View view;
    private ViewPager adViewPager;
    private List<ImageView> imageViews;// 滑动的图片集合

    private List<View> dots; // 图片标题正文的那些点
    private List<View> dotList;

    private TextView tv_date;
    private TextView tv_title;
    private TextView tv_topic_from;
    private TextView tv_topic;
    private int currentItem = 0; // 当前图片的索引号
    // 定义的五个指示点
    private View dot0;
    private View dot1;
    private View dot2;
    private View dot3;
    private View dot4;

    private ScheduledExecutorService scheduledExecutorService;

    // 异步加载图片
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;

    // 轮播banner的数据
    private List<AdDomain> adList;
    List<Auction> auctionDatas;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            adViewPager.setCurrentItem(currentItem);
        }

        ;
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        if (view == null) {
            view = inflater.inflate(R.layout.auction_fragment_banner, null);
            // 使用ImageLoader之前初始化
            initImageLoader();

            // 获取图片加载实例
            mImageLoader = ImageLoader.getInstance();
            options = new DisplayImageOptions.Builder()
                    .showStubImage(R.drawable.auction_top_banner_android)
                    .showImageForEmptyUri(R.drawable.auction_top_banner_android)
                    .showImageOnFail(R.drawable.auction_top_banner_android)
                    .cacheInMemory(true).cacheOnDisc(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.EXACTLY).build();

            initAdData();

            startAd();
        }

        return view;


    }


    private void initImageLoader() {
        File cacheDir = com.nostra13.universalimageloader.utils.StorageUtils
                .getOwnCacheDirectory(getActivity().getApplicationContext(),
                        IMAGE_CACHE_PATH);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getActivity()).defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new LruMemoryCache(12 * 1024 * 1024))
                .memoryCacheSize(12 * 1024 * 1024)
                .discCacheSize(32 * 1024 * 1024).discCacheFileCount(100)
                .discCache(new UnlimitedDiscCache(cacheDir))
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();

        ImageLoader.getInstance().init(config);
    }


    public List<AdDomain> getBannerAd() {
        List<AdDomain> adList = new ArrayList<AdDomain>();
        //loadData();
        List<Auction> data = auctionDatas;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (data != null) {
            for (Auction auction : data) {
                AdDomain adDomain = new AdDomain();
                adDomain.setId("108078");
                adDomain.setDate(format.format(auction.getCreateDate()));
                adDomain.setTitle(auction.getAuctionName());
                adDomain.setTopicFrom(auction.getAuctinner().getName());
                adDomain.setTopic(auction.getIntroduction());
                adDomain.setImgUrl(Server.serverAddress + auction.getPicture());
                adDomain.setAd(false);
                adList.add(adDomain);
            }
        } else {
            AdDomain adDomain = new AdDomain();
            adDomain.setId("108078");
            adDomain.setDate("3月4日");
            adDomain.setTitle("相机");
            adDomain.setTopicFrom("");
            adDomain.setTopic("小明2013年购进");
            adDomain.setImgUrl("http://p2.so.qhmsg.com/bdr/_240_/t01cbbd698c01a70517.jpg");
            adDomain.setAd(false);
            adList.add(adDomain);

            AdDomain adDomain2 = new AdDomain();
            adDomain2.setId("108078");
            adDomain2.setDate("3月5日");
            adDomain2.setTitle("山地车");
            adDomain2.setTopicFrom("小巫");
            adDomain2.setTopic("“去年入手”");
            adDomain2
                    .setImgUrl("http://p0.so.qhmsg.com/bdr/_240_/t01bf82f5f1682715d0.jpg");
            adDomain2.setAd(false);
            adList.add(adDomain2);

            AdDomain adDomain3 = new AdDomain();
            adDomain3.setId("108078");
            adDomain3.setDate("3月6日");
            adDomain3.setTitle("健身器");
            adDomain3.setTopicFrom("旭东");
            adDomain3.setTopic("“全新”");
            adDomain3
                    .setImgUrl("http://p1.so.qhmsg.com/bdr/_240_/t01f21aae7b21fae40b.jpg");
            adDomain3.setAd(false);
            adList.add(adDomain3);

            AdDomain adDomain4 = new AdDomain();
            adDomain4.setId("108078");
            adDomain4.setDate("3月7日");
            adDomain4.setTitle("演唱会门票");
            adDomain4.setTopicFrom("小软");
            adDomain4.setTopic("“汪峰演唱会”");
            adDomain4
                    .setImgUrl("http://p2.so.qhmsg.com/bdr/_240_/t01f273f5253274ae5c.jpg");
            adDomain4.setAd(false);
            adList.add(adDomain4);

            AdDomain adDomain5 = new AdDomain();
            adDomain5.setId("108078");
            adDomain5.setDate("3月8日");
            adDomain5.setTitle("手办");
            adDomain5.setTopicFrom("大熊");
            adDomain5.setTopic("“最爱的手办”");
            adDomain5
                    .setImgUrl("http://p1.so.qhmsg.com/bdr/_240_/t015439a5b12bd5bf41.jpg");
            adDomain5.setAd(true); // 代表是广告
            adList.add(adDomain5);
        }

        return adList;
    }


    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        private int oldPosition = 0;

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            AdDomain adDomain = adList.get(position);
            tv_title.setText(adDomain.getTitle()); // 设置标题
            tv_date.setText(adDomain.getDate());
            tv_topic_from.setText(adDomain.getTopicFrom());
            tv_topic.setText(adDomain.getTopic());
            dots.get(oldPosition).setBackgroundResource(R.drawable.auction_dot_normal);
            dots.get(position).setBackgroundResource(R.drawable.auction_dot_focused);
            oldPosition = position;
        }
    }


    private void startAd() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2,
                TimeUnit.SECONDS);
    }

    private class ScrollTask implements Runnable {

        @Override
        public void run() {
            synchronized (adViewPager) {
                currentItem = (currentItem + 1) % imageViews.size();
                handler.obtainMessage().sendToTarget();
            }
        }
    }


    private void addDynamicView() {
        // 动态添加图片和下面指示的圆点
        // 初始化图片资源
        for (int i = 0; i < adList.size(); i++) {
            ImageView imageView = new ImageView(getActivity());
            // 异步加载图片
            mImageLoader.displayImage(adList.get(i).getImgUrl(), imageView,
                    options);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViews.add(imageView);
            dots.get(i).setVisibility(View.VISIBLE);
            dotList.add(dots.get(i));
        }
    }


    private void initAdData() {
        // 广告数据
        adList = getBannerAd();

        imageViews = new ArrayList<ImageView>();

        // 点
        dots = new ArrayList<View>();
        dotList = new ArrayList<View>();
        dot0 = view.findViewById(R.id.v_dot0);
        dot1 = view.findViewById(R.id.v_dot1);
        dot2 = view.findViewById(R.id.v_dot2);
        dot3 = view.findViewById(R.id.v_dot3);
        dot4 = view.findViewById(R.id.v_dot4);
        dots.add(dot0);
        dots.add(dot1);
        dots.add(dot2);
        dots.add(dot3);
        dots.add(dot4);

        tv_date = (TextView) view.findViewById(R.id.tv_date);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_topic_from = (TextView) view.findViewById(R.id.tv_topic_from);
        tv_topic = (TextView) view.findViewById(R.id.tv_topic);

        adViewPager = (ViewPager) view.findViewById(R.id.vp);
        adViewPager.setAdapter(new ADAdapter(this.getActivity(), adList, imageViews));// 设置填充ViewPager页面的适配器
        // 设置一个监听器，当ViewPager中的页面改变时调用
        adViewPager.setOnPageChangeListener(new MyPageChangeListener());
        addDynamicView();
    }

/*    public void loadData() {

        Request request = Server.requestBuildWithAuction("auctions").method("get", null).build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final String responseString = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ObjectMapper mapper = new ObjectMapper();
                            try {
                                Page<Auction> auction = mapper.readValue(responseString, new TypeReference<Page<Auction>>() {
                                });
                                auctionDatas = auction.getContent();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }*/

    @Override
    public void onResume() {
        super.onResume();
        //loadData();
    }

}
