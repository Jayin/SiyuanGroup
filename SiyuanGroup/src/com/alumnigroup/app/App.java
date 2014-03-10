package com.alumnigroup.app;

import android.app.Application;

import com.alumnigroup.api.RestClient;
import com.alumnigroup.utils.DataPool;
import com.loopj.android.http.PersistentCookieStore;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Appliction Object of this app
 * 
 * @author Jayin Ton
 * 
 */
public class App extends Application {
	@Override
	public void onCreate() {
		initImageLoader();    

		// init RestClient  
		RestClient.init(getApplicationContext());
	}
    
	private void initImageLoader() {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
		//在内存和sd卡中缓存,如果经常OOM 移除.cacheInMemory(true) or 每个Activity.onDestory()的时候调用用ImageLoader.getInstance().clearMemoryCache()
				.cacheOnDisc(true).cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)//适配大小来display
				.showImageOnLoading(R.drawable.ic_image_load_loading)  //正在加载
				.showImageForEmptyUri(R.drawable.ic_image_load_normal) //没有图片
				.showImageOnFail(R.drawable.ic_image_load_faild).build(); //加载失败

		// Create global configuration and initialize ImageLoader with this
		// configuration
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).discCacheSize(50 * 1024 * 1024)
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.discCacheFileCount(100).memoryCacheSize(10 * 1024 * 1024)
				.defaultDisplayImageOptions(defaultOptions).build();
		ImageLoader.getInstance().init(config);
		
	}

	/**
	 * 退出账号时清楚数据
	 */
	public void cleanUpInfo() {
		// 清楚所有缓存
		DataPool dp = new DataPool(DataPool.SP_Name_User, this);
		dp.removeAll();

		PersistentCookieStore cs = new PersistentCookieStore(this);
		cs.clear();
		ImageLoader.getInstance().clearDiscCache();

	}
}
