package com.alumnigroup.widget;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.alumnigroup.app.R;
import com.alumnigroup.utils.AndroidUtils;
import com.alumnigroup.utils.BitmapUtils;
import com.alumnigroup.utils.FileUtils;
import com.alumnigroup.utils.SDCardUtils;

/**
 * A view for advertisement display<br>
 * 使用方法：<br>
 * <li>1.设置urls(可以选择设置是否缓存照图片 setIsCachePic ，项目需要，这里就不缓存了) <br> <li>
 * 2.display()如果有缓存在先显示后下载 ,下载并显示广告 gone -> visable<br>
 * (已分离，每次都下载更新) 2.1.注意,这里默认是如果存在对应的缓存(根据文件名来判断)。就不去下载 <br>
 * 2.2.可以用 cleanCachePic()清楚缓存<br>
 * <strong>一般建议</strong><br> 
 * <strong><li>一般建议,设置3附图片</strong><br>
 * <strong><li>1.urls 从web获取,确定有新的时候，删除缓存再下载(操作比较麻烦)</strong><br>
 * <strong><li>2.urls固定死，每次先显示缓存
 * 然后下载，下载直接替换缓存,这样服务端就保持不用修改文件名就行了（默认这种放）</strong><br>
 * 
 * @author Jayin Ton
 * 
 */
@SuppressLint("HandlerLeak")
public class ADView extends ViewFlipper {
	private static final int ADView_Start = 0;
	private static final int ADView_DownloadFinish = 1;
	private static final int ADView_Faild = 2;
	private static final int ADView_Finish = 3;
	private Handler h;
	private String[] urls;
	private Context context;
	// defaultFilePath : /mnt/sdcard/AD/
	private String FolderPath = SDCardUtils.getSDCardPath() + "AD"
			+ File.separator;
	private boolean isCachePic = false; // default it's cache the pic
	private List<View> adlist = null; // 广告View 实际显示的
	private List<View> downloadADList = null;
	private int reqHeight = 0;
	private int reqWidth = 0;
	private int sampleSize = 0;

	public ADView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public ADView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	/**
	 * init the ADView
	 */
	public void init() {
		reqHeight = 100;
		reqWidth = AndroidUtils.getScreenSize(context)[0];
		BitmapFactory.Options options = new BitmapFactory.Options();
		sampleSize = BitmapUtils.calculateInSampleSize(options, reqWidth,
				reqHeight);

		isCachePic = true;
		adlist = new ArrayList<View>();
		downloadADList = new ArrayList<View>();
		h = new AdViewHandler();
		dismiss();
		this.setInAnimation(context, R.anim.push_left_in);
		this.setOutAnimation(context, R.anim.push_left_out);
		this.setAutoStart(true);
		this.startFlipping();
	}

	/**
	 * set thr url of ad
	 * 
	 * @param urls
	 */
	public void setURL(String... urls) {
		this.urls = urls;
	}

	public void setIsCachePic(boolean cachePic) {
		this.isCachePic = cachePic;
	}

	protected void show() {
		this.setVisibility(View.VISIBLE);
	}

	protected void dismiss() {
		this.setVisibility(View.INVISIBLE);
	}

	public void addADList(View v) {
		adlist.add(v);
	}

	public void addDownloadADList(View v) {
		downloadADList.add(v);
	}

	public void addADView() {
		this.removeAllViews();
		for (View v : adlist) {
			this.addView(v, this.getChildCount());
		}
	}

