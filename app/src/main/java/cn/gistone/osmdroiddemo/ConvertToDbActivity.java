package cn.gistone.osmdroiddemo;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.tileprovider.modules.SqliteArchiveTileWriter;
import org.osmdroid.tileprovider.modules.ZipFileArchive;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Create 2021/2/1
 *
 * @author N
 * desc:
 */
public class ConvertToDbActivity extends AppCompatActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv)
    TextView tv;

    private Unbinder bind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_to_db);
        bind = ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_convert})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_convert:
                convertToDb();
                break;
            default:
                break;
        }
    }

    private void convertToDb() {
        String name = etName.getText().toString().trim();

        File externalStorageState = Environment.getExternalStorageDirectory();
        File path = new File(externalStorageState, name);

        String filePath = path.getName();//xx.zip
        String fileName = filePath.substring(0, filePath.lastIndexOf(".")); //xx
        File file = new File(externalStorageState, fileName + ".sqlite");
        File file2 = new File(externalStorageState, fileName);
        try {
            SqlUtil sqlUtil = new SqlUtil(file.getPath());
            if (!file2.exists()) {
                tv.setText("正在解压...");
                ZipCompressUtil.zipUncompress(path.getPath(), externalStorageState.getPath(), () -> {
                    tv.setText("解压完成");
                    getCompressFile(file2, sqlUtil);
                });
            }else{
                getCompressFile(file2, sqlUtil);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            File file = new File(externalStorageState, fileName + "sqlite");
//            SqliteArchiveTileWriter sqliteArchiveTileWriter = new SqliteArchiveTileWriter(file.getPath());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void getCompressFile(File file2, SqlUtil sqlUtil) {
        List<File> allFileList = new ArrayList<>();
        sqlUtil.getAllFile(allFileList,file2.getPath());
        for (File file1 : allFileList) {
            sqlUtil.insertDb(file1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
