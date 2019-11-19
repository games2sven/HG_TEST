package com.highgreat.education.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 类说明：  	apk 签名信息获取工具类
 *
 * @date 	2014-5-6
 * @version 1.0
 */
public class SignUtils {


	private static final String TAG = true ? "SignUtils" : SignUtils.class.getSimpleName();

	private static String bytes2Hex(byte[] src) {
		char[] res = new char[src.length * 2];
		final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		for (int i = 0, j = 0; i < src.length; i++) {
			res[j++] = hexDigits[src[i] >>> 4 & 0x0f];
			res[j++] = hexDigits[src[i] & 0x0f];
		}

		return new String(res);
	}

	public static String getMd5ByFile(File file) {
		String value = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);

			MessageDigest digester = MessageDigest.getInstance("MD5");
			byte[] bytes = new byte[8192];
			int byteCount;
			while ((byteCount = in.read(bytes)) > 0) {
				digester.update(bytes, 0, byteCount);
			}
			value = bytes2Hex(digester.digest());
		} catch (Exception e) {
//			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
//					e.printStackTrace();
				}
			}
		}
		return value;
	}

	public static String getMd5ByString(String str) {
		if(str == null) return "";
		try {
			MessageDigest digester = MessageDigest.getInstance("MD5");
			digester.update(str.getBytes());
			return bytes2Hex(digester.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 判断文件的MD5是否为指定值
	 *
	 * @param file
	 * @param md5
	 * @return
	 */
	public static boolean checkMd5(File file, String md5) {
		if (TextUtils.isEmpty(md5)) {
			throw new RuntimeException("md5 cannot be empty");
		}



		String fileMd5 = getMd5ByFile(file);

		LogUtil.d(TAG, String.format("file's md5=%s, real md5=%s", fileMd5, md5));

		return md5.equalsIgnoreCase(fileMd5);
	}

	/**
	 * 判断文件的MD5是否为指定值
	 *
	 * @param filePath
	 * @param md5
	 * @return
	 */
	public static boolean checkMd5(String filePath, String md5) {
		return checkMd5(new File(filePath), md5);
	}
}