package com.highgreat.education.utils;

import android.content.Context;
import android.content.SharedPreferences;


import java.nio.ByteBuffer;

/**
 * 项目名称：TestDot
 * 类描述：
 * 创建人：LiKH_OH
 * 创建时间：2015/9/7 14:27
 * 修改人：LiKH_OH
 * 修改时间：2015/9/7 14:27
 * 修改备注：
 */
public class UavDataPaserUtil {

    /**
     * 存储***上一次的位置
     */
    public static SharedPreferences getSharedPreferences(Context context, String spKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(spKey, Context.MODE_PRIVATE);
        return sharedPreferences;
    }


    public static float bytesToFloat(byte[] var0) {
        return Float.intBitsToFloat(bytesToIntBits(var0));
    }

    public static byte[] floatToBytes(float var0) {
        return intBitsToBytes(Float.floatToRawIntBits(var0));
    }

    public static float intsToFloat(int[] var0) {
        return Float.intBitsToFloat(intsToIntBits(var0));
    }

    public static int[] floatToInts(float var0) {
        return intBitsToInts(Float.floatToRawIntBits(var0));
    }

    public static byte[] intBitsToBytes(int var0, int var1) {
        return var1 == 1 ? intBitsToBytes1(var0) :
                (var1 == 2 ? intBitsToBytes2(var0) :
                        (var1 == 3 ? intBitsToBytes3(var0) :
                                (var1 == 4 ? intBitsToBytes(var0) : new byte[]{(byte) 0})));
    }

    public static int bytesToIntBits(byte[] var0, int var1) {
        return var1 == 1 ? bytes1ToIntBits(var0) :
                (var1 == 2 ? bytes2ToIntBits(var0) :
                        (var1 == 3 ? bytes3ToIntBits(var0) :
                                (var1 == 4 ? bytesToIntBits(var0) : 0)));
    }

    public static int bytesToIntBitsNew(byte[] var0, int var1) {
        return var1 == 1 ? bytes1ToIntBits(var0) :
                (var1 == 2 ? bytes2ToIntBitsNews(var0) :
                        (var1 == 3 ? bytes3ToIntBits(var0) :
                                (var1 == 4 ? bytesToIntBits(var0) : 0)));
    }

    public static byte[] intBitsToBytes(int var0) {
        return new byte[]{
                (byte) (var0 & 255),
                (byte) (var0 >>> 8 & 255),
                (byte) (var0 >>> 16 & 255),
                (byte) (var0 >>> 24 & 255)
        };
    }

    public static int bytesToIntBits(byte[] var0) {
        return (var0[3] & 255) << 24 | (var0[2] & 255) << 16 | (var0[1] & 255) << 8 | (var0[0] & 255) << 0;
    }

    public static int bytesToIntBits2(byte[] var0) {
        return (var0[0] & 255) << 24 | (var0[1] & 255) << 16 | (var0[2] & 255) << 8 | (var0[3] & 255) << 0;
    }

    public static int bytes1ToIntBits(byte[] var0) {
        return (var0[0] & 255) ;
       /* //TODO 修改过
        int i=var0[0];
        if(i<0) i += 256;
        return i;*/
    }

    public static byte[] intBitsToBytes1(int var0) {
        return new byte[]{(byte) (var0& 255)};
    }

    public static int bytes2ToIntBits(byte[] var0) {
        return (var0[1] & 255) << 8 | (var0[0] & 255);

    }

    public static int bytes2ToIntBitsNews(byte[] var0) {

        int x = var0[0];
        int y = var0[1];

        if (x < 0) x += 256;
        if (y < 0) y += 256;

        int v = y + x * 256;
        if (v > 32767)
            v -= 65535;

        return v;
    }


    public static int bytes2ToIntBitsSigned(byte[] var0) {
        return var0[1] < 0 ? -65536 | (var0[1] & 255) << 8 | (var0[0] & 255) : (var0[1] & 255) << 8 | (var0[0] & 255);
    }

    public static int bytes3ToIntBitsSigned(byte[] var0) {
        return var0[2] < 0 ? -16777216 | (var0[2] & 255) << 16 | (var0[1] & 255) << 8 | (var0[0] & 255) : (var0[2] & 255) << 16 | (var0[1] & 255) << 8 | (var0[0] & 255);
    }

    public static byte[] intBitsToBytes2(int var0) {
        return new byte[]{(byte) (var0 & 255), (byte) (var0 >>> 8 & 255)};
    }

