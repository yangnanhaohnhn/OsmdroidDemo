package cn.gistone.osmdroiddemo;

import android.util.Log;

import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.MapTileIndex;

/**
 * 谷歌地图的切换
 *
 * @author GDK
 * @date 2019/07/24
 */

public class CustomTileSource extends TileSourceFactory {
    private static final String TAG = CustomTileSource.class.getSimpleName();
    private static final String KEY = "7ae7631593e99a8fdd178671a12eb182";
    //    private static final String KEY = "0eae75863cf43e38391b849b9b76a4d6";
    //谷歌瓦片行编号=[谷歌参照瓦片行编号+(百度行编号 – 百度参照瓦片行编号)] //向右，行为递增
//谷歌瓦片列编号=[谷歌参照瓦片列编号- (百度列编号 – 百度参照瓦片列编号)] //向上，列为递减
//    谷歌分辨率计算公式:
//    Double tileSize=256 //瓦片尺寸(256*256)
//    Double initialResolution = 2 * math.pi * 6378137 / tileSize //6378137为球体半径
//    Double res = initialResolution /math.pow(2,zoom) //zoom为层数(0-21)
    //矢量
    //https://gg.8ditu.com/maps/vt?lyrs=m@207000000&hl=zh-CN&gl=CN&src=app&x=51&y=25&z=6&s=Galile&scale=2
    //谷歌矢量
    //https://gg.8ditu.com/87d9a5cb78d372df49ddebb23f34fa4a/601B9BA7/maps/vt/lyrs=m@207000000&hl=zh-CN&gl=CN&src=app&x=103&y=49&z=7&s=Galile&scale=2 //我登录的
    //https://gg.8ditu.com/6e5801bc14807d1f9b8b4040329babed/601B9BEF/maps/vt/lyrs=m@207000000&hl=zh-CN&gl=CN&src=app&x=24&y=11&z=5&s=Galile&scale=2 //未登录的
    //https://gg.8ditu.com/0b0170b830205c770b1911b63ff04af5/601BBF1C/maps/vt/lyrs=m@207000000&hl=zh-CN&gl=CN&src=app&x=417&y=211&z=9&s=Galile&scale=2
    public static final OnlineTileSourceBase googleTile = new XYTileSource("ggTile",
            4, 19, 256, ".png", new String[]{
            "https://gg.8ditu.com/6e5801bc14807d1f9b8b4040329babed/601B9BEF/maps/vt?lyrs=m@207000000&hl=zh-CN&gl=CN&src=app&s=Galile&scale=2",
    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            String url = getBaseUrl() + "&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
            Log.e(TAG, "getTileURLString: ggTile：" + url);
            return url;
        }
    };
    //谷歌影像
    //https://gg.8ditu.com/maps/vt?lyrs=s%40781&hl=zh-CN&gl=CN&x=108&y=46&z=7&src=app&scale=1
    //https://gg.8ditu.com/a488c1a631bd042e54fd84ccd2e6cb85/601B9A19/maps/vt/lyrs=s%40781&hl=zh-CN&gl=CN&x=0&y=1&z=2&src=app&scale=1
    //https://gg.8ditu.com/1ae5461ce2158d70cf22760f0ce936a5/601BBE98/maps/vt/lyrs=h%40781&hl=zh-CN&gl=CN&x=3336&y=1712&z=12&scale=1
    public static final OnlineTileSourceBase googleImg = new XYTileSource("ggImg",
            4, 19, 256, ".png", new String[]{
            "https://gg.8ditu.com/1ae5461ce2158d70cf22760f0ce936a5/601BBE98/maps/vt?lyrs=s%40781&hl=zh-CN&gl=CN&src=app&scale=1",
    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            String url = getBaseUrl() + "&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
            Log.e(TAG, "getTileURLString: ggImg：" + url);
            return url;
        }
    };
    //谷歌影像标注
    //https://gg.8ditu.com/maps/vt?lyrs=h%40781&hl=zh-CN&gl=CN&x=105&y=46&z=7&scale=1\
    //https://gg.8ditu.com/379159b5262d40fddfc136c3e99d2bc9/601B9A19/maps/vt/lyrs=h%40781&hl=zh-CN&gl=CN&x=0&y=2&z=2&scale=1
    //https://gg.8ditu.com/3ea250deb31c0006b5d97b1e8e353781/601BBE98/maps/vt/lyrs=h%40781&hl=zh-CN&gl=CN&x=3335&y=1711&z=12&scale=1
    public static final OnlineTileSourceBase googleImgMark = new XYTileSource("ggImgMark",
            4, 19, 256, ".png", new String[]{
            "https://gg.8ditu.com/3ea250deb31c0006b5d97b1e8e353781/601BBE98/maps/vt?lyrs=h%40781&hl=zh-CN&gl=CN&scale=1",
    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            String url = getBaseUrl() + "&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
            Log.e(TAG, "getTileURLString: ggImgMark：" + url);
            return url;
        }
    };


