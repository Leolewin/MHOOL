package com.summer.mho.models.gem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.summer.mho.R;

import java.util.ArrayList;

/**
 * 包名      com.summer.mho.main
 * 类名      EquipmentSpinnerAdapter
 * 创建时间   2015/12/22
 * 创建人     Summer
 * 类描述
 * 所属类     {@link }
 */
public class GemSpinnerAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<GemModel> gemModelArrayList;

    public GemSpinnerAdapter(Context context, ArrayList<GemModel> gemModelArrayList) {
        this.context = context;
        this.gemModelArrayList = gemModelArrayList;
    }

    @Override
    public int getCount() {
        return gemModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return gemModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return gemModelArrayList.get(position).getPK();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (null == convertView) {

            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.checkedTextView = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.checkedTextView.setText(gemModelArrayList.get(position).getGEM_NAME());

        return convertView;
    }

    private static class ViewHolder {
        private TextView checkedTextView;
    }
}
