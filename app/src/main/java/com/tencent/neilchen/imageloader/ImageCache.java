package com.tencent.neilchen.imageloader;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * Created by neil.chen on 2017/5/19.
 */

public class ImageCache {
  private LruCache<String, Bitmap> mImageCache;

  public ImageCache() {
    initImageCache();
  }

  private void initImageCache() {

    //计算最大可使用的内存
    long maxMemory = Runtime.getRuntime().maxMemory();
    //使用四分之一作为缓存
    int cacheSize = (int) (maxMemory/4);

    Log.i("cacheSize",cacheSize+"");
    mImageCache = new LruCache<String, Bitmap>(cacheSize) {
      @Override protected int sizeOf(String string, Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
      }
    };
  }

  public boolean put(String key, Bitmap bitmap) {
    Bitmap bitmapValue = get(key);
    if (bitmapValue == null) {
      if (mImageCache != null && bitmap != null)
        bitmapValue = mImageCache.put(key, bitmap);
    }
    return bitmapValue == bitmap;
  }

  public Bitmap get(String key) {
    return mImageCache.get(key);
  }
}
