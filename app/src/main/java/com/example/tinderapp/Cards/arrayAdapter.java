package com.example.tinderapp.Cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tinderapp.R;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<cards>{
    Context context;
    public arrayAdapter(Context context, int resourceId, List<cards> items)
    {
        super(context,resourceId,items);
    }
    public View getView(int position , View convertView, ViewGroup parent)
    {
        cards card_item=getItem(position);
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        TextView name=(TextView)convertView.findViewById(R.id.name);
        ImageView image=(ImageView)convertView.findViewById(R.id.image);
        name.setText(card_item.getName());
        switch (card_item.getprofileimageurl())
        {
            case "default":
                //Glide.with(convertView.getContext()).load(R.mipmap.ic_launcher).into(image);
                image.setImageResource(R.mipmap.ic_launcher);
                break;
            default:
                Glide.clear(image);
                Glide.with(convertView.getContext()).load(card_item.getprofileimageurl()).into(image);
                break;
        }
        return convertView;
    }
}