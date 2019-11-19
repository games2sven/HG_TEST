package com.highgreat.education.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

/**
 * 项目名称：My Application
 * 类描述： SharedPreferences的一个工具类，调用setParam就能保存
 *          String, Integer, Boolean, Float, Long类型的参数
 *          同样调用getParam就能获取到保存在手机里面的数据
 * 创建人：LiKH_OH
 * 创建时间：2015/9/9 11:12
 * 修改人：LiKH_OH
 * 修改时间：2015/9/9 11:12
 * 修改备注：
 */
public class SharedPreferencesUtils {

    public static Context context;
    private static String PreferenceName = "Constant";
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "share_date";

    public static void init(Context cont){
        context = cont;
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param context
     * @param key
     * @param object
     */
    public static String setParam(Context context, String key, Object object){

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if("String".equals(type)){
            editor.putString(key, (String)object);
        }
        else if("Integer".equals(type)){
            editor.putInt(key, (Integer)object);
        }
        else if("Boolean".equals(type)){
            editor.putBoolean(key, (Boolean)object);
        }
        else if("Float".equals(type)){
            editor.putFloat(key, (Float)object);
        }
        else if("Long".equals(type)){
            editor.putLong(key, (Long)object);
        }

        editor.commit();

        return type;
    }

    public static void removeParam(Context context, String key){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    public static boolean isHasParam(Context context , String key){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context , String key, Object defaultObject){
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if("String".equals(type)){
            return sp.getString(key, (String)defaultObject);
        }
        else if("Integer".equals(type)){
            return sp.getInt(key, (Integer)defaultObject);
        }
        else if("Boolean".equals(type)){
            return sp.getBoolean(key, (Boolean)defaultObject);
        }
        else if("Float".equals(type)){
            return sp.getFloat(key, (Float)defaultObject);
        }
        else if("Long".equals(type)){
            return sp.getLong(key, (Long)defaultObject);
        }

        return null;
    }

    /**
     * 获取SharePreference中的值
     */
    public static boolean getBooleanPreferences(Context context, String name,
                                                boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        // 获取数据
        return sp.getBoolean(name, defValue);
    }

    /**
     * 获取SharePreference中的String类型值
     */
    public static String getStringPreferences(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        // 获取数据
        return sp.getString(name, "");
    }

    /**
     *有默认值的
     */
    public static String getStringPreferences(Context context, String name, String defaultValue){
        SharedPreferences sp = context.getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        return sp.getString(name,defaultValue);
    }

    /**
     * 将String信息存入Preferences
     *
     * @param context
     * @param name
     * @param value
     */
    public static void setPreferences(Context context, String name, String value) {
        SharedPreferences sp = context.getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        // 存入数据
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(name, value);
        editor.commit();
    }

    /**
     * 将boolean信息存入Preferences
     *
     * @param context
     * @param name
     * @param value
     */
    public static void setPreferences(Context context, String name,
                                      boolean value) {
        SharedPreferences sp = context.getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);

        // 存入数据
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    /**
     * 获取SharePreference中的int类型值
     */
    public static int getIntPreferences(Context context, String name,
                                        int defValue) {
        SharedPreferences sp = context.getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);

        // 获取数据
        return sp.getInt(name, defValue);
    }

    /**
     * 将int信息存入Preferences
     *
     * @param context
     * @param name
     * @param value
     */
    public static void setPreferences(Context context, String name, int value) {
        SharedPreferences sp = context.getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);

        // 存入数据
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(name, value);
        editor.commit();
    }

    /**
     * 获取SharePreference中的值
     */
    public static float getFloatPreferences(Context context, String name,
                                            float defValue) {
        SharedPreferences sp = context.getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);

        // 获取数据
        return sp.getFloat(name, defValue);
    }

    /**
     * 将float信息存入Preferences
     *
     * @param context
     * @param name
     * @param value
     */
    public static void setPreferences(Context context, String name, float value) {
        SharedPreferences sp = context.getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);

        // 存入数据
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(name, value);
        editor.commit();
    }

    /**
     * 将Object信息Base64后存入Preferences
     *
     * @param context
     * @param name
     * @param obj
     */

    public static <T> void setPreferences(Context context, String name, T obj) {
        SharedPreferences sp = context.getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (obj == null) {
            editor.putString(name, null);
            editor.commit();
            return;
        }

        try {
            ByteArrayOutputStream toByte = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(toByte);
            oos.writeObject(obj);

            // 对byte[]进行Base64编码
            String obj64 = new String(Base64.encode(toByte.toByteArray(),
                    Base64.DEFAULT));

            editor.putString(name, obj64);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取SharePreference中值，Base64解码之后传出
     *
     * @param context
     * @param name
     */
    @SuppressWarnings("unchecked")
    public static <T> T getPreferences(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);

        try {
            String obj64 = sp.getString(name, "");
            if (TextUtils.isEmpty(obj64)) {
                return null;
            }
            byte[] base64Bytes = Base64.decode(obj64, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


}
