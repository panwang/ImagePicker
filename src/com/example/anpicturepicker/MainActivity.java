package com.example.anpicturepicker;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends Activity {
	final String logTag = "ImagePicker";
	boolean isImageClicked = false;

	/**
	 * 图片大小单位标记
	 * 
	 * @author 广驰
	 * 
	 */
	enum SizeType {
		PX, DP, BFB;
	}

	protected float user_width = 100;
	protected float user_Height = 60;

	public static int dip2px(float density, float dipValue) {
		return (int) (dipValue * density + 0.5f);
	}

	public static int px2dip(float density, float pxValue) {
		return (int) (pxValue / density + 0.5f);
	}
	
	//关闭软键盘
	private void hintKbTwo() {
	 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);            
	 if(imm.isActive()&&getCurrentFocus()!=null){
	    if (getCurrentFocus().getWindowToken()!=null) {
	    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }             
	 }
	}

	private SizeType m_sizeType = SizeType.PX;
	private ImageView image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		image = (ImageView) findViewById(R.id.imageSet);
		setRadioGroupListener();
		setttingContent();
	}

	private void setRadioGroupListener() {
		RadioGroup group = (RadioGroup) this.findViewById(R.id.rg_typegroup);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				// 更新文本内容，以符合选中项
				switch (radioButtonId) {
				case R.id.rb_px:
					m_sizeType = SizeType.PX;
					break;
				case R.id.rb_dp:
					m_sizeType = SizeType.DP;
					break;
				case R.id.rb_bfb:
					m_sizeType = SizeType.BFB;
					break;
				default:
					break;
				}
			}
		});
	}

	private void setttingContent() {
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				// 开启pictures画面type设定为image
				intent.setType("image/*");
				// 使用Intent.action-get-content action
				intent.setAction(Intent.ACTION_GET_CONTENT);
				// 获取相片返回本画面
				startActivityForResult(intent, 1);
			}
		});

		findViewById(R.id.bt_setSize).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hintKbTwo();
				analyticalSize();
				setImageViewSize();
			}
		});

		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e(logTag, " 图片是否点击过" + isImageClicked);
				if (!isImageClicked) {
					ImageView image = (ImageView) findViewById(R.id.imageSet);
					String bitmap = (String) image.getTag();

					Intent intent = new Intent();
					intent.putExtra("url", bitmap);
					intent.setClass(MainActivity.this, FullPicture.class);

					startActivity(intent);
				} else {
					image.setScaleType(ScaleType.CENTER_INSIDE);
				}
			}
		});

	}

	protected void setImageViewSize() {
		LayoutParams imgvwDimens = image.getLayoutParams();
		imgvwDimens.width = (int) user_width;
		imgvwDimens.height = (int) user_Height;

		image.setLayoutParams(imgvwDimens);
		image.setScaleType(ScaleType.CENTER_CROP);
	}

	/**
	 * 获取用户输入中的非数字
	 * 
	 * @param s
	 * @return
	 */
	public static String trimToNumber(String s) {
		int n = s.length();

		for (int i = 0; i < n; i++) {
			char ch = s.charAt(i);
			if (!(ch >= '0' && ch <= '9')) {
				return String.valueOf(ch);
			}
		}
		return null;
	}

	protected void analyticalSize() {
		DisplayMetrics outMetrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		float density = outMetrics.density;

		EditText et_Size = (EditText) findViewById(R.id.et_size);
		String str_size = et_Size.getText().toString();
		String str_split = trimToNumber(str_size);
		int uw;
		int uh;
		try {
			if (null != str_split) {
				String[] split = str_size.split("\\*");
				uw = Integer.parseInt(split[0]);
				uh = Integer.parseInt(split[1]);
			} else {
				uw = uh = Integer.parseInt(str_size);
			}
		} catch (Exception e) {
			uw = 100;
			uh = 60;
		}

		switch (m_sizeType) {
		case PX:
			user_width = uw;
			user_Height = uh;
			break;
		case DP:
			user_width = dip2px(density, uw);
			user_Height = dip2px(density, uh);
			break;
		case BFB:
			float widthPixels = outMetrics.widthPixels;
			float heightPixels = outMetrics.heightPixels;
			user_width = uw * widthPixels;
			user_Height = uh * heightPixels;
			break;
		default:
			user_width = 100;
			user_Height = 100;
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();
			Log.e(logTag, uri.toString());
			ContentResolver resolver = this.getContentResolver();
			try {
				Bitmap bitmap = BitmapFactory.decodeStream(resolver
						.openInputStream(uri));
				image.setTag(new String(uri.toString()));
				image.setImageBitmap(bitmap);
				// 设定完成
				Log.e(logTag, "设定完成" + bitmap.toString());
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
