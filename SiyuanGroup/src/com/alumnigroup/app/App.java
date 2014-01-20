package com.alumnigroup.app;

import android.app.Application;

import com.alumnigroup.api.RestClient;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Appliction Object of this app
 * 
 * @author Jayin Ton
 * 
 */
public class App extends Application {
	@Override
	public void onCreate() {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisc(true).build();
		// Create global configuration and initialize ImageLoader with this
		// configuration
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).discCacheSize(50 * 1024 * 1024)
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.discCacheFileCount(100).memoryCacheSize(10 * 1024 * 1024)
				.defaultDisplayImageOptions(defaultOptions).build();

		ImageLoader.getInstance().init(config);
		
		//init RestClient 
		RestClient.init(getApplicationContext());
	}
}
