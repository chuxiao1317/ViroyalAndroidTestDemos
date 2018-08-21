package com.chuxiao.game2048.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.GridLayout;

import com.chuxiao.game2048.activity.MainActivity;
import com.chuxiao.game2048.bean.GameItemView;
import com.chuxiao.game2048.config.Config;

import java.util.ArrayList;
import java.util.List;

public class GameView extends GridLayout implements View.OnTouchListener {

    private MainActivity mMainActivity = new MainActivity();

    // GameView对应矩阵
    private GameItemView[][] mGameMatrix;
    // 空格List
    private List<Point> mBlanks;
    // 矩阵行列数
    private int mGameLines;
    // 记录坐标
    private int mStartX,
            mStartY,
            mEndX,
            mEndY;
    // 辅助数组
    private List<Integer> mCalList;
    private int mKeyItemNum = -1;
    // 历史记录数组
    private int[][] mGameMatrixHistory;
    // 历史记录分数
    private int mScoreHistory;
    // 最高纪录
    private int mHighestScore;
    // 目标分数
    private int mTarget;

    private Config mConfig = new Config();

    public GameView(Context context) {
        super(context);
        initGameMatrix();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameMatrix();
    }

    public void startGame() {
        initGameMatrix();
        initGameView(mConfig.mItemSize);
    }

    /**
     * 初始化View
     */
    private void initGameMatrix() {
        // 删除所有子View，初始化矩阵
        removeAllViews();
        mScoreHistory = 0;
        Config.score = 0;
        Config.mGameLines = Config.mSp.getInt(Config.keyGameLines, 4);
        mGameLines = Config.mGameLines;
        mGameMatrix = new GameItemView[mGameLines][mGameLines];
        mGameMatrixHistory = new int[mGameLines][mGameLines];
        mCalList = new ArrayList<>();
        mBlanks = new ArrayList<>();
        mHighestScore = Config.mSp.getInt(Config.keyHighestScore, 0);
        setColumnCount(mGameLines);
        setRowCount(mGameLines);
        setOnTouchListener(this);
        // 初始化View参数
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = null;
        if (wm != null) {
            display = wm.getDefaultDisplay();
        }
        if (display != null) {
            display.getMetrics(metrics);
        }
        mConfig.mItemSize = metrics.widthPixels / Config.mGameLines;
        initGameView(mConfig.mItemSize);
    }

    private void initGameView(int cardSize) {
        removeAllViews();
        GameItemView card;
        for (int i = 0; i < mGameLines; i++) {
            for (int j = 0; j < mGameLines; j++) {
                card = new GameItemView(getContext(), 0);
                addView(
                        card,
                        cardSize, // 宽度
                        cardSize  // 高度
                );
                // 初始化GameMatrix全部为0 空格List为所有
                mGameMatrix[i][j] = card;
                mBlanks.add(new Point(i, j));
            }
        }
        // 添加随机数字
        addRandomNum();
        addRandomNum();
    }

    /**
     * 添加随机数字
     */
    private void addRandomNum() {
        getBlanks();
        if (mBlanks.size() > 0) {
            int randomNum = (int) (Math.random() * mBlanks.size());
            Point randomPoint = mBlanks.get(randomNum);
            mGameMatrix[randomPoint.x][randomPoint.y].setNum(Math.random() > 0.2d ? 2 : 4);
            animCreate(mGameMatrix[randomPoint.x][randomPoint.y]);
        }
    }

    /**
     * 获取空格Item数组
     */
    private void getBlanks() {
        mBlanks.clear();
        for (int i = 0; i < mGameLines; i++) {
            for (int j = 0; j < mGameLines; j++) {
                if (mGameMatrix[i][j].getCardShowNum() == 0) {
                    mBlanks.add(new Point(i, j));
                }
            }
        }
    }

