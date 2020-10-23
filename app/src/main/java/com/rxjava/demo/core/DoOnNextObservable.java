package com.rxjava.demo.core;

/**
 * 描述：DoOnNext操作符
 *
 * @author fzJiang
 * @date 2020-10-23 10:19.
 */
public class DoOnNextObservable<T> implements ObservableOnSubscribe<T> {

    private ObservableOnSubscribe<T> mSource;
    private Func1<T> mFunc1;

    public DoOnNextObservable(ObservableOnSubscribe<T> source, Func1<T> func1) {
        mSource = source;
        mFunc1 = func1;
    }

    @Override
    public void subscribe(Observer<T> emitter) {
        // 创建DoOnNext操作符,实现连接操作
        OnNextObservable<T> observable = new OnNextObservable<>(emitter, mFunc1);
        mSource.subscribe(observable);
    }

    /**
     * DoOnNext操作符
     */
    private static class OnNextObservable<T> implements Observer<T> {

        private Observer<T> mDownStream;
        private Func1<T> mFunc1;

        public OnNextObservable(Observer<T> downStream, Func1<T> func1) {
            mDownStream = downStream;
            mFunc1 = func1;
        }

        @Override
        public void onSubscribe() {
            mDownStream.onSubscribe();
        }

        @Override
        public void onNext(T t) {
            mDownStream.onNext(mFunc1.invoke(t));
        }

        @Override
        public void onError(Throwable e) {
            mDownStream.onError(e);
        }

        @Override
        public void onComplete() {
            mDownStream.onComplete();
        }
    }
}
