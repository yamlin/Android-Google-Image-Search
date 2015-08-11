package com.yamlin.search.image.googleimagesearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yamlin on 8/1/15.
 */
public class ImageItem {
    public String url;

    public static List<ImageItem> getImages(JSONObject json) {
        List<ImageItem> images = new ArrayList<>();

        if (null != json) {
            try {
                JSONArray results = json.getJSONArray("results");
                if (null != results) {
                    for (int i = 0; i < results.length(); i++) {
                        ImageItem item = new ImageItem();
                        item.url = results.getJSONObject(i).getString("url");
                        images.add(item);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                images.clear();
            }
        }
        return images;
    }

}
