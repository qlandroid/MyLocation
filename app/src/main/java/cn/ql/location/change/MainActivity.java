package cn.ql.location.change;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import cn.ql.location.change.utils.LocationUtils;

public class MainActivity extends BaseActivity {
    public static final int REQUEST_QUERY = 0x321;
    private double mLongitude;
    private double mLatitude;

    MapView mMapView = null;
    AMap mAMap;

    MyLocationStyle myLocationStyle;

    Button btnQuery;
    Button btnStart,btnStop,btnToLocation;
    private Marker mMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnQuery = (Button) findViewById(R.id.btn_query);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnStop = (Button) findViewById(R.id.btn_stop);
        btnToLocation = (Button) findViewById(R.id.btn_toLocation);

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationUtils.stop();
            }
        });

        btnToLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLocation(mLongitude, mLatitude);
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "启动成功", Toast.LENGTH_SHORT).show();
                LocationUtils.startLocation(mLongitude, mLatitude);
            }
        });
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this,QueryAty.class),REQUEST_QUERY);
            }
        });
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        if (mAMap == null) {
            mAMap = mMapView.getMap();
        }
        //设置显示图标，并定位自己的位置
        myLocationStyle = getLocationStyle();
        mAMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style

        mAMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        mAMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        // 绑定 Marker 被点击事件
        mAMap.setOnMarkerClickListener(markerClickListener);
        // 绑定marker拖拽事件
        mAMap.setOnMarkerDragListener(markerDragListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_QUERY) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getBundleExtra(C.addressBundle);
                double latitude = bundle.getDouble(C.latitude);
                double longitude = bundle.getDouble(C.longitude);
                this.mLatitude = latitude;
                this.mLongitude = longitude;
                toLocation(longitude,latitude);
                String city = bundle.getString(C.city);
                String content = bundle.getString(C.content);
                MarkerOptions a = setLocation(longitude, latitude, city, content);
                if (mMarker !=null){
                    mMarker.remove();
                }
                mMarker = showLocationAddress(a);
                mMarker.showInfoWindow();
                toLocation(longitude,latitude);
            }
        }
    }



    /**
     * 获得焦点，显示在中心点，并放大
     *
     * @param longitude 经度
     * @param latitude  纬度
     */
    private void toLocation(double longitude, double latitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 18, 30, 0));
        mAMap.moveCamera(mCameraUpdate);
        //设置希望展示的地图缩放级别
        mCameraUpdate = CameraUpdateFactory.zoomTo(17);
        mAMap.moveCamera(mCameraUpdate);

    }

    /**
     * 在页面上显示锚点
     *
     * @param markerOptions
     */
    private Marker showLocationAddress(MarkerOptions markerOptions) {
        return mAMap.addMarker(markerOptions);
    }

    /**
     * 设置一个锚点
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @param city      城市
     * @param content   描述
     * @return
     */
    private MarkerOptions setLocation(double longitude, double latitude, String city, String content) {
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.title(city).snippet(content);

        markerOption.draggable(true);//设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.mipmap.my_location)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(false);//设置marker平贴地图效果
        return markerOption;
    }

    /**
     * 初始化自己的位置的显示图标
     *
     * @return
     */
    private MyLocationStyle getLocationStyle() {
        MyLocationStyle locationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        locationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        return locationStyle;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }


    // 定义 Marker 点击事件监听
    AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
        // marker 对象被点击时回调的接口
        // 返回 true 则表示接口已响应事件，否则返回false
        @Override
        public boolean onMarkerClick(Marker marker) {
            return false;
        }
    };
    // 定义 Marker拖拽的监听
    AMap.OnMarkerDragListener markerDragListener = new AMap.OnMarkerDragListener() {

        // 当marker开始被拖动时回调此方法, 这个marker的位置可以通过getPosition()方法返回。
        // 这个位置可能与拖动的之前的marker位置不一样。
        // marker 被拖动的marker对象。
        @Override
        public void onMarkerDragStart(Marker arg0) {
            // TODO Auto-generated method stub

        }

        // 在marker拖动完成后回调此方法, 这个marker的位置可以通过getPosition()方法返回。
        // 这个位置可能与拖动的之前的marker位置不一样。
        // marker 被拖动的marker对象。
        @Override
        public void onMarkerDragEnd(Marker arg0) {
            // TODO Auto-generated method stub

        }

        // 在marker拖动过程中回调此方法, 这个marker的位置可以通过getPosition()方法返回。
        // 这个位置可能与拖动的之前的marker位置不一样。
        // marker 被拖动的marker对象。
        @Override
        public void onMarkerDrag(Marker arg0) {
            // TODO Auto-generated method stub

        }
    };
}
