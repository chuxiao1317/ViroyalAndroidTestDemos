package com.viroyal.sloth.widget.dialog;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.sloth.core.R;
import com.viroyal.sloth.widget.MyCalendarView;

import java.util.Date;

/**
 * Created by shengpeng on 2015-10-07.
 */
public class CalendarDialog extends DialogFragment {

    private Listener mListener;

    public interface Listener {
        void onDateSelected(Date date);
    }

    public static CalendarDialog newInstance(Context context, Listener listener) {
        CalendarDialog f = new CalendarDialog();
        f.mListener = listener;
        return f;
    }

    public static CalendarDialog newInstance(Context context, Date date, Listener listener) {
        CalendarDialog f = new CalendarDialog();
        Bundle b = new Bundle();
        b.putSerializable("date", date);
        f.setArguments(b);
        f.mListener = listener;
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        final View v = getActivity().getLayoutInflater().inflate(R.layout.calendar_view_layout, null);
        if (v == null) {
            return null;
        }

        final MyCalendarView calendarView = (MyCalendarView)v.findViewById(R.id.calendar_view);
        Bundle b = this.getArguments();
        if (b != null) {
            Date date = (Date)this.getArguments().getSerializable("date");
            calendarView.setDate(date);
        }
//        final TextView monthText = (TextView) v.findViewById(R.id.month);
//        final View nextAction = v.findViewById(R.id.next_month);
//        View preAction = v.findViewById(R.id.pre_month);
//        preAction.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                calendarView.goToPreMonth(getActivity());
//                monthText.setText(calendarView.getSelectedDateStr(getActivity()));
//                if (!calendarView.isCurrentMonth()) {
//                    nextAction.setEnabled(true);
//                }
//            }
//        });
//        nextAction.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                calendarView.goToNextMonth(getActivity());
//                monthText.setText(calendarView.getSelectedDateStr(getActivity()));
//                if (calendarView.isCurrentMonth()) {
//                    nextAction.setEnabled(false);
//                }
//            }
//        });
        calendarView.setOnDateSelectedListener(new MyCalendarView.OnDateSelectedListener() {
            @Override
            public void onDateTapped(Date date) {
                mListener.onDateSelected(date);
                CalendarDialog.this.dismiss();
            }
        });
//        monthText.setText(calendarView.getSelectedDateStr(getActivity()));
//        if (calendarView.isCurrentMonth()) {
//            nextAction.setEnabled(false);
//        }

        this.getDialog().setCanceledOnTouchOutside(false);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        int width = getResources().getDimensionPixelSize(R.dimen.dialog_calendar_width);
        int height = getResources().getDimensionPixelSize(R.dimen.dialog_calendar_height);
        window.setLayout(width, height);
        window.setGravity(Gravity.CENTER);
    }

    /*@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return buildDialog(savedInstanceState).create();
    }

    @Override
    public AlertDialog.Builder buildDialog(Bundle savedInstanceState) {
        final Bundle arguments = getArguments();
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity())
                .setView(generateContentView(savedInstanceState));
        return  b;
    }

    @Override
    public View generateContentView(Bundle savedInstanceState) {
        final View v = getActivity().getLayoutInflater().inflate(R.layout.calendar_view_layout, null);
        if (v == null) {
            return v;
        }
        final MyCalendarView calendarView = (MyCalendarView)v.findViewById(R.id.calendar_view);
        final TextView monthText = (TextView) v.findViewById(R.id.month);
        View preAction = v.findViewById(R.id.pre_month);
        preAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.goToPreDay(getActivity());
                monthText.setText(calendarView.getSelectedDateStr(getActivity()));
            }
        });
        View nextAction = v.findViewById(R.id.next_month);
        nextAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.goToNextDay(getActivity());
                monthText.setText(calendarView.getSelectedDateStr(getActivity()));
            }
        });
        calendarView.setOnDateSelectedListener(new MyCalendarView.OnDateSelectedListener() {
            @Override
            public void onDateTapped(Date date) {
                mListener.onDateSelected(date);
                CalendarDialog.this.dismiss();
            }
        });
        monthText.setText(calendarView.getSelectedDateStr(getActivity()));
        return v;
    }*/
}