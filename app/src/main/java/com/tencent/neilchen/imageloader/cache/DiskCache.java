package com.tencent.neilchen.imageloader.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by As on 2017/5/21.
 */

public class DiskCache {

    String cacheDir = "sdcard/cache/";

    /**
     *
     * @param key
     * @return 从sdcard中获取图片
     */
    public Bitmap get(String key){
        return BitmapFactory.decodeFile(cacheDir + key);
    }

    /**
     *
     * @param key
     * @param bmp
     *  存入图片到sdcard
     */
    public void put(String key, Bitmap bmp){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(cacheDir + key);
            bmp.compress(Bitmap.CompressFormat.PNG, 70, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
