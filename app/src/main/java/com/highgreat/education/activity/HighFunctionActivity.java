package com.highgreat.education.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.highgreat.education.R;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.common.EventBusCode;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HighFunctionActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.mode_1)
    TextView mode_1;
    @BindView(R.id.mode_2)
    TextView mode_2;
    @BindView(R.id.mode_3)
    TextView mode_3;
    @BindView(R.id.mode_4)
    TextView mode_4;
    @BindView(R.id.mode_5)
    TextView mode_5;
    @BindView(R.id.img_mode)
    ImageView img_mode;
    @BindView(R.id.tv_tips)
    TextView tv_tips;
    @BindView(R.id.tv_start)
    TextView tv_start;
    @BindView(R.id.img_close)
    ImageView img_close;

    private int mode = 2;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_high_function);
        initWindow();
        ButterKnife.bind(this);

        mode_1.setOnClickListener(modeClick);
        mode_2.setOnClickListener(modeClick);
        mode_3.setOnClickListener(modeClick);
        mode_4.setOnClickListener(modeClick);
        mode_5.setOnClickListener(modeClick);
        tv_start.setOnClickListener(this);
        img_close.setOnClickListener(this);
        mode_2.performClick();
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


    private void clearSelect(){
        mode_1.setTextColor(getResources().getColor(R.color.white_20));
        mode_2.setTextColor(getResources().getColor(R.color.white_20));
        mode_3.setTextColor(getResources().getColor(R.color.white_20));
        mode_4.setTextColor(getResources().getColor(R.color.white_20));
        mode_5.setTextColor(getResources().getColor(R.color.white_20));
        mode_1.setBackground(null);
        mode_2.setBackground(null);
        mode_3.setBackground(null);
        mode_4.setBackground(null);
        mode_5.setBackground(null);
    }

    private View.OnClickListener modeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clearSelect();

            ((TextView)v).setTextColor(getResources().getColor(R.color.white));
            v.setBackground(getResources().getDrawable(R.drawable.rectangle_bg));

            switch (v.getId()){
                case R.id.mode_1:
                    img_mode.setImageResource(R.mipmap.mode_paofei);
                    tv_tips.setText(R.string.mode_1_tips);
                    tv_start.setText(R.string.mode_1_start);
                    mode = 1;
                    break;
                case R.id.mode_2:
                    img_mode.setImageResource(R.mipmap.mode_huanrao);
                    tv_tips.setText(R.string.mode_2_tips);
                    tv_start.setText(R.string.mode_2_start);
                    mode = 2;
                    break;
                case R.id.mode_3:
                    img_mode.setImageResource(R.mipmap.mode_xuanzhuan);
                    tv_tips.setText(R.string.mode_3_tips);
                    tv_start.setText(R.string.mode_3_start);
                    mode = 3;
                    break;
                case R.id.mode_4:
                    img_mode.setImageResource(R.mipmap.mode_fangun);
                    tv_tips.setText(R.string.mode_4_tips);
                    tv_start.setText(R.string.mode_4_start);
                    mode = 4;
                    break;
                case R.id.mode_5:
                    img_mode.setImageResource(R.mipmap.mode_tantiao);
                    tv_tips.setText(R.string.mode_5_tips);
                    tv_start.setText(R.string.mode_5_start);
                    mode = 5;
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_start:
                EventBus.getDefault().post(new EventCenter(EventBusCode.HIGH_FUNCTION_CODE,mode));
                finish();
                break;
            case R.id.img_close:
                finish();
                break;
        }
    }
}
