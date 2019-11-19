package com.highgreat.education.mavCommand;


import android.util.Log;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Parser;
import com.highgreat.education.common.UavConstants;
import com.highgreat.education.manager.ThreadManager;
import com.highgreat.education.utils.ByteUtil;
import com.highgreat.education.utils.LogUtil;

import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 季白
 *         create time 2018/8/10.
 */

public class TcpClient {
    public static final String TAG = "TcpClient";
    /**
     * 连接的服务器IP地址
     */
    private String serverIP = "";
    public static TcpClient mInstance = null;
    /**
     * 连接的socket服务端口
     */
    private int serverPort = -1;
    private InputStream is;
    private boolean isRun = false;
    private byte buff[] = new byte[4096];
    private String rcvMsg;
    private Socket socket;
    private int rcvLen;
    /**
     * socket的timeout时间，默认为0
     */
    private int soTimeOut = 0;
    private Runnable connRun;
    private Runnable sendMsgRun;
    /**
     * TCP连接的状态监听
     */
    private int numberOfCores = Runtime.getRuntime().availableProcessors();
    /**
     * 额外线程空状态生存时间
     */
    private int keepAliveTime = 1;
    private BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
    private boolean needReConn = false;
    private int reConnTime = 10;
    ExecutorService executorService = new ThreadPoolExecutor(numberOfCores,
            numberOfCores * 2, keepAliveTime, TimeUnit.SECONDS, taskQueue);

    private FlightReceiveMsg flightReceiveMsg = null;

    private Parser mParser = new Parser();

    public static TcpClient getmInstance() {
        if (mInstance == null) {
            synchronized (TcpClient.class) {
                mInstance = new TcpClient(UavConstants.ipAdress , UavConstants.mavPort);
            }
        }
        return mInstance;
    }

    public TcpClient(String serverIp, int port) {
        this.serverIP = serverIp;
        this.serverPort = port;
        reconnect();
    }

    /**
     * 设置是否需要断开后重新连接
     * @param needReConn
     */
    public void setNeedReConn(boolean needReConn) {
        this.needReConn = needReConn;
    }

    /**
     * 重连
     */
    private void reconnect(){

        ThreadManager.getInstance().addWorkStealingPool(new Runnable() {
            @Override
            public void run() {
                while(UavConstants.needReconnect){
                    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if((System.currentTimeMillis()-UavConstants.tCPConnectTime)>=5000L && UavConstants.needReconnect){//未连接
                        if(socket != null){
                            closeTcpSocket();
                        }
                        startConn();
                    }
                }
            }
        });

    }

    public void startConn() {
        connRun = new Runnable() {
            @Override
            public void run() {

                if (isRun) {
                    return;
                }
                try {
                    socket = new Socket(serverIP, serverPort);
                    socket.setSoTimeout(soTimeOut);
                    is = socket.getInputStream();

                    if (socket == null || is == null) {
                        return;
                    }

                    isRun = true;
                    while (isRun) {
                        try {
                            rcvLen = is.read(buff);
                            byte[] data = new byte[rcvLen];
                            System.arraycopy(buff, 0, data, 0, rcvLen);

                            UavConstants.tCPConnectTime=System.currentTimeMillis();
                            //将收到的消息发给主界面

                            MAVLinkPacket m = null;
                            //将收到的消息发给主界面
                            if (rcvLen>0)
                            UavConstants.CURRENT_FLIGHT_CONNECTED = true;
                            for (int i = 0; i < data.length; i++) {
                                m = mParser.mavlink_parse_char(data[i] & 0xff);
                                if (flightReceiveMsg == null) {
                                    flightReceiveMsg = new FlightReceiveMsg();
                                }
                                if (m != null)
                                    flightReceiveMsg.handleMessage(m);
                                if (null != m) {
                                    m = null;
                                }
                            }

//                            LogUtil.e("Sven","收到的数据"+ByteUtil.bytesToHexString(data));
                        } catch (Exception e) {
                            closeTcpSocket();
                        }
                        Thread.sleep(20);
                    }
                } catch (Exception e) {
                    closeTcpSocket();
                }
            }
        };
        executorService.execute(connRun);
    }


    public synchronized void send(final byte[] data) {
        if(socket != null && socket.isConnected()){
            sendMsgRun = new Runnable() {
                @Override
                public void run() {
                    try {
//                        LogUtil.i("Sven", "发送的数据---------------------------------------------:" + ByteUtil.bytesToHexString(data));
                        socket.getOutputStream().write(data);
                        socket.getOutputStream().flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            executorService.execute(sendMsgRun);
        }
    }

    public void closeTcpSocket() {
        if (is==null||socket==null||executorService==null) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    isRun = false;
                    is.close();
                    socket.close();
//                    EventBus.getDefault().post(new EventCenter(EventCode.CODE_DICONNECTED_WIFI));
                } catch (Exception e) {
                    LogUtil.i("Sven","close exception"+e.getMessage());
                } finally {
                    flightReceiveMsg = null;
//                    socket = null;
                }
            }
        }).start();
    }
}
