package cn.gistone.osmdroiddemo;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by $yn on 2019/8/13.
 *
 * @author $yn
 * Desc:location的监听
 */
public class CommonLocationListener implements LocationListener {

    /**
     * //定位成功调用
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {

    }
    /**
     * 定位状态改变时调用
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /**
     *  //定位可用时调用
     * @param provider
     */
    @Override
    public void onProviderEnabled(String provider) {

    }

    /**
     * //定位关闭时调用
     * @param provider
     */
    @Override
    public void onProviderDisabled(String provider) {

    }
}
