package com.chuxiao.androidwidgetstest.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chuxiao.androidwidgetstest.R;

public class LeftFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.left_fragment, container, false);
        return view;
    }

    public void setType(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        setArguments(bundle);
    }
}
