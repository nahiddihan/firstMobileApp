package com.cste.nstu.courseassociate;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class PostCustomBaseAdapter extends BaseAdapter {
    Context context;
    List<RowItem> rowItems;

    public PostCustomBaseAdapter(Context context, List<RowItem> items) {
        this.context = context;
        this.rowItems = items;
    }

    /*private view holder class*/
    private class ViewHolder {
        // ImageView imageView;
        TextView pdate;
        TextView ptime;
        TextView pinfo;
        TextView pdesc;
    }



    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.post_review_list, null);
            holder = new ViewHolder();
            holder.pdate = (TextView) convertView.findViewById(R.id.tvDate);
            holder.ptime = (TextView) convertView.findViewById(R.id.tvTime);
            holder.pinfo = (TextView) convertView.findViewById(R.id.TvPostInfo);
            holder.pdesc = (TextView) convertView.findViewById(R.id.TvPostDesc);
            // holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        RowItem rowItem = (RowItem) getItem(position);

        holder.pdate.setText(rowItem.getDate());
        holder.ptime.setText(rowItem.getTime());
        holder.pinfo.setText(rowItem.getPostInfo());
        holder.pdesc.setText(rowItem.getPostdesc());


        return convertView;
    }


}
