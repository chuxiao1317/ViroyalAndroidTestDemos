package com.chuxiao.dialogfragmenttest.dialogFragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chuxiao.dialogfragmenttest.R;

public class MyDialogFragment extends DialogFragment {

    public static MyDialogFragment newInstance(int num) {
        MyDialogFragment DialogFragment = new MyDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("num", num);

        DialogFragment.setArguments(bundle);
        return DialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment, container, false);
        return view;
    }
}
