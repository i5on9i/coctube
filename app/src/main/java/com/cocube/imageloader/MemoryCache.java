package com.cocube.imageloader;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 *
 * This memory cache is modified in reference to the Caching Bitmap
 * {@ref: http://developer.android.com/training/displaying-bitmaps/cache-bitmap.html} article
 * 
 *	
 */
public class MemoryCache {

    private static final String TAG = "MemoryCache";
    private Map<String, Bitmap> cache= Collections.synchronizedMap(
            new LinkedHashMap<String, Bitmap>(10, 1.5f, true));//Last argument true for LRU ordering
    private long size = 0;//current allocated size
    private long limit = 0;//max memory in bytes


    public MemoryCache(){

		// http://javarevisited.blogspot.kr/2012/01/find-max-free-total-memory-in-java.html
		//
        //use 25% of available heap size
        setLimit(Runtime.getRuntime().maxMemory()/4);
    }
    
    public void setLimit(long new_limit){
        limit = new_limit;
        Log.i(TAG, "MemoryCache will use up to " + limit / 1024. / 1024. + "MB");
    }

    public Bitmap get(String id){
        try{
            if(!cache.containsKey(id))
                return null;
            //NullPointerException sometimes happen here http://code.google.com/p/osmdroid/issues/detail?id=78 
            return cache.get(id);
        }catch(NullPointerException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public void put(String id, Bitmap bitmap){
        try{
            if(cache.containsKey(id))
                size -= getSizeInBytes(cache.get(id));
            cache.put(id, bitmap);
            size += getSizeInBytes(bitmap);
            checkSize();
        }catch(Throwable th){
            th.printStackTrace();
        }
    }
    
    private void checkSize() {
        Log.i(TAG, "cache size=" + size + " length=" + cache.size());
        if(size > limit){
                synchronized(cache){	//namh added
	            Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();//least recently accessed item will be the first one iterated
	            while(iter.hasNext()){
	                Entry<String, Bitmap> entry = iter.next();
	                size -= getSizeInBytes(entry.getValue());
	                iter.remove();
	                if(size<=limit)
	                    break;
	            }
                }
            Log.i(TAG, "Clean cache. New size " + cache.size());
        }
    }

    public void clear() {
        try{
            //NullPointerException sometimes happen here http://code.google.com/p/osmdroid/issues/detail?id=78

			// http://stackoverflow.com/questions/12218976/cannot-draw-recycled-bitmaps-when-displaying-bitmaps-in-gallery-attached-to-ad
			//
			// recycle() should be called when no reference gets the bitmap.
			// but in this case, when the view is showed, always some bitmap is being used.
			// Thus, you must NOT recycle() here.

			cache.clear();
            size = 0;

			//namh what about this?
			//System.gc();
        }catch(NullPointerException ex){
            ex.printStackTrace();
        }
    }

    long getSizeInBytes(Bitmap bitmap) {
        if(bitmap==null)
            return 0;
        return bitmap.getRowBytes() * bitmap.getHeight();
    }



	// the destructor should also handle the memory in case it still holds memory
	protected void finalize() {
		// http://stackoverflow.com/questions/8996655/android-bitmap-and-memory-management
		//
		synchronized(cache){
			// get HashMap entry iterator
			Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();
			while(iter.hasNext()) {
				// get entry pair
				Entry<String, Bitmap> entry = iter.next();
				// get Bitmap object
				Bitmap image = entry.getValue();
				// recycle if...
				if(image != null && !image.isRecycled()) {
					image.recycle();
					image = null;
				}
			}
			cache.clear();
	    }// end of synchronized
	}
	
		
	
}
