package com.alumnigroup.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alumnigroup.app.R;

/**
 * 下拉刷新 上推刷新
 * 
 * @author Jayin Ton
 * 
 */
public class PullAndLoadListView extends PullToRefreshListView {
	// this params is design to use when then data you cannot load more,set this
	// to false and it won't called listener.loadmore() and updata the
	// UI(progressbar)
	private boolean _canLoadMore = true;
	private TextView mLabLoadMore;

	public PullAndLoadListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initComponent(context);
	}

	// Listener to process load more items when user reaches the end of the list
	private OnLoadMoreListener mOnLoadMoreListener;
	// To know if the list is loading more items
	private boolean mIsLoadingMore = false;

	// footer
	private RelativeLayout mFooterView;
	// private TextView mLabLoadMore;
	private ProgressBar mProgressBarLoadMore;

	public PullAndLoadListView(Context context) {
		super(context);
		initComponent(context);
	}

	public PullAndLoadListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initComponent(context);
	}

	public void initComponent(Context context) {

		// footer
		mFooterView = (RelativeLayout) mInflater.inflate(
				R.layout.load_more_footer, this, false);

		mLabLoadMore = (TextView) mFooterView
				.findViewById(R.id.load_more_lab_view);

		mProgressBarLoadMore = (ProgressBar) mFooterView
				.findViewById(R.id.load_more_progressBar);

		mFooterView.setOnClickListener(new onClickLoadListener());
		addFooterView(mFooterView);
	}

	/**
	 * Register a callback to be invoked when this list reaches the end (last
	 * item be visible)
	 * 
	 * @param onLoadMoreListener
	 *            The callback to run.
	 */

	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
		mOnLoadMoreListener = onLoadMoreListener;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

		// if need a list to load more items
		if (mOnLoadMoreListener != null) {

			// if (mProgressBarLoadMore.getVisibility() == View.GONE)
			// mLabLoadMore.setVisibility(View.VISIBLE);
			// else
			// mLabLoadMore.setVisibility(View.GONE);

			if (visibleItemCount == totalItemCount) {
				mProgressBarLoadMore.setVisibility(View.GONE);
				mLabLoadMore.setVisibility(View.GONE);
				// mFooterView.setVisibility(View.GONE);
				return;
			}

			boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

			if (_canLoadMore && !mIsLoadingMore && loadMore
					&& mRefreshState != REFRESHING
					&& mCurrentScrollState != SCROLL_STATE_IDLE) {
				mProgressBarLoadMore.setVisibility(View.VISIBLE);
				// mLabLoadMore.setVisibility(View.VISIBLE);
				mLabLoadMore.setVisibility(View.GONE);
				// mFooterView.setVisibility(View.VISIBLE);
				mIsLoadingMore = true;
				onLoadMore();
			}
		}
	}

	public void onLoadMore() {
		if (mOnLoadMoreListener != null) {
			mOnLoadMoreListener.onLoadMore();
		}
	}

	/**
	 * Notify the loading more operation has finished
	 */
	public void onLoadMoreComplete() {
		mIsLoadingMore = false;
		mLabLoadMore.setVisibility(View.VISIBLE);
		mProgressBarLoadMore.setVisibility(View.GONE);
	}

	/**
	 * set if it can load more data to update the UI
	 * 
	 * @param canLoadMore
	 */
	public void setCanLoadMore(boolean canLoadMore) {
		this._canLoadMore = canLoadMore;
	}

	public class onClickLoadListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (mLabLoadMore.getVisibility() != View.VISIBLE)
				return;
			if (!mIsLoadingMore && mRefreshState != REFRESHING
					&& mCurrentScrollState == SCROLL_STATE_IDLE) {
				mProgressBarLoadMore.setVisibility(View.VISIBLE);
				mLabLoadMore.setVisibility(View.GONE);
				// mFooterView.setVisibility(View.VISIBLE);
				mIsLoadingMore = true;
				onLoadMore();
			}
		}

	}

	/**
	 * Interface definition for a callback to be invoked when list reaches the
	 * last item (the user load more items in the list)
	 */
	public interface OnLoadMoreListener {
		/**
		 * Called when the list reaches the last item (the last item is visible
		 * to the user) A call to
		 * {@link PullAndLoadListView #onLoadMoreComplete()} is expected to
		 * indicate that the loadmore has completed.
		 */
		public void onLoadMore();
	}
}
