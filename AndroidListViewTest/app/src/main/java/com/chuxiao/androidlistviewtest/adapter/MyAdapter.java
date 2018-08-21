package com.chuxiao.androidlistviewtest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.chuxiao.androidlistviewtest.MainActivity;
import com.chuxiao.androidlistviewtest.R;
import com.chuxiao.androidlistviewtest.entity.Student;

import java.util.List;

/**
 * Created by 12525 on 2018/4/12.
 */

public class MyAdapter extends BaseAdapter {

    private Context mContext;
    private List<Student> mStudentList;

    public MyAdapter(Context context, List<Student> list) {
        this.mContext = context;
        this.mStudentList = list;
    }

    @Override
    public int getCount() {
        return mStudentList == null ? 0 : mStudentList.size();
    }

    @Override
    public Object getItem(int position) {
        return mStudentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_student, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = ((ViewHolder) convertView.getTag());
        }

        viewHolder.tvName.setText(mStudentList.get(position).getName());
        viewHolder.tvSex.setText(mStudentList.get(position).getSex());
        viewHolder.tvAge.setText(mStudentList.get(position).getAge() + "");
        viewHolder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "name的点击事件", Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    class ViewHolder {
        private TextView tvName;
        private TextView tvSex;
        private TextView tvAge;

        public ViewHolder(View convertView) {
            tvName = ((TextView) convertView.findViewById(R.id.tv_name));
            tvSex = ((TextView) convertView.findViewById(R.id.tv_sex));
            tvAge = ((TextView) convertView.findViewById(R.id.tv_age));
        }
    }
}
