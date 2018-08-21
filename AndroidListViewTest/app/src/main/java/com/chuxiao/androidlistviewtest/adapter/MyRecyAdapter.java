package com.chuxiao.androidlistviewtest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chuxiao.androidlistviewtest.R;
import com.chuxiao.androidlistviewtest.entity.Student;

import java.util.List;

/**
 * Created by 12525 on 2018/4/12.
 */

public class MyRecyAdapter extends RecyclerView.Adapter<MyRecyAdapter.MyViewHolder> {
    private Context mContext;
    private List<Student> mStudentList;

    public MyRecyAdapter(Context context, List<Student> list){
        this.mContext = context;
        this.mStudentList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_student,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvName.setText(mStudentList.get(position).getName());
        holder.tvName.setText(mStudentList.get(position).getName());
        holder.tvSex.setText(mStudentList.get(position).getSex());
        holder.tvAge.setText(mStudentList.get(position).getAge()+"");
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"name的点击事件",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStudentList == null ? 0 : mStudentList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private TextView tvSex;
        private TextView tvAge;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = ((TextView) itemView.findViewById(R.id.tv_name));
            tvSex = ((TextView) itemView.findViewById(R.id.tv_sex));
            tvAge = ((TextView) itemView.findViewById(R.id.tv_age));
        }
    }
    public void append(List<Student> list){
        mStudentList.addAll(list);
        notifyDataSetChanged();
    }

}
