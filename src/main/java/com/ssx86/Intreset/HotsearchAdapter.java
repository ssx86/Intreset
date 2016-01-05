package com.ssx86.Intreset;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by new2 on 2016/1/5.
 */
public class HotsearchAdapter extends BaseAdapter{
    List<HotsearchModel> m_list;
    private Activity activity;
    private LayoutInflater inflater;

    class ViewHolder {
        TextView title;
    }

    public HotsearchAdapter(Activity activity, List<HotsearchModel> list){
        this.m_list = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return m_list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.hotsearch_item, null);

            viewHolder = new ViewHolder();

            viewHolder.title = (TextView) convertView.findViewById(R.id.title);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        HotsearchModel m = (HotsearchModel) getItem(position);

        viewHolder.title.setText(m.getTitle());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return m_list.get(position);
    }
}

