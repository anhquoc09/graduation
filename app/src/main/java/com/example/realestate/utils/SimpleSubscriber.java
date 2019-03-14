package com.example.realestate.utils;

import android.util.Log;

import rx.Subscriber;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */

public class SimpleSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        Log.d(SimpleSubscriber.class.getSimpleName(),"SimpleSubscriber Exception");
    }

    @Override
    public void onNext(T t) {
    }

    public static <T> SimpleSubscriber<T> create() {
        return new SimpleSubscriber<>();
    }
}
