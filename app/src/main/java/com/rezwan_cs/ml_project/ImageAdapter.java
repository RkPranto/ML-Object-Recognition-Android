package com.rezwan_cs.ml_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    ArrayList<ImageModel> arrayList = new ArrayList<>();
    Context context;
    LayoutInflater inflater;

    public ImageAdapter(ArrayList<ImageModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        this.inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.image_list_item, null);
        ImageView imageView = view.findViewById(R.id.iv_small_list_image);
        TextView textView  = view.findViewById(R.id.tv_detected_title);
        textView.setText(arrayList.get(position).getName());
        Picasso.get().load(arrayList.get(position).getImageLink()).into(imageView);
        return view;
    }
}
