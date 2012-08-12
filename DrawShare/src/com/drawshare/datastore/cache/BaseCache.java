package com.drawshare.datastore.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;

public abstract class BaseCache {
	protected Context context;
	
	public BaseCache(Context context) {
		this.context = context;
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
				File fileDirs = new File(file.getParent());
				fileDirs.mkdirs();
				file.createNewFile();
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
	
}
