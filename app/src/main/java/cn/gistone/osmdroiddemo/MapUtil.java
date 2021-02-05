package cn.gistone.osmdroiddemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.modules.ArchiveFileFactory;
import org.osmdroid.tileprovider.modules.IArchiveFile;
import org.osmdroid.tileprovider.modules.OfflineTileProvider;
import org.osmdroid.tileprovider.tilesource.FileBasedTileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

import cn.gistone.osmdroiddemo.utils.RxMapUtil;

/**
 * Create 2021/1/22
 *
 * @author N
 * desc:
 */
public class MapUtil {
    public static final int TYPE_TDT_SL = 0;
    public static final int TYPE_TDT_YX = 1;
    public static final int TYPE_TDT_DX = 2;
    public static final int TYPE_GD_SL = 3;
    public static final int TYPE_GD_YX = 4;
    public static final int TYPE_BD_SL = 5;
    public static final int TYPE_BD_YX = 6;
    public static final int TYPE_TX_SL = 7;
    public static final int TYPE_TX_YX = 8;
    public static final int TYPE_TX_DX = 9;
    public static final int TYPE_ArcGis_SL = 10;
    public static final int TYPE_ArcGis_YX = 11;
    public static final int TYPE_OSM_SL = 12;
    public static final int TYPE_OSM_BICYCLE = 13;
    public static final int TYPE_OSM_TRANSPORT = 14;
    public static final int TYPE_LX_DT = 15;
    public static final int TYPE_GG_SL = 16;
    public static final int TYPE_GG_YX = 17;

    public static int LOCATION_STATE = 0;

    /**
     * 移动到当前的位置
     *
     * @param mapView
     * @param point
     * @param mapController
     */
    public static void animateToPoint(MapView mapView, GeoPoint point, IMapController mapController) {
        InfoWindow.closeAllInfoWindowsOn(mapView);
        mapController.animateTo(point);
    }

