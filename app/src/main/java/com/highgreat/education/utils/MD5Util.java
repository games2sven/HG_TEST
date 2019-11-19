package com.highgreat.education.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chengbin on 2016/4/25.
 * MD5工具类
 */
public class MD5Util {

    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    //文件MD5
    public static String getFileMD5(File file) {
        if (!file.isFile()){
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in=null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
//        BigInteger bigInt = new BigInteger(1, digest.digest());
//
//        String  s =bigInt.toString(16);

        return toHexString(digest.digest());
    }

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        String res = formatter.toString();
        formatter.close();

        return res;
    }

    public static Map<String, String> getDirMD5(File file, boolean listChild) {
        if(!file.isDirectory()){
            return null;
        }
        //<filepath,md5>
        Map<String, String> map=new HashMap<String, String>();
        String md5;
        File files[]=file.listFiles();
        for(int i=0;i<files.length;i++){
            File f=files[i];
            if(f.isDirectory()&&listChild){
                map.putAll(getDirMD5(f, listChild));
            } else {
                md5=getFileMD5(f);
                if(md5!=null){
                    map.put(f.getPath(), md5);
                }
            }
        }
        return map;
    }


}
