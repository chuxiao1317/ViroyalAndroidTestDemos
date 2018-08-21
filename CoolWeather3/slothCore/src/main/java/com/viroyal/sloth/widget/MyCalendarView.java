package com.viroyal.sloth.widget;

/**
 * Created by shengpeng on 2015-09-25.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sloth.core.R;
import com.viroyal.sloth.util.DensityUtil;
import com.viroyal.sloth.util.Slog;

import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * Calendar widget
 */
public class MyCalendarView extends View implements View.OnTouchListener {
    private final static String TAG = "MyCalendarView";

    private final Context mContext;
    private Date mSelectedStartDate;
    private Date mSelectedEndDate;
    private Date mCurDate; // 当前日历显示的月
    private Date mToday; // 今天的日期文字显示红色
    private Date mPressedDate; // 手指按下状态时临时日期
    private Date showFirstDate, showLastDate; // 日历显示的第一个日期和最后一个日期
    private int mPressedDateIndex; // 按下的格子索引
    private Calendar mCalendar;
    private Surface mSurface;
    private final int[] mDateArray = new int[42]; // 日历显示数字
    private int mCurMonthStartIndex, mCurMonthEndIndex; // 当前显示的日历起始的索引
    private boolean completed = false; // 为false表示只选择了开始日期，true表示结束日期也选择了
    private final String[] mWeekText = new String[7];
    public String[] mMonthText;

    private Bitmap mTodayBgBitmap;
    float baseTextSize = 0;
    //给控件设置监听事件
    private OnDateSelectedListener mOnDateSelectedListener;
    private OnMonthChangeListener mOnMonthChangeListener;

    public MyCalendarView(Context context) {
        this(context, null);
    }

