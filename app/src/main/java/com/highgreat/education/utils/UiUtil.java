package com.highgreat.education.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.highgreat.education.MyApp;
import com.highgreat.education.dialog.MaterialDialogBuilderL;

import java.text.DecimalFormat;
import java.util.Locale;


/**
 * Do Good App
 * 项目名称：Camera
 * 类描述：
 * 创建人：mac-likh
 * 创建时间：15/12/24 11:52
 * 修改人：mac-likh
 * 修改时间：15/12/24 11:52
 * 修改备注：
 */
public class UiUtil {

    public static final String TAG    = UiUtil.class.getName();
    public static final boolean isShow = true;
    /**
     * 防止按钮连续点击
     * time - lastClickTime > 0 防止手机更改系统时间
     *
     * @return
     */
    public static long           lastClickTime;
    private static long           lastClickTimes;
    private static long lastRefreshime;
    static        MaterialDialog mFtpDialog; //飞机ftp升级提示

    /**
     * 测试延时时间
     *
     * @param times
     * @return
     */
    private static Toast toast = null;


    public static Context getContext() {
        return MyApp.getAppContext();
    }

    public static Resources getResource() {
        return getContext().getResources();
    }

    public static MaterialDialog getMaterialDialog(Activity activity, String str) {

        return getMaterialDialog(activity, str, false);
    }

//    public static MaterialDialog getMaterialDialog(Activity activity) {
//
//        return new MaterialDialogBuilderL(activity).cancelable(false)
//                .progress(true, 0)
//                .content(getString(R.string.sending))
//                .itemsGravity(GravityEnum.CENTER)
//                .progressIndeterminateStyle(false)
//                .show();
//    }

//    public static MaterialDialog getMaterialDialog(Activity activity, Context context){
//        return new MaterialDialogBuilderL(activity).cancelable(false)
//                .progress(true,0)
//                .content(context.getString(R.string.sending))
//                .itemsGravity(GravityEnum.CENTER)
//                .progressIndeterminateStyle(false)
//                .show();
//    }

    public static MaterialDialog getMaterialDialog(Activity activity, String str, boolean cancel) {
        return new MaterialDialogBuilderL(activity).cancelable(cancel)
                .progress(true, 0)
                .content(str)
                .itemsGravity(GravityEnum.CENTER)
                .progressIndeterminateStyle(false)
                .show();
    }

    public static void cancelDialog(MaterialDialog dialog, int msgId) {
        if (null != dialog) {
            dialog.cancel();
        }
        if (msgId!=0)
        UiUtil.showToast(msgId);
    }

    public static void cancelDialogNoToast(MaterialDialog dialog) {
        if (null != dialog) {
            dialog.cancel();
        }
    }

    /**
     * @param info
     */
    public static void showToast(String info) {
//        if (isShow && toast == null) {
//            toast = Toast.makeText(getContext(), info, Toast.LENGTH_SHORT);
//        } else {
//            toast.setText(info);
//        }
//        toast.show();
//        LocaleLanguageUtil.getStance().changeLanguage(getContext());
        Toast.makeText(getContext(),info,Toast.LENGTH_SHORT).show();
    }

    /**
     * @param info
     */
    public static Toast showToast(String info, boolean obj) {
//        LocaleLanguageUtil.getStance().changeLanguage(getContext());
        if (isShow && toast == null) {
            toast = Toast.makeText(getContext(), info, Toast.LENGTH_SHORT);
        } else {
            toast.setText(info);
        }
        toast.show();
        return toast;
    }

    public static void showToast(int strId) {
        showToast(getString(strId));
    }

    public static void showToast(Context context, int strId){
        showToast(context.getResources().getString(strId));
    }

    public static String getString(int strId) {
        return getResource().getString(strId);
    }

    public static String getString(int strId, Object... args) {
        if (args == null || args.length <= 0) {
            return getResource().getString(strId);
        }
        return getResource().getString(strId, args);
    }

    public static String getString(Context context, int strId){
        return context.getResources().getString(strId);
    }

    public static int Dp2Px(float dp) {
        final float scale = getResource().getDisplayMetrics().density;
        LogUtil.e(TAG, scale + "");
        return (int) (dp * scale + 0.5f);
    }