    public static final OnlineTileSourceBase GoogleHybrid = new XYTileSource("Google-Hybrid",
            0, 19, 512, ".png", new String[]{
//            "http://203.208.43.82",
            "http://mt0.google.cn",
            "http://mt1.google.cn",
            "http://mt2.google.cn",
            "http://mt3.google.cn",
    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/vt/lyrs=y&scale=2&hl=zh-CN&gl=CN&src=app&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };

    //谷歌卫星
    public static final OnlineTileSourceBase GoogleSat = new XYTileSource("Google-Sat",
            0, 19, 512, ".png", new String[]{
            "http://mt0.google.cn",
            "http://mt1.google.cn",
            "http://mt2.google.cn",
            "http://mt3.google.cn",
    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/vt/lyrs=s&scale=2&hl=zh-CN&gl=CN&src=app&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };
    //谷歌地图
    public static final OnlineTileSourceBase GoogleRoads = new XYTileSource("Google-Roads",
            0, 18, 512, ".png", new String[]{
            "http://mt0.google.cn",
            "http://mt1.google.cn",
            "http://mt2.google.cn",
            "http://mt3.google.cn",
    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/vt/lyrs=m&scale=2&hl=zh-CN&gl=CN&src=app&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };
    //谷歌地形
    public static final OnlineTileSourceBase GoogleTerrain = new XYTileSource("Google-Terrain",
            0, 16, 512, ".png", new String[]{
            "http://mt0.google.cn",
            "http://mt1.google.cn",
            "http://mt2.google.cn",
            "http://mt3.google.cn",
    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/vt/lyrs=t&scale=2&hl=zh-CN&gl=CN&src=app&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };

    //谷歌地形带标注
    public static final OnlineTileSourceBase GoogleTerrainHybrid = new XYTileSource("Google-Terrain-Hybrid",
            0, 16, 512, ".png", new String[]{
            "http://mt0.google.cn",
            "http://mt1.google.cn",
            "http://mt2.google.cn",
            "http://mt3.google.cn",
    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/vt/lyrs=p&scale=2&hl=zh-CN&gl=CN&src=app&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };

    //高德路网：
    //https://wprd0{1-4}.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&lang=zh_cn&scl=1&style=8
//    https://wprd0{1-4}.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&lang=zh_cn&size=1&scl=1&style=8&ltype=11
    //高德影像：
    //https://webst0{1-4}.is.autonavi.com/appmaptile?style=6&x={x}&y={y}&z={z}
    //高德矢量：
    //https://wprd0{1-4}.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&style=7

    //高德矢量地图带标记
    public static final OnlineTileSourceBase GaoDeTile = new XYTileSource("gdTile",
            1, 19, 256, ".png", new String[]{
            "https://wprd01.is.autonavi.com/appmaptile?style=7",
            "https://wprd02.is.autonavi.com/appmaptile?style=7",
            "https://wprd03.is.autonavi.com/appmaptile?style=7",
            "https://wprd04.is.autonavi.com/appmaptile?style=7",
    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            String url = getBaseUrl() + "&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z="
                    + MapTileIndex.getZoom(pMapTileIndex);
            Log.e(TAG, "getTileURLString: gdTile:" + url);
            return url;
        }
    };
    //高德影像地图
    public static final OnlineTileSourceBase GaoDeImg = new XYTileSource("gdImg",
            1, 19, 256, ".png", new String[]{
            "https://webst01.is.autonavi.com/appmaptile?style=6",
            "https://webst02.is.autonavi.com/appmaptile?style=6",
            "https://webst03.is.autonavi.com/appmaptile?style=6",
            "https://webst04.is.autonavi.com/appmaptile?style=6",
    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            String url = getBaseUrl() + "&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z="
                    + MapTileIndex.getZoom(pMapTileIndex);
            Log.e(TAG, "getTileURLString: gdImg: " + url);
            return url;
        }
    };
    //高德影像地图 路网标记
    public static final OnlineTileSourceBase GeoDeImgMark = new XYTileSource("gdImgMark",
            1, 19, 256, ".png", new String[]{
            "https://wprd01.is.autonavi.com/appmaptile?lang=zh_cn&scl=1&style=8",
            "https://wprd02.is.autonavi.com/appmaptile?lang=zh_cn&scl=1&style=8",
            "https://wprd03.is.autonavi.com/appmaptile?lang=zh_cn&scl=1&style=8",
            "https://wprd04.is.autonavi.com/appmaptile?lang=zh_cn&scl=1&style=8",

    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            String url = getBaseUrl() + "&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z="
                    + MapTileIndex.getZoom(pMapTileIndex);
            Log.e(TAG, "getTileURLString: gdImgMark:" + url);
            return url;
        }
    };


    //https://t6.tianditu.gov.cn/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=3368&TILEROW=1551&TILEMATRIX=12&tk=9a02b3cdd29cd346de4df04229797710
//https://t0.tianditu.gov.cn/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=3368&TILEROW=1552&TILEMATRIX=12&tk=9a02b3cdd29cd346de4df04229797710
    //天地图影像地图 _W是墨卡托投影  _c是国家2000的坐标系
    public static final OnlineTileSourceBase tianDiTuImgSource = new XYTileSource("tdtImg", 1, 18, 512, ".png",
            new String[]{
                    "http://t0.tianditu.gov.cn/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t1.tianditu.gov.cn/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t2.tianditu.gov.cn/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t3.tianditu.gov.cn/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t4.tianditu.gov.cn/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t5.tianditu.gov.cn/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t6.tianditu.gov.cn/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t7.tianditu.gov.cn/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY
            }) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            String url = getBaseUrl() + "&TILECOL=" + MapTileIndex.getX(pMapTileIndex) + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex)
                    + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex);
            Log.e(TAG, "getTileURLString: tdtImg:" + url);
            return url;
        }
    };
    //https://t5.tianditu.gov.cn/cia_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cia&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=3367&TILEROW=1551&TILEMATRIX=12&tk=9a02b3cdd29cd346de4df04229797710
//https://t0.tianditu.gov.cn/cia_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cia&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=3368&TILEROW=1552&TILEMATRIX=12&tk=9a02b3cdd29cd346de4df04229797710
    //天地图影像标注 _W是墨卡托投影  _c是国家2000的坐标系
    public static final OnlineTileSourceBase tianDiTuImgMarkSource = new XYTileSource("tdtImgMark", 1, 18, 512, ".png",
            new String[]{
                    "http://t0.tianditu.gov.cn/cia_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cia&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t1.tianditu.gov.cn/cia_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cia&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t2.tianditu.gov.cn/cia_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cia&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t3.tianditu.gov.cn/cia_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cia&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t4.tianditu.gov.cn/cia_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cia&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t5.tianditu.gov.cn/cia_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cia&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t6.tianditu.gov.cn/cia_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cia&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t7.tianditu.gov.cn/cia_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cia&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY
            }) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            String url = getBaseUrl() + "&TILECOL=" + MapTileIndex.getX(pMapTileIndex) + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex)
                    + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex);
            Log.e(TAG, "getTileURLString: tdtImgMark:" + url);
            return url;
        }
    };

    //https://t3.tianditu.gov.cn/vec_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=420&TILEROW=193&TILEMATRIX=9&tk=9a02b3cdd29cd346de4df04229797710
    //天地图矢量地图
    public static final OnlineTileSourceBase tianDiTuTileSource = new XYTileSource("tdtTile", 1, 18, 512, ".png",
            new String[]{
                    "http://t0.tianditu.gov.cn/vec_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t1.tianditu.gov.cn/vec_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t2.tianditu.gov.cn/vec_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t3.tianditu.gov.cn/vec_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t4.tianditu.gov.cn/vec_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t5.tianditu.gov.cn/vec_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t6.tianditu.gov.cn/vec_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t7.tianditu.gov.cn/vec_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY
            }) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            String url = getBaseUrl() + "&TILECOL=" + MapTileIndex.getX(pMapTileIndex) + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex)
                    + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex);
            Log.e(TAG, "getTileURLString: tdtTile:" + url);
            return url;
        }
    };

    //天地图矢量标注 _W是墨卡托投影  _c是国家2000的坐标系
    public static final OnlineTileSourceBase tianDiTuTileMarkSource = new XYTileSource("tdtTileMark", 1, 18, 512, ".png",
            new String[]{
//                    http://t0.tianditu.gov.cn/cva_w/wmts?tk=您的密钥
                    "http://t0.tianditu.gov.cn/cva_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t1.tianditu.gov.cn/cva_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t2.tianditu.gov.cn/cva_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t3.tianditu.gov.cn/cva_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t4.tianditu.gov.cn/cva_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t5.tianditu.gov.cn/cva_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t6.tianditu.gov.cn/cva_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY,
                    "http://t7.tianditu.gov.cn/cva_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + KEY
            }) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            String url = getBaseUrl() + "&TILECOL=" + MapTileIndex.getX(pMapTileIndex) + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex)
                    + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex);
            Log.e(TAG, "getTileURLString: tdtTileMark:" + url);
            return url;
        }
    };

    //https://t2.tianditu.gov.cn/ter_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=ter&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=7&TILEROW=2&TILEMATRIX=3&tk=9a02b3cdd29cd346de4df04229797710
    //https://t2.tianditu.gov.cn/DataServer?T=ter_w&x=210&y=96&l=8&tk=ef6151d9f0386f3b2a2fdf1d58fe9b32
    //https://t5.tianditu.gov.cn/DataServer?T=ter_w&x=46&y=26&l=6&tk=ef6151d9f0386f3b2a2fdf1d58fe9b32
    //天地图地形地图 经测试之后，某些区域是没有地形图数据
    public static final OnlineTileSourceBase tianDiTuDxSource = new XYTileSource("tdtDx", 3, 14, 256, ".png",
            new String[]{
                    "https://t0.tianditu.gov.cn/DataServer?T=ter_w&tk=" + KEY,
                    "https://t1.tianditu.gov.cn/DataServer?T=ter_w&tk=" + KEY,
                    "https://t2.tianditu.gov.cn/DataServer?T=ter_w&tk=" + KEY,
                    "https://t3.tianditu.gov.cn/DataServer?T=ter_w&tk=" + KEY,
                    "https://t4.tianditu.gov.cn/DataServer?T=ter_w&tk=" + KEY,
                    "https://t5.tianditu.gov.cn/DataServer?T=ter_w&tk=" + KEY,
                    "https://t6.tianditu.gov.cn/DataServer?T=ter_w&tk=" + KEY,
            }) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            int x = MapTileIndex.getX(pMapTileIndex);
            int y = MapTileIndex.getY(pMapTileIndex);
            int z = MapTileIndex.getZoom(pMapTileIndex);
            String url = getBaseUrl() + "&x=" + x + "&y=" + y + "&l=" + z;
            Log.e(TAG, "getTileURLString: tdtDx:" + url);
            return url;
        }
    };

    //天地图地形标注
    //https://t2.tianditu.gov.cn/cta_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cta&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=0&TILEROW=0&TILEMATRIX=2&tk=9a02b3cdd29cd346de4df04229797710
    //https://t1.tianditu.gov.cn/DataServer?T=cta_w&x=49&y=24&l=6&tk=ef6151d9f0386f3b2a2fdf1d58fe9b32
    public static final OnlineTileSourceBase tianDiTuDxMarkSource = new XYTileSource("tdtDxMark", 3, 14, 256, ".png",
            new String[]{
                    "https://t0.tianditu.gov.cn/DataServer?T=cta_w&tk=" + KEY,
                    "https://t1.tianditu.gov.cn/DataServer?T=cta_w&tk=" + KEY,
                    "https://t2.tianditu.gov.cn/DataServer?T=cta_w&tk=" + KEY,
                    "https://t3.tianditu.gov.cn/DataServer?T=cta_w&tk=" + KEY,
                    "https://t4.tianditu.gov.cn/DataServer?T=cta_w&tk=" + KEY,
                    "https://t5.tianditu.gov.cn/DataServer?T=cta_w&tk=" + KEY,
                    "https://t6.tianditu.gov.cn/DataServer?T=cta_w&tk=" + KEY,
            }) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            int x = MapTileIndex.getX(pMapTileIndex);
            int y = MapTileIndex.getY(pMapTileIndex);
            int z = MapTileIndex.getZoom(pMapTileIndex);
            String url = getBaseUrl() + "&x=" + x + "&y=" + y + "&l=" + z;
