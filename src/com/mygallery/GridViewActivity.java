package com.mygallery;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mygallery.R;
import com.mygallery.adapters.MyGridAdapter;
import com.mygallery.adapters.MyProgressDialog;
import com.mygallery.helper.CustomGallery;
import com.mygallery.helper.GeneralClass;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class GridViewActivity extends Activity{

	 
	String bid;
	private ImageLoader imageLoader;
	Handler handler;
	MyGridAdapter adapter;
	GridView gridView;
//	TextView tvAlbumNm;
	
	TextView tvTitle;
    ImageView ivLeft, ivRight;
    int count = 0;
    
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        setContentView(R.layout.activity_grid_view);

	        tvTitle = (TextView)findViewById(R.id.gridcount);
//	        tvAlbumNm = (TextView)findViewById(R.id.activity_grid_album);
//	        InitTitle();
	        
	        bid = getIntent().getStringExtra("bid");
//	        tvAlbumNm.setText(bid);
	        
	        initImageLoader();
	        handler = new Handler();
	        
//	         getImagesFromBucket();
	        
	        
	        
	        
	        gridView = (GridView) findViewById(R.id.gridGallery);
	        gridView.setFastScrollEnabled(true);
	        adapter = new MyGridAdapter(getApplicationContext(), imageLoader);
			gridView.setAdapter(adapter);
			
	        PauseOnScrollListener listener = new PauseOnScrollListener(imageLoader,
					true, true);
	        gridView.setOnScrollListener(listener);
			
	        gridView.setOnItemClickListener(mItemMulClickListener);
	        
	        
	        new SetImages().execute();
	        
			
			
	 }
	 
	 AdapterView.OnItemClickListener mItemMulClickListener = new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> l, View v, int position, long id) {
				adapter.changeSelection(v, position);
				if(GeneralClass.path.get(position).isSeleted)
					count++;
				else
					count--;
				
				tvTitle.setText(count+"");

			}
		};
	 
//	 protected void InitTitle(){
//	    	
//	    	tvTitle = (TextView)findViewById(R.id.action_title);
//	    	tvTitle.setText(count+" "+getResources().getString(R.string.photos_selected));
//	    	
//	    	ivLeft = (ImageView)findViewById(R.id.action_image_left);
//	    	ivLeft.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					GeneralClass.path.clear();
//					   GeneralClass.SelectedImgs.clear();
//					finish();
//				}
//			});
//	    	
//	    	ivRight = (ImageView)findViewById(R.id.action_image_right);
//	    	ivRight.setImageResource(R.drawable.done_selector);
//	    	
//	    	ivRight.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					ArrayList<CustomGallery> selected = adapter.getSelected();
//
//					System.out.println("Total Selected: " + selected.size());
//					for (int i = 0; i < selected.size(); i++) {
//						GeneralClass.SelectedImgs.add(selected.get(i));
//					}
//
//					finish();
//				}
//			});
//	    	
//	    }
	
		
	private class SetImages extends AsyncTask<Void, Void, String> {

		String flag = "";
		String message = "";
		private MyProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new MyProgressDialog(GridViewActivity.this);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				adapter.addAll(getImagesFromBucket());

			} catch (Exception e) {
				e.printStackTrace();
				return "extra";
			}

			return flag;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (pDialog != null)
				pDialog.dismiss();

			gridView.setAdapter(adapter);
			tvTitle.setText(count + "");

		}
	}
	
	 private ArrayList<CustomGallery> getImagesFromBucket() 
	 {
		 ArrayList<CustomGallery> galleryList = new ArrayList<CustomGallery>();
		 
	     int[] ids = null;
	     ArrayList<Integer> lstIds = new ArrayList<Integer>();
	     String searchParams = null;
	     String bucket = getIntent().getStringExtra("bid");
	     if(bucket != null && !bucket.equals("All"))
	     {
	         searchParams = "bucket_display_name = \""+bucket+"\"";
	     }
	     else
	     {
	         return galleryList;
	     }
	     String[] projection = {MediaStore.Images.Media._ID};
	     Cursor cursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
	             projection,
	             searchParams,
	             null,null);
	     if(cursor.moveToFirst())
	     {
	         do
	         {
	             int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
	             lstIds.add(id);
	         }
	         while(cursor.moveToNext());
	     }
	     ids = new int[lstIds.size()];
	     for(int i=0;i<ids.length;i++)
	     {
	    	 int flag=0;
	    	 
	    	 CustomGallery item = new CustomGallery();
	         ids[i] = (int)(lstIds.get(i));
	         
	         Uri uri = Uri.withAppendedPath(
	                 MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Integer.toString(ids[i]));
	         
	         item.sdcardPath = convertMediaUriToPath(uri);
	         
	         for(int j=0;j<GeneralClass.SelectedImgs.size();j++){
	        	 if(GeneralClass.SelectedImgs.get(j).sdcardPath.equals(item.sdcardPath)){
	        		 GeneralClass.SelectedImgs.remove(j);
	        		 flag=1;
	        		 break;
	        	 }
	         }
	         
	         if(flag==1){
	        	 item.isSeleted = true;
	        	 count++;
	         }else{
	        	 item.isSeleted = false;
	         }
	         

				galleryList.add(item);
	         
	     }
	     return galleryList;
	 }
	 
	 private void initImageLoader() {
			try {
				String CACHE_DIR = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/.temp_tmp";
				new File(CACHE_DIR).mkdirs();

				File cacheDir = StorageUtils.getOwnCacheDirectory(getBaseContext(),
						CACHE_DIR);

				DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
						.cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
						.bitmapConfig(Bitmap.Config.RGB_565).build();
				ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
						getBaseContext())
						.defaultDisplayImageOptions(defaultOptions)
						.discCache(new UnlimitedDiscCache(cacheDir))
						.memoryCache(new WeakMemoryCache());

				ImageLoaderConfiguration config = builder.build();
				imageLoader = ImageLoader.getInstance();
				imageLoader.init(config);

			} catch (Exception e) {

			}
		}
	 
	 protected String convertMediaUriToPath(Uri uri) {
	        String [] proj={MediaStore.Images.Media.DATA};
	        Cursor cursor = getContentResolver().query(uri, proj,  null, null, null);
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        cursor.moveToFirst();
	        String path = cursor.getString(column_index); 
	        cursor.close();
	        return path;
	    }

	 
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.main, menu);
	        MenuItem registerMenuItem = menu.findItem(R.id.action_done);
	        registerMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	        return true;
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()){
	        case android.R.id.home:
	        	onBackPressed();
	        	break;
	        case R.id.action_done:
			ArrayList<CustomGallery> selected = adapter.getSelected();

			System.out.println("Total Selected: " + selected.size());
			for (int i = 0; i < selected.size(); i++) {
				GeneralClass.SelectedImgs.add(selected.get(i));
			}

			finish();
			break;
	        }

	        return super.onOptionsItemSelected(item);
	    }
}
