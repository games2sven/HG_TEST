package com.highgreat.education.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.highgreat.education.R;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.common.Constants;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.mavCommand.FlightCommand;
import com.highgreat.education.utils.ByteUtil;
import com.highgreat.education.utils.MD5Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/12/12 0012.
 */

public class UploadingDialog implements View.OnClickListener{

    View mView;
    private ImageView img_close;
    private TextView tv_download;
    private ProgressBar progressBar;

    private AlertDialog dialog ;
    private IDialogClickListner mClickListner;
    private Object mData;
    private Context mContext;

    private int trans_remain_fslen;
    private int trans_remain_last;
    private short pack_num_last;
    private int fsLen;
    private final byte[] file_data_buf;

    private short state;

    byte[] payload = new byte[128];
    short count = 0;
    float perProgress = 0;
    private String fileName;
    private final File file;
    private boolean isUpgrading = false;

    public UploadingDialog(Context context,String filename){
        mContext = context;
        this.fileName = filename;
        EventBus.getDefault().register(this);

        file = new File(Constants.FIRMWARE_PATH,filename);
        fsLen = (int) file.length();
        file_data_buf = new byte[fsLen];
        try {
            FileInputStream stream = new FileInputStream(file);
            stream.read(file_data_buf,0,fsLen);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int num = fsLen/128 + 1;
        perProgress = (float) 1 / num * 100;//
    }


    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void onEventMainThread(EventCenter eventCenter){
        if (null != eventCenter) {
            int code = eventCenter.getEventCode();
            Object object = eventCenter.getData();
            switch (code) {
                //固件上传成功
                case EventBusCode.UPLOAD_OS_SUCCESSED:
                    isUpgrading = false;
                    progressBar.setVisibility(View.GONE);
                    tv_download.setText(mContext.getString(R.string.upload_success));
                    tv_download.setTextColor(mContext.getResources().getColor(R.color.white));
                    break;
                case EventBusCode.UPLOAD_OS_PROGRESS:
                    int progress = (int)object;

                    progressBar.setProgress(progress);
                    tv_download.setText(String.format(mContext.getString(R.string.uploading),progress+"%"));
                    break;
                case EventBusCode.APP_INTO_BACKGROUNG://应用退到后台
                    if(isUpgrading) {
                        isUpgrading = false;
                        FlightCommand.cmdUpdate((byte)0x81);
                        dialog.dismiss();
                    }
                    break;
                case EventBusCode.OS_UPGRADE_CALLBACK://开始上传固件f
                    short result = (short)object;
                    Log.i("Sven","开始发送固件"+result+"");
                    if(result == 1){//开始发送固件
                        isUpgrading = true;
                        count = 0;
                        trans_remain_fslen = fsLen;
                        trans_remain_last = trans_remain_fslen;
                        state = 0x01;
                        Arrays.fill(payload,(byte)0);

                        byte[] bytes = fileName.getBytes();
                        System.arraycopy(bytes,0,payload,0,bytes.length);
                        payload[bytes.length] = 0x00;
                        String length = String.valueOf(fsLen);
                        byte[] chars = length.getBytes();
                        System.arraycopy(chars,0,payload,bytes.length+1,chars.length);
                        payload[bytes.length+chars.length+1] = 0x00;
                        String md5 = MD5Util.getFileMD5(file);
                        byte[] md5s = ByteUtil.hex2byte(md5);
                        System.arraycopy(md5s,0,payload,bytes.length+1+chars.length+1,md5s.length);
                        FlightCommand.command_send_file(state, count,  payload);

                        EventBus.getDefault().post(new EventCenter(EventBusCode.UPLOAD_OS_PROGRESS,(int)(count * perProgress)));
                    }else if(result == 0x06){//请求下一包数据
                        //发完第一包数据之后
                        trans_remain_last = trans_remain_fslen;
                        pack_num_last = count;
                        if(trans_remain_fslen > 1024){
                            state = 0x82;
                            Arrays.fill(payload,(byte)0);
                            //连续发8次，满1024个字节之后等收到回复再判断
                            for (int i = 0; i < 8; i++)
                            {
                                for (int j = 0; j < 128; j++)//file_data_buf
                                {
                                    payload[j] = file_data_buf[fsLen - trans_remain_fslen + j];
                                }
                                count++;
                                FlightCommand.command_send_file(state, count,  payload);
                                trans_remain_fslen -= 128;
                            }
                            EventBus.getDefault().post(new EventCenter(EventBusCode.UPLOAD_OS_PROGRESS,(int)(count * perProgress)));
                        } else if (trans_remain_fslen > 128 && trans_remain_fslen <= 1024) {
                            state = 0x02;
                            Arrays.fill(payload,(byte)0);
                            for (int j = 0; j < 128; j++)
                            {
                                payload[j] = file_data_buf[fsLen - trans_remain_fslen + j];
                            }
                            count++;
                            FlightCommand.command_send_file(state, count, payload);
                            EventBus.getDefault().post(new EventCenter(EventBusCode.UPLOAD_OS_PROGRESS,(int)(count * perProgress)));
                            trans_remain_fslen -= 128;
                        } else {
                            state = 0x04;
                            Arrays.fill(payload,(byte)0);
                            for (int j = 0; j < trans_remain_fslen; j++)
                            {
                                payload[j] = file_data_buf[fsLen - trans_remain_fslen + j];
                            }
                            count++;
                            FlightCommand.command_send_file(state, count, payload);
                            Log.i("Sven","progress:"+" count = "+count);
                            EventBus.getDefault().post(new EventCenter(EventBusCode.UPLOAD_OS_PROGRESS,100));
                            trans_remain_fslen = 0;
                        }
                    }else if(result == 0x02){//升级成功
                        EventBus.getDefault().post(new EventCenter(EventBusCode.UPLOAD_OS_SUCCESSED));
                    }else if(result == 0x15){//升级失败
                        isUpgrading = false;
                    }else if(result == 67){//重传
                        trans_remain_fslen = trans_remain_last;
                        count = pack_num_last;
                        Log.i("Sven","trans_remain_last = "+trans_remain_last+" pack_num_last = "+pack_num_last);
                        EventBus.getDefault().post(new EventCenter(EventBusCode.OS_UPGRADE_CALLBACK,(short)0x06));
                    }else if(result == 0x81){//取消升级

                    }
                    break;
            }
        }
    }


    public void showDialog(){
        dialog = new AlertDialog.Builder(mContext).create();
        mView = LayoutInflater.from(mContext).inflate(R.layout.layout_os_update_dialog, null);
        dialog.show();
        dialog.setContentView(mView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(null);
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        initView(mContext);
    }

    private void initView(Context context) {
        img_close = (ImageView)mView.findViewById(R.id.img_close);
        tv_download = (TextView)mView.findViewById(R.id.tv_download);
        progressBar = (ProgressBar)mView.findViewById(R.id.progressBar);

        img_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_close:
                dialog.dismiss();

                if(isUpgrading){
                    isUpgrading = false;
                    FlightCommand.cmdUpdate((byte)0x81);
                }
                break;
            default:
                break;
        }
    }

    public interface IDialogClickListner {
        void leftClick();
        void rightClick(ProgressBar progressBar);
    }


    public void dismiss(){
        if(dialog != null)dialog.dismiss();
    }

}
