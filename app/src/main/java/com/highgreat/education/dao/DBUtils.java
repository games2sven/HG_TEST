package com.highgreat.education.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class DBUtils {
    
    public static String TAG = DBUtils.class.getSimpleName();
    
    /**
    * 方法1：检查某表列是否存在
    * @param db
    * @param tableName 表名
    * @param columnName 列名
    * @return
    */
    public static boolean checkColumnExist1(SQLiteDatabase db, String tableName
            , String columnName) {
        boolean result = false ;
        Cursor cursor = null ;
        try{
            //查询一行
            cursor = db.rawQuery( "SELECT * FROM " + tableName + " LIMIT 0"
                , null );
            result = cursor != null && cursor.getColumnIndex(columnName) != -1 ;
        }catch (Exception e){
        }finally{
            if(null != cursor && !cursor.isClosed()){
                cursor.close() ;
            }
        }

        return result ;
    }
    
    
    
    
    /**
     * 检测字段是否存在。
     * @param db
     * @param table
     * @param column
     * @return true存在，fasle不存在
     */
    public static boolean checkColumnExist(SQLiteDatabase db, String table, String column) {
        boolean  isExist = true;
        
        Cursor cursor = null;
        try {
            cursor = db.query("sqlite_master", null, "type='table' and name='"+ table +"'", null, null, null, null);
            if(cursor!=null && cursor.moveToFirst()){
                String sql = cursor.getString(cursor.getColumnIndex("sql"));
                if(!TextUtils.isEmpty(sql)){
                    // 为了保证及时新闻左右滑动的数据有序性
                    if(!sql.contains(column)){
                        isExist = false;
                    }else{
                        isExist =  true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(cursor!=null){
                cursor.close();
                cursor = null;
            }
        }
        
        return isExist;
    }
    public  static boolean tabIsExist(SQLiteDatabase db,String tabName){
        boolean result = false;
        if(tabName == null){
                return false;
        }
        Cursor cursor = null;
        try {
              
                String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"+tabName.trim()+"' ";
                cursor = db.rawQuery(sql, null);
                if(cursor.moveToNext()){
                        int count = cursor.getInt(0);
                        if(count>0){
                                result = true;
                        }
                }
               
        } catch (Exception e) {
                // TODO: handle exception
        }               
        return result;
}
}
