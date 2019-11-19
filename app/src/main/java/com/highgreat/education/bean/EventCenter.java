package com.highgreat.education.bean;

/**
 * Do Good App
 * 项目名称：Camera
 * 类描述：EventBus的事件主类
 *          @Override
            protected void onEventComming(EventCenter eventCenter) {
            if (null == mMusicsPresenter) {
            return;
            }
            int eventCode = eventCenter.getEventCode();
            switch (eventCode) {
            case Constants.EVENT_START_PLAY_MUSIC:
            mMusicsPresenter.onRePlay();
            break;
            case Constants.EVENT_STOP_PLAY_MUSIC:
            mMusicsPresenter.onPausePlay();
            break;
            }
            }
 * 创建人：mac-likh
 * 创建时间：16/1/5 10:15
 * 修改人：mac-likh
 * 修改时间：16/1/5 10:15
 * 修改备注：
 */
public class EventCenter<T> {

    /**
     * reserved data
     */
    private T data;

    /**
     * this code distinguish between different events
     */
    private int eventCode = -1;

    public EventCenter(int eventCode) {
        this(eventCode, null);
    }

    public EventCenter(int eventCode, T data) {
        this.eventCode = eventCode;
        this.data = data;
    }

    /**
     * get event code
     *
     * @return
     */
    public int getEventCode() {
        return this.eventCode;
    }

    /**
     * get event reserved data
     *
     * @return
     */
    public T getData() {
        return this.data;
    }
}
