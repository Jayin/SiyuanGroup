package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.alumnigroup.api.IssuesAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.Comment;
import com.alumnigroup.entity.Issue;
import com.alumnigroup.widget.PullAndLoadListView;

public class CommunicationDetail extends BaseActivity {
	private Issue issue;
	private PullAndLoadListView lv_comment;
	private List<Comment> data;
    private CommentAdapter adapter;
    private IssuesAPI api = new IssuesAPI();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_communicationdetail);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		issue = (Issue) getSerializableExtra("issue");
		api = new IssuesAPI();
		data = new ArrayList<Comment>();
	}

	@Override
	protected void initLayout() {
       
	}

	class CommentAdapter extends BaseAdapter {

		private List<Comment> data;

		public CommentAdapter(List<Comment> data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return convertView;
		}

	}

}
