package com.sdmp.project4;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

public class MyCustomAdapter extends BaseAdapter {

    Context context;
    private List<Integer> idImages;
    private static final int PADDING = 8;
    private static final int WIDTH =170;
    private static final int HEIGHT = 170;


    public MyCustomAdapter (Context context, List<Integer> ids) {
        this.context = context;
        this.idImages = ids;
    }

    @Override
    public int getCount() {
        return idImages.size();
    }

    @Override
    public Object getItem(int position) {
        return idImages.get(position);
    }


    @Override
    public long getItemId(int position) {
        return idImages.get(position);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView = (ImageView) convertView;

        if (imageView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(WIDTH, HEIGHT));
            imageView.setPadding(PADDING, PADDING, PADDING, PADDING);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

        imageView.setImageResource(idImages.get(position));
        return imageView;
    }
}
