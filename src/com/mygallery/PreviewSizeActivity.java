package com.mygallery;

import com.mygallery.R;
import com.mygallery.adapters.SelectedGridAdapter;
import com.mygallery.helper.GeneralClass;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PreviewSizeActivity extends Activity{
	
	LinearLayout ll46;
	ImageLoader imageLoader;
	SelectedGridAdapter adapter;
	TextView tvTitle;
    ImageView ivLeft, ivRight;
    GridView gridview;
    
    SharedPreferences preferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview_size);
	
		preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		
		
		gridview = (GridView)findViewById(R.id.gridSelected);
		
		initImageLoader();
		
		System.out.println("Total Imgs: " + GeneralClass.SelectedImgs.size());
		
		adapter = new SelectedGridAdapter(getApplicationContext(), imageLoader);
		gridview.setAdapter(adapter);
		
//		for(int i=0;i<GeneralClass.SelectedImgs.size();i++){
//			
//			final int k=i;
//			
//			final ImageView i46 = new ImageView(getApplicationContext());
//			
//			LinearLayout.LayoutParams layoutParams46 = new LinearLayout.LayoutParams(pixels70, pixels60);
//			layoutParams46.setMargins(2, 2, 2, 2);
//			i46.setScaleType(ScaleType.CENTER_CROP);
//			i46.setLayoutParams(layoutParams46);
//			
//			
//			i46.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					
//						ShowImgDialog46(GeneralClass.SelectedImgs.get(k).sdcardPath);
//				}
//			});
//			
//					
//					imageLoader.displayImage(
//							"file://" + GeneralClass.SelectedImgs.get(i).sdcardPath,
//							i46, new SimpleImageLoadingListener() {
//								@Override
//								public void onLoadingStarted(String imageUri,
//										View view) {
//									i46.setImageResource(R.drawable.no_media);
//									super.onLoadingStarted(imageUri, view);
//								}
//							});
//					
//					ll46.addView(i46);
//				
//		}
		
	}
	
	
	protected void ShowImgDialog46(String path){
	
		Dialog dialog = new Dialog(PreviewSizeActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_image_preview46);

		final ImageView image = (ImageView) dialog.findViewById(R.id.ivDialog);

		imageLoader.displayImage(
				"file://" + path,
				image, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri,
							View view) {
						image.setImageResource(R.drawable.no_media);
						super.onLoadingStarted(imageUri, view);
					}
				});
		
		// !!! Do here setBackground() instead of setImageDrawable() !!! //

		// Without this line there is a very small border around the image (1px)
		// In my opinion it looks much better without it, so the choice is up to you.
		dialog.getWindow().setBackgroundDrawable(null);

		// Show the dialog
		dialog.show();
	}
	
	
	

	private void initImageLoader() {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
				this).defaultDisplayImageOptions(defaultOptions).memoryCache(
				new WeakMemoryCache());

		ImageLoaderConfiguration config = builder.build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);
	}
}
