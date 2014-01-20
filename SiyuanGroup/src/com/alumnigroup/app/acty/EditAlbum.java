package com.alumnigroup.app.acty;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.alumnigroup.api.PhotoAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.adapter.AlbumAdapter;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.Photo;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 编制相册
 * 
 * @author vector
 * 
 */
public class EditAlbum extends BaseActivity {

	private GridView gvAlbums;
	private ArrayList<String> alAlbumUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_edit_album);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		alAlbumUrl = new ArrayList<String>() {
			{
				add("");
				add("");
				add("");
				add("");
			}
		};
	}

	@Override
	protected void initLayout() {
		gvAlbums = (GridView) _getView(R.id.acty_edit_album_gv_album);
		gvAlbums.setAdapter(new AlbumAdapter(alAlbumUrl, this));
		gvAlbums.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
	}

}
