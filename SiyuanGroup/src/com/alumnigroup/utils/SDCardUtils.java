package com.alumnigroup.utils;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

/***
 * SD卡工具类
 * 
 * @author Lenovo
 * 
 */
public class SDCardUtils {
        /** SDCard的根路径 **/
        private static String SDCARD_PATH = null;

        /**
         * 判断sdcard是否存在
         * 
         * @return
         */
        public static boolean isExistSDCard() {
                if (android.os.Environment.getExternalStorageState().equals(
                                android.os.Environment.MEDIA_MOUNTED)) {
                        return true;
                } else
                        return false;
        }

        /**
         * 
         * 取得SD卡路径，以/(依赖File.separator)结尾
         * 
         * @return
         */
        public static String getSDCardPath() {
                if (!isExistSDCard())
                        return null;
                if (null != SDCARD_PATH)
                        return SDCARD_PATH;
                File path = Environment.getExternalStorageDirectory();
                String SDCardPath = path.getAbsolutePath();
                SDCardPath += SDCardPath.endsWith(File.separator) ? "" : File.separator;
                SDCARD_PATH = SDCardPath;
                return SDCardPath;
        }
    /**
     * 获取SD卡剩余空间(MB)
     * @return 0 if SDCard is not exist
     */
        public static long getSDFreeSize() {
                if(!isExistSDCard())return 0;
                // 取得SD卡文件路径
                File path = Environment.getExternalStorageDirectory();
                StatFs sf = new StatFs(path.getPath());
                // 获取单个数据块的大小(Byte)
                long blockSize = sf.getBlockSize();
                // 空闲的数据块的数量
                long freeBlocks = sf.getAvailableBlocks();
                // 返回SD卡空闲大小
                // return freeBlocks * blockSize; //单位Byte
                // return (freeBlocks * blockSize)/1024; //单位KB
                return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
        }
        
         
}