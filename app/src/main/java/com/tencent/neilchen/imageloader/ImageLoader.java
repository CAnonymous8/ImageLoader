package com.tencent.neilchen.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.RelativeDateTimeFormatter;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;
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

  ImageCache imageCache = new ImageCache();
  private Context context;

  public void displayImage(final Context context, final String url, final ImageView imageView) {
    this.context = context;

    final Bitmap bitmap = imageCache.get(url);
    if (bitmap != null) {
      imageView.setImageBitmap(bitmap);
      return;
    }

    imageView.setTag(url);
    executorService.submit(new Runnable() {
      @Override public void run() {

        final Bitmap bitmap = downloadImage(url);

        //final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        if (bitmap == null) {
          return;
        }
        if (imageView.getTag().equals(url)) {
          int byteCount = bitmap.getByteCount();
          Log.i("bitmap.getByteCount()", byteCount + "");
          //存缓存
          imageCache.put(url, bitmap);
          imageView.post(new Runnable() {
            @Override public void run() {
              imageView.setImageBitmap(bitmap);
            }
          });
        }
      }
    });
  }

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
