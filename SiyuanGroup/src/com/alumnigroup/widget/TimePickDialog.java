package com.alumnigroup.widget;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alumnigroup.app.R;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;

public class TimePickDialog extends Dialog {
	private Context context;
	private CalendarPickerView calendarView;
	private Button done;
	private Date minDate, maxDate;
	private OnSiglePickFinishedListener mOnSiglePickFinishedListener;
	private OnMultiplePickFinishedListener mOnMultiplePickFinishedListener;

	public TimePickDialog(Context context) {
		super(context, R.style.Dialog_Theme_BaseDialog);
		this.context = context;
		init();
	}

	public TimePickDialog(Context context, Date minDate, Date maxDate) {
		super(context, R.style.Dialog_Theme_BaseDialog);
		this.context = context;
		this.minDate = minDate;
		this.maxDate = maxDate;
		init();
	}

	private void init() {
		setContentView(R.layout.dlg_timepick_oneday);
		calendarView = (CalendarPickerView) findViewById(R.id.calendarview);
		done = (Button) findViewById(R.id.comfirm);
		done.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TimePickDialog.this.dismiss();
				if (mOnSiglePickFinishedListener != null) {
					mOnSiglePickFinishedListener.onFinish(getTime());
				}

				if (mOnMultiplePickFinishedListener != null) {
					mOnMultiplePickFinishedListener.onFinish(getTimes());
				}

			}
		});
		initCalendarVew();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void setOnSiglePickFinishedListener(
			OnSiglePickFinishedListener listener) {
		this.mOnSiglePickFinishedListener = listener;
	}

	public void setOnMultiplePickFinishedListener(
			OnMultiplePickFinishedListener listener) {
		this.mOnMultiplePickFinishedListener = listener;
	}

	/**
	 * 
	 * @return
	 */
	public long getTime() {
		return calendarView.getSelectedDate().getTime();
	}

	/**
	 * 多选的时候返回多个时间
	 * 
	 * @return List<Date>
	 */
	public List<Date> getTimes() {
		return calendarView.getSelectedDates();
	}

	public void setTime(Date minDate, Date maxDate) {
	}

	public void initCalendarVew() {
		Calendar nextYear = Calendar.getInstance();
		nextYear.add(Calendar.YEAR, 1);
		if (minDate == null)
			minDate = new Date();
		if (maxDate == null)
			maxDate = nextYear.getTime();
		calendarView.init(minDate, maxDate).inMode(SelectionMode.SINGLE)
				.withSelectedDate(new Date());
	}

	/** SelectionMode.MULTIPLE rang sigle */
	public void setCalendarVewMode(SelectionMode mode) {
		calendarView.init(minDate, maxDate).inMode(mode)
				.withSelectedDate(new Date());
	}

	// 选择完成的接口
	public interface OnSiglePickFinishedListener {
		/**
		 * 
		 * @param selecttime
		 *            选择的时间
		 */
		public void onFinish(long selecttime);
	}

	public interface OnMultiplePickFinishedListener {
		/**
		 * 
		 * @param selecttime
		 *            选择的时间
		 */
		public void onFinish(List<Date> selecttimes);
	}
}