    /**
     * 生成动画
     */
    private void animCreate(GameItemView target) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.1f,
                1,
                0.1f,
                1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(100);
        target.setAnimation(null);
        target.getItemTv().startAnimation(scaleAnimation);
    }

    /**
     * 判断游戏是否结束
     * <p>0:结束 1：正常 2：成功
     */
    private void checkCompleted() {
        int result = checkNums();
        if (result == 0) {
            if (Config.score > mHighestScore) {
                SharedPreferences.Editor editor = Config.mSp.edit();
                editor.putInt(Config.keyHighestScore, Config.score);
                editor.apply();
                mMainActivity.getMainActivity().setScore(Config.score, 1);
                Config.score = 0;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("游戏结束").setPositiveButton("重新开始",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 重新开始
                            startGame();
                        }
                    }
            ).create().show();
            Config.score = 0;
        } else if (result == 2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("任务完成").setPositiveButton("重新开始",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 重新开始
                            startGame();
                        }
                    }).setNegativeButton("继续",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 继续游戏 修改目标分数
                            SharedPreferences.Editor editor = Config.mSp.edit();
                            if (mTarget == 1024) {
                                editor.putInt(Config.keyGameGoal, 2048);
                                mTarget = 2048;
                                mMainActivity.getMainActivity().setGoal(2048);
                            } else if (mTarget == 2048) {
                                editor.putInt(Config.keyGameGoal, 4096);
                                mTarget = 4096;
                                mMainActivity.getMainActivity().setGoal(4096);
                            } else {
                                editor.putInt(Config.keyGameGoal, 4096);
                                mTarget = 4096;
                                mMainActivity.getMainActivity().setGoal(4096);
                            }
                            editor.apply();
                        }
                    }).create().show();
            Config.score = 0;
        }
    }

    /**
     * 检测是否存在满足条件的数字
     */
    private int checkNums() {
        getBlanks();
        if (mBlanks.size() == 0) {
            for (int i = 0; i < mGameLines; i++) {
                for (int j = 0; i < mGameLines; j++) {
                    if (j < mGameLines - 1) {
                        if (mGameMatrix[i][j].getCardShowNum() ==
                                mGameMatrix[i][j + 1].getCardShowNum()) {
                            return 1;
                        }
                    }
                    if (i < mGameLines - 1) {
                        if (mGameMatrix[i][j].getCardShowNum() ==
                                mGameMatrix[i][j + 1].getCardShowNum()) {
                            return 1;
                        }
                    }
                }
            }
            return 0;
        }
        for (int i = 0; i < mGameLines; i++) {
            for (int j = 0; j < mGameLines; j++) {
                if (mGameMatrix[i][j].getCardShowNum() == mTarget) {
                    return 2;
                }
            }
        }
        return 1;
    }

    /**
     * 撤销上次移动（只可向后撤销一步）
     */
    public void revertLastSwipe() {
        // 第一次不能撤销
        int sum = 0;
        for (int[] element : mGameMatrixHistory) {
            for (int i : element) {
                sum += i;
            }
        }
        if (sum != 0) {
            mMainActivity.getMainActivity().setScore(mScoreHistory, 0);
            Config.score = mScoreHistory;
            for (int i = 0; i < mGameLines; i++) {
                for (int j = 0; j < mGameLines; j++) {
                    mGameMatrix[i][j].setNum(mGameMatrixHistory[i][j]);
                }
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                saveHistoryMatrix();
                mStartX = (int) event.getX();
                mStartY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                mEndX = (int) event.getX();
                mEndY = (int) event.getY();
                judgeDirection(mEndX - mStartX, mEndY - mStartY);
                if (isMoved()) {
                    addRandomNum();
                    // 修改显示分数
                    mMainActivity.getMainActivity().setScore(Config.score, 0);
                }
                break;
        }
        return true;
    }

    /**
     * 保存历史记录
     */
    private void saveHistoryMatrix() {
        mScoreHistory = Config.score;
        for (int i = 0; i < mGameLines; i++) {
            for (int j = 0; j < mGameLines; j++) {
                mGameMatrixHistory[i][j] = mGameMatrix[i][j].getCardShowNum();
            }
        }
    }

    /**
     * 根据偏移量判断移动方向
     */
    private void judgeDirection(int offsetX, int offsetY) {
        int density = getDeviceDensity();
        int slideDistance = 5 * density;
        int maxDistance = 200 * density;
        boolean flagNormal = (Math.abs(offsetX) > slideDistance || Math.abs(offsetY) > maxDistance) &&
                (Math.abs(offsetX) < maxDistance) &&
                (Math.abs(offsetY) < maxDistance);
        boolean flagSuper = Math.abs(offsetX) > maxDistance ||
                Math.abs(offsetY) > maxDistance;
        if (flagNormal && !flagSuper) {
            if (Math.abs(offsetX) > Math.abs(offsetY)) {
                if (offsetX > slideDistance) {
                    swipeRight();
                } else {
                    swipeLeft();
                }
            } else {
                if (offsetY > slideDistance) {
                    swipeDown();
                } else {
                    swipeUp();
                }
            }
        } else if (flagSuper) {// 启动超级用户权限来添加自定义数字
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            final EditText et = new EditText(getContext());
            builder.setTitle("后门").
                    setView(et).
                    setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (!TextUtils.isEmpty(et.getText())) {
                                        addSuperNum(Integer.parseInt(et.getText().toString()));
                                        checkCompleted();
                                    }
                                }
                            }).setPositiveButton("拜拜",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        }
    }

    /**
     * super模式下添加一个指定数字
     */
    private void addSuperNum(int num) {
        if (checkSuperNum(num)) {
            getBlanks();
            if (mBlanks.size() > 0) {
                int randomNum = (int) (Math.random() * mBlanks.size());
                Point randomPoint = mBlanks.get(randomNum);
                mGameMatrix[randomPoint.x][randomPoint.y].setNum(num);
                animCreate(mGameMatrix[randomPoint.x][randomPoint.y]);
            }
        }
    }

    /**
     * 检查添加的数是否为指定数字
     */
    private boolean checkSuperNum(int num) {
        return num == 2 ||
                num == 4 ||
                num == 8 ||
                num == 16 ||
                num == 32 ||
                num == 64 ||
                num == 128 ||
                num == 256 ||
                num == 512 ||
                num == 1024;
    }

    /**
     * 判断是否移动过（是否需要新增Item）
     */
    private boolean isMoved() {
        for (int i = 0; i < mGameLines; i++) {
            for (int j = 0; j < mGameLines; j++) {
                if (mGameMatrixHistory[i][j] != mGameMatrix[i][j].getCardShowNum()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取屏幕密度
     */
    private int getDeviceDensity() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(displayMetrics);
        }
        return (int) displayMetrics.density;
    }

    /**
     * 向左滑动
     */
    private void swipeLeft() {
        for (int i = mGameLines - 1; i >= 0; i--) {
            for (int j = mGameLines - 1; j >= 0; j--) {
                int currentNum = mGameMatrix[i][j].getCardShowNum();
                if (currentNum != 0) {
                    if (mKeyItemNum == -1) {
                        mKeyItemNum = currentNum;
                    } else {
                        if (mKeyItemNum == currentNum) {
                            mCalList.add(mKeyItemNum * 2);
                            Config.score += mKeyItemNum * 2;
                            mKeyItemNum = -1;
                        } else {
                            mCalList.add(mKeyItemNum);
                            mKeyItemNum = currentNum;
                        }
                    }
                } else {
                    continue;
                }
            }
            if (mKeyItemNum != -1) {
                mCalList.add(mKeyItemNum);
            }
            Log.d("111", mGameMatrix.length + "");
            // 改变Item值
            for (int j = 0; j < mGameLines - mCalList.size(); j++) {
                mGameMatrix[i][j].setNum(mCalList.get(j));
            }
            for (int m = mGameLines - mCalList.size(); m < mGameLines; m++) {
                mGameMatrix[i][m].setNum(0);
            }
            // 重置行参数
            mKeyItemNum = -1;
            mCalList.clear();
        }
    }

    /**
     * 向上滑动
     */
    private void swipeUp() {
        for (int i = mGameLines - 1; i >= 0; i--) {
            for (int j = mGameLines - 1; j >= 0; j--) {
                int currentNum = mGameMatrix[j][i].getCardShowNum();
                if (currentNum != 0) {
                    if (mKeyItemNum == -1) {
                        mKeyItemNum = currentNum;
                    } else {
                        if (mKeyItemNum == currentNum) {
                            mCalList.add(mKeyItemNum * 2);
                            Config.score += mKeyItemNum * 2;
                            mKeyItemNum = -1;
                        } else {
                            mCalList.add(mKeyItemNum);
                            mKeyItemNum = currentNum;
                        }
                    }
                } else {
                    return;
                }
            }
            if (mKeyItemNum != -1) {
                mCalList.add(mKeyItemNum);
            }
            // 改变Item值
            for (int j = 0; j < mGameLines - mCalList.size(); j++) {
                mGameMatrix[j][i].setNum(mCalList.get(j));
            }
            for (int m = mGameLines - mCalList.size(); m < mGameLines; m++) {
                mGameMatrix[m][i].setNum(0);
            }
            // 重置行参数
            mKeyItemNum = -1;
            mCalList.clear();
        }
    }

    /**
     * 向右滑动
     */
    private void swipeRight() {
        for (int i = mGameLines - 1; i >= 0; i--) {
            for (int j = mGameLines - 1; j >= 0; j--) {
                int currentNum = mGameMatrix[i][j].getCardShowNum();
                if (currentNum != 0) {
                    if (mKeyItemNum == -1) {
                        mKeyItemNum = currentNum;
                    } else {
                        if (mKeyItemNum == currentNum) {
                            mCalList.add(mKeyItemNum * 2);
                            Config.score += mKeyItemNum * 2;
                            mKeyItemNum = -1;
                        } else {
                            mCalList.add(mKeyItemNum);
                            mKeyItemNum = currentNum;
                        }
                    }
                } else {
                    return;
                }
            }
            if (mKeyItemNum != -1) {
                mCalList.add(mKeyItemNum);
            }
            // 改变Item值
            for (int j = 0; j < mGameLines - mCalList.size(); j++) {
                mGameMatrix[i][j].setNum(0);
            }
            int index = mCalList.size() - 1;
            for (int m = mGameLines - mCalList.size(); m < mGameLines; m++) {
                mGameMatrix[i][m].setNum(mCalList.get(index));
                index--;
            }
            // 重置行参数
            mKeyItemNum = -1;
            mCalList.clear();
            index = 0;
        }
    }

    /**
     * 向下滑动
     */
    private void swipeDown() {
        for (int i = mGameLines - 1; i >= 0; i--) {
            for (int j = mGameLines - 1; j >= 0; j--) {
                int currentNum = mGameMatrix[j][i].getCardShowNum();
                if (currentNum != 0) {
                    if (mKeyItemNum == -1) {
                        mKeyItemNum = currentNum;
                    } else {
                        if (mKeyItemNum == currentNum) {
                            mCalList.add(mKeyItemNum * 2);
                            Config.score += mKeyItemNum * 2;
                            mKeyItemNum = -1;
                        } else {
                            mCalList.add(mKeyItemNum);
                            mKeyItemNum = currentNum;
                        }
                    }
                } else {
                    return;
                }
            }
            if (mKeyItemNum != -1) {
                mCalList.add(mKeyItemNum);
            }
            // 改变Item值
            for (int j = 0; j < mGameLines - mCalList.size(); j++) {
                mGameMatrix[j][i].setNum(0);
            }
            int index = mCalList.size() - 1;
            for (int m = mGameLines - mCalList.size(); m < mGameLines; m++) {
                mGameMatrix[m][i].setNum(mCalList.get(index));
                index--;
            }
            // 重置行参数
            mKeyItemNum = -1;
            mCalList.clear();
            index = 0;
        }
    }
}
