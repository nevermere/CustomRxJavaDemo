package com.rxjava.demo.core;

/**
 * 描述：观察者对象(下游)
 *
 * @author fzJiang
 * @date 2020-10-22 11:31.
 */
public interface Observer<T> {

    void onSubscribe();

    void onNext(T t);

    void onError(Throwable e);

    void onComplete();
}
