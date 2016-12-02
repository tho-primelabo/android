package com.example.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.entity.MyImage;
import com.example.mainactivity.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter  extends ArrayAdapter<MyImage> {
	private final int THUMBSIZE = 150;
	private List<MyImage> searchList;
	private List<MyImage> imgList;
	

	/**
     * applying ViewHolder pattern to speed up ListView, smoother and faster
     * item loading by caching view in A ViewHolder object
     */
    private static class ViewHolder {
        ImageView imgIcon;
        TextView description;
    }

    /*public ImageAdapter() {
    	super(null, 0);
    	this.searchList = new ArrayList<>();
    }*/
    public ImageAdapter(Context context, int resource, ArrayList<MyImage> images) {
        super(context, resource, images);
        //Log.d("XXXXX", "Images size: " +images.size());
        this.searchList = new ArrayList<MyImage>();
        this.imgList = images;
        this.searchList.addAll(images);
        //Log.d("XXXXX", "Search List size: " +searchList.size());
    }

    @Override public View getView(int position, View convertView,
            ViewGroup parent) {
        // view lookup cache stored in tag
        ViewHolder viewHolder;
        // Check if an existing view is being reused, otherwise inflate the
        // item view
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_image, parent, false);
            viewHolder.description =
                    (TextView) convertView.findViewById(R.id.item_img_infor);
            viewHolder.imgIcon =
                    (ImageView) convertView.findViewById(R.id.item_img_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Get the data item for this position
        MyImage image = getItem(position);
        // set description text
        viewHolder.description.setText(image.toString());
        // set image icon
        viewHolder.imgIcon.setImageBitmap(ThumbnailUtils
                .extractThumbnail(BitmapFactory.decodeFile(image.getPath()),
                        THUMBSIZE, THUMBSIZE));

        // Return the completed view to render on screen
        return convertView;
    }
    // Filter method
    public void fillter(String filterText) {
    	//Log.d("XXXXX", "searchList start----------: "+ searchList.size());
    	filterText = filterText.toLowerCase(Locale.getDefault());
    	imgList.clear();
    	if(filterText.length() == 0) {
    		imgList.addAll(searchList);
    	}
    	else {
    		
    		//Log.d("XXXXX", "imgList---------- "+ imgList.size());
    		//Log.d("XXXXX", "searchList----------"+ searchList.size());
    		for (MyImage s: searchList) {
    			if (s.getTitle().toLowerCase(Locale.getDefault()).contains(filterText) || 
					s.getDescription().toLowerCase(Locale.getDefault()).contains(filterText)	) {
    				imgList.add(s);
    			}
    		}
    	}
    	notifyDataSetChanged();
    	//Log.d("XXXXX", "Filter---- " + imgList.size());
    }
}
