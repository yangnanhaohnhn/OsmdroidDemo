package cn.gistone.osmdroiddemo;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author EDZ
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.dl_drawer)
    DrawerLayout dlDrawer;
    @BindView(R.id.tv_gps_info)
    TextView tvGpsInfo;
    @BindView(R.id.pb_gps_info)
    ProgressBar pbGpsInfo;
    @BindView(R.id.ll_gps_info)
    LinearLayout llGpsInfo;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_satellite_number)
    TextView tvSatelliteNumber;
    @BindView(R.id.tv_type)
    TextView tvType;


    @BindView(R.id.mv_view)
    MapView mvView;
    private Unbinder unbinder;
    private SensorManager mSensorManager;
    private IMapController mapController;
    private CustomOsmMapConfig config;
    private SensorEventListener mSensorEventListener;
    private TilesOverlay tilesOverlay;
    private int type = 0;
    private final String[] typeName = {
            "天地图-矢量", "天地图-影像", "天地图-地形",
            "高德-矢量", "高德-影像",
            "百度-矢量", "百度-影像",
            "腾讯-矢量", "腾讯-影像", "腾讯-地形",
            "arcGis矢量", "arcGis影像",
            "OSM_默认", "OSM_自行车", "OSM_运输",
            "离线-数据",
            "谷歌-矢量", "谷歌-影像"
    };
    private LocationManager locationManager;
    private DirectedLocationOverlay locationOverlay;
    private boolean isFirstLocation = true;
    private Location locationBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        File externalStorageState = Environment.getExternalStorageDirectory();