    /**
     * 网络连接相关
     *
     * @param context
     * @return true:有网
     */
    public static final boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                // 判断当前网络状态是否为连接状态
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 初始化location
     *
     * @param locationListener
     * @param tvHomeStatelliteNum
     */
    @SuppressLint("MissingPermission")
    public static LocationManager initLocation(Activity activity, LocationListener locationListener, TextView tvHomeStatelliteNum) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        setGpsLocation(activity, locationManager, locationListener);
        //判断所有权限是否包含网络权限
        if (isNetworkAvailable(activity)) {
            if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER) && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                setNetWorkLocation(activity, locationManager, locationListener);
            }
        }

        //设置Gps卫星监听
        locationManager.addGpsStatusListener(event -> {
            if (GpsStatus.GPS_EVENT_SATELLITE_STATUS == event) {
                GpsStatus gpsStatus = locationManager.getGpsStatus(null);
                //获取卫星颗数的默认最大值
                int maxSatellites = gpsStatus.getMaxSatellites();
                //获取所有的卫星
                Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                //卫星颗数统计
                int count = 0;
                while (iters.hasNext() && count <= maxSatellites) {
                    GpsSatellite s = iters.next();
                    //卫星的信噪比
                    float snr = s.getSnr();
                    if (snr != 0) {
                        count++;
                    }
                }
                if (null != tvHomeStatelliteNum) {
                    tvHomeStatelliteNum.setText(String.valueOf(count));
                }
                int locationState = 0;
                try {
                    locationState = LOCATION_STATE;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (count >= 10) {
                    //使用GPS定位
                    if (locationState != 0) {
                        setGpsLocation(activity, locationManager, locationListener);
                    }
                } else {
                    //使用网络定位
                    if (isNetworkAvailable(activity)) {
                        if (locationState != 1) {
                            setNetWorkLocation(activity, locationManager, locationListener);
                        }
                    } else {
                        //小于10颗卫星，且没有网络的时候，
                        if (count >= 4) {
                            if (locationState != 0) {
                                setGpsLocation(activity, locationManager, locationListener);
                            }
                        } else {
                            //没有信息
                            if (locationState != 2) {
                                if (locationListener != null) {
                                    locationListener.onProviderDisabled(locationState == 0 ? "gps" : "network");
                                }
                                //没有定位到
                                LOCATION_STATE = 2;
                            }
                        }
                    }
                }
            }
        });
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            RxToastUtil.showToast(activity, R.string.please_open_gps);
            //返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            activity.startActivityForResult(intent, 0);
        }
        return locationManager;
    }

    /**
     * 设置Gps定位
     *
     * @param activity
     * @param locationManager
     * @param locationListener
     */
    private static void setGpsLocation(Activity activity, LocationManager locationManager, LocationListener locationListener) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return;
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 5, locationListener);
//        SPUtils.putInt(Constant.KEY_LOCATION_STATE, 0);
        LOCATION_STATE = 0;
    }

    /**
     * 设置网络定位
     *
     * @param activity
     * @param locationManager
     * @param locationListener
     */
    private static void setNetWorkLocation(Activity activity, LocationManager locationManager, LocationListener locationListener) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return;
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 5, locationListener);
        LOCATION_STATE = 1;
    }

    /**
     * 设置地图的类型
     *
     * @param mapType
     * @param mapView
     * @param tilesOverlay
     * @param path         离线地图的地址
     */
    public static TilesOverlay setMapType(Activity activity, int mapType, MapView mapView, TilesOverlay tilesOverlay, String path) {
        mapView.setTileProvider(new MapTileProviderBasic(activity));
        if (tilesOverlay != null) {
            mapView.getOverlayManager().remove(tilesOverlay);
        }
        mapView.getTileProvider().clearTileCache();
//        mapView.getTileProvider().getTileSource().name();
        switch (mapType) {
            case TYPE_GG_SL://加载的慢 但能出来 需要动态去设置 不实用
                mapView.setTileSource(CustomTileSource.googleTile);
                break;
            case TYPE_GG_YX://加载的慢 但能出来 需要动态去设置 不实用
                mapView.setTileSource(CustomTileSource.googleImg);
                tilesOverlay = new TilesOverlay(new MapTileProviderBasic(activity, CustomTileSource.googleImgMark), activity);
                mapView.getOverlayManager().add(0, tilesOverlay);
                break;
            case TYPE_TDT_SL://ok
                mapView.setTileSource(CustomTileSource.tianDiTuTileSource);
                tilesOverlay = new TilesOverlay(new MapTileProviderBasic(activity, CustomTileSource.tianDiTuTileMarkSource), activity);
                mapView.getOverlayManager().add(0, tilesOverlay);
                break;
            //天地图影像
            case TYPE_TDT_YX://ok
                mapView.setTileSource(CustomTileSource.tianDiTuImgSource);
                tilesOverlay = new TilesOverlay(new MapTileProviderBasic(activity, CustomTileSource.tianDiTuImgMarkSource), activity);
                mapView.getOverlayManager().add(0, tilesOverlay);
                break;
            case TYPE_TDT_DX:
                //天地图地形 ok
                mapView.setTileSource(CustomTileSource.tianDiTuDxSource);
                tilesOverlay = new TilesOverlay(new MapTileProviderBasic(activity, CustomTileSource.tianDiTuDxMarkSource), activity);
                mapView.getOverlayManager().add(0, tilesOverlay);
                break;
            case TYPE_GD_SL://ok
                mapView.setTileSource(CustomTileSource.GaoDeTile);
                break;
            case TYPE_GD_YX://ok
                mapView.setTileSource(CustomTileSource.GaoDeImg);
                tilesOverlay = new TilesOverlay(new MapTileProviderBasic(activity, CustomTileSource.GeoDeImgMark), activity);
                mapView.getOverlayManager().add(0, tilesOverlay);
                break;
            case TYPE_TX_SL://可以
                mapView.setTileSource(CustomTileSource.tengxunTile);
                break;
            case TYPE_TX_YX://可以
                mapView.setTileSource(CustomTileSource.tengxunImg);
                tilesOverlay = new TilesOverlay(new MapTileProviderBasic(activity, CustomTileSource.tengxunImgMark), activity);
                mapView.getOverlayManager().add(0, tilesOverlay);
                break;
            case TYPE_TX_DX://可以
                mapView.setTileSource(CustomTileSource.tengxunDx);
                tilesOverlay = new TilesOverlay(new MapTileProviderBasic(activity, CustomTileSource.tengxunDxMark), activity);
                mapView.getOverlayManager().add(0, tilesOverlay);
                break;
            case TYPE_BD_SL://不行 试了很多，都不行
                mapView.setTileSource(CustomTileSource.baiduTile);
                break;
            case TYPE_BD_YX://不行 试了很多，都不行
                mapView.setTileSource(CustomTileSource.baiduImg);
//                tilesOverlay = new TilesOverlay(new MapTileProviderBasic(activity, CustomTileSource.baiduRoad), activity);
//                mapView.getOverlayManager().add(0, tilesOverlay);
                break;
            case TYPE_ArcGis_SL://可以
                mapView.setTileSource(CustomTileSource.arcgisTile);
                break;
            case TYPE_ArcGis_YX://可以
                mapView.setTileSource(CustomTileSource.arcgisImg);
                tilesOverlay = new TilesOverlay(new MapTileProviderBasic(activity, CustomTileSource.tianDiTuDxMarkSource), activity);
                mapView.getOverlayManager().add(0, tilesOverlay);
                break;
            case TYPE_OSM_SL://ok 慢
                //矢量图
//                mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);//能出来
//                mapView.setTileSource(TileSourceFactory.OpenTopo);//能出来
//                mapView.setTileSource(TileSourceFactory.USGS_TOPO);//能出来,单缩放放大 图片不刷新
//                mapView.setTileSource(TileSourceFactory.HIKEBIKEMAP);//徒步自行车
//                mapView.setTileSource(TileSourceFactory.FIETS_OVERLAY_NL);//不行
//                mapView.setTileSource(TileSourceFactory.PUBLIC_TRANSPORT);
//                mapView.setTileSource(TileSourceFactory.USGS_SAT);//能出来
//                mapView.setTileSource(TileSourceFactory.BASE_OVERLAY_NL);//能出来，单是不行
//                mapView.setTileSource(TileSourceFactory.ROADS_OVERLAY_NL);//能出来，单是不行
                mapView.setTileSource(CustomTileSource.osmTile);
                break;
            case TYPE_OSM_BICYCLE:
                mapView.setTileSource(CustomTileSource.osmBicycle);
                break;
            case TYPE_OSM_TRANSPORT:
                mapView.setTileSource(CustomTileSource.osmTransport);
                break;
            case TYPE_LX_DT:
                File externalStorageState = Environment.getExternalStorageDirectory();
                path = new File(externalStorageState, "_alllayers.zip").getPath();
//                path = new File(externalStorageState, "_alllayers2.zip").getPath();
                loadOfflineMap(activity, mapView, tilesOverlay, path);
                break;
            default:
                break;
        }
        return tilesOverlay;
    }

    /**
     * 加载离线数据
     *
     * @param activity
     * @param mapView
     */
    public static void loadOfflineMap(Activity activity, MapView mapView, TilesOverlay tilesOverlay, String path) {
        mapView.setUseDataConnection(false);
        File exitFile = new File(path);
        if (!exitFile.exists()) {
            //加载天地图矢量
            mapView.setTileSource(CustomTileSource.tianDiTuTileSource);
            tilesOverlay = new TilesOverlay(new MapTileProviderBasic(activity, CustomTileSource.tianDiTuTileMarkSource), activity);
            mapView.getOverlayManager().add(0, tilesOverlay);
            return;
        }
        String fileName = exitFile.getName();
        fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (fileName.length() == 0) {
            return;
        }
        Configuration.getInstance().setDebugMode(false);
        Log.e("TAG", "loadMbtilesFile: " + exitFile.getPath());
//hefeiosm/6/54/23.png
        if (ArchiveFileFactory.isFileExtensionRegistered(fileName)) {
            try {
                OfflineTileProvider tileProvider = new OfflineTileProvider(new SimpleRegisterReceiver(activity), new File[]{exitFile});
                mapView.setTileProvider(tileProvider);
                String source = "";
                IArchiveFile[] archives = tileProvider.getArchives();
                if (archives.length > 0) {
                    IArchiveFile archive = archives[0];
                    Set<String> tileSources = archive.getTileSources();
                    if (!tileSources.isEmpty()) {
                        source = tileSources.iterator().next();
                        FileBasedTileSource offlineTile = (FileBasedTileSource) FileBasedTileSource.getSource(source);
                        mapView.setTileSource(offlineTile);
                    }
                } else {
                    Log.e("TAG", "加载的文件是 " + exitFile.getAbsolutePath() + " " + source);
                }
                mapView.invalidate();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Log.e("TAG", "没有任何可以打开的文件！请先下载离线地图文件");
        }
    }

    //定位到北京
    public static final double DEFAULT_LAT = 39.914658;
    public static final double DEFAULT_LON = 116.403909;

    /**
     * 转成Gcj02
     * 拿到的是WGS84
     *
     * @param type
     * @param lat
     * @param lgt
     * @return
     */
    public static GeoPoint convertToGcj02(int type, double lat, double lgt) {
        if (lgt == 0 || lat == 0) {
            return new GeoPoint(DEFAULT_LAT, DEFAULT_LON);
        }
        Log.e("TAG", "convertToGcj02: " + type);
        GeoPoint resGeoPoint = new GeoPoint(lat, lgt);
        switch (type) {
            case TYPE_GG_SL:
            case TYPE_GG_YX:
            case TYPE_GD_SL:
            case TYPE_GD_YX:
            case TYPE_TX_YX:
            case TYPE_TX_SL:
            case TYPE_TX_DX:
            case TYPE_ArcGis_SL:
                double[] doubles = RxMapUtil.gps84ToGcj02(lat, lgt);
                resGeoPoint = new GeoPoint(doubles[0], doubles[1]);
                break;
            default:
                break;
        }
        return resGeoPoint;
    }
}