//            String url = getBaseUrl() + "&TILECOL=" + MapTileIndex.getX(pMapTileIndex) + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex)
//                    + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex);
            Log.e(TAG, "getTileURLString: tdtDxMark:" + url);
            return url;
        }
    };

    //腾讯地图矢量图
    //http://rt{0-3}.map.gtimg.com/realtimerender?z={z}&x={x}&y={y}&type=vector&style=0
//    腾讯地图地形图
//    https://p{0-3}.map.gtimg.com/demTiles/4/0/0/10_11.jpg
    //地形的标注点
//    https://rt0.map.gtimg.com/tile?type=vector&styleid=3&version=644&z={z}&x={x}&y={y}
//    腾讯地图影像图
//    https://p{0-3}.map.gtimg.com/sateTiles/4/0/0/10_11.jpg?version=236
    //影像标注
    //https://rt0.map.gtimg.com/tile?z=4&x=11&y=9&styleid=2&version=644

//URL = z  /  Math.Floor(x / 16.0)  / Math.Floor(y / 16.0) / x_y.png，其中x,y,z为TMS瓦片坐标参数。
// y = (int)Math.pow(2, z) - 1 - y;
//    与Google瓦片坐标的关系，上式中腾讯地图瓦片坐标的x,z与Google 瓦片坐标系的X 、Z是相等的，Y 的转换关系为：Y(Tencent) +  Y(Google) = Math.pow(2，zoom) - 1

    //腾讯地图矢量
    public static final OnlineTileSourceBase tengxunTile = new XYTileSource("txTile", 4, 18, 512, ".png",
            new String[]{
                    "http://rt0.map.gtimg.com/realtimerender?type=vector&style=0",
                    "http://rt1.map.gtimg.com/realtimerender?type=vector&style=0",
                    "http://rt2.map.gtimg.com/realtimerender?type=vector&style=0",
                    "http://rt3.map.gtimg.com/realtimerender?type=vector&style=0",
            }) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            int x = MapTileIndex.getX(pMapTileIndex);
            int y = MapTileIndex.getY(pMapTileIndex);
            int z = MapTileIndex.getZoom(pMapTileIndex);
            y = (int) Math.pow(2, z) - 1 - y;
            String url = getBaseUrl() + "&x=" + x + "&y=" + y + "&z=" + z;
            Log.e(TAG, "getTileURLString: tengxunTile:" + url);
            return url;
        }
    };
    //腾讯地图地形
    //https://rt0.map.gtimg.com/tile?z=4&x=11&y=9&type=vector&styleid=3&version=644 地形标注点
    public static final OnlineTileSourceBase tengxunDx = new XYTileSource("txDx", 4, 18, 512, ".png",
            new String[]{
                    " https://p0.map.gtimg.com/demTiles/",
                    " https://p1.map.gtimg.com/demTiles/",
                    " https://p2.map.gtimg.com/demTiles/",
                    " https://p3.map.gtimg.com/demTiles/",
            }) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            int x = MapTileIndex.getX(pMapTileIndex);
            int y = MapTileIndex.getY(pMapTileIndex);
            int z = MapTileIndex.getZoom(pMapTileIndex);
            y = (int) Math.pow(2, z) - 1 - y;
            String url = getBaseUrl() + z + "/" + (int) Math.floor(x / 16.0) + "/" + (int) Math.floor(y / 16.0) + "/" + x + "_" + y + ".jpg?version=236";
