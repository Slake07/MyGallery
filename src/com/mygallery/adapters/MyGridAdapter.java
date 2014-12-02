package com.mygallery.adapters;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mygallery.R;
import com.mygallery.helper.CustomGallery;
import com.mygallery.helper.GeneralClass;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyGridAdapter extends BaseAdapter {

	Context context;
    LayoutInflater inflater;
    ImageLoader imageLoader;

    public MyGridAdapter(Context context, ImageLoader imageLoader) {
    	this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageLoader = imageLoader;
        
    }


    @Override
    public int getCount() {
        return GeneralClass.path.size();
    }


    @Override
    public Object getItem(int position) {
        return GeneralClass.path.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public void selectAll(boolean selection) {
		for (int i = 0; i < GeneralClass.path.size(); i++) {
			GeneralClass.path.get(i).isSeleted = selection;

		}
		notifyDataSetChanged();
	}
    
    public boolean isAllSelected() {
		boolean isAllSelected = true;

		for (int i = 0; i < GeneralClass.path.size(); i++) {
			if (!GeneralClass.path.get(i).isSeleted) {
				isAllSelected = false;
				break;
			}
		}

		return isAllSelected;
	}
    
    public boolean isAnySelected() {
		boolean isAnySelected = false;

		for (int i = 0; i < GeneralClass.path.size(); i++) {
			if (GeneralClass.path.get(i).isSeleted) {
				isAnySelected = true;
				break;
			}
		}

		return isAnySelected;
	}
    
    
    public ArrayList<CustomGallery> getSelected() {
		ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();

		for (int i = 0; i < GeneralClass.path.size(); i++) {
			if (GeneralClass.path.get(i).isSeleted) {
				dataT.add(GeneralClass.path.get(i));
			}
		}

		return dataT;
	}
    
    public void addAll(ArrayList<CustomGallery> files) {

		try {
			GeneralClass.path.clear();
			GeneralClass.path.addAll(files);
			Collections.reverse(GeneralClass.path);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		notifyDataSetChanged();
	}
    
    public void changeSelection(View v, int position) {

		if (GeneralClass.path.get(position).isSeleted) {
			GeneralClass.path.get(position).isSeleted = false;
		} else {
			GeneralClass.path.get(position).isSeleted = true;
		}

		((ViewHolder) v.getTag()).llSel
				.setSelected(GeneralClass.path.get(position).isSeleted);
	}


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

    	final ViewHolder holder;
    	
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item, null);
            
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.llSel = (ImageView)convertView.findViewById(R.id.llSelected);
            
            convertView.setTag(holder);
        }else {
			holder = (ViewHolder) convertView.getTag();
		}

        holder.imageView.setTag(position);
        
        
        imageLoader.displayImage(
				"file://" + GeneralClass.path.get(position).sdcardPath,
				holder.imageView, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri,
							View view) {
						holder.imageView
								.setImageResource(R.drawable.no_media);
						super.onLoadingStarted(imageUri, view);
					}
				});
        
//        Bitmap thumbnail;
//		try {
//			thumbnail = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
//        if (GeneralClass.path.get(position).isSeleted) {
//          holder.llSel.setSelected(true);
//        }

        holder.llSel.setSelected(GeneralClass.path
				.get(position).isSeleted);
        
        

        return convertView;
    }
    
    protected String convertMediaUriToPath(Uri uri) {
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj,  null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index); 
        cursor.close();
        return path;
    }
    
    public class ViewHolder {
		ImageView imageView;
		ImageView llSel;
	}

    public void clearCache() {
		imageLoader.clearDiscCache();
		imageLoader.clearMemoryCache();
	}

	public void clear() {
		GeneralClass.path.clear();
		notifyDataSetChanged();
	}
	
}