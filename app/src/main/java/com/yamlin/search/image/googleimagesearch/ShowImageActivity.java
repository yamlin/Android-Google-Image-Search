package com.yamlin.search.image.googleimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class ShowImageActivity extends Activity {
    public static final String TAG = "ShowImageActivity";

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        Bundle bundle = getIntent().getExtras();
        if (null == bundle) {
            return;
        }
        mImageView = (ImageView) findViewById(R.id.imageShow);
        String url = bundle.getString("url");
        Picasso.with(this).load(url).fit().centerCrop()
                .placeholder(R.drawable.placeholder).into(mImageView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            onShareItem();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Can be triggered by a view event such as a button press
    protected void onShareItem() {

        // Get access to the URI for the bitmap
        Drawable drawable = mImageView.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                bitmap, "Image Description", null);


        Uri uri = Uri.parse(path);

        if (uri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/*");
            // Launch sharing dialog for image
            startActivity(Intent.createChooser(shareIntent, "Share Image"));
        } else {
            // ...sharing failed, handle error
            Log.e(TAG, "Error open uri");
        }
    }

}
