package com.cocube.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.cocube.R;
import com.cocube.imageloader.cache.DiskLruImageCache;
import com.cocube.parser.YouTubeVideoItem;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {

    private final boolean downScaledThumb = true;
    private final int DOWNSCALE_WIDTH = 480;
    private final int DOWNSCALE_HEIGHT = 360;


    private Bitmap mEmptyImage;
//    private Bitmap mNormalImage;
//    private Bitmap mLikeImage;

    final int stub_id = R.drawable.noimage;
    protected static final int BUFFER_SIZE = 8 * 1024; // 8 Kb
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB

    private MemoryCache mMemoryCache = new MemoryCache();
    private DiskLruImageCache mDiskCache;

    private Map<ImageView, String> mImageViews
            = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService mExecutorService;
    Handler mHandler = new Handler();//mHandler to display images in UI thread


    private Map<String, YouTubeVideoItem> mLikeSet = Collections.synchronizedMap(
            new HashMap<String, YouTubeVideoItem>());//Last argument true for LRU ordering

    private volatile static ImageLoader instance;


    /**
     * Returns singleton class instance
     */
    public static ImageLoader getInstance() {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader();
                }
            }
        }
        return instance;
    }

    protected ImageLoader() {
    }

    public synchronized void init(Context context) {

        // namh
        mDiskCache = new DiskLruImageCache(context, "com.cocube",
                DISK_CACHE_SIZE,
                CompressFormat.JPEG,
                70);

        mExecutorService = Executors.newFixedThreadPool(5);
//        mEmptyImage = getRoundedCornerBitmap(
//                BitmapFactory.decodeResource(context.getResources(), R.drawable.noimage)
//        );
        mEmptyImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.noimage);
        // TODO : like function
//        mNormalImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_normal);
//        mLikeImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_like);
    }




    public void displayImage(ImageView imageView, String url, String key) {
        if (url.equals("")) {
            imageView.setImageBitmap(mEmptyImage);
        } else {

            mImageViews.put(imageView, key);


            Bitmap bitmap = mMemoryCache.get(key);
            if (bitmap != null) {
//                Bitmap roundedBitmap = getRoundedCornerBitmap(bitmap);
                imageView.setImageBitmap(bitmap);
            } else {
                queuePhoto(imageView, url, key);
                imageView.setImageBitmap(mEmptyImage);
                //imageView.setImageResource(stub_id);
            }
        }
    }

    /**
     * This method checks that the image source urls {@param photoToLoad}
     * and {@link #mImageViews} holds are same.
     * <p/>
     * Assume that the {@link android.widget.ListView} uses the getView() function and
     * the user scrolls down faster than the speed of the showing the image.
     * <p/>
     * Keep in mind that the imageView is recycled when
     * the ListView uses the getView().
     * <p/>
     * +==================================+
     * | +-------------------------------
     * | + imageView A -> http://a.jpg  +
     * | +-------------------------------
     * | +-------------------------------
     * | + imageView B -> http://d.jpg  +
     * | +-------------------------------
     * +==================================+
     * <p/>
     * |
     * v
     * +==================================+
     * | +-------------------------------
     * | + imageView B -> http://d.jpg  +
     * | +-------------------------------
     * | +-------------------------------
     * | + imageView A -> http://f.jpg  +
     * | +-------------------------------
     * +==================================+
     * <p/>
     * |
     * v
     * <p/>
     * +==================================+
     * | +-------------------------------
     * | + imageView A -> http://f.jpg  +
     * | +-------------------------------
     * | +-------------------------------
     * | + imageView B -> http://g.jpg  +
     * | +-------------------------------
     * +==================================+
     * <p/>
     * <p/>
     * After queuePhoto(), imageView is waited in the queue to display by
     * the UI thread through the Handler(). However, If the url is changed before
     * the image from previous url is showed, the previous image should not be
     * shown which causes the blink of the image.
     * <p/>
     * That is why the below method exists.
     *
     *
     * expected value : tag
     * current value : photoToLoad.url
     */
    boolean imageViewRecycled(PhotoToLoad photoToLoad) {
        String tag = mImageViews.get(photoToLoad.imageView);

        if (tag == null || !tag.equals(photoToLoad.key)) {
            return true;
        }
        return false;
    }


    private void queuePhoto(ImageView imageView, String url, String key) {
        PhotoToLoad p = new PhotoToLoad(imageView, url, key);
        mExecutorService.submit(new PhotosLoader(p));    // thread.execute()
    }


    private Bitmap decodeFile(InputStream in) {
        //decode with inSampleSize
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = getProperScale(in);

        return BitmapFactory.decodeStream(in, null, opt);
    }

    private int getProperScale(InputStream in) {
        //decode image size
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;

        BitmapFactory.decodeStream(in, null, opt);

        //Find the correct scale value. It should be the power of 2.
        final int REQUIRED_SIZE = 70;
        int width_tmp = opt.outWidth, height_tmp = opt.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        return scale;


    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f) {
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();

            //Find the correct scale value. It should be the power of 2.
            final int targetSize = 70;    // px unit ?
            // get the targetSize from the ImageView
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < targetSize
                        || height_tmp / 2 < targetSize)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // package-private
    Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap roundedBitmap = null;

        try {
            int widthSize = bitmap.getWidth();
            int heightSize = bitmap.getHeight();


            roundedBitmap = Bitmap.createBitmap(widthSize, heightSize, Config.ARGB_8888);
            Canvas canvas = new Canvas(roundedBitmap);

            final Rect outerRect = new Rect(0, 0, widthSize, heightSize);
            final RectF outerRectF = new RectF(0, 0, widthSize, heightSize);
            float outerRadius = widthSize / 18f;

            // draw the Red rectangle
            final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.RED);
            canvas.drawRoundRect(outerRectF, outerRadius, outerRadius, paint);

            // Compose the image with the red rectangle.
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            // Apply the paint to the bitmap
            canvas.drawBitmap(bitmap, outerRect, outerRect, paint);

        } catch (OutOfMemoryError e) {
            Log.e("ImageLoader", "Can't create bitmap with rounded corners. Not enough memory.");
            // The reason of usage the recycle() is at the below link,
            // {@link http://stackoverflow.com/questions/8996655/android-bitmap-and-memory-management}
            // recycle if...
            if (roundedBitmap != null && !roundedBitmap.isRecycled()) {
                // maybe this is not needed, because the createBitmap was failed, if
                // OutOfMemoryError occurs
                roundedBitmap.recycle();
                roundedBitmap = null;
            }
            roundedBitmap = bitmap;
        }
        return roundedBitmap;
    }



