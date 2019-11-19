package com.highgreat.education.utils;


import android.text.TextUtils;
import android.util.Log;

import java.nio.ByteBuffer;

/**
 * Created by liangzi on 2016/1/7.
 */
public class ByteUtil {
    public ByteUtil() {
    }

    private static String hexStr = "0123456789ABCDEF";
    private static String[] binaryArray =
            {"0000", "0001", "0010", "0011",
                    "0100", "0101", "0110", "0111",
                    "1000", "1001", "1010", "1011",
                    "1100", "1101", "1110", "1111"};

    public static byte[] allocate(int len) {
        return new byte[len];
    }

    /**
     * 将byte数组用字符串形式展现出来
     *
     * @param var0
     * @return
     */
    public static String bytesToHexString(byte[] var0) {
        StringBuilder var1 = new StringBuilder("");
        if (var0 != null && var0.length > 0) {
            for (byte aVar0 : var0) {
                String var3;
                if ((var3 = Integer.toHexString(aVar0 & 255)).length() < 2) {
                    var1.append(0);
                }
                var1.append(var3 + " ");
            }
            return var1.toString();
        } else {
            return "";
        }
    }

    /**
     * @param hexString
     * @return 将十六进制转换为二进制字节数组   16-2
     */
    public static byte[] hexStr2BinArr(String hexString) {
        //hexString的长度对2取整，作为bytes的长度
        int len = hexString.length() / 2;
        byte[] bytes = new byte[len];
        byte high = 0;//字节高四位
        byte low = 0;//字节低四位
        for (int i = 0; i < len; i++) {
            //右移四位得到高位
            high = (byte) ((hexStr.indexOf(hexString.charAt(2 * i))) << 4);
            low = (byte) hexStr.indexOf(hexString.charAt(2 * i + 1));
            bytes[i] = (byte) (high | low);//高地位做或运算
        }
        return bytes;
    }



    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    private static byte charToByte(char var0) {
        return (byte) "0123456789ABCDEF".indexOf(var0);
    }

