package com.drawshare.datastore.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public abstract class BaseCache {
	//protected Context context;
	
	private static final String LOG_TAG = "BaseCache";
	
	public BaseCache() {
	}
	
	/**
	 * 
	 * @param filePath 文件名+路径, 包括第一个"/"
	 * @return
	 */
	public FileOutputStream getFileOutputStream(String filePath) {
		FileOutputStream fouts = null;
		File file = new File(Environment.getExternalStorageDirectory() + filePath);
		
		if (file.exists() && file.isFile()) {
			try {
				fouts = new FileOutputStream(file);
				return fouts;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {	
			try {
				//file.mkdirs();
				//File fileDirs = new File(file.getParent());
				//fileDirs.mkdirs();
				//Log.d(LOG_TAG, "make the fileDirs " + fileDirs.getPath());
				//file.createNewFile();	
				//File.createTempFile(file.getName(), "");
				Log.d(LOG_TAG, "create a new file name " + file.getName());
				fouts = new FileOutputStream(file);
				return fouts;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return null;
	}
	
	/**
	 * 
	 * @param filePath, 文件名+路径, 包括第一个"/"
	 * @return
	 * @throws FileNotFoundException, 当文件不存在的时候,抛出此异常
	 */
	public FileInputStream getFileInputStream(String filePath) throws FileNotFoundException {
		File file = new File(Environment.getExternalStorageDirectory() + filePath);
		FileInputStream fins = new FileInputStream(file);
		return fins;
	}
	
	/**
	 * 删除某一个文件夹下的所有内容
	 * @param path
	 * @return
	 */
	public boolean deleteAllUnderPath(String path) {
		boolean bool = false; 
	     File f = new File(path);
	     if(f.exists() && f.isDirectory()){
	       if(f.listFiles().length==0)
	       	return true;
	       else{
	         File[] flist = f.listFiles();
	         for(int i = 0; i < flist.length; i++){
	           if(flist[i].isDirectory()){
	           	deleteAllUnderPath(flist[i].getAbsolutePath());
	           }
	           flist[i].delete();
	         }
	       }
	       bool = true;
	    }
	    return bool;
	}
	
	/**
	 * 清空缓存
	 * @return
	 */
	public boolean clearCache() {
		return false;
	}
	
}
