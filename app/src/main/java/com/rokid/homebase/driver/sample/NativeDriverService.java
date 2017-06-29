package com.rokid.homebase.driver.sample;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.rokid.homebase.driver.sample.Util.ThreadFactory;


public class NativeDriverService extends Service {

    public static final String TAG = "sample";

    private boolean isCreate = true;

    public static final int REQUEST_PORT_SUCCESS = 1024;

    public static final int REQUEST_PORT_FAILED = 1025;

    public static final int CONN_ERROR = 1026;

    public static final int IOEXCEPTION_ERROR = 1027;

    public static final int JSONEXCEPTION_ERROR = 1028;

    private int port;

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case REQUEST_PORT_SUCCESS:
                    NativeDriverService.this.port = msg.arg1;
                    initServer();
                    break;

                case REQUEST_PORT_FAILED:

                    break;

                case CONN_ERROR:

                    break;

                case IOEXCEPTION_ERROR:

                    break;

                case JSONEXCEPTION_ERROR:

                    break;
            }
        }
    };

    private Runnable requestPortRunnable = new RequestPortTask(mHandler);

    private Runnable serverRunnable = new Runnable() {
        @Override
        public void run() {
            NativeDriverServer.getInstance(port).startServer();
        }
    };

    private void initServer() {
        ThreadFactory.executeRunnable(serverRunnable);
    }

    public NativeDriverService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: is called , get port from server client");
        requestPort();
    }

    private void requestPort() {
        ThreadFactory.executeRunnable(requestPortRunnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: is called");
        if (!isCreate) {
            if (NativeDriverServer.getInstance(port).isAlive()) {
                NativeDriverServer.getInstance(port).closeAllConnections();
                requestPort();
            } else {

                requestPort();
            }
        } else {
            Log.i(TAG, "onStartCommand: the first time call onStartCommand and server is running");
        }

        isCreate = false;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: is called");
        NativeDriverServer.getInstance(port).closeAllConnections();
    }
}
