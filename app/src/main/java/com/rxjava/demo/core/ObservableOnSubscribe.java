package com.rxjava.demo.core;

/**
 * 描述：被观察者对象,持有观察者对象(上游)
 *
 * @author fzJiang
 * @date 2020-10-22 11:32.
 */
public interface ObservableOnSubscribe<T> {

    /**
     * 注入观察者对象
     *
     * @param emitter Observer
     */
    void subscribe(Observer<T> emitter);
}