//            String url = getBaseUrl() + "&x=" + x + "&y=" + MapTileIndex.getY(pMapTileIndex)
//                    + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
            Log.e(TAG, "getTileURLString: tengxunImg:" + url);
            return url;
        }
    };
    //腾讯地图地形标注
    //https://rt0.map.gtimg.com/tile?z=4&x=11&y=9&type=vector&styleid=3&version=644 地形标注点
    public static final OnlineTileSourceBase tengxunDxMark = new XYTileSource("txDxMark", 4, 18, 512, ".png",
            new String[]{
                    "https://rt0.map.gtimg.com/tile?type=vector&styleid=3&version=644",
                    "https://rt1.map.gtimg.com/tile?type=vector&styleid=3&version=644",
                    "https://rt2.map.gtimg.com/tile?type=vector&styleid=3&version=644",
                    "https://rt3.map.gtimg.com/tile?type=vector&styleid=3&version=644",
            }) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            int x = MapTileIndex.getX(pMapTileIndex);
            int y = MapTileIndex.getY(pMapTileIndex);
            int z = MapTileIndex.getZoom(pMapTileIndex);
            y = (int) Math.pow(2, z) - 1 - y;
            String url = getBaseUrl() + "&x=" + x + "&y=" + y + "&z=" + z;
            Log.e(TAG, "getTileURLString: tengxunDxMark:" + url);
            return url;
        }
    };
    //腾讯地图影像
    public static final OnlineTileSourceBase tengxunImg = new XYTileSource("txImg", 4, 18, 512, ".png",
            new String[]{
                    "https://p0.map.gtimg.com/sateTiles/",
                    "https://p1.map.gtimg.com/sateTiles/",
                    "https://p2.map.gtimg.com/sateTiles/",
                    "https://p3.map.gtimg.com/sateTiles/",
            }) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            int x = MapTileIndex.getX(pMapTileIndex);
            int y = MapTileIndex.getY(pMapTileIndex);
            int z = MapTileIndex.getZoom(pMapTileIndex);
            y = (int) Math.pow(2, z) - 1 - y;
            String url = getBaseUrl() + z + "/" + (int) Math.floor(x / 16.0) + "/" + (int) Math.floor(y / 16.0) + "/" + x + "_" + y + ".jpg?version=236";