    public MyCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        Slog.d(TAG, "init");
        mCurDate = mSelectedStartDate = mSelectedEndDate = mToday = new Date();
        mCalendar = Calendar.getInstance();
        mCalendar.setTime(mCurDate);
        mSurface = new Surface(attrs);
        mSurface.density = getResources().getDisplayMetrics().density;
        mSurface.width = getWidth();
        mSurface.height = getHeight();
        if (getBackground() != null) {
        } else {
            setBackgroundColor(mSurface.bgColor);
        }
        setOnTouchListener(this);
        setWeekdayInfo();
        requestLayout();
    }

    public void setDate(Date date) {
        if (date != null) {
            mCurDate = mSelectedStartDate = mSelectedEndDate = date;
            mCalendar.setTime(mCurDate);
            setWeekdayInfo();
            requestLayout();
        }
    }

    public void clear() {
        Slog.d(TAG, "clear");
        init(null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Slog.d(TAG, "onMeasure");
        //mSurface.width = getResources().getDisplayMetrics().widthPixels;
        //mSurface.height = (int) (getResources().getDisplayMetrics().heightPixels*2/5);
        if (View.MeasureSpec.getMode(widthMeasureSpec) == View.MeasureSpec.EXACTLY) {
            mSurface.width = View.MeasureSpec.getSize(widthMeasureSpec);
        }
        if (View.MeasureSpec.getMode(heightMeasureSpec) == View.MeasureSpec.EXACTLY) {
            mSurface.height = View.MeasureSpec.getSize(heightMeasureSpec);
        }
//        widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(mSurface.width,
//                View.MeasureSpec.EXACTLY);
//        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(mSurface.height,
//                View.MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        Slog.d(TAG, "[onLayout] changed:"
                + (changed ? "new size" : "not change") + " left:" + left
                + " top:" + top + " right:" + right + " bottom:" + bottom);
        //if (changed) {
        mSurface.init();
        //}
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Slog.d(TAG, "onDraw");
        // Draw lines
        boolean mShowLine = false;
        if (mShowLine) {
            canvas.drawPath(mSurface.boxPath, mSurface.borderPaint);
        }
        // 年月
        String monthText = getSelectedDateStr(getContext());
        float textWidth = mSurface.monthPaint.measureText(monthText);
        Rect r = new Rect();
        mSurface.monthPaint.getTextBounds(monthText, 0, monthText.length() - 1, r);
        canvas.drawText(monthText, (mSurface.width - textWidth) / 2f,
                mSurface.monthHeight * 1 / 2f - r.top - r.height() / 2, mSurface.monthPaint);

        // 上一月/下一月
        canvas.drawPath(mSurface.preMonthBtnPath, mSurface.monthChangeBtnPaint);
        canvas.drawPath(mSurface.nextMonthBtnPath, mSurface.monthChangeBtnPaint);

        // 星期
        float weekTextY = mSurface.monthHeight + mSurface.weekHeight * 3 / 4f;
        // 星期背景
//        mSurface.cellBgPaint.setColor(mSurface.weekBgColor);
//        canvas.drawRect(0, mSurface.monthHeight, mSurface.width, mSurface.monthHeight + mSurface.weekHeight, mSurface.cellBgPaint);

        for (int i = 0; i < mWeekText.length; i++) {
            float weekTextX = i
                    * mSurface.cellWidth
                    + (mSurface.cellWidth - mSurface.weekPaint
                    .measureText(mWeekText[i])) / 2f;
            canvas.drawText(mWeekText[i], weekTextX, weekTextY,
                    mSurface.weekPaint);
        }

        // 计算日期
        calculateDate();
        // 按下状态，选择状态背景色
        drawDownOrSelectedBg(canvas);
        // write mDateArray number
        // mToday index
        int todayIndex = -1;
        mCalendar.setTime(mCurDate);
        String curYearAndMonth = mCalendar.get(Calendar.YEAR) + ""
                + mCalendar.get(Calendar.MONTH);
        mCalendar.setTime(mToday);
        String mTodayYearAndMonth = mCalendar.get(Calendar.YEAR) + ""
                + mCalendar.get(Calendar.MONTH);
        if (curYearAndMonth.equals(mTodayYearAndMonth)) {
            int mTodayNumber = mCalendar.get(Calendar.DAY_OF_MONTH);
            todayIndex = mCurMonthStartIndex + mTodayNumber - 1;
        }

        for (int i = 0; i < 42; i++) {
            int color = mSurface.textColor;
            if (isLastMonth(i)) {
                color = mSurface.borderColor;
            } else if (isNextMonth(i)) {
                color = mSurface.borderColor;
            }
            if (todayIndex != -1 && i == todayIndex) {
                color = mSurface.mTodayNumberColor;
                // beck add
                drawTodayBg(canvas, todayIndex, mTodayBgBitmap);
            } else if (todayIndex != -1 && i > todayIndex) { // 今天之后的显示会灰色
                color = mSurface.borderColor;
            }
            drawCellText(canvas, i, mDateArray[i] + "", color);
        }
        super.onDraw(canvas);
    }

    private void calculateDate() {
        mCalendar.setTime(mCurDate);
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int dayInWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
        Slog.d(TAG, "day in week:" + dayInWeek);
        int monthStart = dayInWeek;
        if (monthStart == 1) {
            monthStart = 8;
        }
        monthStart -= 1;  //以日为开头-1，以星期一为开头-2
        mCurMonthStartIndex = monthStart;
        mDateArray[monthStart] = 1;
        // last month
        if (monthStart > 0) {
            mCalendar.set(Calendar.DAY_OF_MONTH, 0);
            int dayInmonth = mCalendar.get(Calendar.DAY_OF_MONTH);
            for (int i = monthStart - 1; i >= 0; i--) {
                mDateArray[i] = dayInmonth;
                dayInmonth--;
            }
            mCalendar.set(Calendar.DAY_OF_MONTH, mDateArray[0]);
        }
        showFirstDate = mCalendar.getTime();
        // this month
        mCalendar.setTime(mCurDate);
        mCalendar.add(Calendar.MONTH, 1);
        mCalendar.set(Calendar.DAY_OF_MONTH, 0);
        // Log.d(TAG, "m:" + mCalendar.get(Calendar.MONTH) + " d:" +
        // mCalendar.get(Calendar.DAY_OF_MONTH));
        int monthDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        for (int i = 1; i < monthDay; i++) {
            mDateArray[monthStart + i] = i + 1;
        }
        mCurMonthEndIndex = monthStart + monthDay;
        // next month
        for (int i = monthStart + monthDay; i < 42; i++) {
            mDateArray[i] = i - (monthStart + monthDay) + 1;
        }
        if (mCurMonthEndIndex < 42) {
            // 显示了下一月的
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        mCalendar.set(Calendar.DAY_OF_MONTH, mDateArray[41]);
        showLastDate = mCalendar.getTime();
    }

    /**
     * @param canvas
     * @param index
     * @param text
     */
    private void drawCellText(Canvas canvas, int index, String text, int color) {
        int x = getXByIndex(index);
        int y = getYByIndex(index);
        mSurface.datePaint.setColor(color);
        //float cellY = mSurface.monthHeight + mSurface.weekHeight + (y - 1)
        //        * mSurface.cellHeight + mSurface.cellHeight * 3 / 4f;
        float cellX = (mSurface.cellWidth * (x - 1))
                + (mSurface.cellWidth - mSurface.datePaint.measureText(text))
                / 2f;

        float cellY = mSurface.monthHeight + mSurface.weekHeight + (y - 1) * mSurface.cellHeight;

        Rect bounds = new Rect();
        mSurface.datePaint.getTextBounds(text, 0, text.length(), bounds);
        //Paint.FontMetricsInt fontMetrics = mSurface.datePaint.getFontMetricsInt();
        //float baseline = (mSurface.cellHeight / 2 - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;

        // 计算Baseline绘制的Y坐标
        float baseline = (int) ((mSurface.cellHeight / 2) - ((mSurface.datePaint.descent() + mSurface.datePaint.ascent()) / 2));

        //if (index == 0 ) {
        //Slog.d("beck", "drawCellText baseline:"+baseline);
        //Slog.d("beck", "drawCellText fontMetrics bottom:"+fontMetrics.bottom);
        //Slog.d("beck", "drawCellText fontMetrics top:"+fontMetrics.top);
        //Slog.d("beck", "drawCellText cellHeight:"+mSurface.cellHeight);

        //drawCellBg(canvas, index, Color.GREEN);
        //}

        canvas.drawText(text, cellX, cellY + baseline, mSurface.datePaint);
    }

    /**
     * @param canvas
     * @param index
     * @param color
     */
    private void drawCellBg(Canvas canvas, int index, int color) {
        int x = getXByIndex(index);
        int y = getYByIndex(index);
        mSurface.cellBgPaint.setColor(color);
        float left = mSurface.cellWidth * (x - 1) + mSurface.borderWidth;
        float top = mSurface.monthHeight + mSurface.weekHeight + (y - 1)
                * mSurface.cellHeight + mSurface.borderWidth;
        canvas.drawRect(left, top, left + mSurface.cellWidth
                - mSurface.borderWidth, top + mSurface.cellHeight
                - mSurface.borderWidth, mSurface.cellBgPaint);
    }

    private void drawTodayBg(Canvas canvas, int index, Bitmap bitmap) {
        int x = getXByIndex(index);
        int y = getYByIndex(index);
        //mSurface.cellBgPaint.setColor(color);
        float left = mSurface.cellWidth * (x - 1) + mSurface.borderWidth;
        float top = mSurface.monthHeight + mSurface.weekHeight + (y - 1)
                * mSurface.cellHeight + mSurface.borderWidth;
        /*canvas.drawRect(left, top, left + mSurface.cellWidth
                - mSurface.borderWidth, top + mSurface.cellHeight
                - mSurface.borderWidth, mSurface.cellBgPaint);*/

        int bitWidth = bitmap.getWidth();
        int bitHeight = bitWidth;//bitmap.getHeight();

        Rect src = new Rect();
        Rect dst = new Rect();
        if (bitWidth > mSurface.cellWidth || bitHeight > mSurface.cellHeight) {
            dst.left = (int) left + (int) (bitHeight - mSurface.cellHeight) / 5;
            dst.top = (int) top - (int) (bitHeight - mSurface.cellHeight) / 2;
            dst.right = dst.left + (int) mSurface.cellWidth - (int) (bitHeight - mSurface.cellHeight) / 3;
            dst.bottom = dst.top + (int) mSurface.cellHeight + (int) (bitHeight - mSurface.cellHeight);
        } else {
            dst.left = (int) left + (int) (mSurface.cellWidth - bitWidth) / 2;
            dst.top = (int) top + (int) (mSurface.cellHeight - bitHeight) / 2;
            dst.right = dst.left + bitWidth;
            dst.bottom = dst.top + bitHeight;
        }
        canvas.drawBitmap(bitmap, null, dst, mSurface.cellBgPaint);
    }

    private void drawDownOrSelectedBg(Canvas canvas) {
        // down and not up
        /*点击效果不需要*/
        /*if (mPressedDate != null) {
            drawCellBg(canvas, mPressedDateIndex, mSurface.cellDownColor);
        }*/
        // selected bg color
        if (!mSelectedEndDate.before(showFirstDate)
                && !mSelectedStartDate.after(showLastDate)) {
            int[] section = new int[]{-1, -1};
            mCalendar.setTime(mCurDate);
            mCalendar.add(Calendar.MONTH, -1);
            findSelectedIndex(0, mCurMonthStartIndex, mCalendar, section);
            if (section[1] == -1) {
                mCalendar.setTime(mCurDate);
                findSelectedIndex(mCurMonthStartIndex, mCurMonthEndIndex, mCalendar, section);
            }
            if (section[1] == -1) {
                mCalendar.setTime(mCurDate);
                mCalendar.add(Calendar.MONTH, 1);
                findSelectedIndex(mCurMonthEndIndex, 42, mCalendar, section);
            }
            if (section[0] == -1) {
                section[0] = 0;
            }
            if (section[1] == -1) {
                section[1] = 41;
            }
            for (int i = section[0]; i <= section[1]; i++) {
                // beck mark

                if (mSurface.cellSelectedBgRes != 0) {
                    Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), mSurface.cellSelectedBgRes);
                    drawTodayBg(canvas, i, bitmap);
                } else {
                    drawCellBg(canvas, i, mSurface.cellSelectedColor);
                }
            }
        }
    }

    private void findSelectedIndex(int startIndex, int endIndex,
                                   Calendar mCalendar, int[] section) {
        for (int i = startIndex; i < endIndex; i++) {
            mCalendar.set(Calendar.DAY_OF_MONTH, mDateArray[i]);
            Date temp = mCalendar.getTime();
            // Log.d(TAG, "temp:" + temp.toLocaleString());
            if (temp.compareTo(mSelectedStartDate) == 0) {
                section[0] = i;
            }
            if (temp.compareTo(mSelectedEndDate) == 0) {
                section[1] = i;
                return;
            }
        }
    }

    public Date getSelectedStartDate() {
        return mSelectedStartDate;
    }

    public Date getSelectedEndDate() {
        return mSelectedEndDate;
    }

    private boolean isLastMonth(int i) {
        return i < mCurMonthStartIndex;
    }

    private boolean isNextMonth(int i) {
        return i >= mCurMonthEndIndex;
    }

    private int getXByIndex(int i) {
        return i % 7 + 1; // 1 2 3 4 5 6 7
    }

    private int getYByIndex(int i) {
        return i / 7 + 1; // 1 2 3 4 5 6
    }

    /**
     * This moves to previous month
     */
    public String goToPreMonth(Context context) {
        mCalendar.setTime(mCurDate);
        mCalendar.add(Calendar.MONTH, -1);
        mCurDate = mCalendar.getTime();
        invalidate();
        return getSelectedDateStr(context);
    }

    /**
     * 是否是当前月
     *
     * @return
     */
    public boolean isCurrentMonth() {
        mCalendar.setTime(mCurDate);
        String curYearAndMonth = mCalendar.get(Calendar.YEAR) + ""
                + mCalendar.get(Calendar.MONTH);
        mCalendar.setTime(mToday);
        String mTodayYearAndMonth = mCalendar.get(Calendar.YEAR) + ""
                + mCalendar.get(Calendar.MONTH);
        return curYearAndMonth.equals(mTodayYearAndMonth);
    }

    /**
     * This moves to Next month
     */
    public String goToNextMonth(Context context) {
        mCalendar.setTime(mCurDate);
        mCalendar.add(Calendar.MONTH, 1);
        mCurDate = mCalendar.getTime();
        invalidate();
        return getSelectedDateStr(context);
    }

    /**
     * This moves to previous day
     */
    public String goToPreDay(Context context) {
        mCalendar.setTime(mCurDate);
        mCalendar.add(Calendar.DATE, -1);
        mCurDate = mCalendar.getTime();
        invalidate();
        return getSelectedDateStr(context);
    }

    /**
     * This moves to Next day
     */
    public String goToNextDay(Context context) {
        mCalendar.setTime(mCurDate);
        mCalendar.add(Calendar.DATE, 1);
        mCurDate = mCalendar.getTime();
        invalidate();
        return getSelectedDateStr(context);
    }

    private void setSelectedDateByCoor(float x, float y) {
        //change month
        if (y < mSurface.monthHeight) {
            // pre month
            if (x < mSurface.monthChangeWidth) {
                mCalendar.setTime(mCurDate);
                mCalendar.add(Calendar.MONTH, -1);
                mCurDate = mCalendar.getTime();
                if (null != mOnMonthChangeListener) {
                    mOnMonthChangeListener.onMonthChange(mCurDate);
                }
            }
            // next month
            else if (x > mSurface.width - mSurface.monthChangeWidth) {
                mCalendar.setTime(mCurDate);
                mCalendar.add(Calendar.MONTH, 1);
                mCurDate = mCalendar.getTime();
                if (null != mOnMonthChangeListener) {
                    mOnMonthChangeListener.onMonthChange(mCurDate);
                }
            }
        }
        // cell click down
        if (y > mSurface.monthHeight + mSurface.weekHeight) {
            int m = (int) (Math.floor(x / mSurface.cellWidth) + 1);
            int n = (int) (Math
                    .floor((y - (mSurface.monthHeight + mSurface.weekHeight))
                            / mSurface.cellHeight) + 1);
            mPressedDateIndex = (n - 1) * 7 + m - 1;
            Slog.d(TAG, "mPressedDateIndex:" + mPressedDateIndex);
            mCalendar.setTime(mCurDate);
            if (isLastMonth(mPressedDateIndex)) {
                mCalendar.add(Calendar.MONTH, -1);
            } else if (isNextMonth(mPressedDateIndex)) {
                mCalendar.add(Calendar.MONTH, 1);
            }
            mCalendar.set(Calendar.DAY_OF_MONTH, mDateArray[mPressedDateIndex]);
            mPressedDate = mCalendar.getTime();
        }
        invalidate();
    }

    private void setWeekdayInfo() {
        Slog.d(TAG, "setWeekdayInfo()");
        int mStartDay = Calendar.SUNDAY;
        int diff = mStartDay - Calendar.SUNDAY - 1;
        String dayString = DateUtils.getDayOfWeekString(
                (Calendar.SUNDAY + diff) % 7 + 1,
                DateUtils.LENGTH_SHORTEST);
        mWeekText[0] = dayString;
        dayString = DateUtils.getDayOfWeekString(
                (Calendar.MONDAY + diff) % 7 + 1,
                DateUtils.LENGTH_SHORTEST);
        mWeekText[1] = dayString;
        dayString = DateUtils.getDayOfWeekString(
                (Calendar.TUESDAY + diff) % 7 + 1,
                DateUtils.LENGTH_SHORTEST);
        mWeekText[2] = dayString;
        dayString = DateUtils.getDayOfWeekString(
                (Calendar.WEDNESDAY + diff) % 7 + 1,
                DateUtils.LENGTH_SHORTEST);
        mWeekText[3] = dayString;
        dayString = DateUtils.getDayOfWeekString(
                (Calendar.THURSDAY + diff) % 7 + 1,
                DateUtils.LENGTH_SHORTEST);
        mWeekText[4] = dayString;
        dayString = DateUtils.getDayOfWeekString(
                (Calendar.FRIDAY + diff) % 7 + 1,
                DateUtils.LENGTH_SHORTEST);
        mWeekText[5] = dayString;
        dayString = DateUtils.getDayOfWeekString(
                (Calendar.SATURDAY + diff) % 7 + 1,
                DateUtils.LENGTH_SHORTEST);
        mWeekText[6] = dayString;
    }

    public String getSelectedDateStr(Context context) {
        Slog.d(TAG, "getSelectedDateStr()");
        int flags = DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_NO_MONTH_DAY;
        Time time = new Time();
        time.set(System.currentTimeMillis());
        String tz;
        if ((flags & DateUtils.FORMAT_UTC) != 0) {
            tz = Time.TIMEZONE_UTC;
        } else {
            tz = Time.getCurrentTimezone();
        }

        StringBuilder mSB = new StringBuilder(50);
        Formatter mF = new Formatter(mSB, Locale.getDefault());
        long millis = mCurDate.getTime();

        return DateUtils.formatDateRange(context, mF, millis, millis, flags,
                tz).toString();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setSelectedDateByCoor(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                if (mPressedDate != null) {
                    if (!completed) {
                        if (mPressedDate.before(mSelectedStartDate)) {
                            mSelectedEndDate = mSelectedStartDate;
                            mSelectedStartDate = mPressedDate;
                        } else {
                            mSelectedEndDate = mPressedDate;
                        }
                        completed = true;
                    } else {
                        mSelectedStartDate = mSelectedEndDate = mPressedDate;
                        completed = false;
                    }
                    mSelectedStartDate = mSelectedEndDate = mPressedDate;
                    mOnDateSelectedListener.onDateTapped(mSelectedStartDate);
                    mPressedDate = null;
                    invalidate();
                }
                break;
        }
        return true;
    }

    public void setOnDateSelectedListener(OnDateSelectedListener dateSelectedListener) {
        this.mOnDateSelectedListener = dateSelectedListener;
    }
    public void setOnMonthChangeListener(OnMonthChangeListener monthChangeListener){
        this.mOnMonthChangeListener = monthChangeListener;
    }

    /**
     * The callback used to indicate the user tapped a date
     */
    public interface OnDateSelectedListener {
        void onDateTapped(Date date);
    }
    /**
     * The callback used to indicate the user tapped a Month
     */
    public interface OnMonthChangeListener {
        void onMonthChange(Date date);
    }

    /**
     * 1. 布局尺寸 2. 文字颜色，大小 3. 当前日期的颜色，选择的日期颜色
     */
    private class Surface {
        public float density;
        public int width; // 整个控件的宽度
        public int height; // 整个控件的高度
        public float monthHeight; // 显示月的高度
        public float monthChangeWidth; // 上一月、下一月按钮宽度
        public float weekHeight; // 显示星期的高度
        public float cellWidth; // 日期方框宽度
        public float cellHeight; // 日期方框高度
        public float borderWidth;
        public final int bgColor = Color.parseColor("#FFFFFF");
        public int bgRes = 0;
        public final int weekBgColor = Color.parseColor("#e3e6e6");
        private int textColor = Color.BLACK;
        //private int textColorUnimportant = Color.parseColor("#666666");
        private final int btnColor = Color.parseColor("#666666");
        private final int borderColor = Color.parseColor("#CCCCCC");
        public int mTodayNumberColor = Color.WHITE;

        public final int cellDownColor = Color.parseColor("#CCFFFF");
        public final int cellSelectedColor = Color.parseColor("#99CCFF");
        public int cellSelectedBgRes = 0;
        public Paint borderPaint = new Paint();
        public final Paint monthPaint = new Paint();
        public final Paint weekPaint = new Paint();
        public final Paint datePaint = new Paint();
        public Paint monthChangeBtnPaint = new Paint();
        public Paint cellBgPaint = new Paint();
        public final Path boxPath = new Path(); // 边框路径
        public Path preMonthBtnPath; // 上一月按钮三角形
        public Path nextMonthBtnPath; // 下一月按钮三角形

        public Surface(AttributeSet attrs) {
            TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.MyCalendarView);
            baseTextSize = typedArray.getDimensionPixelSize(
                    R.styleable.MyCalendarView_CalendarBaseTextSizes,
                    DensityUtil.dip2px(mContext, 15));
            int calendarChoseBgRes = typedArray.getResourceId(R.styleable.MyCalendarView_CalendarChoseBg, 0);
            cellSelectedBgRes = calendarChoseBgRes;
            BitmapFactory.Options options = new BitmapFactory.Options();
            int calendarTodayBgRes = typedArray.getResourceId(R.styleable.MyCalendarView_CalendarTodayBg, 0);
            if (calendarTodayBgRes != 0) {
                mTodayBgBitmap = BitmapFactory.decodeResource(mContext.getResources(), calendarTodayBgRes, options);
            } else {
                mTodayBgBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_calendar_today_bg, options);
            }

            float hWScale = typedArray.getFloat(R.styleable.MyCalendarView_CalendarHWScale, 1f);
            int textColorRes = typedArray.getResourceId(R.styleable.MyCalendarView_CalendarTextColor, 0);
            if (textColorRes != 0) {
                textColor = mContext.getResources().getColor(textColorRes);
                mTodayNumberColor = textColor;
            }
            int textColorFadeRes = typedArray.getResourceId(R.styleable.MyCalendarView_CalendarTextColorFade, 0);
            int preImageRes = typedArray.getResourceId(R.styleable.MyCalendarView_CalendarPreImageRes, 0);
            int nextImageRes = typedArray.getResourceId(R.styleable.MyCalendarView_CalendarNextImageRes, 0);
            typedArray.recycle();
        }

        public void init() {
            Slog.d(TAG, "[Surface]init");
            float temp = height / 7f;
            monthHeight = (float) ((temp + temp * 0.3f) * 0.6);
            monthChangeWidth = monthHeight * 1.5f;
            weekHeight = (float) ((temp + temp * 0.3f) * 0.7);
            cellHeight = (height - monthHeight - weekHeight) / 6f;
            cellWidth = width / 7f;
            borderPaint = new Paint();
            borderPaint.setColor(borderColor);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderWidth = (float) (0.5 * density);
            // Log.d(TAG, "borderwidth:" + borderWidth);
            borderWidth = borderWidth < 1 ? 1 : borderWidth;
            borderPaint.setStrokeWidth(borderWidth);
            monthPaint.setColor(textColor);
            monthPaint.setAntiAlias(true);
            float textSize = baseTextSize * 1.1f;
            Slog.d(TAG, "text size:" + textSize);
            monthPaint.setTextSize(textSize);
//            monthPaint.setTypeface(Typeface.DEFAULT_BOLD);
            weekPaint.setColor(textColor);
            weekPaint.setAntiAlias(true);
            float weekTextSize = baseTextSize * 1.0f;
            weekPaint.setTextSize(weekTextSize);
            //weekPaint.setTypeface(Typeface.DEFAULT_BOLD);
            datePaint.setColor(textColor);
            datePaint.setAntiAlias(true);
            float cellTextSize = baseTextSize * 1.0f;
            datePaint.setTextSize(cellTextSize);
            //datePaint.setTypeface(Typeface.DEFAULT_BOLD);
            //boxPath.addRect(0, 0, width, height, Direction.CW);
            //boxPath.moveTo(0, monthHeight);
            boxPath.rLineTo(width, 0);
            boxPath.moveTo(0, monthHeight + weekHeight);
            boxPath.rLineTo(width, 0);
            for (int i = 1; i < 6; i++) {
                boxPath.moveTo(0, monthHeight + weekHeight + i * cellHeight);
                boxPath.rLineTo(width, 0);
                boxPath.moveTo(i * cellWidth, monthHeight);
                boxPath.rLineTo(0, height - monthHeight);
            }
            boxPath.moveTo(6 * cellWidth, monthHeight);
            boxPath.rLineTo(0, height - monthHeight);
            preMonthBtnPath = new Path();
            int btnHeight = (int) (monthHeight * 0.6f);
            preMonthBtnPath.moveTo(monthChangeWidth / 2f, monthHeight / 2f);
            preMonthBtnPath.rLineTo(btnHeight / 2f, -btnHeight / 2f);
            preMonthBtnPath.rLineTo(0, btnHeight);
            preMonthBtnPath.close();
            nextMonthBtnPath = new Path();
            nextMonthBtnPath.moveTo(width - monthChangeWidth / 2f,
                    monthHeight / 2f);
            nextMonthBtnPath.rLineTo(-btnHeight / 2f, -btnHeight / 2f);
            nextMonthBtnPath.rLineTo(0, btnHeight);
            nextMonthBtnPath.close();
            monthChangeBtnPaint = new Paint();
            monthChangeBtnPaint.setAntiAlias(true);
            monthChangeBtnPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            monthChangeBtnPaint.setColor(btnColor);
            cellBgPaint = new Paint();
            cellBgPaint.setAntiAlias(true);
            cellBgPaint.setStyle(Paint.Style.FILL);
            cellBgPaint.setColor(cellSelectedColor);
        }
    }
}
