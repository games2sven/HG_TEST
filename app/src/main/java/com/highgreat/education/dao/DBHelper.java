package com.highgreat.education.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;


import com.highgreat.education.bean.ImageTasksManagerModel;
import com.highgreat.education.bean.TasksManagerModel;
import com.highgreat.education.common.UavConstants;
import com.highgreat.education.utils.LogUtil;
import com.highgreat.education.utils.SharedPreferencesUtils;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBHelper {

    private static final String TAG = "DBHelper";
    private static DBHelper instance;

    /**
     * 数据库升级,  1
     */
    private static final int DATABASE_VERSION = 6;

    public static final String DATABASE_NAME = "ZeroCamera.db";

    /**************************************
     * 用户表
     **************************************************/
    public static final String TABLE_USER = "TUser"; // 表
    public static final String USER_ID    = "UserID   "; // 用户ID

    /**************************************
     * 飞机表
     **************************************************/
    public static final String TABLE_FLIGHT = "TFlight"; //
    public static final String FLIGHT_ID    = "FlightID"; //


    // CREATE TABLE IF NOT EXISTS T_MSG_INFO (_ID INTEGER PRIMARY KEY
    // AUTOINCREMENT,FlightID TEXT )

    /**************************************
     * 操作表
     **************************************************/
    public static final String TABLE_OPERATION          = "TOperation"; //
    public static final String O_USER_ID                = "UserID"; //
    public static final String O_FLIGHT_ID              = "FlightID"; //
    public static final String O_OPERATION_INFO_ID      = "OperationInfoID"; //
    public static final String O_FLIGHT_SORTIES_INFO_ID = "FlightSortiesInfoID"; //

    /**************************************
     * 飞行架次信息表
     **************************************************/
    public static final String TABLE_FLIGHT_SORTIES_INFO = "TFlightSortiesInfo"; // 表
    public static final String FLIGHT_DATE               = "FlightDate"; //
    public static final String GPS_LATITUDE              = "GPSLatitude"; //
    public static final String GPS_LONGITUTDE            = "GPSLongitude"; // 用户ID
    public static final String PHONE_LAT              = "PhoneLat"; //
    public static final String PHONE_LNG            = "PhoneLng"; //
    public static final String FLIGHT_TIME               = "FlightTime"; // 消息名称
    public static final String FLIGHT_MAX_SPEED          = "FlightMaxSpeed"; // 消息内容
    public static final String OTYPE                     = "otype"; // 飞机类型 11dobby   14 hunter


    /**************************************
     * 用户操作信息表
     **************************************************/
    public static final String TABLE_USER_OPREATION_INFO = "TUserOperationInfo"; //
    public static final String AIR_CRASH_TIMES           = "AirCrashTimes"; // 用户炸机次数：记录用户炸机的次数。
    public static final String KNOCKED_TIMES             = "KnockedTimes"; // 碰撞东西次数：记录用户撞东西的次数。
    public static final String FLIGHT_TIMES              = "FlightTimes"; // 起落次数：记录客户一共飞行了多少起落（飞了多少次）。
    public static final String TAKE_PHOTO_TIMES          = "TakePhotoTimes"; // （拍照次数：记录客户拍照的次数。
    public static final String TAKE_VIDEO_TIMES          = "TakeVideoTimes"; // 录像次数：记录客户录像的次数。
    public static final String ASSISIT_FLIGHT_TIMES      = "AssistFlightTimes"; // 有GPS/光流辅助的飞行次数：记录有辅助的飞行有多少次
    public static final String DISASSIST_FLIGHT_TIMES    = "DisAssistFlightTimes"; // 无辅助的飞行次数：记录无辅助的飞行有多少次。
    public static final String CONTINU_SHOOTING_TIMES    = "ContinuShootingTimies"; // 连拍功能使用次数：记录连拍功能的使用次数。
    public static final String BEAT_BEAT_TIMES           = "BeatBeatTimes"; // 拍拍起飞功能使用次数：记录拍拍起飞功能的使用次数
    public static final String HOLD_DOWN_TIMES           = "HoldDownTimes"; // 手抓降落功能使用次数：记录手抓降落功能的使用次数
    public static final String DRAG_TIMES                = "DragTimes"; //拖拽飞行功能使用次数：记录拖拽飞行功能的使用次数。
    public static final String VOICE_CONTROL_TIMES       = "VoiceControlTimes"; // 语音控制使用次数：记录语音控制功能的使用次数

    public static final String THREE_TO_ONE_TIMES = "ThreeToOneTimes"; // 三键合一使用次数：记录三键合一功能的使用次数
    public static final String SOMATIC_TIMES      = "SomaticTimes"; // 体感操作使用次数：记录体感操作模式的使用次数
    public static final String DRAW_SCREEN_TIMES  = "DrawScreenTimes"; // 滑屏操作使用次数：记录滑屏操作模式的使用次数
    public static final String SHARE_TIMES        = "ShareTimes"; // 分享功能使用次数：记录分享功能的使用次数（不管通过那个平台分享的


    /**
     * 多媒体下载相关
     */
    public final static String DOWNLOAD_TABLE_NAME       = "tasksmanger";
    public final static String IMAGE_DOWNLOAD_TABLE_NAME = "iamgetable";
    public static       String TABLE_DOWNLOAD_INFO       = "download_info";//download_info表存储下载信息
    public static       String TABLE_LOCALDOWN_INFO      = "localdown_info";//localdown_info表存储本地下载信息
    /* =======================判定参数====================================== */
    public static final int    ORDER_BY_DISCOUNT         = 1;
    public static final int    ORDER_BY_CREATE_TIME      = 2;

	/* =======================判定参数END=================================== */

    protected MySQLiteDatabase db;
    private   DatabaseHelper   dbHelper;
    private static final byte[]  mLocker = new byte[0];
    protected            Context context = null;

    private DBHelper(Context c) {
        context = c;
        if (db == null) {
            db = new MySQLiteDatabase();
        }
        if (!db.isOpen()) {
            open();
        }
    }

    public synchronized static DBHelper getInstance(Context context) {
        if (null == instance) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    public static void init(Context context) {
        if (null == instance) {
            instance = new DBHelper(context);
        }
    }

    /**
     * Opens a database
     *
     * @return database reference
     */
    public void open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        synchronized (mLocker) {
            try {
                if (db != null && db.isOpen()) {
                    db.close();
                }
                if (db != null) {
                    db.setDB(dbHelper.getWritableDatabase());
                }
            } catch (Exception e) {
                try {
                    Thread.sleep(1500);
                    if (db != null && db.isOpen()) {
                        db.close();
                    }
                    if (db != null) {
                        db.setDB(dbHelper.getWritableDatabase());
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                // e.printStackTrace();
            }
        }
    }

    public void close() {
        try {
            if (db != null) {
                db.close();
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }
    }

    /**
     * ClassName:DatabaseHelper Function: internal class, helps to create the
     * database when application instllation, and help to upgrade the database
     * when application upgrade.
     */
    protected class DatabaseHelper extends SQLiteOpenHelper {

        Context thisContext = null;

        /**
         * Creates a new instance of DatabaseHelper.
         *
         * @param context
         */
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.thisContext = context;
        }

        /**
         * @see SQLiteOpenHelper#onCreate(SQLiteDatabase)
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            // EDIT END
            initDB(db, thisContext);
        }

        /**
         * 初始化数据库
         *
         * @param db
         */
        private void initDB(SQLiteDatabase db, Context context) {
            db.execSQL(getOperationSql());
            db.execSQL(getFlightSortiesInfoSql());
            db.execSQL(getUserOperationInfoSql());
            db.execSQL(getVideoDownLoadSql());
            db.execSQL(getImageDownLoadSql());

        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion,
                                int newVersion) {
            // edit by java_bing for android 4.0 ++
            try {
                // 如果为降级， 就全部删除数据！重新建表
                clearDB(db);
                initDB(db, thisContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // super.onDowngrade(db, oldVersion, newVersion);
        }

        /**
         * @see SQLiteOpenHelper#onUpgrade(SQLiteDatabase,
         * int, int)
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                if (newVersion > oldVersion) {
                    int curVersion = oldVersion;
                    if (curVersion <= 1) {
                        upGrade0to2();
                        curVersion = 2;
                    }

                    if (curVersion == 2) {
                        upGrade0to3(db);
                        curVersion = 3;
                    }

                    if (curVersion == 3) {
                        upGrade0to4(db);
                        curVersion = 4;
                    }
                    if (curVersion == 4) {
                        upGrade0to5(db);
                        curVersion = 5;
                    }
                    if (curVersion == 5) {
                        upGrade0to6(db);
                        curVersion = 6;
                    }
                    if(curVersion == 7){
                        upGrade0to7(db);
                    }


                } else if (oldVersion > newVersion) {
                    // 如果为降级， 就全部删除数据！重新建表
                    clearDB(db);
                    initDB(db, thisContext);
                }
                LogUtil.d("db", "oldVersion:" + oldVersion + "  newVersion:"
                        + newVersion);
            } catch (Exception e) {
                clearDB(db);
                initDB(db, thisContext);

                LogUtil.d("db", "DB upgrade error! 数据库升级发生错误！！");
                e.printStackTrace();
            }

        }

        private void upGrade0to2() {
            db.execSQL(getDownLoadInfoSql());
            db.execSQL(getDownLoadLocalInfoSql());
        }

        /**
         * 用于逆向安装时，删除所有的数据库
         *
         * @param db
         */
        private void clearDB(SQLiteDatabase db) {
            String sqlDeleteFormat = "DROP TABLE IF EXISTS %s ;";
            String resetSQL = "update sqlite_sequence SET seq=0 where name='%s';";

            String[] tables = new String[]{TABLE_FLIGHT_SORTIES_INFO, TABLE_USER_OPREATION_INFO};
            for (String table : tables) {
                db.execSQL(String.format(sqlDeleteFormat, table));
                db.execSQL(String.format(resetSQL, table));
            }
        }

    }

    /**
     * 创建新表 删除2张表
     *
     * @param db
     */
    private void upGrade0to3(SQLiteDatabase db) {
        db.execSQL(getVideoDownLoadSql());
        String sqlDeleteFormat = "DROP TABLE IF EXISTS %s ;";
        String[] tables = new String[]{TABLE_LOCALDOWN_INFO, TABLE_DOWNLOAD_INFO};
        for (String table : tables) {
            db.execSQL(String.format(sqlDeleteFormat, table));
        }
    }

    /**
     * 创建新表 删除2张表
     *
     * @param db
     */
    private void upGrade0to4(SQLiteDatabase db) {
        db.execSQL(getVideoDownLoadSql());
        String sqlDeleteFormat = "DROP TABLE IF EXISTS %s ;";
        String[] tables = new String[]{TABLE_LOCALDOWN_INFO, TABLE_DOWNLOAD_INFO};
        for (String table : tables) {
            db.execSQL(String.format(sqlDeleteFormat, table));
        }
    }

    /**
     * 创建新表
     *
     * @param db
     */
    private void upGrade0to5(SQLiteDatabase db) {
        db.execSQL(getImageDownLoadSql());
    }

    /**
     * 新建字段
     *
     * @param db
     */
    private void upGrade0to6(SQLiteDatabase db) {
        db.execSQL("alter table " + DBHelper.TABLE_FLIGHT_SORTIES_INFO +
                " add " + PHONE_LAT + " float ");
        db.execSQL("alter table " + DBHelper.TABLE_FLIGHT_SORTIES_INFO +
                " add " + PHONE_LNG + " float ");
    }

    /**
     * 新建字段 添加飞机类型 dobby hunter
     *
     * @param db
     */
    private void upGrade0to7(SQLiteDatabase db) {
        db.execSQL("alter table " + DBHelper.TABLE_FLIGHT_SORTIES_INFO +
                " add " + OTYPE + " INTEGER ");
    }

    /**
     * 为了处理长期在后台挂起而导致数据库关闭而再次打开客户端
     * <p/>
     * 并对数据库操作导致的crash，其中报database not open
     * <p/>
     * 需要增加check操作
     *
     * @author kebinzeng
     * @return
     */
    public class MySQLiteDatabase {
        private SQLiteDatabase mydb = null;

        public synchronized void checkDBIsOpen() {
            try {
                if (mydb == null || !mydb.isOpen()) {
                    mydb = dbHelper.getWritableDatabase();
                }
            } catch (Exception e) {
                LogUtil.e(TAG, "需要增加check操作" + e.getMessage());
            }
        }

        public synchronized void execSQL(String sql) throws SQLException {
            LogUtil.d("db", "DB Operate == " + "sql:" + sql);
            checkDBIsOpen();
            mydb.execSQL(sql);
        }

        public synchronized Cursor rawQuery(String sql, String[] selectionArgs) {
            LogUtil.d("db", "DB Operate == " + "rawQuery sql:" + sql + " args:"
                    + selectionArgs);
            checkDBIsOpen();
            return mydb.rawQuery(sql, selectionArgs);
        }

        public synchronized void execSQL(String sql, Object[] bindArgs)
                throws SQLException {
            LogUtil.d("db", "DB Operate == " + "execSQL sql:" + sql + " args:"
                    + bindArgs);
            checkDBIsOpen();
            mydb.execSQL(sql, bindArgs);
        }

        public void beginTransaction(){
            mydb.beginTransaction();
        }

        public void endTransaction(){
            mydb.endTransaction();
        }

        public synchronized boolean isOpen() {
            return mydb != null && mydb.isOpen();
        }

        public synchronized void close() {
            mydb.close();
        }

        public synchronized void setDB(SQLiteDatabase mdb) {
            mydb = mdb;
        }

        public synchronized SQLiteDatabase getDB() {
            return mydb;
        }

        public synchronized Cursor query(String table, String[] columns,
                                         String selection, String[] selectionArgs, String groupBy,
                                         String having, String orderBy) {
            checkDBIsOpen();
            return mydb.query(table, columns, selection, selectionArgs,
                    groupBy, having, orderBy);
        }

        public synchronized Cursor query(String table, String[] columns,
                                         String selection, String[] selectionArgs, String groupBy,
                                         String having, String orderBy, String limit) {
            checkDBIsOpen();
            return mydb.query(table, columns, selection, selectionArgs,
                    groupBy, having, orderBy, limit);
        }

        public synchronized Cursor query(boolean distinct, String table,
                                         String[] columns, String selection, String[] selectionArgs,
                                         String groupBy, String having, String orderBy, String limit) {
            checkDBIsOpen();
            return mydb.query(distinct, table, columns, selection,
                    selectionArgs, groupBy, having, orderBy, limit);
        }

        public synchronized int update(String table, ContentValues values,
                                       String whereClause, String[] whereArgs) {
            checkDBIsOpen();
            return mydb.update(table, values, whereClause, whereArgs);
        }

        public synchronized long insert(String table, String nullColumnHack,
                                        ContentValues values) {
            checkDBIsOpen();
            return mydb.insert(table, nullColumnHack, values);
        }

        public synchronized int delete(String table, String whereClause,
                                       String[] whereArgs) {
            checkDBIsOpen();
            return mydb.delete(table, whereClause, whereArgs);
        }
    }

    protected boolean insertData(String tableName, ContentValues values,
                                 SQLiteDatabase db) {
        try {
            ArrayList<ContentValues> arrayList = new ArrayList<ContentValues>();
            arrayList.add(values);
            return insertData(tableName, arrayList, db);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    protected boolean insertData(String tableName,
                                 ArrayList<ContentValues> values, SQLiteDatabase db) {

        try {
            for (ContentValues value : values) {
                db.insert(tableName, null, value);
                value.clear();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 清除缓存
     */
    private void rmDBData(SQLiteDatabase db) {
        if (DBUtils.tabIsExist(db, TABLE_FLIGHT_SORTIES_INFO)) {
            db.execSQL("delete from " + TABLE_FLIGHT_SORTIES_INFO);
        }
        if (DBUtils.tabIsExist(db, TABLE_USER_OPREATION_INFO)) {
            db.execSQL("delete from " + TABLE_USER_OPREATION_INFO);
        }
    }


    /**
     * 创建表语句
     *
     * @return
     */
    private String getUserSql() {

        return new StringBuffer().append("CREATE TABLE IF NOT EXISTS ")
                .append(DBHelper.TABLE_USER)
                .append("(_id INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(DBHelper.USER_ID).append(" TEXT NOT NULL, ")
                .toString();
    }

    /**
     * 创建表
     *
     * @return
     */
    private String getFlightSql() {

        return new StringBuffer().append("CREATE TABLE IF NOT EXISTS _")
                .append(DBHelper.TABLE_FLIGHT)
                .append("(_id INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(DBHelper.FLIGHT_ID).append(" TEXT NOT NULL, ")
                .toString();
    }


    /**
     * 创建表语句
     *
     * @return
     */
    private String getOperationSql() {
        return new StringBuffer().append("CREATE TABLE IF NOT EXISTS ")
                .append(DBHelper.TABLE_OPERATION)
                .append("(_id INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(DBHelper.O_USER_ID).append(" TEXT NOT NULL, ")
                .append(DBHelper.O_FLIGHT_ID).append(" TEXT, ")
                .append(DBHelper.O_OPERATION_INFO_ID).append(" INTEGER, ")
                .append(DBHelper.O_FLIGHT_SORTIES_INFO_ID).append(" INTEGER)")
                .toString();
    }

    /**
     * 创建语句
     *
     * @return
     */
    private String getFlightSortiesInfoSql() {
        return new StringBuffer().append("CREATE TABLE IF NOT EXISTS ")
                .append(DBHelper.TABLE_FLIGHT_SORTIES_INFO)
                .append("(_id INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(DBHelper.FLIGHT_DATE).append(" TEXT, ")
                .append(DBHelper.GPS_LATITUDE).append(" float, ")
                .append(DBHelper.GPS_LONGITUTDE).append(" float, ")
                .append(DBHelper.PHONE_LAT).append(" float, ")
                .append(DBHelper.PHONE_LNG).append(" float, ")
                .append(DBHelper.FLIGHT_TIME).append(" INTEGER, ")
                .append(DBHelper.OTYPE).append(" INTEGER)")
                .toString();
    }

    /**
     * @return
     */
    private String getUserOperationInfoSql() {
        return new StringBuffer().append("CREATE TABLE IF NOT EXISTS ")
                .append(DBHelper.TABLE_USER_OPREATION_INFO)
                .append("(_id INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(DBHelper.AIR_CRASH_TIMES).append(" INTEGER, ")
                .append(DBHelper.KNOCKED_TIMES).append(" INTEGER, ")
                .append(DBHelper.FLIGHT_TIMES).append(" INTEGER, ")
                .append(DBHelper.TAKE_PHOTO_TIMES).append(" INTEGER, ")
                .append(DBHelper.TAKE_VIDEO_TIMES).append(" INTEGER, ")
                .append(DBHelper.ASSISIT_FLIGHT_TIMES).append(" INTEGER, ")
                .append(DBHelper.DISASSIST_FLIGHT_TIMES).append(" INTEGER, ")
                .append(DBHelper.CONTINU_SHOOTING_TIMES).append(" INTEGER, ")
                .append(DBHelper.BEAT_BEAT_TIMES).append(" INTEGER, ")
                .append(DBHelper.HOLD_DOWN_TIMES).append(" INTEGER, ")
                .append(DBHelper.DRAG_TIMES).append(" INTEGER, ")
                .append(DBHelper.VOICE_CONTROL_TIMES).append(" INTEGER, ")
                .append(DBHelper.THREE_TO_ONE_TIMES).append(" INTEGER, ")
                .append(DBHelper.SOMATIC_TIMES).append(" INTEGER, ")
                .append(DBHelper.DRAW_SCREEN_TIMES).append(" INTEGER, ")
                .append(DBHelper.SHARE_TIMES).append(" INTEGER)")
                .toString();
    }

    /**
     * 下载相关的sql语句
     *
     * @return
     */
    private String getDownLoadInfoSql() {
        return "create table  IF NOT EXISTS " + TABLE_DOWNLOAD_INFO + "(" +
                "_id integer PRIMARY KEY AUTOINCREMENT," +
                "thread_id integer," +
                "start_pos integer," +
                "end_pos integer," +
                "compelete_size integer," +
                "url varchar(50))";
    }

    /**
     * localdown_info表存储本地下载信息
     *
     * @return
     */
    private String getDownLoadLocalInfoSql() {
        return "create table  IF NOT EXISTS " + TABLE_LOCALDOWN_INFO + "(" +
                "_id integer PRIMARY KEY AUTOINCREMENT," +
                "name varchar(30)," +
                "url varchar(50)," +
                "completeSize integer," +
                "fileSize integer," +
                "status integer)";
    }

    /**
     * 视频下载
     *
     * @return
     */
    private String getVideoDownLoadSql() {
        return "CREATE TABLE IF NOT EXISTS "
                + DOWNLOAD_TABLE_NAME
                + String.format(
                "("
                        + "%s INTEGER PRIMARY KEY, " // id, download id
                        + "%s VARCHAR, " // name
                        + "%s VARCHAR, " // url
                        + "%s VARCHAR " // path
                        + ")"
                , TasksManagerModel.ID
                , TasksManagerModel.NAME
                , TasksManagerModel.URL
                , TasksManagerModel.PATH);
    }

    /**
     * 图片下载
     *
     * @return
     */
    private String getImageDownLoadSql() {
        return "CREATE TABLE IF NOT EXISTS "
                + IMAGE_DOWNLOAD_TABLE_NAME
                + String.format(
                "("
                        + "%s INTEGER PRIMARY KEY, " // id, download id
                        + "%s VARCHAR, " // name
                        + "%s VARCHAR, " // url
                        + "%s VARCHAR " // path
                        + ")"
                , ImageTasksManagerModel.ID
                , ImageTasksManagerModel.NAME
                , ImageTasksManagerModel.URL
                , ImageTasksManagerModel.PATH);
    }


    protected boolean updateData(String tableName, ContentValues values,
                                 String whereClause, String[] whereArgs) {
        try {
            db.update(tableName, values, whereClause, whereArgs);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    protected long insertData(String tableName, ContentValues values) {
        try {
            ArrayList<ContentValues> arrayList = new ArrayList<ContentValues>();
            arrayList.add(values);
            return insertData(tableName, arrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    protected long insertData(String tableName,
                              ArrayList<ContentValues> values) {
        long id = -1;
        try {
            for (ContentValues value : values) {
                id = db.insert(tableName, null, value);
                value.clear();
            }
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    protected boolean deleteData(String table, String whereClause,
                                 String[] whereArgs) {
        try {
            db.delete(table, whereClause, whereArgs);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /************************************** 操作表 **************************************************/


    /**
     * 获取操作
     *
     * @return
     */
    public ArrayList<EntityOperation> getOperationUser() {
        ArrayList<EntityOperation> list = new ArrayList();
        Cursor cursor = null;
        try {
            String sql = new StringBuffer().append("SELECT UserID FROM ")
                    .append(TABLE_OPERATION)
                    .append(" group by UserID ")
                    .toString();
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                int size = cursor.getCount();
                for (int i = 0; i < size; i++) {
                    EntityOperation item = new EntityOperation();
                    item.mUserID = cursor.getString(cursor.getColumnIndex(O_USER_ID));
                    list.add(item);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 获取操作
     *
     * @return
     */
    public ArrayList<EntityOperation> getOperationFlight(String userId) {
        ArrayList<EntityOperation> list = new ArrayList();
        Cursor cursor = null;
        try {
            String sql = new StringBuffer().append("SELECT * FROM ")
                    .append(TABLE_OPERATION)
                    .append(" where UserID = '").append(userId).append("'")
                    .append(" group by ").append(O_FLIGHT_ID)
                    .toString();
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                int size = cursor.getCount();
                for (int i = 0; i < size; i++) {
                    EntityOperation item = new EntityOperation();
                    item.mUserID = cursor.getString(cursor.getColumnIndex(O_USER_ID));
                    item.mFlightID = cursor.getString(cursor.getColumnIndex(O_FLIGHT_ID));
                    list.add(item);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /***/
    public long getOperationInfoId(String userId, String flightId) {
//		String userId = (String)SharedPreferencesUtils.getParam(context, UavConstants.USER_ID, "default");
//		String flightId = (String)SharedPreferencesUtils.getParam(context, UavConstants.FLIGHT_ID, "default");
        long id = -1;
        Cursor cursor = null;
        try {
            String sql = new StringBuffer().append("SELECT " + O_OPERATION_INFO_ID + " FROM ")
                    .append(TABLE_OPERATION)
                    .append(" where " + O_USER_ID + " = '").append(userId).append("' ")
                    .append(" and " + O_FLIGHT_ID + " = '").append(flightId).append("' ")
                    .toString();
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                int size = cursor.getCount();
                for (int i = 0; i < size; i++) {
                    id = cursor.getInt(cursor.getColumnIndex(O_OPERATION_INFO_ID));
                    cursor.moveToNext();
                }
            }
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return -1;
    }


    /**
     * 获取用户所有飞行架次id
     */
    public ArrayList<EntityOperation> getUserFlightSortiesInfoID(String userId) {
        ArrayList<EntityOperation> list = new ArrayList();
        long id = -1;
        Cursor cursor = null;
        try {
            String sql = new StringBuffer().append("SELECT " + O_FLIGHT_SORTIES_INFO_ID + " FROM ")
                    .append(TABLE_OPERATION)
                    .append(" where " + O_USER_ID + " = '").append(userId).append("' ")
                    .toString();
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                int size = cursor.getCount();
                for (int i = 0; i < size; i++) {
                    EntityOperation item = new EntityOperation();
//					item.mUserID = cursor.getString(cursor.getColumnIndex(O_USER_ID));
                    item.mFlightSortiesInfoID = cursor.getInt(cursor.getColumnIndex(O_FLIGHT_SORTIES_INFO_ID));
                    list.add(item);
                    cursor.moveToNext();
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * 获取用户所有操作数据id
     */
    public ArrayList<EntityOperation> getUserOperationID(String userId) {
        ArrayList<EntityOperation> list = new ArrayList();
        long id = -1;
        Cursor cursor = null;
        try {
            String sql = new StringBuffer().append("SELECT " + O_OPERATION_INFO_ID + " FROM ")
                    .append(TABLE_OPERATION)
                    .append(" where " + O_USER_ID + " = '").append(userId).append("' ")
                    .toString();
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                int size = cursor.getCount();
                for (int i = 0; i < size; i++) {
                    EntityOperation item = new EntityOperation();
                    item.mOperationInfoID = cursor.getInt(cursor.getColumnIndex(O_OPERATION_INFO_ID));
                    list.add(item);
                    cursor.moveToNext();
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /***
     * 删除信息
     */
    public void deleteOperation(String userId) {
        try {
            String sql = new StringBuffer().append("delete from ")
                    .append(TABLE_OPERATION)
                    .append(" where " + O_USER_ID + " = '").append(userId).append("' ")
                    .toString();
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


/************************************** 飞行架次信息表 **************************************************/
    /**
     * 添加一条飞行架次表
     */
    public long addFlightSortiesInfo(EntityFlightSorties entity) {
        try {
            ContentValues value = new ContentValues();
            value.put(FLIGHT_DATE, entity.mFlightDate);
            value.put(GPS_LATITUDE, entity.mGPSLatitude);
            value.put(GPS_LONGITUTDE, entity.mGPSLongitude);
            value.put(PHONE_LAT, entity.mPhoneLat);
            value.put(PHONE_LNG, entity.mPhoneLng);
            value.put(FLIGHT_TIME, entity.mFlightTime);
            value.put(OTYPE, entity.mOtype);
            return insertData(TABLE_FLIGHT_SORTIES_INFO, value);
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }
        return -1;
    }


    /**
     * 更新一条飞行架次表
     */
    public boolean updateFlightSortiesInfo(long id, long flightTime) {
        try {
            String sql = "update " + TABLE_FLIGHT_SORTIES_INFO + " set "
                    + FLIGHT_TIME + "=" + flightTime
                    + " where "
                    + FLIGHT_TIME + "<" + flightTime + " and "
                    + " _id=" + id;
            db.execSQL(sql, new Object[]{});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取飞行架次信息
     *
     * @return
     */
    public ArrayList<EntityFlightSorties> getFlightSortiesInfo(String userId, String flightId) {
//		String userId = (String)SharedPreferencesUtils.getParam(context, UavConstants.USER_ID, "default");
        ArrayList<EntityFlightSorties> list = new ArrayList();
        Cursor cursor = null;
        try {
            String sql = new StringBuffer().append("SELECT f.*,o.* FROM ")
                    .append(TABLE_FLIGHT_SORTIES_INFO).append(" f, ").append(TABLE_OPERATION).append(" o ")
                    .append(" where f._id = o.FlightSortiesInfoID ")
                    .append(" and o.FlightID = '").append(flightId).append("' ")
                    .append(" and o.UserID = '").append(userId).append("' ")
                    .toString();
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                int size = cursor.getCount();
                for (int i = 0; i < size; i++) {
                    EntityFlightSorties item = new EntityFlightSorties();
                    item.mId = cursor.getString(cursor.getColumnIndex("_id"));
                    item.mFlightDate = cursor.getString(cursor.getColumnIndex(FLIGHT_DATE));
                    item.mGPSLatitude = cursor.getFloat(cursor.getColumnIndex(GPS_LATITUDE));
                    item.mGPSLongitude = cursor.getFloat(cursor.getColumnIndex(GPS_LONGITUTDE));
                    item.mPhoneLat = cursor.getFloat(cursor.getColumnIndex(PHONE_LAT));
                    item.mPhoneLng = cursor.getFloat(cursor.getColumnIndex(PHONE_LNG));
                    item.mFlightTime = cursor.getLong(cursor.getColumnIndex(FLIGHT_TIME));
                    item.mOtype = cursor.getInt(cursor.getColumnIndex(OTYPE));
                    list.add(item);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /***
     * 删除信息
     */
    public void deleteFlightSortiesInfo(String userId) {
        try {
//			String sql = new StringBuffer().append("delete ").append(" f.*,o.* ").append(" from ")
//					.append(TABLE_FLIGHT_SORTIES_INFO).append(" f, ").append(TABLE_OPERATION).append(" o ")
//					.append(" where f._id = o.FlightSortiesInfoID ")
//					.append(" and o.UserID = '").append(userId).append("' ")
//					.toString();

            ArrayList<EntityOperation> data = getUserFlightSortiesInfoID(userId);
            if (data != null && data.size() > 0) {
                for (EntityOperation flightSortiesId : data) {
                    String sql = new StringBuffer().append("DELETE ").append(" FROM ")
                            .append(TABLE_FLIGHT_SORTIES_INFO)
                            .append(" WHERE _id =  ").append(flightSortiesId.mFlightSortiesInfoID)
                            .toString();
                    db.execSQL(sql);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /************************************** 用户操作信息表  **************************************************/
    /**
     * 添加一条用户操作信息
     */
    public long addUserOperationInfo(EntityUserOperation entity) {
        try {

            ContentValues value = new ContentValues();
            value.put(AIR_CRASH_TIMES, entity.mAirCrashTimes);
            value.put(KNOCKED_TIMES, entity.mKnockedTimes);
            value.put(FLIGHT_TIMES, entity.mFlightTimes);
            value.put(TAKE_PHOTO_TIMES, entity.mTakePhotoTimes);
            value.put(TAKE_VIDEO_TIMES, entity.mTakeVideoTimes);
            value.put(ASSISIT_FLIGHT_TIMES, entity.mAssistFlightTimes);
            value.put(DISASSIST_FLIGHT_TIMES, entity.mDisAssistFlightTimes);
            value.put(CONTINU_SHOOTING_TIMES, entity.mContinuShootingTimies);
            value.put(BEAT_BEAT_TIMES, entity.mBeatBeatTimes);
            value.put(HOLD_DOWN_TIMES, entity.mHoldDownTimes);
            value.put(DRAG_TIMES, entity.mDragTimes);
            value.put(VOICE_CONTROL_TIMES, entity.mVoiceControlTimes);
            value.put(THREE_TO_ONE_TIMES, entity.mThreeToOneTimes);
            value.put(SOMATIC_TIMES, entity.mSomaticTimes);
            value.put(DRAW_SCREEN_TIMES, entity.mDrawScreenTimes);
            value.put(SHARE_TIMES, entity.mShareTimes);
            return insertData(TABLE_USER_OPREATION_INFO, value);
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }
        return -1;
    }

    /**
     * 获取用户操作信息
     *
     * @return
     */
    public ArrayList<EntityUserOperation> getUserOperationInfo(String userId, String flightId) {
//		String userId = (String)SharedPreferencesUtils.getParam(context, UavConstants.USER_ID, "default");
        ArrayList<EntityUserOperation> list = new ArrayList();
        Cursor cursor = null;
        try {
            String sql = new StringBuffer().append("SELECT u.*,o.* FROM ")
                    .append(TABLE_USER_OPREATION_INFO).append(" u, ").append(TABLE_OPERATION).append(" o ")
                    .append(" where u._id = o.").append(O_OPERATION_INFO_ID)
                    .append(" and o.").append(O_FLIGHT_ID).append("= '").append(flightId).append("' ")
                    .append(" and o.").append(O_USER_ID).append("= '").append(userId).append("' ")
                    .toString();
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                int size = cursor.getCount();
                for (int i = 0; i < size; i++) {
                    EntityUserOperation item = new EntityUserOperation();
                    item.mAirCrashTimes = cursor.getInt(cursor.getColumnIndex(AIR_CRASH_TIMES));
                    item.mKnockedTimes = cursor.getInt(cursor.getColumnIndex(KNOCKED_TIMES));
                    item.mFlightTimes = cursor.getInt(cursor.getColumnIndex(FLIGHT_TIMES));
                    item.mTakePhotoTimes = cursor.getInt(cursor.getColumnIndex(TAKE_PHOTO_TIMES));
                    item.mTakeVideoTimes = cursor.getInt(cursor.getColumnIndex(TAKE_VIDEO_TIMES));
                    item.mAssistFlightTimes = cursor.getInt(cursor.getColumnIndex(ASSISIT_FLIGHT_TIMES));
                    item.mDisAssistFlightTimes = cursor.getInt(cursor.getColumnIndex(DISASSIST_FLIGHT_TIMES));
                    item.mContinuShootingTimies = cursor.getInt(cursor.getColumnIndex(CONTINU_SHOOTING_TIMES));
                    item.mBeatBeatTimes = cursor.getInt(cursor.getColumnIndex(BEAT_BEAT_TIMES));
                    item.mHoldDownTimes = cursor.getInt(cursor.getColumnIndex(HOLD_DOWN_TIMES));
                    item.mDragTimes = cursor.getInt(cursor.getColumnIndex(DRAG_TIMES));
                    item.mVoiceControlTimes = cursor.getInt(cursor.getColumnIndex(VOICE_CONTROL_TIMES));
                    item.mThreeToOneTimes = cursor.getInt(cursor.getColumnIndex(THREE_TO_ONE_TIMES));
                    item.mSomaticTimes = cursor.getInt(cursor.getColumnIndex(SOMATIC_TIMES));
                    item.mDrawScreenTimes = cursor.getInt(cursor.getColumnIndex(DRAW_SCREEN_TIMES));
                    item.mShareTimes = cursor.getInt(cursor.getColumnIndex(SHARE_TIMES));
                    list.add(item);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 获取用户操作信息
     *
     * @return
     */
    public ArrayList<EntityUserOperation> getUserOperationInfo(long operationInfoId) {
        ArrayList<EntityUserOperation> list = new ArrayList();
        Cursor cursor = null;
        try {
            String sql = new StringBuffer().append("SELECT * FROM ")
                    .append(TABLE_USER_OPREATION_INFO)//.append(" u, ")//.append(TABLE_OPERATION).append(" o ")
                    .append(" where _id=").append(operationInfoId)
                    .toString();
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                String dfs[] = cursor.getColumnNames();
                for (String d : dfs) {
                    LogUtil.d("db", "dColumnName = " + d);
                }
                int size = cursor.getCount();
                for (int i = 0; i < size; i++) {
                    EntityUserOperation item = new EntityUserOperation();
                    item.mAirCrashTimes = cursor.getInt(cursor.getColumnIndex(AIR_CRASH_TIMES));
                    item.mKnockedTimes = cursor.getInt(cursor.getColumnIndex(KNOCKED_TIMES));
                    item.mFlightTimes = cursor.getInt(cursor.getColumnIndex(FLIGHT_TIMES));
                    item.mTakePhotoTimes = cursor.getInt(cursor.getColumnIndex(TAKE_PHOTO_TIMES));
                    item.mTakeVideoTimes = cursor.getInt(cursor.getColumnIndex("TakeVideoTimes"));
                    item.mAssistFlightTimes = cursor.getInt(cursor.getColumnIndex(ASSISIT_FLIGHT_TIMES));
                    item.mDisAssistFlightTimes = cursor.getInt(cursor.getColumnIndex(DISASSIST_FLIGHT_TIMES));
                    item.mContinuShootingTimies = cursor.getInt(cursor.getColumnIndex(CONTINU_SHOOTING_TIMES));
                    item.mBeatBeatTimes = cursor.getInt(cursor.getColumnIndex(BEAT_BEAT_TIMES));
                    item.mHoldDownTimes = cursor.getInt(cursor.getColumnIndex(HOLD_DOWN_TIMES));
                    item.mDragTimes = cursor.getInt(cursor.getColumnIndex(DRAG_TIMES));
                    item.mVoiceControlTimes = cursor.getInt(cursor.getColumnIndex(VOICE_CONTROL_TIMES));
                    item.mThreeToOneTimes = cursor.getInt(cursor.getColumnIndex(THREE_TO_ONE_TIMES));
                    item.mSomaticTimes = cursor.getInt(cursor.getColumnIndex(SOMATIC_TIMES));
                    item.mDrawScreenTimes = cursor.getInt(cursor.getColumnIndex(DRAW_SCREEN_TIMES));
                    item.mShareTimes = cursor.getInt(cursor.getColumnIndex(SHARE_TIMES));
                    list.add(item);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /***
     * 删除信息
     */
    public void deleteUserOperationInfo(String userId) {
        try {
            ArrayList<EntityOperation> data = getUserFlightSortiesInfoID(userId);
            if (data != null && data.size() > 0) {
                for (EntityOperation operationInfoID : data) {
                    String sql = new StringBuffer().append("DELETE ").append(" FROM ")
                            .append(TABLE_USER_OPREATION_INFO)
                            .append(" WHERE _id =  ").append(operationInfoID.mOperationInfoID)
                            .toString();
                    db.execSQL(sql);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新炸机次数
     */
    public boolean updateAirCrash(String userId, String flightId) {
        long id = -1;
        try {
            id = getOperationInfoId(userId, flightId);
            String sql = "update " + TABLE_USER_OPREATION_INFO + " set "
                    + AIR_CRASH_TIMES + "=" + AIR_CRASH_TIMES
                    + "+1 where _id = " + id + " ";
            db.execSQL(sql, new Object[]{});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新碰撞东西次数：记录用户撞东西的次数
     */
    public boolean updateKnockedTimes(String userId, String flightId) {
        long id = -1;
        try {
            id = getOperationInfoId(userId, flightId);
            String sql = "update " + TABLE_USER_OPREATION_INFO + " set "
                    + KNOCKED_TIMES + "=" + KNOCKED_TIMES
                    + "+1 where _id = " + id + " ";
            db.execSQL(sql, new Object[]{});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新起落次数：记录客户一共飞行了多少起落（飞了多少次）
     */
    public boolean updateFlightTimes(String userId, String flightId) {
        long id = -1;
        try {
            id = getOperationInfoId(userId, flightId);
            String sql = "update " + TABLE_USER_OPREATION_INFO + " set "
                    + FLIGHT_TIMES + "=" + FLIGHT_TIMES
                    + "+1 where _id = " + id + " ";
            db.execSQL(sql, new Object[]{});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新拍照次数：记录客户拍照的次数。
     */
    public boolean updateTakePhotoTimes(String userId, String flightId) {
        long id = -1;
        try {
            id = getOperationInfoId(userId, flightId);
            String sql = "update " + TABLE_USER_OPREATION_INFO + " set "
                    + TAKE_PHOTO_TIMES + "=" + TAKE_PHOTO_TIMES
                    + "+1 where _id = " + id + " ";
            db.execSQL(sql, new Object[]{});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新 录像次数：记录客户录像的次数
     */
    public boolean updateTakeVideoTimes(String userId, String flightId) {
        long id = -1;
        try {
            id = getOperationInfoId(userId, flightId);
            String sql = "update " + TABLE_USER_OPREATION_INFO + " set "
                    + TAKE_VIDEO_TIMES + "=" + TAKE_VIDEO_TIMES
                    + "+1 where _id = " + id + " ";
            db.execSQL(sql, new Object[]{});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新有GPS/光流辅助的飞行次数：记录有辅助的飞行有多少次
     */
    public boolean updateAssistFlightTimes(String userId, String flightId) {
        long id = -1;
        try {
            id = getOperationInfoId(userId, flightId);
            String sql = "update " + TABLE_USER_OPREATION_INFO + " set "
                    + ASSISIT_FLIGHT_TIMES + "=" + ASSISIT_FLIGHT_TIMES
                    + "+1 where _id = " + id + " ";
            db.execSQL(sql, new Object[]{});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新无辅助的飞行次数：记录无辅助的飞行有多少次。
     */
    public boolean updateDisAssistFlightTimes(String userId, String flightId) {
        long id = -1;
        try {
            id = getOperationInfoId(userId, flightId);
            String sql = "update " + TABLE_USER_OPREATION_INFO + " set "
                    + DISASSIST_FLIGHT_TIMES + "=" + DISASSIST_FLIGHT_TIMES
                    + "+1 where _id = " + id + " ";
            db.execSQL(sql, new Object[]{});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新连拍功能使用次数：记录连拍功能的使用次数
     */
    public boolean updateContinuShootingTimies(String userId, String flightId) {
        long id = -1;
        try {
            id = getOperationInfoId(userId, flightId);
            String sql = "update " + TABLE_USER_OPREATION_INFO + " set "
                    + CONTINU_SHOOTING_TIMES + "=" + CONTINU_SHOOTING_TIMES
                    + "+1 where _id = " + id + " ";
            db.execSQL(sql, new Object[]{});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新拍拍起飞功能使用次数：记录拍拍起飞功能的使用次数
     */
    public boolean updateBeatBeatTimes(String userId, String flightId) {
        long id = -1;
        try {
            id = getOperationInfoId(userId, flightId);
            String sql = "update " + TABLE_USER_OPREATION_INFO + " set "
                    + BEAT_BEAT_TIMES + "=" + BEAT_BEAT_TIMES
                    + "+1 where _id = " + id + " ";
            db.execSQL(sql, new Object[]{});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新手抓降落功能使用次数：记录手抓降落功能的使用次数
     */
    public boolean updateHoldDownTimes(String userId, String flightId) {
        long id = -1;
        try {
            id = getOperationInfoId(userId, flightId);
            String sql = "update " + TABLE_USER_OPREATION_INFO + " set "
                    + HOLD_DOWN_TIMES + "=" + HOLD_DOWN_TIMES
                    + "+1 where _id = " + id + " ";
            db.execSQL(sql, new Object[]{});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新手抓降落功能使用次数：记录手抓降落功能的使用次数
     */
    public boolean updateDragTimes(String userId, String flightId) {
        long id = -1;
        try {
            id = getOperationInfoId(userId, flightId);
            String sql = "update " + TABLE_USER_OPREATION_INFO + " set "
                    + DRAG_TIMES + "=" + DRAG_TIMES
                    + "+1 where _id = " + id + " ";
            db.execSQL(sql, new Object[]{});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新语音控制使用次数：记录语音控制功能的使用次数
     */
    public boolean updateVoiceControlTimes(String userId, String flightId) {
        long id = -1;
        try {
            id = getOperationInfoId(userId, flightId);
            String sql = "update " + TABLE_USER_OPREATION_INFO + " set "
                    + VOICE_CONTROL_TIMES + "=" + VOICE_CONTROL_TIMES
                    + "+1 where _id = " + id + " ";
            db.execSQL(sql, new Object[]{});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新三键合一使用次数：记录三键合一功能的使用次数
     */
    public boolean updateThreeToOneTimes(String userId, String flightId) {
        long id = -1;
        try {
            id = getOperationInfoId(userId, flightId);
            String sql = "update " + TABLE_USER_OPREATION_INFO + " set "
                    + THREE_TO_ONE_TIMES + "=" + THREE_TO_ONE_TIMES
                    + "+1 where _id = " + id + " ";
            db.execSQL(sql, new Object[]{});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新体感操作使用次数：记录体感操作模式的使用次数
     */
    public boolean updateSomaticTimes(String userId, String flightId) {
        long id = -1;
        try {
            id = getOperationInfoId(userId, flightId);
            String sql = "update " + TABLE_USER_OPREATION_INFO + " set "
                    + SOMATIC_TIMES + "=" + SOMATIC_TIMES
                    + "+1 where _id = " + id + " ";
            db.execSQL(sql, new Object[]{});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新滑屏操作使用次数：记录滑屏操作模式的使用次数
     */
    public boolean updateDrawScreenTimes(String userId, String flightId) {
        long id = -1;
        try {
            id = getOperationInfoId(userId, flightId);
            String sql = "update " + TABLE_USER_OPREATION_INFO + " set "
                    + DRAW_SCREEN_TIMES + "=" + DRAW_SCREEN_TIMES
                    + "+1 where _id = " + id + " ";
            db.execSQL(sql, new Object[]{});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新分享功能使用次数：记录分享功能的使用次数（不管通过那个平台分享的
     */
    public boolean updateShareTimes(String userId, String flightId) {
        long id = -1;
        try {
            id = getOperationInfoId(userId, flightId);
            String sql = "update " + TABLE_USER_OPREATION_INFO + " set "
                    + DRAW_SCREEN_TIMES + "=" + DRAW_SCREEN_TIMES
                    + "+1 where _id = " + id + " ";
            db.execSQL(sql, new Object[]{});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /***************************视频下载*************************************/

    /**
     * 得到所有的视频下载任务
     *
     * @return
     */
    public Map<String,TasksManagerModel> getAllTasks() {
        final Cursor c = db.rawQuery("SELECT * FROM " + DOWNLOAD_TABLE_NAME, null);

        final Map<String,TasksManagerModel> listMap = new HashMap<>();
        try {
            while (c.moveToNext()) {
                TasksManagerModel model = new TasksManagerModel();
                model.setId(c.getInt(c.getColumnIndex(TasksManagerModel.ID)));
                model.setName(c.getString(c.getColumnIndex(TasksManagerModel.NAME)));
                model.setUrl(c.getString(c.getColumnIndex(TasksManagerModel.URL)));
                model.setPath(c.getString(c.getColumnIndex(TasksManagerModel.PATH)));
                listMap.put(c.getString(c.getColumnIndex(TasksManagerModel.URL)),model);

            }
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return listMap;
    }

    /**
     * 添加下载任务
     *
     * @param name
     * @param url
     * @param path
     * @return
     */
    public TasksManagerModel addTask(final String name, final String url, final String path) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) {
            return null;
        }
        final int id = FileDownloadUtils.generateId(url, path);

        TasksManagerModel model = new TasksManagerModel();
        model.setId(id);
        model.setName(name);
        model.setUrl(url);
        model.setPath(path);

        final boolean succeed = db.insert(DOWNLOAD_TABLE_NAME, null, model.toContentValues()) != -1;
        return succeed ? model : null;
    }

    /**
     * @param url
     */
    public void deleteDownLoadTask(String url) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            database.delete(DBHelper.DOWNLOAD_TABLE_NAME, TasksManagerModel.URL + "=?", new String[]{url});
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }

    /**
     * 检查本地下载记录、是否下载过
     **/
    public boolean isExistTaskUrl(String url) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String sql = "select count(" + TasksManagerModel.ID + ") from " + DBHelper.DOWNLOAD_TABLE_NAME + " where url=?";
        Cursor cursor = database.rawQuery(sql, new String[]{url});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    /***************************图片下载*************************************/
    /**
     * 得到所有的图片下载任务
     *
     * @return
     */
    public Map<String,ImageTasksManagerModel> getAllImageTasks() {
        final Cursor c = db.rawQuery("SELECT * FROM " + IMAGE_DOWNLOAD_TABLE_NAME, null);

        final Map<String,ImageTasksManagerModel> listMap = new HashMap<>();
        try {
            while (c.moveToNext()) {
                ImageTasksManagerModel model = new ImageTasksManagerModel();
                model.setId(c.getInt(c.getColumnIndex(ImageTasksManagerModel.ID)));
                model.setName(c.getString(c.getColumnIndex(ImageTasksManagerModel.NAME)));
                model.setUrl(c.getString(c.getColumnIndex(ImageTasksManagerModel.URL)));
                model.setPath(c.getString(c.getColumnIndex(ImageTasksManagerModel.PATH)));
                listMap.put(c.getString(c.getColumnIndex(ImageTasksManagerModel.URL)),model);
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return listMap;
    }

    public void beginTransaction(){
        db.beginTransaction();
    }

    public void endTransaction(){
        db.endTransaction();
    }

    /**
     * 添加下载任务
     *
     * @param name
     * @param url
     * @param path
     * @return
     */
    public ImageTasksManagerModel addImageTask(final String name, final String url, final String path) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) {
            return null;
        }
        final int id = FileDownloadUtils.generateId(url, path);

        ImageTasksManagerModel model = new ImageTasksManagerModel();
        model.setId(id);
        model.setName(name);
        model.setUrl(url);
        model.setPath(path);

        final boolean succeed = db.insert(IMAGE_DOWNLOAD_TABLE_NAME, null, model.toContentValues()) != -1;
        return succeed ? model : null;
    }

    /**
     * @param url
     */
    public void deleteImageDownLoadTask(String url) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            database.delete(DBHelper.IMAGE_DOWNLOAD_TABLE_NAME, ImageTasksManagerModel.URL + "=?", new String[]{url});
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }

    /**
     * 检查本地下载记录、是否下载过
     **/
//    public SQLiteDatabase database = null;
    public boolean isExistImageTaskUrl(String url) {
//        if(database == null){
//            database = dbHelper.getReadableDatabase();
//        }
        String sql = "select count(" + ImageTasksManagerModel.ID + ") from " + DBHelper.IMAGE_DOWNLOAD_TABLE_NAME + " where url=?";
        Cursor cursor = db.rawQuery(sql, new String[]{url});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

}