//    public void displayLike(ImageView likeView, String youTubeId) {
//
//        // TODO : like function
//        if (youTubeId.equals("")) {
//            likeView.setImageBitmap(mNormalImage);
//            return;
//        }
//
//        if (mLikeSet.containsKey(youTubeId)) {
//            likeView.setImageBitmap(mLikeImage);
//        } else {
//            likeView.setImageBitmap(mNormalImage);
//        }
//
//
//    }
//
//    public void toggleLikeItem(ImageView view, YouTubeVideoItem item) {
//
//        String youTubeId = item.getYoutubeId();
//
//        if (mLikeSet.containsKey(youTubeId)) {
//            view.setImageBitmap(mNormalImage);
//            mLikeSet.remove(youTubeId);
//        } else {
//            view.setImageBitmap(mLikeImage);
//            mLikeSet.put(item.getYoutubeId(), item);
//        }
//
//
//    }
//
//    public boolean existLike(){
//        return (mLikeSet.size() > 0);
//    }


    ////////////////////////////////////////////////////
    ////
    //// inner class PhotoToLoad
    ////

    //Task for the queue
    private class PhotoToLoad {
        public String url;
        public ImageView imageView;
        public String key;

        public PhotoToLoad(ImageView i, String u, String key) {
            url = u;
            imageView = i;
            this.key = key;
        }
    }


    ////////////////////////////////////////////////////
    ////
    //// inner class PhotosLoader
    ////
    private class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            try {

                if (imageViewRecycled(photoToLoad))
                    return;

                Bitmap thumbnail = null;
                try {
                    String url = photoToLoad.url;
                    String imageKey = photoToLoad.key;


                    //namh
                    //from SD cache
                    thumbnail = mDiskCache.getBitmap(imageKey);


                    if (thumbnail == null) {

                        thumbnail = getBitmapFromTheWeb(url);
                        if (thumbnail != null) {
                            // decodeFile()
                            mDiskCache.put(imageKey, thumbnail);
                        }

                    }
                    // the reason not to check the thumnail is null that
                    // to determine faster because mMemoryCache is used
                    // before execute this thread.
                    mMemoryCache.put(imageKey, thumbnail);


                } catch (Exception e) {
                    Log.e("DownloadImageTask", e.getMessage());
                    e.printStackTrace();

                } catch (OutOfMemoryError oom) {
                    Log.e("PhotosLoader", "Out of memory maybe in getBitmap()");
                    mMemoryCache.clear();
                } catch (Throwable ex) {
                    Log.e("PhotosLoader", "run()");
                    ex.printStackTrace();
                }


				if(imageViewRecycled(photoToLoad))
                    return;

                BitmapDisplayer bd = new BitmapDisplayer(thumbnail, photoToLoad);
                mHandler.post(bd);


            } catch (Throwable th) {
                th.printStackTrace();
            }
        }


        //namh
