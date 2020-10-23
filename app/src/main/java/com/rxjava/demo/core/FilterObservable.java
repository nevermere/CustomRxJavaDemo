package com.rxjava.demo.core;

/**
 * 描述：Filter过滤操作符
 *
 * @author fzJiang
 * @date 2020-10-23 14:14.
 */
public class FilterObservable<T> implements ObservableOnSubscribe<T> {

    private ObservableOnSubscribe<T> mSource;
    private Func1<T> mFunc1;

    public FilterObservable(ObservableOnSubscribe<T> source, Func1<T> func1) {
        mSource = source;
        mFunc1 = func1;
    }

    @Override
    public void subscribe(Observer<T> emitter) {
        // 创建Filter过滤操作符,进行数据转换
        FilterObserver<T> filterObserver = new FilterObserver<>(emitter, mFunc1);
        mSource.subscribe(filterObserver);
    }

    /**
     * Filter过滤操作符
     */
    private static class FilterObserver<T> implements Observer<T> {

        private Observer<T> mDownstream;
        private Func1<T> mFunc1;

        public FilterObserver(Observer<T> downstream, Func1<T> func1) {
            mDownstream = downstream;
            mFunc1 = func1;
        }

        @Override
        public void onSubscribe() {
            mDownstream.onSubscribe();
        }

        @Override
        public void onNext(T t) {
            mDownstream.onNext(mFunc1.invoke(t));
        }

        @Override
        public void onError(Throwable e) {
            mDownstream.onError(e);
        }

        @Override
        public void onComplete() {
            mDownstream.onComplete();
        }
    }
}
