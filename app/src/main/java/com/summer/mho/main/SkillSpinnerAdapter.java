package com.summer.mho.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.summer.mho.R;
import com.summer.mho.models.skill.SkillModel;

import java.util.ArrayList;

/**
 * 包名      com.summer.mho.main
 * 类名      SkillSpinnerAdapter
 * 创建时间   2015/12/22
 * 创建人     Summer
 * 类描述
 * 所属类     {@link }
 */
public class SkillSpinnerAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<SkillModel> skillModelArrayList;

    public SkillSpinnerAdapter(Context context, ArrayList<SkillModel> skillModelArrayList) {
        this.context = context;
        this.skillModelArrayList = skillModelArrayList;
    }

    @Override
    public int getCount() {
        return skillModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return skillModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return skillModelArrayList.get(position).getPK();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (null == convertView) {

            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(skillModelArrayList.get(position).getNAME());

        return convertView;
    }

    private static class ViewHolder {
        private TextView textView;
    }
}
