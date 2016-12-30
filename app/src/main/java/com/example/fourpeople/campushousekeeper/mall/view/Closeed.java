package com.example.fourpeople.campushousekeeper.mall.view;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Administrator on 2016/12/27.
 */

public class Closeed {
    //关闭小键盘事件
    public static void onCloseClick(Activity activity) {
        //View view = activity.getWindow().peekDecorView();
       // if (view != null && view.getWindowToken() != null) {
            try {
                ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            } catch (Exception e) {
                Log.w("SOS", "==软键盘关闭时出了异常");
            }
        }
   // }
}
