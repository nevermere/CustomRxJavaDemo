package com.rxjava.demo.core;

/**
 * 描述：用于创建被观察者对象及绑定
 *
 * @author fzJiang
 * @date 2020-10-22 11:39.
 */
public class Observable<T> {

    private Observer<T> downStream;
    private ObservableOnSubscribe<T> mSource;

    private Observable(ObservableOnSubscribe<T> source) {
        this.mSource = source;
    }

    /**
     * 创建上游虚假对象
     */
    public static <T> Observable<T> create(ObservableOnSubscribe<T> source) {
        return new Observable<T>(source);
    }

    /**
     * doOnNext操作符
     */
    public Observable<T> doOnNext(Func1<T> func1) {
        DoOnNextObservable<T> doOnNextObservable = new DoOnNextObservable<>(mSource, func1);
        return new Observable<T>(doOnNextObservable);
    }

    /**
     * filter过滤操作符
     */
    public Observable<T> filter(Func1<T> func1) {
        FilterObservable<T> filterObservable = new FilterObservable<>(mSource, func1);
        return new Observable<T>(filterObservable);
    }

    /**
     * map操作符
     */
    public <R> Observable<R> map(Func2<T, R> func2) {
        MapObservable<T, R> mapObservable = new MapObservable<>(mSource, func2);
        return new Observable<R>(mapObservable);
    }

    /**
     * 线程切换操作符(上游)
     */
    public Observable<T> subscribeOn(int thread) {
        SubscribeObservable<T> subscribe = new SubscribeObservable<T>(mSource, thread);
        return new Observable<T>(subscribe);
    }

    /**
     * 线程切换操作符(下游)
     */
    public Observable<T> observerOn(int thread) {
        ObserverObservable<T> observable = new ObserverObservable<T>(mSource, thread);
        return new Observable<T>(observable);
    }

    /**
     * 执行订阅
     */
    public void subscribe(Observer<T> downStream) {
        downStream.onSubscribe();
        if (mSource != null) {
            mSource.subscribe(downStream);
        }
    }
}
