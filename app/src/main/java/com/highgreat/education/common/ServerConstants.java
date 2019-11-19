package com.highgreat.education.common;

/**
 * Created by chengbin on 2016/3/25.
 * 服务器相关常亮
 */
public class ServerConstants {
  public static final String  A               = "1";
  public static       boolean LOG_IN_STATE    = false;
  public static       String  SP_LOG_IN_STATE = "spLogInState";

  public static final int COUNTDWON_TIME = 60000;//120秒
  public static final int CONN_TIMEOUT   = 10;

  public static String SP_USER_NICKNAME             = "spUserNickname";
  public static String SP_USER_GENDER               = "spUserGender";
  public static String SP_USER_GENDER_INT           = "userGender";
  public static String SP_USER_LEVEL                = "userLV";
  public static String SP_USER_LEVEL_INT            = "userLevel";
  public static String SP_USER_HEAD                 = "spUserHead";
  public static String SP_INTRODUCE                 = "spIntroduce";
  public static String SP_IS_UPDATE_DIALOG_HAS_SHOW = "spIsUpdateDialogHasShow";

  public static String U_STATUS        = "uStatus";
  public static String U_NICKNAME      = "uNickname";
  public static String U_GENDER        = "uGender";
  public static String U_LEVEL         = "uLevel";
  public static String U_HEAD_PATH     = "uHeadPath";
  public static String U_INTRODUCE     = "uIntroduce";
  public static String U_FIRST_INSTALL = "uFirstInstall";
  public static String U_FLY_TIME      = "fly_time";

  public static String SP_VERSION_CODE = "spVersionCode";

  public static final String FTP_INTENT_KEY = "ftpIntentFragmentKey";
  public static final String FTP_INTENT_VALUE = "ftpIntentFragmentInde";

  public static String SP_OTA_INTENT_KEY = "fTPIntentKey";
  public static String SP_OTA_INTENT_VALUE = "oTAIntentvalue";

  public static String REGISTER_OR_RETRIVE = "";
  public static final String REGISTER = "register";
  public static final String RETRIVE = "retrive";

  public static final int APP_MANUAL_UPDATE = 1;
  public static final int APP_AUTO_UPDATE   = 2;
  //*********************以下值勿改动**************************
  public static final int RESPONSE_FAIL     = -1;
  public static final int RESPONSE_SUCCESS  = 11;

  public static final String SESS = "cbsess";


  //*********************以上值勿改动**************************

  /*  //存储路径
    public static final String SUGGESTION_FEEDBACK_DIALOG = Environment.getExternalStorageDirectory().getPath() + "/Android/data/" +
            mContext.getPackageName() + "/app/";*/
}
