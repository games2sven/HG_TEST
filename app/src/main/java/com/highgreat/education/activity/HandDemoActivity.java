package com.highgreat.education.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.highgreat.education.R;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.dialog.ResetPasswordTipDialog;
import com.highgreat.education.utils.SpSetGetUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HandDemoActivity extends  BaseActivity {
    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }
    Unbinder unbinder;

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.img_content)
    ImageView img_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_demo);
        initWindow();
        unbinder = ButterKnife.bind(this);

        int  rockmode = SpSetGetUtils.getRockerMode();
        if(rockmode == 2){
            tv_title.setText(getString(R.string.japannese_hand_demo));
            img_content.setImageResource(R.mipmap.ribenshou);
        }else{
            tv_title.setText(getString(R.string.american_hand_demo));
            img_content.setImageResource(R.mipmap.meiguoshou);
        }


    }
    private void initWindow() {
        getWindow().setGravity(Gravity.CENTER);       //设置靠右对齐
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.x = 0;//向右靠齐的情况下向左偏移的像素大小
        p.y= 0;
        p.height= ViewGroup.LayoutParams.MATCH_PARENT;
        p.width= ViewGroup.LayoutParams.MATCH_PARENT;

        getWindow().setAttributes(p);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.img_close)
    void colse (){
        finish();
    }

}
