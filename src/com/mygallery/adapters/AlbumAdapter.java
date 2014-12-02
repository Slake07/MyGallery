package com.mygallery.adapters;

import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mygallery.R;
import com.mygallery.helper.Album;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class AlbumAdapter extends BaseAdapter {

	Context context;
    LayoutInflater inflater;
    List<Album> items;
    ImageLoader imageLoader;


    public AlbumAdapter(Context context, List<Album> items, ImageLoader imageLoader) {
    	this.context = context;
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageLoader = imageLoader;
    }


    @Override
    public int getCount() {
        return items.size();
    }


    @Override
    public Object getItem(int position) {
        return items.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.album_row, null);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        tvName.setText(items.get(position).getName());
        
        TextView tvCount = (TextView) convertView.findViewById(R.id.tvTotal);
        tvCount.setText(items.get(position).getCount()+"");
        System.out.println(items.get(position).getName()+": "+items.get(position).getCount());

        Uri uri = Uri.withAppendedPath( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Long.toString(items.get(position).getCoverID()));

        convertView.setTag(items.get(position).getName());
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        
        imageLoader.displayImage(
				"file://" + convertMediaUriToPath(uri),
				imageView, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri,
							View view) {
						imageView
								.setImageResource(R.drawable.no_media);
						super.onLoadingStarted(imageUri, view);
					}
				});
        
        //Bitmap image = unmarshallBitmap(uri, context.getContentResolver());
        //imageView.setImageBitmap(image);
//
//        if (image != null){
//            imageView.setImageBitmap(image);
//        }
//        else {
//            // If no image is provided, display a folder icon.
//            imageView.setImageResource(R.drawable.ic_launcher);
//        }

        return convertView;
    }

    public static Bitmap unmarshallBitmap(Uri imageUri, ContentResolver resolver) {
    	   return imageFromGallery(imageUri, resolver);
    	}
    
    private static Bitmap imageFromGallery(Uri imageUri, ContentResolver resolver) {
        try {
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = resolver.query(imageUri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            return BitmapFactory.decodeFile(picturePath);
        } catch (Exception x) {
            return null;
        }
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
    
}