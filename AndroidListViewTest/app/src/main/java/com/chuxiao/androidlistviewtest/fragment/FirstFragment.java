package com.chuxiao.androidlistviewtest.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chuxiao.androidlistviewtest.R;

/**
 * Created by 12525 on 2018/4/12.
 */

public class FirstFragment extends Fragment {
    public static final String TAG = FirstFragment.class.getSimpleName();
    public String stats = "bbb";

    public interface FragmentInface {
        void result(String type);
    }

    public FragmentInface fragmentInface;

    public void setFragmentInface(FragmentInface fragmentInface) {
        this.fragmentInface = fragmentInface;
    }

    public FirstFragment() {

    }

    public void setParmas(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        setArguments(bundle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        Bundle bundle = getArguments();
        String type = bundle.getString("type");
        Log.d("type", type);

        //Fragment与Activity通信
        if (fragmentInface != null) {
            fragmentInface.result(stats);
        }
//        ((Fragment1Activity) getActivity()).getStatus(stats);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_first, null);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach");
        super.onDetach();
    }
}
