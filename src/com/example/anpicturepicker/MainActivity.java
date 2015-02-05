package com.example.anpicturepicker;

import android.R.bool;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class MainActivity extends Activity {
	
	final String logTag = "ImagePicker";
	boolean isImageClicked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setttingContent();
	}

	private void setttingContent() {
		
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				//开启pictures画面type设定为image
				intent.setType("image/*");
				//使用Intent.action-get-content action
				intent.setAction(Intent.ACTION_GET_CONTENT);
				//获取相片返回本画面
				startActivityForResult(intent, 1);
			}
		});
		
		//设置图片点击事件
		final ImageView image = (ImageView)findViewById(R.id.imageSet);
		image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.e(logTag," 图片是否点击过"+isImageClicked);
				if(!isImageClicked){
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, FullPicture.class);
					startActivity(intent);
				}else{
					image.setScaleType(ScaleType.CENTER_INSIDE);
				}
			}
		});
		
	}

     @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	 if (resultCode == RESULT_OK) {
    		 Uri uri = data.getData();
    		 Log.e(logTag, uri.toString());
    		 ContentResolver resolver = this.getContentResolver();
    		 try {
				Bitmap bitmap = BitmapFactory.decodeStream(resolver.openInputStream(uri));
				ImageView image = (ImageView)findViewById(R.id.imageSet);
				image.setImageBitmap(bitmap);
				//设定完成
				Log.e(logTag, "设定完成"+bitmap.toString());
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(logTag, e.toString());
			}
		}
    	super.onActivityResult(requestCode, resultCode, data);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
