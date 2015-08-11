package com.yamlin.search.image.googleimagesearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by yamlin on 8/1/15.
 */
public class ImageAdapter extends ArrayAdapter<ImageItem> {
    private static class ViewHolder {
        ImageView imageView;
    }

    public ImageAdapter(Context context, List<ImageItem> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.gridview_item, parent, false);

            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Get the data item for this position
        ImageItem imageItem = getItem(position);

        Picasso.with(convertView.getContext()).load(imageItem.url).fit().centerCrop()
                .placeholder(R.drawable.placeholder).into(viewHolder.imageView);

        // Return the completed view to render on screen
        return convertView;
    }

}