    public static byte[] intBitsToBytes2Signed(int var0) {
        return intBitsToBytes2(var0);
    }

    public static byte[] intBitsToBytes3Signed(int var0) {
        return intBitsToBytes3(var0);
    }


    public static int ints2ToIntBits(int[] var0) {
        return (var0[1] & 255) << 8 | (var0[0] & 255);
    }

    public static int[] intBitsToInts2(int var0) {
        return new int[]{var0 & 255, var0 >>> 8 & 255};
    }

    //低位在前，高位在后
    public static byte[] intBitsToByte2(int var0) {
        return new byte[]{(byte) (var0 >> 8), (byte) (var0 & 0xFF)};
    }

    public static int intsToIntBits(int[] var0) {
        return (var0[3] & 255) << 24 | (var0[2] & 255) << 16 | (var0[1] & 255) << 8 | (var0[0] & 255);
    }

    public static int[] intBitsToInts(int var0) {
        return new int[]{var0 & 255, var0 >>> 8 & 255, var0 >>> 16 & 255, var0 >>> 24 & 255};
    }

    public static int bytes3ToIntBits(byte[] var0) {
        return (var0[2] & 255) << 16 | (var0[1] & 255) << 8 | (var0[0] & 255);
    }

    public static byte[] intBitsToBytes3(int var0) {
        return new byte[]{(byte) (var0 & 255), (byte) (var0 >>> 8 & 255), (byte) (var0 >>> 16 & 255)};
    }

    public static byte[] copyBytes(byte[] var0, int srcPoc, int length) {
        byte[] des = new byte[length];
        System.arraycopy(var0, srcPoc, des, 0, length);
        return des;
    }


    public static String bytesToChars(byte[] var0) {
        if (null != var0) {
            StringBuilder var1 = new StringBuilder();
            for (byte data : var0) {
                if(data == -1){//byte -1 = 0xFF
                    return "";
                }
                var1.append(Character.toChars(getUnsignedByte(data)));
            }
            return var1.toString();
        } else {
            return null;
        }
    }

    //add by cb
    public static String bytes2String(byte[] filgitSN) {
        int index = 0;
        for (byte b : filgitSN){//如果前面是0，去掉
            if (b == 48){
                index ++;
            } else {
                break;
            }
        }
        int len = filgitSN.length - index;
        if (len <= 0) return "";
        byte[] fsn = new byte[len];
        System.arraycopy(filgitSN, index, fsn, 0 , len);
        return bytesToChars(fsn);
    }


    public static byte[] str2Byte(String str) {
        byte[] byteStr = str.getBytes();
        ByteBuffer var2 = ByteBuffer.allocate(byteStr.length + 1);
        var2.put((byte) byteStr.length);
        var2.put(byteStr);
        return var2.array();
    }


    public static byte[] convertIntsToBytes(int[] var0) {
        if (null == var0) {
            return null;
        } else {
            byte[] var1 = new byte[var0.length];

            for (int var2 = 0; var2 < var0.length; ++var2) {
                var1[var2] = intBitsToBytes1(var0[var2])[0];
            }

            return var1;
        }
    }


    public static String bytesToHexString(byte[] var0) {
        StringBuilder var1 = new StringBuilder("");
        if (var0 != null && var0.length > 0) {
            for (byte aVar0 : var0) {
                String var3;
                if ((var3 = Integer.toHexString(aVar0 & 255)).length() < 2) {
                    var1.append(0);
                }

                var1.append(var3);
            }

            return var1.toString();
        } else {
            return "";
        }
    }

    public static String byteToHexString(byte var0) {
        StringBuilder var1 = new StringBuilder("");
        String var3;
        if ((var3 = Integer.toHexString(var0 & 255)).length() < 2) {
            var1.append(0);
        }
        var1.append(var3);
        return var1.toString();
    }

    public static int getUnsignedByte(byte var0) {
        int var1 = var0;
        if (var0 < 0) {
            var1 = var0 + 256;
        }

        return var1;
    }

    public static int get2ByteToInt(byte var0, byte var1) {
        return var1 << 8 | getUnsignedByte(var0);
    }

