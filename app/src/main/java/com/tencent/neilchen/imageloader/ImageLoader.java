package com.tencent.neilchen.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.tencent.neilchen.imageloader.cache.DiskCache;
import com.tencent.neilchen.imageloader.cache.ImageCache;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by neil.chen on 2017/5/19.
 */

public class ImageLoader {

  ExecutorService executorService =
      Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  //内存缓存
  ImageCache imageCache = new ImageCache();
  //sdcard缓存
  DiskCache diskCache = new DiskCache();
  //是否使用sdcard缓存
  private boolean isUseDiskCache = false;
  private Context context;

  public void displayImage(final Context context, final String url, final ImageView imageView) {
    this.context = context;

    Bitmap bmp = isUseDiskCache ? diskCache.get(url) : imageCache.get(url);
    if (bmp != null){
      imageView.setImageBitmap(bmp);
    }

    imageView.setTag(url);
    executorService.submit(new Runnable() {
      @Override public void run() {
        final Bitmap bitmap = downloadImage(url);

        if (bitmap == null) {
          return;
        }
        if (imageView.getTag().equals(url)) {
          //存缓存,sdcard
          imageCache.put(url, bitmap);
          diskCache.put(url,bitmap);
          imageView.post(new Runnable() {
            @Override public void run() {
              imageView.setImageBitmap(bitmap);
            }
          });
        }
      }
    });
  }

  /**
   * 是否使用sdcard缓存
   * @param useDiskCache
   */
  public void useDiskCache(boolean useDiskCache){
    isUseDiskCache = useDiskCache;
  }
  /**
   * 下载图片
   * @param imageUrl
   * @return
   */
  public Bitmap downloadImage(String imageUrl) {
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
