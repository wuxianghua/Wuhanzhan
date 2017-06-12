package com.example.stone.wuhanzhan;

import android.app.Application;
import android.widget.Toast;

import com.example.stone.wuhanzhan.config.Constant;
import com.example.stone.wuhanzhan.util.FileUtilsTools;

/**
 * Created by stone on 2017/6/8.
 */

public class AppAplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (FileUtilsTools.checkoutSDCard()) {
            FileUtilsTools.copyDirToSDCardFromAsserts(this, Constant.LUA_NAME, "font");
            FileUtilsTools.copyDirToSDCardFromAsserts(this, Constant.LUA_NAME, "Nagrand/lua");
        } else {
            Toast.makeText(this, "未找到SDCard", Toast.LENGTH_LONG).show();
        }
    }
}