//            String url = getBaseUrl() + "&x=" + x + "&y=" + MapTileIndex.getY(pMapTileIndex)
//                    + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
            Log.e(TAG, "getTileURLString: tengxunImg:" + url);
            return url;
        }
    };

    //腾讯地图影像标注点
    //https://rt0.map.gtimg.com/tile?z=7&x=104&y=76&styleid=2&version=644
    public static final OnlineTileSourceBase tengxunImgMark = new XYTileSource("txImgMark", 4, 18, 512, ".png",
            new String[]{
                    //https://rt0.map.gtimg.com/tile?z=4&x=11&y=9&styleid=2&version=644
                    "https://rt0.map.gtimg.com/tile?styleid=2&version=644",
                    "https://rt1.map.gtimg.com/tile?styleid=2&version=644",
                    "https://rt2.map.gtimg.com/tile?styleid=2&version=644",
                    "https://rt3.map.gtimg.com/tile?styleid=2&version=644",
            }) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            int x = MapTileIndex.getX(pMapTileIndex);
            int y = MapTileIndex.getY(pMapTileIndex);
            int z = MapTileIndex.getZoom(pMapTileIndex);
            y = (int) Math.pow(2, z) - 1 - y;
            String url = getBaseUrl() + "&x=" + x + "&y=" + y + "&z=" + z;
            Log.e(TAG, "getTileURLString: tengxunImgMark:" + url);
            return url;
        }
    };
    private static final String BD_KEY = "NepHuEeZ8AMyGGsZ5M4pftmG4BB8bNp3";
    //矢量图
//    http://online{$s}.map.bdimg.com/tile/?qt=vtil&x={x}&y={y}&z={z}&styles=pl&scale=1&udt=20210202
    //http://online{$s}.map.bdimg.com/onlinelabel/?qt=tile&x={$x}&y={$y}&z={$z} 百度图片瓦片的层级是[3~18]
    //百度地图路网图
//    https://maponline{0-3}.bdimg.com/tile/?qt=vtile&x=195&y=74&z=10&styles=sl&showtext=0&v=083&udt=20210122

    //百度地图影像图
