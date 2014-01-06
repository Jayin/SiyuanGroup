package com.alumnigroup.app.acty;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.User;
import com.alumnigroup.widget.PullAndLoadListView;
import com.alumnigroup.widget.PullAndLoadListView.OnLoadMoreListener;
import com.alumnigroup.widget.PullToRefreshListView.OnRefreshListener;

/**
 * 
 * @author create by vector<br>
 *         coded by Jayin Ton
 * 
 */
public class Allmember extends BaseActivity {
	private TextView tv_title;
	private View btn_back, btn_allmenmber, btn_myfriend;
	private PullAndLoadListView lv_allmember, lv_myfriend;
	private List<User> data_allmember = null, data_myfriend = null;
	private ViewFlipper flipper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_allmember);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initLayout() {
		initFlipper();
		tv_title = (TextView) _getView(R.id.acty_head_tv_title);
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_allmenmber = _getView(R.id.acty_allmember_footer_allmember);
		btn_myfriend = _getView(R.id.acty_allmember_footer_myfriend);

		lv_allmember.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {

			}
		});

		lv_allmember.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

			}
		});

		lv_myfriend.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {

			}
		});

		lv_allmember.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

			}
		});
		btn_back.setOnClickListener(this);
		btn_allmenmber.setOnClickListener(this);
		btn_myfriend.setOnClickListener(this);
	}

	private void initFlipper() {
		flipper = (ViewFlipper) _getView(R.id.acty_allmember_content);
		flipper.setInAnimation(getContext(), R.anim.push_right_in);
		flipper.setOutAnimation(getContext(), R.anim.push_right_out);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.acty_allmember_footer_allmember:
			break;
		case R.id.acty_allmember_footer_myfriend:
			break;
		default:
			break;
		}
	}
}
