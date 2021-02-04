package cn.gistone.osmdroiddemo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteFullException;
import android.util.Log;

import org.osmdroid.api.IMapView;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.modules.CantContinueException;
import org.osmdroid.tileprovider.modules.DatabaseFileArchive;
import org.osmdroid.tileprovider.modules.SqlTileWriter;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.util.Counters;
import org.osmdroid.tileprovider.util.StreamUtils;
import org.osmdroid.util.MapTileIndex;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.zip.ZipEntry;

import static org.osmdroid.tileprovider.modules.DatabaseFileArchive.TABLE;

/**
 * Create 2021/2/1
 *
 * @author N
 * desc:
 */
public class SqlUtil {

    private final File db_file;
    private final SQLiteDatabase mDatabase;

    public SqlUtil(String outputFile) throws Exception {
        // do this in the background because it takes a long time
        db_file = new File(outputFile);
        try {
            mDatabase = SQLiteDatabase.openOrCreateDatabase(db_file.getAbsolutePath(), null);
        } catch (Exception ex) {
            throw new Exception("Trouble creating database file at " + outputFile, ex);
        }
        try {
            mDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + DatabaseFileArchive.TABLE + " (" + DatabaseFileArchive.COLUMN_KEY + " INTEGER , " + DatabaseFileArchive.COLUMN_PROVIDER + " TEXT, tile BLOB, PRIMARY KEY (key, provider));");
        } catch (Throwable t) {
            t.printStackTrace();
            Log.d(IMapView.LOGTAG, "error setting db schema, it probably exists already", t);
            // throw new IOException("Trouble creating database file"+ t.getMessage());
        }
    }

    public void insertDb(File file) {
        //   storage/emulated/0/_alllayers/L05/R0000000c/C0000001a.png
        String path = file.getPath();
        String[] item = path.split("/");
        String xStr = item[item.length - 1];
        String yStr = item[item.length - 2];
        String zoomStr = item[item.length - 3];
        String name = item[item.length - 4];

        int zoom = Integer.parseInt(zoomStr.replace("L", ""));
        int y = hex16To10(yStr.replace("R", ""));
        String xStr2 = xStr.replace("C", "");
        int x = hex16To10(xStr2.substring(0, xStr2.lastIndexOf(".")));
        long index = SqlTileWriter.getIndex(x, y, zoom);
        ByteArrayInputStream inputStream = getInputStream(file);
        long isSuccess = saveFile(index, inputStream, name);
        Log.e("TAG", "insertDb: " + isSuccess);
    }


    private int hex16To10(String str) {
        BigInteger b = new BigInteger(str, 16);
        return b.intValue();
    }


    public void getAllFile(List<File> resList, String dirPath) {
        File offlineFile = new File(dirPath);
        if (!offlineFile.exists()) {
            return;
        }
        File[] files = offlineFile.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isFile()) {
                resList.add(file);
            } else if (file.isDirectory()) {
                getAllFile(resList, file.getAbsolutePath());
            }
        }
    }

    public ByteArrayInputStream getInputStream(File filePath) {

        InputStream in = null;
        OutputStream out = null;
        ByteArrayInputStream byteStream = null;
        ByteArrayOutputStream dataStream = null;
        try {
                in = new FileInputStream(filePath);
            dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, StreamUtils.IO_BUFFER_SIZE);
            StreamUtils.copy(in, out);
            out.flush();
            final byte[] data = dataStream.toByteArray();
            byteStream = new ByteArrayInputStream(data);

        } catch (final Exception e) {
        } finally {
            StreamUtils.closeStream(in);
            StreamUtils.closeStream(out);
            StreamUtils.closeStream(byteStream);
            StreamUtils.closeStream(dataStream);
        }

        return byteStream;
    }

    public long saveFile(long index, final InputStream pStream,String name) {
        ByteArrayOutputStream bos = null;
        try {
            ContentValues cv = new ContentValues();
            cv.put(DatabaseFileArchive.COLUMN_PROVIDER, name);

            byte[] buffer = new byte[4096];
            int l;
            bos = new ByteArrayOutputStream();
            while((l = pStream.read(buffer)) != -1 ) {
                bos.write(buffer, 0, l);
            }
            byte[] bits = bos.toByteArray(); // if a variable is required at all

            cv.put(DatabaseFileArchive.COLUMN_KEY, index);
            cv.put(DatabaseFileArchive.COLUMN_TILE, bits);
            return mDatabase.replaceOrThrow(TABLE, null, cv);
        } catch (Exception ex) {
            return -1;
        } finally {
            try {
                bos.close();
            } catch (IOException e) {

            }
        }
    }
}