    public static byte getCalibration(byte[] var0) {
        byte var1 = 0;

        for (int var2 = 0; var2 < var0.length - 1; ++var2) {
            var1 += var0[var2];
        }

        return var1;
    }
    public static String bytesToHexFun3(short[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for(short b : bytes) { // 使用String的format方法进行转换
            buf.append(String.format("%02x", new Integer(b & 0xff)));
        }

        return buf.toString();
    }
    public static byte[] float2byte(float var0) {
        int var3 = Float.floatToIntBits(var0);
        byte[] var1 = new byte[4];

        int var2;
        for (var2 = 0; var2 < 4; ++var2) {
            var1[var2] = (byte) (var3 >> 24 - (var2 << 3));
        }

        byte[] var4 = new byte[4];
        System.arraycopy(var1, 0, var4, 0, 4);

        for (var2 = 0; var2 < 2; ++var2) {
            byte var5 = var4[var2];
            var4[var2] = var4[4 - var2 - 1];
            var4[4 - var2 - 1] = var5;
        }

        return var4;
    }

    public static float byte2float(byte[] b) {
        int accum = 0;
        accum = accum|(b[0] & 0xff) << 0;
        accum = accum|(b[1] & 0xff) << 8;
        accum = accum|(b[2] & 0xff) << 16;
        accum = accum|(b[3] & 0xff) << 24;
        System.out.println(accum);
        return Float.intBitsToFloat(accum);

    }
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] b = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            b[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return b;
    }
    public static final String bytesToHexString3(short[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
    public static byte[] int2Byte(int var0) {
        byte[] var1 = new byte[4];

        for (int var2 = 0; var2 < 4; ++var2) {
            var1[var2] = (byte) (var0 >> 8 * (3 - var2));
        }

        return var1;
    }

    public static byte[] short2Byte(short var0) {
        byte[] var1 = new byte[2];
        for (int var2 = 0; var2 < 2; ++var2) {
            var1[1 - var2] = (byte) (var0 >> 8 * (1 - var2));
        }
        return var1;
    }

    public static byte[] long2Byte(long var0) {
        byte[] var1 = new byte[8];

        for (int var2 = 0; var2 < 8; ++var2) {
            var1[var2] = (byte) (var0 >> 8 * (7 - var2));
        }

        return var1;
    }

    public static byte[] long2Bytes(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getLong();
    }


    /**
     * byte数组转换为int
     * var1 为数组起始位置
     */
    public static int byte2Int(byte[] var0, int var1) {
        return (var0[var1] << 24) + (var0[var1 + 1] << 16) + (var0[var1 + 2] << 8) + var0[var1 + 3];
    }

    public static int getUnsignedByte(byte var0) {
        int var1 = var0;
        if (var0 < 0) {
            var1 = var0 + 256;
        }
        return var1;
    }

    /**
     * crc校验结果转为两位byte数组
     **/
    public static byte[] computeCheckBytes(int result) {
        short crc = (short) result;
        return new byte[]{(byte) (crc >> 8), (byte) (crc & 0x00ff)};
    }

    /**
     * 将一个byte转为int, var1为起始位置
     */
    public static int byteToInt(byte[] var0, int var1) {
        return getUnsignedByte(var0[var1]) | var0[var1 + 1] << 8;
    }

    public static int get2UnsignedByteToInt(byte[] var0, int var1) {
        int var2 = getUnsignedByte(var0[var1 + 1]);
        int var3 = getUnsignedByte(var0[var1]);
        return var2 << 8 | var3;
    }

    public static int get2ByteToInt(byte var0, byte var1) {
        return var1 << 8 | getUnsignedByte(var0);
    }

    //byte 数组与 int 的相互转换
    public static int byteArrayToInt(byte[] b) {
        byte[] a = new byte[4];
        int i = a.length - 1, j = b.length - 1;
        for (; i >= 0; i--, j--) {//从b的尾部(即int值的低位)开始copy数据
            if (j >= 0)
                a[i] = b[j];
            else
                a[i] = 0;//如果b.length不足4,则将高位补0
        }
        int v0 = (a[0] & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        int v1 = (a[1] & 0xff) << 16;
        int v2 = (a[2] & 0xff) << 8;
        int v3 = (a[3] & 0xff);
        return v0 + v1 + v2 + v3;
    }

    // 任意长度数组转为有符号int
    public static int toInt(byte[] bRefArr) {
        int iOutcome = 0;
        byte bLoop;
        for (int i = 0; i < bRefArr.length; i++) {
            bLoop = bRefArr[i];
            iOutcome += (bLoop & 0xFF) << (8 * i);
        }
        return iOutcome;
    }

    /**
     * 将4个byte转为int, var1为起始位置
     */
    public static int get4ByteToInt(byte[] var0, int var1) {
        return var0[var1 + 3] << 24 | getUnsignedByte(var0[var1 + 2]) << 16 | getUnsignedByte(var0[var1 + 1]) << 8 | getUnsignedByte(var0[var1]);
    }

    /**
     * 将8个byte（java 字节序从低到高LH）转为int, var1为起始位置
     */
    public static long get8ByteToLong(byte[] var0) {
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (var0[ix] & 0xff);
        }
        return num;
//        return getUnsignedByte(var0[var1 + 7]) << 56 | getUnsignedByte(var0[var1 + 6]) << 48 | getUnsignedByte(var0[var1 + 5]) << 40 |getUnsignedByte(var0[var1 + 4]) << 32 | getUnsignedByte(var0[var1 + 3]) << 24 | getUnsignedByte(var0[var1 + 2]) << 16 | getUnsignedByte(var0[var1 + 1]) << 8 | getUnsignedByte(var0[var1]);
//        return getUnsignedByte(var0[var1]) | getUnsignedByte(var0[var1 + 1]) << 8 | getUnsignedByte(var0[var1 + 2]) << 16 |  getUnsignedByte(var0[var1 + 3]) << 24 |getUnsignedByte(var0[var1 + 4]) << 32 | getUnsignedByte(var0[var1 + 5]) << 40 | getUnsignedByte(var0[var1 + 6]) << 48 | getUnsignedByte(var0[var1 + 7]) << 56 ;
    }

    public static String byteToBitString(byte var0) {
        return "" + (byte) (var0 >> 7 & 1) + (byte) (var0 >> 6 & 1) + (byte) (var0 >> 5 & 1) + (byte) (var0 >> 4 & 1) + (byte) (var0 >> 3 & 1) + (byte) (var0 >> 2 & 1) + (byte) (var0 >> 1 & 1) + (byte) (var0 & 1);
    }

    public static byte[] toLH(int var0) {
        byte[] var1;
        (var1 = new byte[4])[0] = (byte) var0;
        var1[1] = (byte) (var0 >> 8);
        var1[2] = (byte) (var0 >> 16);
        var1[3] = (byte) (var0 >>> 24);
        return var1;
    }

    public static byte[] toHL(int var0) {
        byte[] var1;
        (var1 = new byte[4])[3] = (byte) var0;
        var1[2] = (byte) (var0 >> 8);
        var1[1] = (byte) (var0 >> 16);
        var1[0] = (byte) (var0 >>> 24);
        return var1;
    }

    /**
     * 用于时间设置
     */
    public static byte[] toHL(long var0) {//000001529bd6ef03
        byte[] var1;
        (var1 = new byte[8])[0] = (byte) var0;
//        var1[6] = (byte)(var0 >> 8);
//        var1[5] = (byte)(var0 >> 16);
//        var1[4] = (byte)(var0 >> 24);
//        var1[3] = (byte)(var0 >> 32);
//        var1[2] = (byte)(var0 >> 40);
//        var1[1] = (byte)(var0 >> 48);
//        var1[0] = (byte)(var0 >>> 56);
        var1[1] = (byte) (var0 >> 8);
        var1[2] = (byte) (var0 >> 16);
        var1[3] = (byte) (var0 >> 24);
        var1[4] = (byte) (var0 >> 32);
        var1[5] = (byte) (var0 >> 40);
        var1[6] = (byte) (var0 >> 48);
        var1[7] = (byte) (var0 >>> 56);
        return var1;
    }

    /**
     * 用于接收时间设置
     */
    public static byte[] toLH_long(byte[] var0, int start) {//000001529bd6ef03
        byte[] var1;
        (var1 = new byte[8])[7] = var0[start];
        var1[6] = var0[start + 1];
        var1[5] = var0[start + 2];
        var1[4] = var0[start + 3];
        var1[3] = var0[start + 4];
        var1[2] = var0[start + 5];
        var1[1] = var0[start + 6];
        var1[0] = var0[start + 7];
        return var1;
    }

    public static byte[] toHLShort(short var0) {
        byte[] var1;
        (var1 = new byte[2])[0] = (byte) var0;
        var1[1] = (byte) (var0 >>> 8);
        return var1;
    }

    public static String bit2String(byte var0) {
        StringBuffer varStr = new StringBuffer();
        int d = var0 & 0xf0000000;
        varStr.append(var0 & 0xf0000000);
        varStr.append(var0 & 0x0f000000);
        varStr.append(var0 & 0x00f00000);
        varStr.append(var0 & 0x000f0000);
        varStr.append(var0 & 0x0000f000);
        varStr.append(var0 & 0x00000f00);
        varStr.append(var0 & 0x000000f0);
        varStr.append(var0 & 0x0000000f);
        return varStr.toString();
    }

    public static byte[] str2Byte(String str) {
        byte[] byteStr = str.getBytes();
        ByteBuffer var2 = ByteBuffer.allocate(byteStr.length + 1);
        var2.put((byte) byteStr.length);
        var2.put(byteStr);
        return var2.array();
    }

    public static byte[] bytesToByteArr(byte[] des, int offset, int length) {

        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer = buffer.get(des, offset, length);
        return buffer.array();
    }

    public static byte[] sps(byte[] ch, int offset) {
        int spsLength = getUnsignedByte(ch[offset]);

        byte[] sps = new byte[spsLength];

        for (int i = 0; i < spsLength; i++)
            sps[i] = ch[i + 1 + offset];

        return sps;
    }

    public static byte[] pps(byte[] ch, int offset) {
        int ppsLength = getUnsignedByte(ch[offset]);

        byte[] pps = new byte[ppsLength];

        for (int i = 0; i < ppsLength; i++)
            pps[i] = ch[i + 1 + offset];
        return pps;
    }

    //java 合并两个byte数组
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    /**
     * @param hexString
     * @return 16进制转二进制
     */
    public static String hexString2binaryString(String hexString) {
//        if (hexString == null || hexString.length() % 2 != 0)
//            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000"
                    + Integer.toBinaryString(Integer.parseInt(hexString
                    .substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    public static byte[] hex2byte(String str) { // 字符串转二进制
        if (str == null)
            return null;
        str = str.trim();
        int len = str.length();
        if (len == 0 || len % 2 == 1)
            return null;

        byte[] b = new byte[len / 2];
        try {
            for (int i = 0; i < str.length(); i += 2) {
                b[i / 2] = (byte) Integer
                        .decode("0x" + str.substring(i, i + 2)).intValue();
            }
            return b;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * lch 把字节数组转换成16进制字符串
     *
     * @param bArray 字节数组
     * @return String 转换成的字符串
     */
    public static final String bytesToHexString2(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        int len = bArray.length;
        int last = len - 1;
        for (int i = 0; i < len; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append("0");
            sb.append(sTemp.toUpperCase());
            if (i != last)
                sb.append("-");
        }
        return sb.toString();
    }

    /**
     * @param bArray
     * @return
     * @author lch 把字节数组转换成16进制字符串,不带分隔符
     */
    public static final String bytesToHexStringNoBar(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        int len = bArray.length;
        int last = len - 1;
        for (int i = 0; i < len; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append("0");
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static long bytes2Long(byte[] bytes) {
        ByteBuffer bf = ByteBuffer.allocate(8);
        bf.put(bytes, 0, bytes.length);
        bf.flip();
        return bf.getLong();
    }


    public static short[] string2HexShort(String HexString){
          short[] s  =  new short[HexString.length()/2];
          for (int i=0;i<HexString.length();i+=2){
              String  hex =HexString.substring(i,i+2);
              int a =Integer.valueOf(hex, 16);
              s[i/2] = (short) a;
          }
        return s;
    }


    public static int[] string2HexByte(String HexString) {
        int length = HexString.length();
        int[] array = null;
        if (length % 2 == 1) {
            array = new int[length / 2 + 1];
            HexString = "0" + HexString;
            length += 1;
        } else
            array = new int[length / 2];
        int i = 0;
        int index = 0;
        while (i < length) {
            String data = HexString.substring(i, i + 2);
             array[index] =  HexToInt(data);
            i += 2;
            index++;
        }
        return array;
    }

    /*将大写字母转换成小写字母*/
    public static int tolower(int c) {
        if (c >= 'A' && c <= 'Z') {
            return c + 'a' - 'A';
        } else
            return c;
    }

    /**将两位字符转为16进制**/
    public static int HexToInt(String str){
        int n = 0;
        char first = str.charAt(0);
        char second = str.charAt(1);
        int sum1 = 0;
        int sum2 = 0;
        if(first>=tolower('a')){
            sum1 = 16*(10 + (tolower(first) - 'a'));
        }else {
            sum1 = 16*(first-48);
        }
        if(second>=tolower('a')){
            sum2 = 10 + (tolower(second) - 'a');
        }else{
            sum2 = (second-48);
        }
        n = sum1 + sum2;
        return n;
    }


//    public static void saveToSDByte(byte[] dataResult) {
//        FileOutputStream os = null;//文件输出流用于写文件
//        String temp=null;//录音数据字符串的形式保存在该文件中，便于做仿真对比
//        // 数据存储文件
//        String fileNameTemp ="video_log.txt";
//        // 获得SD卡路径
//        File file = new File(OtherFinals.DIR_VIDEO_LOG+fileNameTemp);
//
//            try {
//                os=new FileOutputStream(file);
//                os.write(dataResult);
//            } catch (Exception e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();
//            }
//            finally{
//                if(os!=null)
//                {
//                    try {
//                        os.close();
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//
//    }

    public static byte[] intToBytes(int value){
        byte[] byte_src = new byte[4];
        byte_src[3] = (byte) ((value & 0xFF000000)>>24);
        byte_src[2] = (byte) ((value & 0x00FF0000)>>16);
        byte_src[1] = (byte) ((value & 0x0000FF00)>>8);
        byte_src[0] = (byte) ((value & 0x000000FF));
        return byte_src;
    }

    public final byte[] longToBytes(long v) {
        byte[] writeBuffer = new byte[ 8 ];

        writeBuffer[0] = (byte)(v >>> 56);
        writeBuffer[1] = (byte)(v >>> 48);
        writeBuffer[2] = (byte)(v >>> 40);
        writeBuffer[3] = (byte)(v >>> 32);
        writeBuffer[4] = (byte)(v >>> 24);
        writeBuffer[5] = (byte)(v >>> 16);
        writeBuffer[6] = (byte)(v >>>  8);
        writeBuffer[7] = (byte)(v >>>  0);

        return writeBuffer;
    }
    public static short[] toShortArray(byte[] src) {

        int count = src.length >> 1;
        short[] dest = new short[count];
        for (int i = 0; i < count; i++) {
            dest[i] = (short) (src[i * 2] << 8 | src[2 * i + 1] & 0xff);
        }
        return dest;
    }

    public static byte[] toByteArray(short[] src) {

        int count = src.length;
        byte[] dest = new byte[count << 1];
        for (int i = 0; i < count; i++) {
            dest[i * 2] = (byte) (src[i] >> 8);
            dest[i * 2 + 1] = (byte) (src[i] >> 0);
        }

        return dest;
    }

    /**
     * 将8个byte（java 字节序从低到高LH）转为int, var1为起始位置
     */
    public static int get8ByteToInt(byte[] var0) {
        int num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (var0[ix] & 0xff);
        }
        return num;
//        return getUnsignedByte(var0[var1 + 7]) << 56 | getUnsignedByte(var0[var1 + 6]) << 48 | getUnsignedByte(var0[var1 + 5]) << 40 |getUnsignedByte(var0[var1 + 4]) << 32 | getUnsignedByte(var0[var1 + 3]) << 24 | getUnsignedByte(var0[var1 + 2]) << 16 | getUnsignedByte(var0[var1 + 1]) << 8 | getUnsignedByte(var0[var1]);
//        return getUnsignedByte(var0[var1]) | getUnsignedByte(var0[var1 + 1]) << 8 | getUnsignedByte(var0[var1 + 2]) << 16 |  getUnsignedByte(var0[var1 + 3]) << 24 |getUnsignedByte(var0[var1 + 4]) << 32 | getUnsignedByte(var0[var1 + 5]) << 40 | getUnsignedByte(var0[var1 + 6]) << 48 | getUnsignedByte(var0[var1 + 7]) << 56 ;
    }

    /**格式化版本号**/
    public  static String formatVersionName(long version){
        String hexString  = Integer.toHexString((int) version);
        while (hexString.length()<8){//不足8位 补齐8位
            hexString="0"+hexString;
        }
        int  lenght=hexString.length();
        String mainVersion= (String) hexString.subSequence(lenght-4,lenght-2);
        String sonVersion= (String) hexString.subSequence(lenght-6,lenght-4);
        String lastVersion =(String) hexString.subSequence(lenght-8,lenght-6);
        return  Integer.parseInt(mainVersion,16)+"."+ Integer.parseInt(sonVersion,16)+"."+ Integer.parseInt(lastVersion,16);
    }

    public static byte[] hexString2ByteArr(String test) {
        if (TextUtils.isEmpty(test)) return  null;

        String [] data1 =test.split(" ");
        if (data1==null||data1.length==0) return null ;

        final byte[] packet =new byte[data1.length];
        for (int  i = 0 ;i<data1.length;i++){

            if (isHexString(data1[i].trim())){
                int  j = Integer.valueOf(data1[i].trim(), 16);
                packet[i] = (byte) j;
            }else{

                return  null;
            }

        }

        return  packet;
    }

    public static boolean  isHexString(String hexStr){
        String regex="([0-9A-Fa-f]{2})+";
        if(hexStr.matches(regex)){
            return  true;
        }else{
            return  false;
        }

    }

    /**将字节转为二进制字节数组*/
    public static byte[] byteToBinaryByteArr(long i, int length) {
        int index = 0;
        byte[] buf = new byte[length];
        do {
            buf[index++] = (byte)(i & 1);
        }  while ((i >>>= 1) != 0);
        return buf;
    }


    public static int sumByte(byte[] s2) {

        int sum=0;
        for (byte b:s2){
         sum+=b&0xff;
     }
    return sum;
    }


    /**将三个字节转为int值**/


}
