package com.tencent.neilchen.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.RelativeDateTimeFormatter;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by neil.chen on 2017/5/19.
 */

public class ImageLoader {

  ExecutorService executorService =
      Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
  private LruCache<String, Bitmap> mImageCache;

  public ImageLoader() {
    initImageCache();
  }

  private void initImageCache() {

    //计算最大可使用的内存
    long maxMemory = Runtime.getRuntime().maxMemory();
    //使用四分之一作为缓存
    int cacheSize = (int) (maxMemory / 4);

    mImageCache = new LruCache<String, Bitmap>(cacheSize) {
      @Override protected int sizeOf(String string, Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
      }
    };
  }

  public void displayImage(final String url, final ImageView imageView) {
    imageView.setTag(url);
    executorService.submit(new Runnable() {
      @Override public void run() {
        Log.i("diaplay", Thread.currentThread().getName());

        final Bitmap bitmap = downloadImage(url);
        if (bitmap == null){
          return;
        }
        if (imageView.getTag().equals(url)){

          imageView.post(new Runnable() {
            @Override public void run() {
              imageView.setImageBitmap(bitmap);
            }
          });
        }

        mImageCache.put(url,bitmap);
      }
    });
  }

  public Bitmap downloadImage(String imageUrl){
    try {
      URL url = new URL(imageUrl);
      HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
      Bitmap bitmap = BitmapFactory.decodeStream(urlConnection.getInputStream());
      urlConnection.disconnect();
      return bitmap;
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