//		public Bitmap getBitmapFromDiskCache(String key) {
//		    synchronized (mDiskCacheLock) {
//		        // Wait while disk cache is started from background thread
//		        while (mDiskCacheStarting) {
//		            try {
//		                mDiskCacheLock.wait();
//		            } catch (InterruptedException e) {}
//		        }
//		        if (mDiskLruCache != null) {
//		            return mDiskLruCache.get(key);
//		        }
//		    }
//		    return null;
//		}


//		private Bitmap getBitmap(String url) 
//		{
//			
        //from SD cache
        //namh

//			String imageKey = url;
//			Bitmap bitmap = mDiskCache.getBitmap(imageKey);
//			if(bitmap == null){
//				// do something
//				bitmap = getBitmapFromTheWeb(url);
//				if(bitmap != null)
//					mDiskCache.put(imageKey, bitmap);
//				
//			}
//			return bitmap;


//			File imageFile = mFileCache.getFile(url);
//			Bitmap thumbnail = null;


        // Try to load image from disc cache
//			if (imageFile.exists()) {
//				thumbnail = decodeFile(imageFile);
//				if(thumbnail != null)
//					return thumbnail;
//			}

        // Missing Cache, Get the image From web
//			try {
//				
//				URL imageUrl = new URL(url);
//				
//				HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
//				conn.setConnectTimeout(30000);
//				conn.setReadTimeout(30000);
//				conn.setInstanceFollowRedirects(true);
//				
//				// caching as a file.
//				InputStream is=new BufferedInputStream(conn.getInputStream(), BUFFER_SIZE);
//				thumbnail = BitmapFactory.decodeStream(is);
//				try {
//					// TODO
//					// http://stackoverflow.com/questions/1239026/how-to-create-a-file-in-android
//
//					OutputStream os = new BufferedOutputStream(new FileOutputStream(imageFile), BUFFER_SIZE);
//					try {
//						// save a image file to the disk
//						FileUtils.copyStream(is, os);
//					} finally {
//						os.close();
//					}
//				
//				}finally{
//					is.close();
//				}
//
//				
//
//				return thumbnail;
//
//			} catch (Throwable ex){
//			   ex.printStackTrace();
//			   if(ex instanceof OutOfMemoryError)
//				   mMemoryCache.clear();
//			   return null;
//			}
//		}

        private Bitmap getBitmapFromTheWeb(String url) {
            if (url.equals("")) {
                // When dealing with the rss item which does NOT have image url.
                return null;
            }

            Bitmap bitmap = null;
            // Missing Cache, Get the image From web


            URL imageUrl;
            try {

                imageUrl = new URL(url);

                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);
                conn.setInstanceFollowRedirects(true);

                InputStream is = null;
                try {

                    is = new BufferedInputStream(conn.getInputStream(), BUFFER_SIZE);

                    if (downScaledThumb) {
                        int widthRequested = DOWNSCALE_WIDTH;    // 60dp on 320dpi
                        int heightRequested = DOWNSCALE_HEIGHT;
                        ByteArrayOutputStream bao = FileUtils.convertToByteArrayOutputStream(is);
                        bitmap = Decoder.decodeSampledBitmapFromByteArray(bao.toByteArray(),
                                widthRequested,
                                heightRequested);
                        Log.d("downscale", "bitmap = " + bitmap);
                    } else {
                        bitmap = BitmapFactory.decodeStream(is);
                    }
                } catch (FileNotFoundException e) {
                    // FileNotFoundException
                    //
                    // When the conn.getInputStream() ecounters the 404 error,
                    // FileNotFoundException occurs.
                    // some rss items have unperfect URL information of thumb-nail
                    Log.e("getBitmapFromTheWeb", "FileNotFoundException, Http response code 404");


                } finally {
                    if (is != null) {
                        is.close();
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                }

            } catch (MalformedURLException e1) {
                Log.e("new URL(url)", "MalformedURLException");
                e1.printStackTrace();
            } catch (IOException e) {
                Log.e("convertToByteArrayOutputStream", "io exception");
                e.printStackTrace();
            }

            return bitmap;
        }

    }


    ////////////////////////////////////////////////////
    ////
    //// inner class BitmapDisplayer
    ////

    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        Bitmap mBitmap;
        PhotoToLoad mPhotoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            mBitmap = b;
            mPhotoToLoad = p;
        }

        public void run() {
            if (imageViewRecycled(mPhotoToLoad))
                return;

            if (mBitmap != null) {
//                Bitmap roundedBitmap = getRoundedCornerBitmap(mBitmap);
                mPhotoToLoad.imageView.setImageBitmap(mBitmap);
            } else {
                // {@link #ImageLoader} shows stub_id, before the below code,
                // on {@link #displayImage()} which is invoked from
                // {@link #ChartAdapter.getView()}
                //
                // Thus, if the empty image and no_image has the
                // same image, I think the below code is redundant.

                // mPhotoToLoad.imageView.setImageResource(stub_id);
            }

        }

    }


}
