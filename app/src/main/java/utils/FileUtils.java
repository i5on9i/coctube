package utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {


    private static String TAG = "FileUtils";

    /**
     * If the directory does not exist,
     * NullPointerException occurs from File class constructor
     *
     * @param path
     * @return null, if there is no file in the folder,
     */
    public static File[] getListOfFilesFromExternalStorage(String path) {

        File parentPath = Environment.getExternalStorageDirectory();
        File rootPath = new File(parentPath, path);
        File[] list = rootPath.listFiles();
        return list;

    }

    public static boolean exists(File parentPath, String path) {
        File file = new File(parentPath, path);
        if (file.exists())
            return true;
        else
            return false;

    }

    public static boolean exists(String path) {
        File file = new File(path);

        if (file.exists())
            return true;
        else
            return false;


    }

    public static boolean existsOnExternalStorage(String path) {

        File parentPath = Environment.getExternalStorageDirectory();
        return exists(parentPath, path);
    }


    public static void deleteExternalStorageFile(String path) {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        File file = new File(externalStorageDirectory, path);
        if(file.isFile()){
            file.delete();
        }

    }

    /**
     *
     * @param is
     * @param destPath
     * @return false, only the file already has been existed
     * @throws java.io.IOException
     */
    public static boolean saveToExternalStorageAsFile(InputStream is, String destPath) throws IOException {
        // write the inputStream to a FileOutputStream

        File parentPath = Environment.getExternalStorageDirectory();
        OutputStream os = null;
        try {

            File file = new File(parentPath, destPath);

            if (file.isFile()) {
                Log.i(TAG, "file has been existed");
                return false;

            } else {
                File parent = new File(file.getParent());
                parent.mkdirs();

                os = new FileOutputStream(file);

                int read = 0;
                byte[] bytes = new byte[1024];

                while ((read = is.read(bytes)) != -1) {
                    os.write(bytes, 0, read);
                }

            }

        } finally {
            if (os != null) {
                os.close();
            }
        }
        return true;
    }



}