//    https://maponline{$s}.bdimg.com/starpic/?qt=satepc&u=x={$x};y={$y};z={$z};v=009;type=sate&fm=46&app=webearth2&v=009&udt=20210122
    //https://maponline1.bdimg.com/starpic/?qt=satepc&u=x=1;y=8;z=6;v=009;type=sate&fm=46&app=webearth2&v=009&udt=20210128
    //百度的分辨率
//    Double res = math.pow(2,(18 - n)) // n为层数(1-18)


    public static final OnlineTileSourceBase baiduTile = new XYTileSource("bdTile", 3, 18, 256, ".png",
            new String[]{
                    "http://online0.map.bdimg.com/tile/?qt=vtil&styles=pl&scale=1&udt=" + DateUtil.getDateByLong(System.currentTimeMillis(), DateUtil.TYPE_1),
                    "http://online1.map.bdimg.com/tile/?qt=vtil&styles=pl&scale=1&udt=" + DateUtil.getDateByLong(System.currentTimeMillis(), DateUtil.TYPE_1),
                    "http://online2.map.bdimg.com/tile/?qt=vtil&styles=pl&scale=1&udt=" + DateUtil.getDateByLong(System.currentTimeMillis(), DateUtil.TYPE_1),
                    "http://online3.map.bdimg.com/tile/?qt=vtil&styles=pl&scale=1&udt=" + DateUtil.getDateByLong(System.currentTimeMillis(), DateUtil.TYPE_1),
                    "http://online4.map.bdimg.com/tile/?qt=vtil&styles=pl&scale=1&udt=" + DateUtil.getDateByLong(System.currentTimeMillis(), DateUtil.TYPE_1),
            }) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            //4035232506901960760
            int x = MapTileIndex.getX(pMapTileIndex);
            int y = MapTileIndex.getY(pMapTileIndex);
            int z = MapTileIndex.getZoom(pMapTileIndex);
            y = (int) Math.pow(2, z) - 1 - y;
            String url = getBaseUrl() + "&x=" + x + "&y=" + y + "&z=" + z;
            Log.e(TAG, "getTileURLString: baiduTile:" + url);
            return url;
        }
    };
    public static final OnlineTileSourceBase baiduRoad = new XYTileSource("bdRoad", 3, 18, 256, ".png",
            new String[]{
                    "https://maponline0.bdimg.com/tile/?qt=vtile&styles=sl&showtext=0&v=083&udt=" + DateUtil.getDateByLong(System.currentTimeMillis(), DateUtil.TYPE_1),
                    "https://maponline1.bdimg.com/tile/?qt=vtile&styles=sl&showtext=0&v=083&udt=" + DateUtil.getDateByLong(System.currentTimeMillis(), DateUtil.TYPE_1),
                    "https://maponline2.bdimg.com/tile/?qt=vtile&styles=sl&showtext=0&v=083&udt=" + DateUtil.getDateByLong(System.currentTimeMillis(), DateUtil.TYPE_1),
                    "https://maponline3.bdimg.com/tile/?qt=vtile&styles=sl&showtext=0&v=083&udt=" + DateUtil.getDateByLong(System.currentTimeMillis(), DateUtil.TYPE_1),
            }) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            int x = MapTileIndex.getX(pMapTileIndex);
            int y = MapTileIndex.getY(pMapTileIndex);
            int z = MapTileIndex.getZoom(pMapTileIndex);
            String xStr, yStr;
            if (x < 0) {
                xStr = "M" + (-x);
            } else {
                xStr = x + "";
            }
            if (y < 0) {
                yStr = "M" + (-y);
            } else {
                yStr = y + "";
            }
            String url = getBaseUrl() + "&x=" + xStr + "&y=" + yStr + "&z=" + z;
            Log.e(TAG, "getTileURLString: baiduRoad:" + url);
            return url;
        }
    };
    //https://ss2.bdstatic.com/8bo_dTSlR1gBo1vgoIiO_jowehsv/starpic/?qt=satepc&s=1&u=x=11;y=3;z=6;v=009;type=sate&fm=46&app=webearth2&v=009
    public static final OnlineTileSourceBase baiduImg = new XYTileSource("bdImg", 3, 18, 256, ".png",
            new String[]{
//                    "https://maponline0.bdimg.com/starpic/?qt=satepc&fm=46&app=webearth2&v=009&udt=" + DateUtil.getDateByLong(System.currentTimeMillis(), DateUtil.TYPE_1),
//                    "https://maponline1.bdimg.com/starpic/?qt=satepc&fm=46&app=webearth2&v=009&udt=" + DateUtil.getDateByLong(System.currentTimeMillis(), DateUtil.TYPE_1),
//                    "https://maponline2.bdimg.com/starpic/?qt=satepc&fm=46&app=webearth2&v=009&udt=" + DateUtil.getDateByLong(System.currentTimeMillis(), DateUtil.TYPE_1),
//                    "https://maponline3.bdimg.com/starpic/?qt=satepc&fm=46&app=webearth2&v=009&udt=" + DateUtil.getDateByLong(System.currentTimeMillis(), DateUtil.TYPE_1),
                    "https://ss0.bdstatic.com/8bo_dTSlR1gBo1vgoIiO_jowehsv/starpic/?qt=satepc&s=1&fm=46&app=webearth2&v=009",
                    "https://ss1.bdstatic.com/8bo_dTSlR1gBo1vgoIiO_jowehsv/starpic/?qt=satepc&s=1&fm=46&app=webearth2&v=009",
                    "https://ss2.bdstatic.com/8bo_dTSlR1gBo1vgoIiO_jowehsv/starpic/?qt=satepc&s=1&fm=46&app=webearth2&v=009",
                    "https://ss3.bdstatic.com/8bo_dTSlR1gBo1vgoIiO_jowehsv/starpic/?qt=satepc&s=1&fm=46&app=webearth2&v=009",
            }) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            int x = MapTileIndex.getX(pMapTileIndex);
            int y = MapTileIndex.getY(pMapTileIndex);
            int z = MapTileIndex.getZoom(pMapTileIndex);
            String xStr, yStr;
            if (x < 0) {
                xStr = "M" + (-x);
            } else {
                xStr = x + "";
            }
            if (y < 0) {
                yStr = "M" + (-y);
            } else {
                yStr = y + "";
            }
            String url = getBaseUrl() + "&u=x=" + xStr + ";y=" + yStr + ";z=" + z + ";v=009;type=sate";
            Log.e(TAG, "getTileURLString: baiduImg:" + url);
            return url;
        }
    };


    /**
     * 记载的是自己发布的服务
     */
    public static final OnlineTileSourceBase customArcgisTile = new XYTileSource("customArcgisTile", 1, 18, 256, ".png",
            new String[]{
//                    "https://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer/tile"
//                    "http://192.168.1.133:6080/arcgis/rest/services/test/bj/MapServer/tile",
                    "http://192.168.1.133:6080/arcgis/rest/services/test/MyMapService/MapServer/tile",
                    //http://192.168.1.133:6080/arcgis/rest/services/test/MyMapService/MapServer/tile/8/88/210
            }) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            int x = MapTileIndex.getX(pMapTileIndex);
            int y = MapTileIndex.getY(pMapTileIndex);
            int z = MapTileIndex.getZoom(pMapTileIndex);
            //4.简答描述思路：arcgis瓦片命名规则是
            //L+层数（0补齐） / R + 瓦片行号的16进制（0补齐） /  C+瓦片列号的16进制（0补齐）.png(z/y/x)
            //而下载的google切片规则是
            //层数 / 瓦片列号的10进制 / 瓦片行号的10进制.png
            //那么，通过命名规则的转换，即可找到arcgis 瓦片对应的google瓦片，进行替换即可
            //http://192.168.1.133:6080/arcgis/rest/services/test/MyMapService/MapServer/tile/3/3/5
            String url = getBaseUrl() + "/" + z + "/" + y + "/" + x;
            Log.e(TAG, "getTileURLString: arcgisTile:" + url);
            return url;
        }
    };

    public static final OnlineTileSourceBase arcgisTile = new XYTileSource("arcgisTile", 1, 18, 256, ".png",
            new String[]{
                    "https://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer/tile",
            }) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            int x = MapTileIndex.getX(pMapTileIndex);
            int y = MapTileIndex.getY(pMapTileIndex);
            int z = MapTileIndex.getZoom(pMapTileIndex);
            //4.简答描述思路：arcgis瓦片命名规则是
            //L+层数（0补齐） / R + 瓦片行号的16进制（0补齐） /  C+瓦片列号的16进制（0补齐）.png(z/y/x)
            //而下载的google切片规则是
            //层数 / 瓦片列号的10进制 / 瓦片行号的10进制.png
            //那么，通过命名规则的转换，即可找到arcgis 瓦片对应的google瓦片，进行替换即可
            //http://192.168.1.133:6080/arcgis/rest/services/test/MyMapService/MapServer/tile/3/3/5
            String url = getBaseUrl() + "/" + z + "/" + y + "/" + x;
            Log.e(TAG, "getTileURLString: arcgisTile:" + url);
            return url;
        }
    };
    /**
     * 用的平台发布的影像数据，路网数据用的是天地图的数据
     */
    public static final OnlineTileSourceBase arcgisImg = new XYTileSource("arcgisImg", 1, 18, 256, ".png",
            new String[]{
                    "https://services.arcgisonline.com/arcgis/rest/services/World_Imagery/MapServer/tile/",
            }) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            int x = MapTileIndex.getX(pMapTileIndex);
            int y = MapTileIndex.getY(pMapTileIndex);
            int z = MapTileIndex.getZoom(pMapTileIndex);
            String url = getBaseUrl() + "/" + z + "/" + y + "/" + x;
            Log.e(TAG, "getTileURLString: arcgisTile:" + url);
            return url;
        }
    };
    /**
     * osm矢量
     */
    //https://tile.openstreetmap.org/13/6745/3102.png
    //https://tile.openstreetmap.org/6/55/23.png
    public static final OnlineTileSourceBase osmTile = new XYTileSource("osmTile", 0, 18, 256, ".png",
            new String[]{
                    "https://tile.openstreetmap.org",
                    "https://tile.openstreetmap.org",
                    "https://tile.openstreetmap.org",
            }) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            int x = MapTileIndex.getX(pMapTileIndex);
            int y = MapTileIndex.getY(pMapTileIndex);
            int z = MapTileIndex.getZoom(pMapTileIndex);
            String url = getBaseUrl() + "/" + z + "/" + x + "/" + y + ".png";
            Log.e(TAG, "getTileURLString: osmTile:" + url);
            return url;
        }
    };
    /**
     * osm骑行运动地图
     */
    //https://b.tile.thunderforest.com/cycle/13/6742/3102.png?apikey=6170aad10dfd42a38d4d8c709a536f38
    public static final OnlineTileSourceBase osmBicycle = new XYTileSource("osmBicycle", 0, 18, 256, ".png",
            new String[]{
                    "http://a.tile.thunderforest.com/cycle",
                    "http://b.tile.thunderforest.com/cycle",
                    "http://c.tile.thunderforest.com/cycle",
            }) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            int x = MapTileIndex.getX(pMapTileIndex);
            int y = MapTileIndex.getY(pMapTileIndex);
            int z = MapTileIndex.getZoom(pMapTileIndex);
            String url = getBaseUrl() + "/" + z + "/" + x + "/" + y + ".png?apikey=" + Osm_Key;
            Log.e(TAG, "getTileURLString: osmBicycle:" + url);
            return url;
        }
    };
    private static final String Osm_Key = "6170aad10dfd42a38d4d8c709a536f38";
    /**
     * osm运输
     */
    //https://b.tile.thunderforest.com/transport/16/53947/24822.png?apikey=6170aad10dfd42a38d4d8c709a536f38
    public static final OnlineTileSourceBase osmTransport = new XYTileSource("osmTransport", 0, 18, 256, ".png",
            new String[]{
                    "http://a.tile.thunderforest.com/transport",
                    "http://b.tile.thunderforest.com/transport",
                    "http://c.tile.thunderforest.com/transport",
            }) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            int x = MapTileIndex.getX(pMapTileIndex);
            int y = MapTileIndex.getY(pMapTileIndex);
            int z = MapTileIndex.getZoom(pMapTileIndex);
            String url = getBaseUrl() + "/" + z + "/" + x + "/" + y + ".png?apikey=" + Osm_Key;
            Log.e(TAG, "getTileURLString: osmTransport:" + url);
            return url;
        }
    };

    //OSM