	/**
	 * 设置缓存图片文件夹名称
	 * 
	 * @param folder
	 *            name
	 * @return true if set ok
	 */
	public boolean setCachefoldeName(String foldername) {
		if (FileUtils.makeDirs(SDCardUtils.getSDCardPath() + foldername
				+ File.separator)) {
			FolderPath = SDCardUtils.getSDCardPath() + foldername
					+ File.separator;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 新建一个广告栏
	 * 
	 * @return
	 */
	public View creatADBaner(Bitmap bitmap) {

		View v = LayoutInflater.from(context).inflate(R.layout.item_adview,
				null);
		ImageView iv = (ImageView) v.findViewById(R.id.item_adview_iv);
		BitmapDrawable bitmapDrawable = new BitmapDrawable(
				context.getResources(), bitmap);
		iv.setBackgroundDrawable(bitmapDrawable);

		return v;
	}

	/**
	 * 若有缓存,则先显示后下载
	 */
	public void display() {
		this.startFlipping();
		if (FileUtils.isFolderExist(FolderPath)) {
			File file = new File(FolderPath);
			String[] files = file.list();

			if (files != null && files.length > 0) {
				adlist = new ArrayList<View>();
				for (String fileName : files) {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = sampleSize;
					Bitmap bitmap = BitmapFactory.decodeFile(FolderPath
							+ fileName, options);
					if (bitmap != null)
						addADList(creatADBaner(bitmap));
				}
				addADView();
				show();
				goDownload();
			} else {
				goDownload();
			}
		} else {
			FileUtils.makeDirs(FolderPath);
			goDownload();
		}
	}

	/**
	 * 开始下载广告并播放广告
	 * 
	 * @deprecated
	 */
	public void displayAD() {
		goDownload();
	}

	public void goDownload() {
		try {

			new Thread(new Runnable() {
				@Override
				public void run() {
					downloadADList = new ArrayList<View>();
					h.sendEmptyMessage(ADView_Start);
					if (urls == null || urls.length == 0) {
						return;
					} else {
						for (int i = 0; i < urls.length; i++) {
							String url = urls[i];
							// 2.1.注意,这里默认是如果存在对应的缓存(根据文件名来判断)。就不去下载，如果需要缓存请不注释
							// if (FileUtils.isFileExist(FolderPath
							// + FileUtils.getFileName(url)))
							// continue;
							byte[] data = downloadPic(url);
							if (data != null) {
								if (isCachePic) {
									saveToFile(data, FileUtils.getFileName(url)); // hascache
								}
								Message msg = h.obtainMessage();
								BitmapFactory.Options options = new BitmapFactory.Options();
								options.inSampleSize = sampleSize;
								msg.obj = BitmapFactory.decodeByteArray(data,
										0, data.length, options);
								msg.what = ADView_DownloadFinish;
								h.sendMessage(msg);
							}
						}
						h.sendEmptyMessage(ADView_Finish); // 第一次没有图片并下载完成
					}

				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
			// there is no space to save the cache-pic;
		}
	}

	/**
	 * 保存图片
	 * 
	 * @param data
	 * @param fileName
	 * @return
	 */
	protected boolean saveToFile(byte[] data, String fileName) {
		if (FileUtils.isFileExist(FolderPath + fileName))
			FileUtils.deleteFile(FolderPath + fileName);
		if (SDCardUtils.getSDFreeSize() >= 5) {
			return FileUtils.writeFile(FolderPath + fileName, data);
		} else {
			return false;
		}

	}

	class AdViewHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ADView_Start:
			
				break;
			case ADView_Faild:
				 //可能有缓存正在显示。。
				break;
			case ADView_DownloadFinish:
				addDownloadADList(creatADBaner((Bitmap) msg.obj));
				break;
			case ADView_Finish: // all download mission finished
				if (downloadADList != null && downloadADList.size() > 0)
					// adlist = new ArrayList<View>(downloadADList);
					adlist.addAll(downloadADList);
				downloadADList = null;
				addADView();
				show();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 下载图片
	 * 
	 * @param url
	 * @return
	 */
	protected byte[] downloadPic(String url) {
		if (!AndroidUtils.isNetworkConnected(getContext()))
			return null;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response = null;
		byte[] data = null;
		ByteArrayOutputStream bos = null;
		InputStream in = null;
		try {
			response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {
				in = response.getEntity().getContent();
				bos = new ByteArrayOutputStream();
				int len = -1;
				byte[] buffer = new byte[1024];
				while ((len = in.read(buffer, 0, buffer.length)) != -1) {
					bos.write(buffer, 0, len);
				}
				return bos.toByteArray();
			} else {
				return null;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (client != null) {
				client.getConnectionManager().shutdown();
			}
		}
	}

	/**
	 * 删除缓存图片
	 * 
	 * @return true if delete successfully
	 */
	public boolean cleanCachePic() {
		return FileUtils.deleteFilesInFolder(FolderPath);
	}

}
