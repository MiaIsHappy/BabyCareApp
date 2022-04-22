package edu.neu.babyfoto;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;

public class ImageDetail extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagedetail);
        Intent intent = getIntent();
        int images = intent.getIntExtra("image", R.mipmap.ic_launcher);
        Log.e("detail", String.valueOf(images));
        ImageView imag = (ImageView) findViewById(R.id.details_img);
        imag.setImageResource(images);
    }
}
