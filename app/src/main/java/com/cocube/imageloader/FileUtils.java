package com.cocube.imageloader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Provides operations with files
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */

public class FileUtils {
	private static final int BUFFER_SIZE = 8 * 1024; // 8 KB

	private FileUtils() {
	}

    public static void copyStream(InputStream is, OutputStream os) throws IOException {
		byte[] bytes = new byte[BUFFER_SIZE];
		int count = 0;
		while (true) {

			count = is.read(bytes, 0, BUFFER_SIZE);
			if (count == -1) {
				break;
			}
			os.write(bytes, 0, count);
		}
	}

    public static ByteArrayOutputStream convertToByteArrayOutputStream(InputStream is)
																		throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		byte[] bytes = new byte[BUFFER_SIZE];
		int count = 0;
		while (true) {

			count = is.read(bytes, 0, BUFFER_SIZE);
			if (count == -1) {
				break;
			}
			baos.write(bytes, 0, count);
		}
		baos.flush();

		return  baos;
	}
}