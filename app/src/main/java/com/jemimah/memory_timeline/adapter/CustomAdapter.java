package com.jemimah.memory_timeline.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jemimah.memory_timeline.R;
import com.jemimah.memory_timeline.model.DataModel;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener {

    private final ArrayList<DataModel> dataSet;
    Context mContext;

    private static String TAG = "CustomAdapter";

    // View lookup cache
    private static class ViewHolder {
        TextView txtDate;

        ImageView info;
    }

    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.memory_item, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        DataModel dataModel = (DataModel) object;
    }

    public ArrayList<DataModel> getData() {
        return dataSet;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DataModel dataModel = dataSet.get(position);
        ViewHolder viewHolder; // view lookup cache stored in tag

        Log.d(TAG, "getView: " + dataModel.getName());

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.memory_item, parent, false);

            viewHolder.txtDate = convertView.findViewById(R.id.txtDate);

            convertView.setTag(viewHolder);
        } else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }

        lastPosition = position;

        // assign values if the object is not null
        if(dataModel != null) {
            // get the TextView from the ViewHolder and then set the text (item name)
            viewHolder.txtDate.setText(dataModel.getDate());
        }

        return convertView;
    }
}
