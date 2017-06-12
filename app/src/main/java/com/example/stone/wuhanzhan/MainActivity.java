package com.example.stone.wuhanzhan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.example.stone.wuhanzhan.config.Constant;
import com.palmaplus.nagrand.core.Engine;
import com.palmaplus.nagrand.data.DataList;
import com.palmaplus.nagrand.data.DataSource;
import com.palmaplus.nagrand.data.LocationList;
import com.palmaplus.nagrand.data.MapModel;
import com.palmaplus.nagrand.data.PlanarGraph;
import com.palmaplus.nagrand.view.MapView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MapView mapView;
    private Engine mEngine;
    private LinearLayout mLlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLlayout = (LinearLayout) findViewById(R.id.map);
        mEngine = Engine.getInstance(); //初始化引擎
        mEngine.startWithLicense(Constant.APP_KEY, this); //设置验证lincense，可以通过开发者平台去查找自己的lincense
        final DataSource mDataSource = new DataSource("http://api.ipalmap.com/"); //填写服务器的URL
        mapView = new MapView("default", this);
        mLlayout.addView(mapView);
        mapView.start(); //开始绘制地图
        mDataSource.requestMaps(new DataSource.OnRequestDataEventListener<DataList<MapModel>>() {
            @Override
            public void onRequestDataEvent(DataSource.ResourceState state, DataList<MapModel> data) {
                if (state != DataSource.ResourceState.OK)
                    return;
                if (data.getSize() == 0) //如果列表中的地图数量是0，请去开发者平台添加一些地图
                    return;
                mDataSource.requestPOIChildren(MapModel.POI.get(data.getPOI(0)), new DataSource.OnRequestDataEventListener<LocationList>() {
                    @Override
                    public void onRequestDataEvent(DataSource.ResourceState state, final LocationList data) {
                        if (state != DataSource.ResourceState.OK)
                            return;
                        if (data.getSize() == 0) //如果是0说明这套图没有楼层，请反馈给我们
                            return;
                        mDataSource.requestPlanarGraph(
                                1921922, //获取这套图的默认楼层id
                                new DataSource.OnRequestDataEventListener<PlanarGraph>() { //发起获取一个平面图的请求
                                    @Override
                                    public void onRequestDataEvent(DataSource.ResourceState state, PlanarGraph data) {
                                        if (state == DataSource.ResourceState.OK) {
                                            mapView.drawPlanarGraph(data);  //加载平面图
                                            data.drop();
                                        } else {
                                            //error
                                        }
                                    }
                                });
                        data.drop();
                    }
                });
                data.drop();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.drop();
    }
}