//   z: [0-18]    x,y: [0-
    //瓦片地址格式：http://a.tile.openstreetmap.org/9/420/193.png
    //Cycle Map：http://c.tile.opencyclemap.org/cycle/9/420/193.png
    //Transport Map：http://b.tile2.opencyclemap.org/transport/9/420/193.png
    //MapQuest Map：http://otile3.mqcdn.com/tiles/1.0.0/osm/9/420/193.png

//    public static final OnlineTileSourceBase baiduImg = new XYTileSource("bdImg", 1, 18, 512, ".png",
//            new String[]{
//                    "https://maponline0.bdimg.com/starpic/?qt=satepc&fm=46&app=webearth2&v=009&udt=" + DateUtil.getDateByLong(System.currentTimeMillis(), DateUtil.TYPE_1),
//                    "https://maponline1.bdimg.com/starpic/?qt=satepc&fm=46&app=webearth2&v=009&udt=" + DateUtil.getDateByLong(System.currentTimeMillis(), DateUtil.TYPE_1),
//                    "https://maponline2.bdimg.com/starpic/?qt=satepc&fm=46&app=webearth2&v=009&udt=" + DateUtil.getDateByLong(System.currentTimeMillis(), DateUtil.TYPE_1),
//                    "https://maponline3.bdimg.com/starpic/?qt=satepc&fm=46&app=webearth2&v=009&udt=" + DateUtil.getDateByLong(System.currentTimeMillis(), DateUtil.TYPE_1),
//            }) {
//        @Override
//        public String getTileURLString(final long pMapTileIndex) {
//            String url = getBaseUrl() + "&u=x=" + MapTileIndex.getX(pMapTileIndex) + ";y=" + MapTileIndex.getY(pMapTileIndex)
//                    + ";z=" + MapTileIndex.getZoom(pMapTileIndex) + ";v=009;type=sate";
//            Log.e(TAG, "getTileURLString: baiduImg:" + url);
//            return url;
//        }
//    };

    public static void main(String[] args) {
        int maxZoom = 5;
        double[] resolutions = new double[]{};
        for (int i = 0; i <= maxZoom + 1; i++) {
            resolutions[i] = Math.pow(2, maxZoom - i);
        }

        System.out.println(resolutions.toString());
        int z = (int) resolutions[0];
        int x = (int) resolutions[1];
        int y = (int) resolutions[2];

        // 百度瓦片服务url将负数使用M前缀来标识
        if (x < 0) {
            x = 'M' + (-x);
        }
        if (y < 0) {
            y = 'M' + (-y);
        }
//        http://online0.map.bdimg.com/onlinelabel/?qt=tile&x=" + x + "&y=" + y + "&z=" + z + "&styles=pl&udt=20170115&scaler=1&p=1
    }


}

