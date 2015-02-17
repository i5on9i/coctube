package com.cocube.imageloader.cache;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FileCache {
    
    private File cacheDir;
	private FileNameGenerator fileNameGenerator;
	
    public FileCache(Context context){

		fileNameGenerator = new Md5FileNameGenerator();

		//Find the dir to save cached images
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            cacheDir=new File(Environment.getExternalStorageDirectory(),"LazyList");
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();

    }
    
    public File getFile(String url){

		String filename = fileNameGenerator.generate(url);
		File f = new File(cacheDir, filename);
		return f;
	}
    
    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files != null)
        {
	        for(File f:files){
    	        f.delete();
	        }
        }// end of if
    }

	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

}