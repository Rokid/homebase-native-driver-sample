package com.rokid.homebase.driver.sample.Util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by heshun on 2017/3/8.
 */

public class ThreadFactory {

    private static final ExecutorService mExecutorService = Executors.newCachedThreadPool();

    public static void executeRunnable(Runnable runnable) {
        mExecutorService.execute(runnable);
    }

}
