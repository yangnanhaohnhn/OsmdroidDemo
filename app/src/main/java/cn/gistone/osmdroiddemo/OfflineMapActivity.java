package cn.gistone.osmdroiddemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.osmdroid.tileprovider.modules.MBTilesFileArchive;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Create 2021/1/30
 *
 * @author N
 * desc:
 */
public class OfflineMapActivity extends AppCompatActivity {

    @BindView(R.id.rv_data)
    RecyclerView rvData;
    private SQLiteDatabase sqLiteDatabase;
    private Unbinder bind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_map);
        bind = ButterKnife.bind(this);
        OfflineMapAdapter adapter = new OfflineMapAdapter(R.layout.item_offline_map);
        GridLayoutManager gm = new GridLayoutManager(this, 3);
        rvData.setLayoutManager(gm);
        rvData.setAdapter(adapter);


        File externalStorageState = Environment.getExternalStorageDirectory();
        String path = new File(externalStorageState, "bd.mbtiles").getPath();

        sqLiteDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READONLY);
        List<ResultBean> resultBean = getResultBean(sqLiteDatabase);
        adapter.setNewData(resultBean);
    }

    private List<ResultBean> getResultBean(SQLiteDatabase sqLiteDatabase) {
        List<ResultBean> resList = new ArrayList<>();
        try {
            final String[] tile = {MBTilesFileArchive.COL_TILES_TILE_COLUMN, MBTilesFileArchive.COL_TILES_TILE_ROW, MBTilesFileArchive.COL_TILES_ZOOM_LEVEL, MBTilesFileArchive.COL_TILES_TILE_DATA};
            final Cursor cur = sqLiteDatabase.query(MBTilesFileArchive.TABLE_TILES, tile, null, null, null, null, null);
            if (cur != null) {
                while (cur.moveToNext()) {
                    ResultBean resultBean = new ResultBean();
                    resultBean.x = cur.getInt(0);
                    resultBean.y = cur.getInt(1);
                    resultBean.z = cur.getInt(2);
                    resultBean.inputStream = new ByteArrayInputStream(cur.getBlob(3));
                    resList.add(resultBean);
                }
                cur.close();
            }
        } catch (final Throwable e) {

        }
        return resList;
    }
//    private InputStream getInputStream(SQLiteDatabase sqLiteDatabase, String x, String y, String z) {
//        try {
//            InputStream ret = null;
//            final String[] tile = {MBTilesFileArchive.COL_TILES_TILE_DATA};
//            final String[] xyz = {String.valueOf(x), String.valueOf(y), String.valueOf(z)};
//
//            final Cursor cur = sqLiteDatabase.query(MBTilesFileArchive.TABLE_TILES, tile, "tile_column=? and tile_row=? and zoom_level=?", xyz, null, null, null);
//
//            if (cur.getCount() != 0) {
//                cur.moveToFirst();
//                ret = new ByteArrayInputStream(cur.getBlob(0));
//            }
//            cur.close();
//            if (ret != null) {
//                return ret;
//            }
//        } catch (final Throwable e) {
//        }
//
//        return null;
//    }

    class ResultBean implements Serializable {
        int x;
        int y;
        int z;
        InputStream inputStream;
    }

    class OfflineMapAdapter extends BaseQuickAdapter<ResultBean, BaseViewHolder> {

        public OfflineMapAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, ResultBean item) {
            ImageView ivImg = helper.getView(R.id.iv_img);
            TextView tv = helper.getView(R.id.tv);
            ivImg.setImageBitmap(BitmapFactory.decodeStream(item.inputStream));
            tv.setText(item.x + "，" + item.y + "，" + item.z);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
