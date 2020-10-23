package com.rxjava.demo.core;


/**
 * 描述：Map操作符
 * T:上游发射的类型，
 * R:转换之后的类型
 *
 * @author fzJiang
 * @date 2020-10-22 14:53.
 */
public class MapObservable<T, R> implements ObservableOnSubscribe<R> {

    private ObservableOnSubscribe<T> mSource;
    private Func2<T, R> mFunc1;

    public MapObservable(ObservableOnSubscribe<T> source, Func2<T, R> func1) {
        mSource = source;
        mFunc1 = func1;
    }

    @Override
    public void subscribe(Observer<R> downStream) {
        // 创建Map操作符观察者,并传递给被观察者
        MapObserver<T, R> mapObserver = new MapObserver<>(downStream, mFunc1);
        mSource.subscribe(mapObserver);
    }

    /**
     * Map操作符观察者,用来接收被观察者数据
     *
     * @param <T>
     * @param <R>
     */
    private static class MapObserver<T, R> implements Observer<T> {

        private Observer<R> mDownStream;
        private Func2<T, R> mFunc1;

        public MapObserver(Observer<R> downStream, Func2<T, R> func2) {
            this.mDownStream = downStream;
            this.mFunc1 = func2;
        }

        @Override
        public void onSubscribe() {
            // 当接收到上游传来的订阅后，将事件传递给下
            mDownStream.onSubscribe();
        }

        @Override
        public void onNext(T t) {
            // 应用转换规则,并将转换后的数据传递到下游
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