    public static int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal,
                getContext().getResources().getDisplayMetrics());
    }


    /**
     * 获得屏幕高度
     *
     * @deprecated
     */
    public static int getScreenWidth() {
        return getScreenWidth(true);
    }

    /**
     * @param auto true:跟随屏幕获取宽高，false:一律按竖屏方式取宽高（宽小于高）
     * @return
     */
    public static int getScreenWidth(boolean auto) {
        WindowManager wm = (WindowManager) UiUtil.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        if (!auto) {
            return Math.min(outMetrics.widthPixels, outMetrics.heightPixels);
        }
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     * <p>
     *
     * @deprecated
     */
    public static int getScreenHeight() {
        return getScreenHeight(true);
    }

    /**
     * @param isFollowScreen true:跟随屏幕获取宽高，false:一律按竖屏方式取宽高（宽小于高）
     * @return
     */
    public static int getScreenHeight(boolean isFollowScreen) {
        WindowManager wm = (WindowManager) UiUtil.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        if (!isFollowScreen) {
            return Math.max(outMetrics.widthPixels, outMetrics.heightPixels);
        }
        return outMetrics.heightPixels;
    }

    /**
     * app更新加载框：返回键可取消、点击屏幕不可取消
     *
     * @param context
     * @param str
     * @param cancel
     * @param listener
     * @return
     */
    public static MaterialDialog getUpdateDialog(Context context, String str, boolean cancel, DialogInterface.OnDismissListener listener) {
        return new MaterialDialogBuilderL(context)
                .cancelable(cancel)
                .canceledOnTouchOutside(false)
                .dismissListener(listener)
                .progress(true, 0)
                .content(str)
                .itemsGravity(GravityEnum.CENTER)
                .progressIndeterminateStyle(false)
                .show();
    }





    public static void showDefaultDialog(Context context, String text, int cancelBtn, int okBtn,
                                         final View.OnClickListener cancelcClick1, final View.OnClickListener positivecClick2) {
        //new MaterialDialogBuilderL(context).content(UiUtil.getContext().getString(contentID))
        new MaterialDialogBuilderL(context).content(text)
                .negativeText(cancelBtn)
                .cancelable(false)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        if (cancelcClick1 != null) {
                            cancelcClick1.onClick(null);
                        }
                    }
                })
                .positiveText(UiUtil.getString(okBtn))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        if (positivecClick2 != null) {
                            positivecClick2.onClick(null);
                        }
                    }
                })
                .show();
    }

    public static void showSimpleNoteDialog(Context context, int contentID, int buttonId,
                                            int cancelbButtonId, final View.OnClickListener click) {
        new MaterialDialogBuilderL(context).content(contentID)
                .cancelable(false)
                .negativeText(UiUtil.getString(cancelbButtonId))
                .positiveText(UiUtil.getString(buttonId))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        if (click != null) {
                            click.onClick(null);
                        }
                    }
                })
                .show();
    }

    /**
     * 应用重启,由于测试要求重置引导页后需要进入预览页面
     */
   /* public static void restartApp(Context context, boolean isStartPreview) {
        AgpsRfreshPop.getInstance(context, null).clearInstance();
        ActivityManager.getInstance().finishAllActivity();
        Intent intent = new Intent(context, LoadingActivity.class);
        intent.putExtra("isStartPreview", isStartPreview);
        context.startActivity(intent);
    }*/

    /**
     * float保留两位小数
     *
     * @param value
     * @return
     */
    public static String getFomatFloat(float value) {
        try {
            Locale locale = Locale.getDefault();
            Locale.setDefault(Locale.US);
            DecimalFormat df = new DecimalFormat("0.00");
            String result = df.format(value);
            Locale.setDefault(locale);
//            DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            return result;//format 返回的是字符串
        } catch (Exception e) {
            //CrashReport.postCatchedException(e);  // bugly会将这个throwable上报
            return "0.00";
        }
    }

    /**
     * 解决edittext的inputtype影响hint的字体样式
     * 需要删除对应xml标签中的inputtype属性 或者password=true的属性
     */
    public static void passwordTextStyleFixed(EditText et) {
       // et.setTypeface(Typeface.DEFAULT);
        et.setTransformationMethod(new PasswordTransformationMethod());
    }


    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 1000 && time - lastClickTime > 0) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public synchronized static boolean isRepeatClick(long times) {
        long time = System.currentTimeMillis();
        if (time - lastClickTimes < times) {
            return true;
        }
        lastClickTimes = time;
        return false;
    }

    /**
     * 自动刷新
     */
    public static void initAutoRefresh(final SwipeRefreshLayout mSwipeLayout) {
        mSwipeLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(true);
            }
        });
    }

    public static boolean isFastRefresh(long yourTime) {
        long time = System.currentTimeMillis();
        if (time - lastRefreshime < yourTime && time - lastRefreshime > 0) {
            return true;
        }
        lastRefreshime = time;
        return false;
    }

    /**
     * @param context 上下文
     * @param context 上下文
     */
    public static void showMaterialDialog(Context context, int contentID, int buttonResId,
                                          final View.OnClickListener click, int cancelResId) {
        new MaterialDialogBuilderL(context).content(UiUtil.getString(contentID))
                .cancelable(false)
                .positiveText(context.getString(buttonResId))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        if (click != null) {
                            click.onClick(null);
                        }
                    }
                })
                .negativeText(context.getString(cancelResId))
                .show();
    }

    /**
     * MD 对话框
     */
    public static MaterialDialog showSimpleAlertDialog(Context context, String content,
                                                       String button) {
        return showSimpleAlertDialog(context, content, button, null);
    }

    public static MaterialDialog showSimpleAlertDialog(Context context, String content, String button,
                                                       final View.OnClickListener click) {
        MaterialDialog dialog = new MaterialDialogBuilderL(context).content(content)
                .positiveText(button)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        if (click != null) {
                            click.onClick(null);
                        }
                    }
                })
                .show();
        return dialog;
    }

    /**
     * 带有标题的
     */
    public static void showSimpleTitleDialog(Context context, int titleID, int contentID,
                                             int positivebButtonId, int cancelbButtonId, final View.OnClickListener click) {
        new MaterialDialogBuilderL(context).title(UiUtil.getString(titleID))
                .content(UiUtil.getString(contentID))
                .cancelable(false)
                .negativeText(UiUtil.getString(cancelbButtonId))
                .positiveText(UiUtil.getString(positivebButtonId))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        if (click != null) {
                            click.onClick(null);
                        }
                    }
                })
                .show();
    }



    /**
     * 双击退出应用
     */
