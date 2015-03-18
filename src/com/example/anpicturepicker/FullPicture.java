package com.example.anpicturepicker;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class FullPicture extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fullscreen);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		String imgurl = getIntent().getExtras().getString("url");
		if (null != imgurl) {
			Uri uri = Uri.parse(imgurl);
			ContentResolver resolver = this.getContentResolver();
			Bitmap bitmap = null;
			try {
				bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ImageView image = (ImageView) findViewById(R.id.imageViewFull);
			image.setImageBitmap(bitmap);
		}

		setImageListener();
	}

	private void setImageListener() {
		// TODO Auto-generated method stub
		final ImageView image = (ImageView) findViewById(R.id.imageViewFull);
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent intent = new Intent();
				// intent.setClass(FullPicture.this, MainActivity.class);
				// startActivity(intent);
				finish();
			}
		});
	}
}
