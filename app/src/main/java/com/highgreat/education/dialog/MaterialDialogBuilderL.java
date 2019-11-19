package com.highgreat.education.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewTreeObserver;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * 多语言弹出框
 * Created by Din on 2016/10/24.
 */

public class MaterialDialogBuilderL extends MaterialDialog.Builder {


    public MaterialDialogBuilderL(@NonNull Context context) {
        super(context);
    }


    @Override
    public MaterialDialog build() {
        return new MaterialDialogL(this);
    }

    public class MaterialDialogL extends MaterialDialog {
        private ViewTreeObserver.OnWindowFocusChangeListener listener;

        protected MaterialDialogL(Builder builder) {
            super(builder);
            if (positiveButton != null) {
                positiveButton.setAllCapsCompat(false);
            }
            if (neutralButton != null) {
                neutralButton.setAllCapsCompat(false);
            }
            if (negativeButton != null) {
                negativeButton.setAllCapsCompat(false);
            }
        }

        public void setOnWindowFocusChangeListener(ViewTreeObserver.OnWindowFocusChangeListener l) {
            this.listener = l;
        }

        @Override
        public void onWindowFocusChanged(boolean hasFocus) {
            super.onWindowFocusChanged(hasFocus);
            if (listener != null) {
                listener.onWindowFocusChanged(hasFocus);
            }
        }
    }

}
