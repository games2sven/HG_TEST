package com.highgreat.education.flight;

import android.animation.ValueAnimator;
import android.view.View;

/**
 * Created by Administrator on 2017/3/3.
 */

public class AnimUtils {
    public ValueAnimator mAnimator = null;
    public void valueAnim(final View view, float start, float end, long duration){
        if(mAnimator!=null){
            mAnimator.cancel();
        }
        mAnimator = ValueAnimator.ofFloat(start,end);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                view.setAlpha(alpha);
            }
        });
        mAnimator.setDuration(duration);
        mAnimator.setTarget(view);
        mAnimator.setRepeatCount(0);
        mAnimator.start();
    }
}
