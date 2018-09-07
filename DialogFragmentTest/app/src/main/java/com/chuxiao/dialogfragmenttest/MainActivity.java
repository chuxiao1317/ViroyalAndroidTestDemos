package com.chuxiao.dialogfragmenttest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chuxiao.dialogfragmenttest.dialogFragment.MyDialogFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showDialogFragment(View view) {
        MyDialogFragment dialogFragment = new MyDialogFragment();
        dialogFragment.show(getFragmentManager(), "MyDialogFragment");
    }

    public void clickToShowDialogFragment(View view) {
        showDialogFragment(view);
    }

    public void clickToShowDialog(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("你的名字");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }
}
