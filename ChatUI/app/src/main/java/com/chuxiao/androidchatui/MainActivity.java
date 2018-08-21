package com.chuxiao.androidchatui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chuxiao.androidchatui.adapter.MsgRecyclerViewAdapter;
import com.chuxiao.androidchatui.enrity.Msg;
import com.chuxiao.androidchatui.util.ScreenUtils;
import com.chuxiao.androidchatui.util.SoftHideKeyBoardUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Msg> msgList = new ArrayList<>();

    private EditText inputText;

    private Button send;

    private RecyclerView msgRecyclerView;

    private MsgRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View toolbar = findViewById(R.id.toolbar);
        toolbar.setFitsSystemWindows(true);
        toolbar.setPadding(0, ScreenUtils.getStatusHeight(this), 0, 0);
        initMsgs();
        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.sendButton);
        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgRecyclerViewAdapter(msgList);
        msgRecyclerView.scrollToPosition(msgList.size() - 1);
        //解决键盘档住输入框、聊天内容的问题
        SoftHideKeyBoardUtil.assistActivity(this);
        msgRecyclerView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    msgList.add(new Msg(content, Msg.TYPE_SEND));
                    //当有新消息时，刷新RecyclerView中的显示
                    adapter.notifyItemInserted(msgList.size() - 1);
                    // 将RecyclerView定位到最后一行
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);
                    //清空输入框中的内容
                    inputText.setText("");
                }
            }
        });
//        inputText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    SoftHideKeyBoardUtil.assistActivity(MainActivity.this);
//                }
//            }
//        });
    }


    private void initMsgs() {
        msgList.add(new Msg("你好，伙计！", Msg.TYPE_RECEIVED));
        msgList.add(new Msg("你好，朋友！吃饭了吗？", Msg.TYPE_SEND));
        msgList.add(new Msg("还没呢，在写安卓......", Msg.TYPE_RECEIVED));
        msgList.add(new Msg("写安卓好玩吗？", Msg.TYPE_SEND));
        msgList.add(new Msg("不好玩", Msg.TYPE_RECEIVED));
        msgList.add(new Msg("那我们去看书吧", Msg.TYPE_SEND));
        msgList.add(new Msg("走起！", Msg.TYPE_RECEIVED));
    }

}
