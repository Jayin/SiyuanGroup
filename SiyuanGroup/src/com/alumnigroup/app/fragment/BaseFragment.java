package com.alumnigroup.app.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class BaseFragment extends Fragment implements OnClickListener {
	
	@Override
	public void onClick(View v) {
		 
	}
	
	public void toast(String content) {
		Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
	}

}
