package com.highgreat.education.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.highgreat.education.R;
import com.highgreat.education.common.Constants;
import com.highgreat.education.common.UavConstants;
import com.highgreat.education.utils.LogUtil;
import com.highgreat.education.utils.UiUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by 11158 on 2016-11-29.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private List<String> list;
    private String message;
    private HashMap<String, String> map;
    private Context context;
    private onChildListener listener;
    private RecyclerView recyclerView;
    public static final int RED_ITEM = 1;
    public static final int YELLOW_ITEM = 2;
    public static final int GREEN_ITEM = 3;


    public void setListener(onChildListener listener) {
        this.listener = listener;
    }

    public RecycleViewAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == YELLOW_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
            holder = new MyViewHolder(view);
        } else if (viewType == RED_ITEM) {
            holder = new RedViewHolder(LayoutInflater.from(context).inflate(R.layout.red_item, parent, false));
        } else if (viewType == GREEN_ITEM) {
            holder = new GreenViewholder(LayoutInflater.from(context).inflate(R.layout.greenview_item, parent, false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        int index = 0;
        if (type == YELLOW_ITEM) {
            if (holder instanceof MyViewHolder) {
                ((MyViewHolder) holder).imageView.setOnClickListener(this);

                switch (Integer.valueOf(list.get(position))) {
                    case Constants.WARM_1:
                        ((MyViewHolder) holder).textView.setText((R.string.tip_warm1));
                        index = 1;
                        break;
                    case Constants.WARM_2:
                        ((MyViewHolder) holder).textView.setText((R.string.tip_warm2));
                        index = 2;
                        break;
                    case Constants.WARM_3:
                        ((MyViewHolder) holder).textView.setText((R.string.tip_warm3));
                        index = 3;
                        break;
                    case Constants.WARM_4:
                        ((MyViewHolder) holder).textView.setText((R.string.tip_warm4));
                        index = 4;
                        break;
                    case Constants.WARM_5:
                        ((MyViewHolder) holder).textView.setText((R.string.tip_warm5));
                        index = 5;
                        break;
                    case Constants.WARM_6:
                        ((MyViewHolder) holder).textView.setText((R.string.tip_warm6));
                        index = 6;
                        break;
                    default:
                        break;

                }
            }
        } else if (type == RED_ITEM) {
            if (holder instanceof RedViewHolder) {

                switch (Integer.valueOf(list.get(position))) {

                    case Constants.ERROR_1://
                        ((RedViewHolder) holder).tv_red.setText((R.string.tip_error_1));
                        index = 7;
                        break;
                    case Constants.ERROR_2://
                        ((RedViewHolder) holder).tv_red.setText((R.string.tip_error_2));
                        index = 8;
                        break;
                    case Constants.ERROR_3://
                        ((RedViewHolder) holder).tv_red.setText((R.string.tip_error_3));
                        index = 9;
                        break;
                    case Constants.ERROR_4://
                        ((RedViewHolder) holder).tv_red.setText((R.string.tip_error_4));
                        index = 10;
                        break;
                    case Constants.ERROR_5://
                        ((RedViewHolder) holder).tv_red.setText(R.string.tip_error_5);
                        index = 11;
                        break;
                    case Constants.ERROR_6://
                        ((RedViewHolder) holder).tv_red.setText((R.string.tip_error_6));
                        index = 12;
                        break;
                    case Constants.ERROR_7://
                        ((RedViewHolder) holder).tv_red.setText((R.string.tip_error_7));
                        index = 13;
                        break;
                    case Constants.ERROR_8://
                        ((RedViewHolder) holder).tv_red.setText((R.string.tip_error_8));
                        index = 14;
                        break;
                    case Constants.ERROR_9://
                        ((RedViewHolder) holder).tv_red.setText((R.string.tip_error_9));
                        index = 14;
                        break;
                    default:
                        break;
                }
            }//
        } else if (type == GREEN_ITEM) {
            if (holder instanceof GreenViewholder) {
                if (holder instanceof GreenViewholder) {
                    switch (Integer.valueOf(list.get(position))) {
                        default:
                            break;

                    }
                }
            }
        }
//        SoundPoolUtil.getmInstall(context).playIndex(index);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.item_image) {
            listener.onImageClick(recyclerView.getChildAdapterPosition((View) view.getParent()));
        } else {
            int position = recyclerView.getChildAdapterPosition(view);
            if (recyclerView != null && listener != null && !recyclerView.getItemAnimator().isRunning()) {
                listener.onChildClick(recyclerView, view, position, list.get(position));
            }
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_text);
            imageView = (ImageView) itemView.findViewById(R.id.item_image);
        }
    }

    public static class RedViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_red;

        public RedViewHolder(View itemView) {
            super(itemView);
            tv_red = (TextView) itemView.findViewById(R.id.tv_red);
        }
    }

    public class GreenViewholder extends RecyclerView.ViewHolder {
        private TextView tv_green;

        public GreenViewholder(View itemView) {
            super(itemView);
            tv_green = (TextView) itemView.findViewById(R.id.tv_green);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = Integer.valueOf(list.get(position));
        if (type >= 0 && type < 6) {
            return YELLOW_ITEM;
        } else if (type >=6 && type < 14) {
            return RED_ITEM;
        } else if (type >= 14) {
            return GREEN_ITEM;
        } else {
            return RED_ITEM;
        }
    }

    /**
     * 当连接到RecyclerView后，提供数据的时候调用这个方法
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    /**
     * 解绑
     *
     * @param recyclerView
     */
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        recyclerView = null;
    }

    public interface onChildListener {
        void onChildClick(RecyclerView parent, View view, int position, String data);

        void onImageClick(int position);
    }

    public List<String> getList() {
        return list;
    }

    public void Add(int position, String data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    public void Remove(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void AddMessage(int position, String data, String msg) {
        list.add(position, data);
        message = msg;
    }

    public void Change(int position, String data) {
        list.remove(position);
        list.add(position, data);
        notifyItemChanged(position);
    }
}
