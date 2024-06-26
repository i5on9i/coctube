package com.cocube.imageloader.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DiskLruImageCache {

    private DiskLruCache mDiskCache;
    private CompressFormat mCompressFormat = CompressFormat.JPEG;
    private int mCompressQuality = 70;
    private static final int APP_VERSION = 1;
    private static final int VALUE_COUNT = 1;
    private static final String TAG = "DiskLruImageCache";

	private static final FileNameGenerator mFileNameGenerator = new Md5FileNameGenerator();
    
    private static final boolean debug = true;

    public DiskLruImageCache( Context context,String uniqueName, int diskCacheSize,
        CompressFormat compressFormat, int quality ) {
        	try {
                final File diskCacheDir = getDiskCacheDir(context, uniqueName );
                mDiskCache = DiskLruCache.open( diskCacheDir, APP_VERSION, VALUE_COUNT, diskCacheSize );
                mCompressFormat = compressFormat;
                mCompressQuality = quality;
				
            } catch (IOException e) {
                e.printStackTrace();
            }
    }


    private boolean writeBitmapToFile( Bitmap bitmap, DiskLruCache.Editor editor )
        throws IOException, FileNotFoundException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream( editor.newOutputStream( 0 ), Utils.IO_BUFFER_SIZE );
            return bitmap.compress( mCompressFormat, mCompressQuality, out );
        } finally {
            if ( out != null ) {
                out.close();
            }
        }
    }

    private File getDiskCacheDir(Context context, String uniqueName) {

    // Check if media is mounted or storage is built-in, if so, try and use external cache dir
    // otherwise use internal cache dir
        final String cachePath =
            Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED ||
                    !Utils.isExternalStorageRemovable() ?
                    Utils.getExternalCacheDir(context).getPath() :
                    context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }

    public void put( String seed, Bitmap data ) {

        DiskLruCache.Editor editor = null;
        String key = mFileNameGenerator.generate(seed);
        try {
			
            editor = mDiskCache.edit( key );
            if ( editor == null ) {
                return;
            }

            if( writeBitmapToFile( data, editor ) ) {               
                mDiskCache.flush();
                editor.commit();
                if ( debug ) {
                   Log.d("cache_test_DISK_", "image put on disk cache " + key);
                }
            } else {
                editor.abort();
                if ( debug ) {
                    Log.d("cache_test_DISK_", "ERROR on: image put on disk cache " + key);
                }
            }   
        } catch (IOException e) {
            if ( debug ) {
                Log.d("cache_test_DISK_", "ERROR on: image put on disk cache " + key);
            }
            try {
                if ( editor != null ) {
                    editor.abort();
                }
            } catch (IOException ignored) {
            }           
        }

    }

    public Bitmap getBitmap( String seed ) {

		String key = mFileNameGenerator.generate(seed);
		Bitmap bitmap = null;
		DiskLruCache.Snapshot snapshot = null;
		try {

			snapshot = mDiskCache.get( key );
			if ( snapshot == null ) {
				return null;
			}
			final InputStream in = snapshot.getInputStream( 0 );
			if ( in != null ) {
				final BufferedInputStream buffIn =
				new BufferedInputStream( in, Utils.IO_BUFFER_SIZE );
				bitmap = BitmapFactory.decodeStream(buffIn);
			}   
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		finally {
			if ( snapshot != null ) {
				snapshot.close();
			}
		}


		if ( debug ) {
			Log.d("cache_test_DISK_", bitmap == null ? "" : "image read from disk " + key);
		}

		return bitmap;

    }

    public boolean containsKey( String seed ) {

        boolean contained = false;
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = mDiskCache.get( mFileNameGenerator.generate(seed) );
            contained = (snapshot != null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if ( snapshot != null ) {
                snapshot.close();
            }
        }

        return contained;

    }

    public void clearCache() {
        if ( debug ) {
            Log.d("cache_test_DISK_", "disk cache CLEARED");
        }
        try {
            mDiskCache.delete();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public File getCacheFolder() {
        return mDiskCache.getDirectory();
    }

}