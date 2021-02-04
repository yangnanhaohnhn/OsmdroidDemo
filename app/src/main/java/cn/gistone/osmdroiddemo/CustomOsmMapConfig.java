package cn.gistone.osmdroiddemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;


/**
 * 设置OSM的部分属性
 *
 * @author haibobu
 * @date 2019/4/22
 */


public class CustomOsmMapConfig {

    //地图旋转
    private RotationGestureOverlay mRotationGestureOverlay;
    //比例尺
    private ScaleBarOverlay mScaleBarOverlay;
    //指南针方向
    private CompassOverlay mCompassOverlay = null;
    private DirectedLocationOverlay myLocationOverlay;
    //    //是否地图旋转
//    protected boolean mbRotation = false;

    public CustomOsmMapConfig() {

    }

    public void initMapOverlays(MapView mapView, Activity context) {
        List<Overlay> overlays = mapView.getOverlays();

        mapView.setDrawingCacheEnabled(false);
        mapView.setMaxZoomLevel(20.0);
        mapView.setMinZoomLevel(3.0);
        mapView.getController().setZoom(14.0);
        mapView.setUseDataConnection(true);
        mapView.setMultiTouchControls(true);// 触控放大缩小
        mapView.getOverlayManager().getTilesOverlay().setEnabled(true);
        //禁止自动出现放大，缩小的按钮 osmdroid 6.0以后才有的
        mapView.setBuiltInZoomControls(false);

//        initRotation(mapView, overlays);

        //比例尺配置
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mScaleBarOverlay = new ScaleBarOverlay(mapView);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setAlignBottom(true); //底部显示
        mScaleBarOverlay.setLineWidth(1);
        mScaleBarOverlay.setMaxLength(1.0F);
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 25, dip2px(context, 10));
        overlays.add(this.mScaleBarOverlay);

        //指南针方向
        mCompassOverlay = new CompassOverlay(context, new InternalCompassOrientationProvider(context), mapView);
        mCompassOverlay.enableCompass();
        mapView.getOverlays().add(this.mCompassOverlay);

        //设置中心点
        //定位当前位置，佛坪顾家沟
//        GeoPoint center = new GeoPoint(39.914658, 116.403909);
//        mapView.getController().setCenter(center);

        Bitmap bMap = BitmapFactory.decodeResource(mapView.getContext().getResources(), R.mipmap.img_home_my_location2);
        myLocationOverlay = new DirectedLocationOverlay(mapView.getContext());
        myLocationOverlay.setDirectionArrow(bMap);
        myLocationOverlay.setEnabled(true);
        overlays.add(1, myLocationOverlay);
    }

//    /**
//     * 初始化rotation
//     * @param mapView
//     * @param overlays
//     */
//    public void initRotation(MapView mapView, List<Overlay> overlays) {
//        boolean mbRotation = SPUtils.getBoolean(Constant.KEY_IS_ROTATION,false);
//        if (mRotationGestureOverlay == null){
//            mRotationGestureOverlay = new RotationGestureOverlay(mapView);
//        }
//        //地图自由旋转
//        mRotationGestureOverlay.setEnabled(mbRotation);
//        if (mbRotation) {
//            overlays.add(this.mRotationGestureOverlay);
//        }else{
//            mapView.setMapOrientation(0);
//            overlays.remove(mRotationGestureOverlay);
//            //把地图规整
//        }
//    }

    public DirectedLocationOverlay getLocationOverlay() {
        return myLocationOverlay;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void onPause() {
        mCompassOverlay.disableCompass();
//        myLocationOverlay.disableFollowLocation();
//        mLocationOverlay.disableMyLocation();
        mScaleBarOverlay.enableScaleBar();
    }

    public void onResume() {
        mCompassOverlay.enableCompass();
//        mLocationOverlay.enableFollowLocation();
//        mLocationOverlay.enableMyLocation();
        mScaleBarOverlay.disableScaleBar();
    }


}
