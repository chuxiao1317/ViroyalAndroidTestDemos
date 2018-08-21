package com.viroyal.sloth.widget.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;

import com.sloth.core.R;
import com.viroyal.sloth.util.DensityUtil;


/**
 * Created by yu.zai on 2015/12/8.
 */
public class CustomDatePickerDialog extends DialogFragment implements View.OnClickListener {
    private DatePickerDialog.OnDateSetListener mListener;
    private final static String YEAR = "year";
    private final static String MONTHOFYEAR = "monthOfYear";
    private final static String DAYOFMONTH = "dayOfMonth";
    private DatePicker mDatePicker;

    public static CustomDatePickerDialog getInstance(Context context, DatePickerDialog.OnDateSetListener callBack, int year, int monthOfYear, int dayOfDay) {
        CustomDatePickerDialog f = new CustomDatePickerDialog();
        Bundle b = new Bundle();

        b.putInt(YEAR, year);
        b.putInt(MONTHOFYEAR, monthOfYear);
        b.putInt(DAYOFMONTH, dayOfDay);

        f.setArguments(b);
        f.mListener = callBack;
        Context mContext = context;
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        Window window = dialog.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = getActivity().getWindow().getWindowManager().getDefaultDisplay().getWidth();
        window.setAttributes(attributes);
        window.setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);
        View v = View.inflate(getActivity(), R.layout.dialog_custom_date_picker_layout, null);
        mDatePicker = (DatePicker) v.findViewById(R.id.date_picker_custom_dialog);
        ViewGroup.LayoutParams layoutParams = mDatePicker.getLayoutParams();
        layoutParams.width = getActivity().getWindow().getWindowManager().getDefaultDisplay().getWidth();
        mDatePicker.setLayoutParams(layoutParams);
        mDatePicker.init(getArguments().getInt(YEAR),
                getArguments().getInt(MONTHOFYEAR),
                getArguments().getInt(DAYOFMONTH), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    }
                });

        TextView title = (TextView) v.findViewById(R.id.dialog_custom_title);
        TextView no = (TextView) v.findViewById(R.id.dialog_custom_no);
        no.setOnClickListener(this);
        TextView yes = (TextView) v.findViewById(R.id.dialog_custom_yes);
        yes.setOnClickListener(this);
        dialog.setContentView(v);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.dialog_custom_no) {
            CustomDatePickerDialog.this.dismiss();

        } else if (i == R.id.dialog_custom_yes) {
            mListener.onDateSet(mDatePicker, mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
            CustomDatePickerDialog.this.dismiss();
        }
    }
}