//    public static void exitBy2Click(final Activity activity) {
//        new MaterialDialogBuilderL(activity).content(UiUtil.getString(R.string.exit_app_info))
//                .cancelable(false)
//                .positiveText(R.string.agree)
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
//                        ActivityManager.getInstance().finishAllActivity();
//                        android.os.Process.killProcess(android.os.Process.myPid());   //获取PID
//                        System.exit(0);
//                    }
//                })
//                .negativeText(R.string.disagree)
//                .onNegative(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
//                    }
//                })
//                .show();
//    }

//    public static void showTipsDialog(Context context, String title, String tips) {
//        new MaterialDialogBuilderL(context)
//                .cancelable(false)
//                .title(title)
//                .content(tips)
//                .positiveText(R.string.know)
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        dialog.dismiss();
//                    }
//                })
//                .show();
//    }


    /**
     * 比较服务器版本号是否高于当前版本号
     */
    public static boolean compare(String currentVersion,String lastVersion){
        if(TextUtils.isEmpty(currentVersion) | TextUtils.isEmpty(lastVersion)){
            return false;
        }

        try{
            lastVersion = lastVersion.trim().replaceAll("\\.","");
            currentVersion = currentVersion.trim().replaceAll("\\.","");
            int   lastVersionCode =  Integer.parseInt(lastVersion);
            int   currenttVersionCode =  Integer.parseInt(currentVersion);
            return  lastVersionCode>currenttVersionCode;
        }catch (Exception e){
            return false;
        }
    }
    /**
     * 带有标题的
     */
    public static void showSimpleNoTitleDialog(Context context, boolean  cancelable ,int contentID,
                                               int positivebButtonId, int cancelbButtonId, final View.OnClickListener click) {
        showSimpleNoTitleDialog(context,cancelable,getString(contentID),getString(positivebButtonId),getString(cancelbButtonId),click);
    }
    /**
     * 带有标题的
     */
    public static void showSimpleNoTitleDialog(Context context, boolean  cancelable ,String contentID,
                                               String positivebButtonId, String cancelbButtonId, final View.OnClickListener click) {
        new MaterialDialogBuilderL(context)
                .content(contentID)
                .cancelable(cancelable)
                .negativeText(cancelbButtonId)
                .positiveText(positivebButtonId)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        if (click != null) {
                            click.onClick(null);
                        }
                    }
                })
                .show();
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static float limitValue(float a, float b) {
        float valve = 0;
        final float min = Math.min(a, b);
        final float max = Math.max(a, b);
        valve = valve > min ? valve : min;
        valve = valve < max ? valve : max;
        return valve;
    }

    //隐藏软键盘
    public static void  hideSoftKeyboard(Activity activity,View view){
        InputMethodManager imm1=(InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm1.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}
