package com.reales.realestate.data.remote.rest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */

public final class SchedulerProvider {

    private static ExecutorService mExecutorService = Executors.newFixedThreadPool(4);

    public static Scheduler io(){
        return Schedulers.io();
    }

    public static Scheduler ui(){
        return AndroidSchedulers.mainThread();
    }

    public static Scheduler background(){
        return Schedulers.from(mExecutorService);
    }
}