//        //设置Osmdroid的文件路径，需要在MapView初始化之前进行设置
//        Configuration.getInstance().setOsmdroidBasePath(new File(externalStorageState , "osmdroid/img/"));
//       //设置Osmdroid的瓦片缓存路径
//        Configuration.getInstance().setOsmdroidTileCache(new File(externalStorageState , "osmdroid/img/"));
        //新版osm需要设置代理
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //设置初始内存缓存大小瓦片的多少，最少是3*3
        Configuration.getInstance().setCacheMapTileCount((short) 25);
        //设置矢量覆盖物超出的缓存数量
        Configuration.getInstance().setCacheMapTileOvershoot((short) 25);
        Configuration.getInstance().setTileFileSystemMaxQueueSize((short) 200);
        Configuration.getInstance().setTileFileSystemCacheTrimBytes(10000L * 1024 * 1024);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        initMap();
        initChooseMap(0);
        //初始化location
        initLocation();
        //初始化陀螺仪
        initSensorManager();
        tvType.setText(typeName[type]);
    }

    /**
     * 初始化定位
     * 开始的时候 使用默认的定位 待确定之后再进行定位
     */
    private void initLocation() {
        locationOverlay = config.getLocationOverlay();
        locationManager = MapUtil.initLocation(this, locationListener, tvSatelliteNumber);
    }

    /**
     * 获取当前的经纬度并显示在地图上
     */
    private LocationListener locationListener = new CommonLocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                if (llGpsInfo != null) {
                    llGpsInfo.setVisibility(View.GONE);
                }
                Log.e("TAG", "定位监听位置信息:lat:" + location.getLatitude() + "--lng" + location.getLongitude());
                //当前的坐标点
                locationBean = location;
                tvLocation.setText(String.format("%sE，", location.getLongitude()) + String.format("%sN", location.getLatitude()));

                GeoPoint center = MapUtil.convertToGcj02(type, location.getLatitude(), location.getLongitude());
                if (mapController != null) {
                    if (isFirstLocation) {
                        MapUtil.animateToPoint(mvView, center, mapController);
                        mapController.setCenter(center);
                        isFirstLocation = false;
                    }
                }
                locationOverlay.setLocation(center);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            super.onProviderDisabled(provider);
            if (llGpsInfo != null) {
                llGpsInfo.setVisibility(View.VISIBLE);
            }
        }
    };

    /**
     * 初始化陀螺仪
     */
    private void initSensorManager() {
        mSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float compassValue = event.values[0];
//                curLocationMarker.setRotation(compassValue);
                locationOverlay.setBearing(compassValue);
                if (mvView != null) {
                    mvView.invalidate();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        //注册监听器
        mSensorManager.registerListener(mSensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
    }


    @OnClick({R.id.tv_gg_sl, R.id.tv_gg_yx,
            R.id.tv_tdt_sl, R.id.tv_tdt_yx, R.id.ll_location, R.id.tv_tdt_dx,
            R.id.tv_gd_sl, R.id.tv_gd_yx,
            R.id.tv_bd_sl, R.id.tv_bd_yx,
            R.id.tv_tx_sl, R.id.tv_tx_yx, R.id.tv_tx_dx,
            R.id.tv_arcgis_sl, R.id.tv_arcgis_yx,
            R.id.tv_osm_sl, R.id.tv_osm_bicycle, R.id.tv_osm_transport,
            R.id.tv_lx_map,
            R.id.btn_enter,
            R.id.btn_convert
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_location:
                dlDrawer.openDrawer(GravityCompat.START);
                break;
            case R.id.tv_gg_sl:
                type = MapUtil.TYPE_GG_SL;
                initChooseMap(type);
                break;
            case R.id.tv_gg_yx:
                type = MapUtil.TYPE_GG_YX;
                initChooseMap(type);
                break;
            case R.id.tv_tdt_sl:
                type = MapUtil.TYPE_TDT_SL;
                initChooseMap(type);
                break;
            case R.id.tv_tdt_yx:
                type = MapUtil.TYPE_TDT_YX;
                initChooseMap(type);
                break;
            case R.id.tv_tdt_dx:
                type = MapUtil.TYPE_TDT_DX;
                initChooseMap(type);
                break;
            case R.id.tv_gd_sl:
                type = MapUtil.TYPE_GD_SL;
                initChooseMap(type);
                break;
            case R.id.tv_gd_yx:
                type = MapUtil.TYPE_GD_YX;
                initChooseMap(type);
                break;
            case R.id.tv_bd_sl:
                type = MapUtil.TYPE_BD_SL;
                initChooseMap(type);
                break;
            case R.id.tv_bd_yx:
                type = MapUtil.TYPE_BD_YX;
                initChooseMap(type);
                break;
            case R.id.tv_tx_sl:
                type = MapUtil.TYPE_TX_SL;
                initChooseMap(type);
                break;
            case R.id.tv_tx_yx:
                type = MapUtil.TYPE_TX_YX;
                initChooseMap(type);
                break;
            case R.id.tv_tx_dx:
                type = MapUtil.TYPE_TX_DX;
                initChooseMap(type);
                break;
            case R.id.tv_arcgis_sl:
                type = MapUtil.TYPE_ArcGis_SL;
                initChooseMap(type);
                break;
            case R.id.tv_arcgis_yx:
                type = MapUtil.TYPE_ArcGis_YX;
                initChooseMap(type);
                break;
            case R.id.tv_osm_sl:
                type = MapUtil.TYPE_OSM_SL;
                initChooseMap(type);
                break;
            case R.id.tv_osm_bicycle:
                type = MapUtil.TYPE_OSM_BICYCLE;
                initChooseMap(type);
                break;
            case R.id.tv_osm_transport:
                type = MapUtil.TYPE_OSM_TRANSPORT;
                initChooseMap(type);
                break;
            case R.id.tv_lx_map:
                type = MapUtil.TYPE_LX_DT;
                initChooseMap(type);
                break;
            case R.id.btn_enter:
                startActivity(new Intent(this, OfflineMapActivity.class));
                break;
            case R.id.btn_convert:
                startActivity(new Intent(this, ConvertToDbActivity.class));
                break;
            default:
                break;
        }
        tvType.setText(typeName[type]);
    }

    /**
     * 初始化地图类型
     */
    private void initMap() {
        mapController = mvView.getController();
        config = new CustomOsmMapConfig();
        config.initMapOverlays(mvView, this);
        //关闭硬件加速(绘制轨迹时需要)
//        getMapView().setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mvView.setLayerType(View.LAYER_TYPE_NONE, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mvView.onResume();
        config.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mvView.onPause();
        config.onPause();
    }

    /**
     * 初始化地图类型
     *
     * @param which
     */
    protected void initChooseMap(int which) {
        //加载地图类型
        tilesOverlay = MapUtil.setMapType(this, which, mvView, tilesOverlay, null);

        if (locationBean != null) {
            GeoPoint center = MapUtil.convertToGcj02(which, locationBean.getLatitude(), locationBean.getLongitude());
            if (locationOverlay != null && center != null) {
                locationOverlay.setLocation(center);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        //关闭定位信息
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(mSensorEventListener);
        }
    }
}
