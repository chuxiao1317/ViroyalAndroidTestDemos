package com.chuxiao.androidwidgetstest.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chuxiao.androidwidgetstest.R;
import com.chuxiao.androidwidgetstest.entity.Fruit;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    private Context mContext;// 在构造方法中初始化此上下文
    private int resourcesId;
    private List<Fruit> mFruitList;

    public ListViewAdapter(@NonNull Context context, int textViewResourcesId, @NonNull List<Fruit> objects) {
        resourcesId = textViewResourcesId;
        mContext = context;
        mFruitList = objects;
    }

    @Override
    public int getCount() {
        return mFruitList == null ? 0 : mFruitList.size();
    }

    @Override
    public Fruit getItem(int position) {
        return mFruitList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Fruit fruit = getItem(position);
        ViewHolder viewHolder;
        // 如果convertView不为null则对convertView进行重用
        if (convertView == null) {
            // 加载布局
            convertView = LayoutInflater.from(mContext).inflate(resourcesId, parent, false);
            //通过构造函数初始化ViewHolder
            viewHolder = new ViewHolder(convertView);
            //将viewHolder存储在view中
            convertView.setTag(viewHolder);
        } else {
//            convertView = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.fruitImage.setImageResource(fruit.getImageId());
        viewHolder.fruitName.setText(fruit.getName());
        /**
         * 设置fruitImage的点击事件
         * */
        viewHolder.fruitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "fruitImage的点击事件", Toast.LENGTH_SHORT).show();
            }
        });
//        ImageView fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
//        TextView fruitName = (TextView) view.findViewById(R.id.fruit_name);
//        fruitImage.setImageResource(fruit.getImageId());
//        fruitName.setText(fruit.getName());
        return convertView;
    }

    /**
     * 借助viewHolder避免每次getView都findViewById
     */
    private class ViewHolder {
        private ImageView fruitImage;

        private TextView fruitName;

        //通过构造函数初始化ViewHolder
        private ViewHolder(View convertView) {
            this.fruitImage = (ImageView) convertView.findViewById(R.id.fruit_image);
            this.fruitName = (TextView) convertView.findViewById(R.id.fruit_name);
        }
    }
}