    public static byte[] toHLShort(short var0) {
        LogUtil.d("byteUtil", "ByteUtil -> toHLShort var0 =  " + var0);
        byte[] var1;
        (var1 = new byte[2])[0] = (byte) var0;
        var1[1] = (byte) (var0 >>> 8);
        return var1;
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

    public static byte getCheckSum(byte[] var0) {
        if (null != var0 && var0.length > 0) {
            byte var1 = 0;

            for (int var2 = 0; var2 < var0.length - 1; ++var2) {
                int var3 = var1 + bytes1ToIntBits(new byte[]{var0[var2]});
                var1 = intBitsToBytes1(var3)[0];
            }

            return intBitsToBytes1(var1)[0];
        } else {
            return (byte) 0;
        }
    }


    public static boolean checkSumFailed(byte[] var0) {
        return !(null != var0 && var0.length > 0) || getCheckSum(var0) != var0[var0.length - 1];
    }

    public static boolean resetCheckSum(byte[] var0) {
        if (null != var0 && var0.length > 0) {
            var0[var0.length - 1] = getCheckSum(var0);
        }

        return true;
    }

    /**
     * 将4个byte转为int, var1为起始位置
     */
    public static int get4ByteToInt(byte[] var0, int var1) {
        return var0[var1 + 3] << 24 | getUnsignedByte(var0[var1 + 2]) << 16 | getUnsignedByte(var0[var1 + 1]) << 8 | getUnsignedByte(var0[var1]);
    }

    /**
     * 将4个byte转为int, var1为起始位置 小大端
     */
    public static int get4ByteToIntLH(byte[] var0, int var1) {
        return var0[var1] << 24 | getUnsignedByte(var0[var1 + 1]) << 16 | getUnsignedByte(var0[var1 + 2]) << 8 | getUnsignedByte(var0[var1 + 3]);
    }


    public static byte getCalibration(byte[] var0) {
        byte var1 = 0;
        int len = var0.length - 1;
        for (int var2 = 0; var2 < len; ++var2) {
            var1 += var0[var2];
        }
        return var1;
    }
    /**
     * UBX  搜星的校验和方法
     * @param data  数据
     * @return
     */
    public static boolean getPositiveCalibration(byte[] data) {
        if (null != data) {
            if (data.length != 0) {
                byte cka = 0, ckb = 0;
                int length = data.length;
                for (int i = 2; i < length - 2; ++i) {
                    cka += data[i];
                    ckb += cka;
                }
                return (cka == data[length - 2]) && (ckb == data[length - 1]);
            }
        }
        return false;
    }

    /**
     * 用于时间设置
     */
    public static byte[] toHL(long var0) {
        byte[] var1;
        (var1 = new byte[8])[0] = (byte) var0;
        var1[1] = (byte) (var0 >> 8);
        var1[2] = (byte) (var0 >> 16);
        var1[3] = (byte) (var0 >> 24);
        var1[4] = (byte) (var0 >> 32);
        var1[5] = (byte) (var0 >> 40);
        var1[6] = (byte) (var0 >> 48);
        var1[7] = (byte) (var0 >>> 56);
        return var1;
    }

    public static byte[] short2Byte(short var0) {
        byte[] var1 = new byte[2];
        for (int var2 = 0; var2 < 2; ++var2) {
            var1[1 - var2] = (byte) (var0 >> 8 * (1 - var2));
        }
        return var1;
    }

    public static byte[] int2Byte(int var0) {
        byte[] var1 = new byte[4];

        for (int var2 = 0; var2 < 4; ++var2) {
            var1[var2] = (byte) (var0 >> 8 * (3 - var2));
        }
        return var1;
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
    }

    public static long byte8ToLong(byte[] data) {
        return ((data[7] & 0xffL) << 56) | ((data[6] & 0xff) << 48) | ((data[5] & 0xff) << 40) | ((data[4] & 0xff) << 32) | ((data[3] & 0xff) << 24) | ((data[2] & 0xff) << 16) | ((data[1] & 0xff) << 8) | data[0] & 0xff;
//        return data[0] & 0xff | data[1] & 0xff << 8 | data[2] & 0xff << 16 | data[3] & 0xff << 24 | data[4] & 0xff << 32 | data[5] & 0xff << 40 | data[6] & 0xff << 48 | data[7] & 0xff << 56;
    }
    /**用于接收时间设置*/
    public static byte[] toLH_long(byte[] var0, int start) {//000001529bd6ef03
        byte[] var1;
        (var1 = new byte[8])[7] = var0[start];
        var1[6] = var0[ start + 1];
        var1[5] = var0[start + 2];
        var1[4] = var0[start + 3];
        var1[3] = var0[start + 4];
        var1[2] = var0[start + 5];
        var1[1] = var0[start + 6];
        var1[0] = var0[start + 7];
        return var1;
    }



}
