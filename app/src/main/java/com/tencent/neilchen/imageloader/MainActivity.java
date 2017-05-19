package com.tencent.neilchen.imageloader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  String imageurl = "http://pic55.nipic.com/file/20141208/19462408_171130083000_2.jpg";
  private ImageView imageView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    findViewById(R.id.tvIl).setOnClickListener(this);
    imageView = (ImageView) findViewById(R.id.iv);
  }

  /**
   * Called when a view has been clicked.
   *
   * @param v The view that was clicked.
   */
  @Override public void onClick(View v) {
    switch (v.getId()){
      case R.id.tvIl :
        new ImageLoader().displayImage(imageurl,imageView);
        break;
    }
  }
}
