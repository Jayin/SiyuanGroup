package com.alumnigroup.utils;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class FilePath {
	public final static String File_Save_Path = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ "Siyuan" + File.separator;

	public static String getFileSavePath() {
		return File_Save_Path;
	}

	public static String getImageFilePath() {
		File f = new File(File_Save_Path + "img" + File.separator);
		if (!f.exists())
			f.mkdirs();
		return f.getAbsolutePath()+File.separator;
	}
}
