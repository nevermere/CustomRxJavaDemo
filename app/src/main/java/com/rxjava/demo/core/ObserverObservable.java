package com.rxjava.demo.core;

/**
 * 描述：线程切换操作符(下游)
 *
 * @author fzJiang
 * @date 2020-10-23 9:32.
 */
public class ObserverObservable<T> implements ObservableOnSubscribe<T> {

    private ObservableOnSubscribe<T> mSource;
    private int mThread;

    public ObserverObservable(ObservableOnSubscribe<T> source, int thread) {
        mSource = source;
        mThread = thread;
    }

    @Override
    public void subscribe(Observer<T> emitter) {
        // 创建线程切换操作符,切换下游线程
        ObserverObserver<T> observerObserver = new ObserverObserver<>(emitter, mThread);
        mSource.subscribe(observerObserver);
    }

    /**
     * 线程切换操作符,用来切换下游线程
     */
    private static class ObserverObserver<T> implements Observer<T> {

        private Observer<T> mDownStream;
        private int mThread;

        public ObserverObserver(Observer<T> downStream, int thread) {
            this.mDownStream = downStream;
            mThread = thread;
        }

        @Override
        public void onSubscribe() {
            Schedulers.getInstance()
                    .submitObserverWork(() -> mDownStream.onSubscribe(), mThread);
        }

        @Override
        public void onNext(T t) {
            Schedulers.getInstance()
                    .submitObserverWork(() -> mDownStream.onNext(t), mThread);
        }

        @Override
        public void onError(Throwable e) {
            Schedulers.getInstance()
                    .submitObserverWork(() -> mDownStream.onError(e), mThread);
        }

        @Override
        public void onComplete() {
            Schedulers.getInstance()
                    .submitObserverWork(() -> mDownStream.onComplete(), mThread);
        }
    }
}
