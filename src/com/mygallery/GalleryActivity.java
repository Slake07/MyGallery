package com.mygallery;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mygallery.R;
import com.mygallery.adapters.AlbumAdapter;
import com.mygallery.helper.Album;
import com.mygallery.helper.GeneralClass;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class GalleryActivity extends Activity{

	
    private ImageLoader imageLoader;
    SharedPreferences preferences;
    TextView tvTitle;
    ImageView ivLeft, ivRight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.list_albums);
        
        
        
        preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
        
       
        
//        InitTitle();
        
        initImageLoader();
        setAlbum();
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
    
//    protected void InitTitle(){
//    	
//    	tvTitle = (TextView)findViewById(R.id.action_title);
//    	tvTitle.setText(GeneralClass.SelectedImgs.size()+" "+getResources().getString(R.string.photos_selected));
//    	
//    	ivLeft = (ImageView)findViewById(R.id.action_image_left);
//    	ivLeft.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				GeneralClass.path.clear();
//				   GeneralClass.SelectedImgs.clear();
//				
//				if(getIntent().hasExtra("thnk")){
//					   preferences = PreferenceManager
//								.getDefaultSharedPreferences(getApplicationContext());
//						
//								SharedPreferences.Editor editor = preferences.edit();
//							    editor.clear();
//							    editor.commit(); 
//							    
//							  Intent i = new Intent(GalleryActivity.this, FirstActivity.class);
//							  startActivity(i);
//							  finish();
//			       }else{
//			    	   finish();
//			       }
//			}
//		});
//    	
//    	ivRight = (ImageView)findViewById(R.id.action_image_right);
//    	ivRight.setImageResource(R.drawable.done_selector);
//    	
//    	ivRight.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//				
//				if(GeneralClass.SelectedImgs.size()>0){
//					Intent i = new Intent(GalleryActivity.this, PicSelectActivity.class);
//					startActivity(i);
//					//finish();
//				}else{
//					GeneralClass.showDialog(GalleryActivity.this, getString(R.string.message), "Select at least one picture");
//				}
//			}
//		});
//    	
//    }
    
    
	@Override
	protected void onResume() {
		super.onResume();
		
//		tvTitle = (TextView)findViewById(R.id.action_title);
//    	tvTitle.setText(GeneralClass.SelectedImgs.size()+" "+getResources().getString(R.string.photos_selected));
	}
    
    protected void setAlbum(){
    	
    	ArrayList<Album> mAlbumsList = new ArrayList<Album>();
		
		 Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		    String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID,MediaStore.Images.Thumbnails._ID,
		            MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

		    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
		    

		    ArrayList<String> ids = new ArrayList<String>();
		    mAlbumsList.clear();
		    if (cursor != null) {
		        while (cursor.moveToNext()) {
		            Album album = new Album();

		            int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
		            album.id = cursor.getString(columnIndex);

		            if (!ids.contains(album.id)) {
		                columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
		                album.name = cursor.getString(columnIndex);

		                columnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
		                album.coverID = cursor.getLong(columnIndex);

		                mAlbumsList.add(album);
		                ids.add(album.id);
		            } else {
		                mAlbumsList.get(ids.indexOf(album.id)).count++;
		            }
		        }
		        cursor.close();

		    }
		    
		    AlbumAdapter adapter = new AlbumAdapter(this, mAlbumsList, imageLoader);
		    ListView gridView = (ListView) findViewById(R.id.gridView);
	        gridView.setAdapter(adapter);
	        Collections.sort(mAlbumsList, myComparator);
	        
	        gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					
					String bid = view.getTag().toString();
					System.out.println("Bucket id = " + bid);
					Intent i = new Intent(GalleryActivity.this, GridViewActivity.class);
					i.putExtra("bid", bid);
					startActivity(i);
					
				}
			});
    }
    
    Comparator<Album> myComparator = new Comparator<Album>() {
        public int compare(Album obj1,Album obj2) {
            return obj1.getName().toLowerCase().compareTo(obj2.getName().toLowerCase());
        }
     };
    
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
        	if(GeneralClass.SelectedImgs.size()>0){
				Intent i = new Intent(GalleryActivity.this, PreviewSizeActivity.class);
				startActivity(i);
				//finish();
			}else{
				Toast.makeText(GalleryActivity.this, "Select atleast a picture", Toast.LENGTH_LONG).show();
			}
        	break;
        }

        return super.onOptionsItemSelected(item);
    }
    
//    @Override
//	public void onBackPressed() {
//		super.onBackPressed();
//	   Log.d("CDA", "onBackPressed Called");
//	   GeneralClass.path.clear();
//	   GeneralClass.SelectedImgs.clear();
//	   
//	   if(getIntent().hasExtra("thnk")){
//		   preferences = PreferenceManager
//					.getDefaultSharedPreferences(getApplicationContext());
//			
//					SharedPreferences.Editor editor = preferences.edit();
//				    editor.clear();
//				    editor.commit(); 
//				    
//				  Intent i = new Intent(GalleryActivity.this, FirstActivity.class);
//				  startActivity(i);
//				  finish();
//       }
//	}

}
