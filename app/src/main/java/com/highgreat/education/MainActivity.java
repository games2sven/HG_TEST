package com.highgreat.education;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.highgreat.education.activity.FolkCameraActivity;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.common.UavConstants;
import com.highgreat.education.mavCommand.TcpClient;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_playLive)
    Button btn_playLive;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        btn_playLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 防止频繁的切换过快，导致问题
                 *
                 *Prevent frequent switching too fast and cause problems.
                 */
                btn_playLive.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UavConstants.needReconnect = true;
                        TcpClient.getmInstance().startConn();

                        startActivity(new Intent(getApplication(), FolkCameraActivity.class));
                    }
                }, 500);

            }
        });

//        FirmwareDownLoadDialog mDialog = new FirmwareDownLoadDialog(new FirmwareDownLoadDialog.IDialogClickListner() {
//            @Override
//            public void leftClick() {
//
//            }
//
//            @Override
//            public void rightClick() {
//
//                UploadingDialog dialog = new UploadingDialog(new UploadingDialog.IDialogClickListner() {
//                    @Override
//                    public void leftClick() {
//
//                    }
//
//                    @Override
//                    public void rightClick(ProgressBar progressBar) {
////                        mProgressBar = progressBar;
//                        handler.postDelayed(runnable, 100);
//                    }
//                });
//                dialog.showDialog(MainActivity.this);
//
//            }
//        });
//        mDialog.showDialog(this);

    }


    private int i = 0;
    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            i++;
            EventBus.getDefault().post(new EventCenter(EventBusCode.UPLOAD_OS_PROGRESS,i));
            handler.postDelayed(this, 100);
            if (i == 100) {
                handler.removeCallbacks(this);

            }
//            mProgressBar.setProgress(i);
        }
    };


}
