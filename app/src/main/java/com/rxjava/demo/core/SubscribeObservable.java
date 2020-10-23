package com.rxjava.demo.core;

/**
 * 描述：线程切换操作符(上游)
 *
 * @author fzJiang
 * @date 2020-10-22 15:40.
 */
public class SubscribeObservable<T> implements ObservableOnSubscribe<T> {

    private ObservableOnSubscribe<T> mSource;
    private int mThread;

    public SubscribeObservable(ObservableOnSubscribe<T> source, int thread) {
        mSource = source;
        mThread = thread;
    }

    @Override
    public void subscribe(Observer<T> emitter) {
        // 创建线程切换操作符,并切换上游线程
        SubscribeObserver<T> downStream = new SubscribeObserver<>(emitter);
        Schedulers.getInstance().submitSubscribeWork(mSource, downStream, mThread);
    }

    /**
     * 线程切换操作符,用来切换上游线程
     */
    private static class SubscribeObserver<T> implements Observer<T> {

        private Observer<T> mDownStream;

        public SubscribeObserver(Observer<T> downStream) {
            this.mDownStream = downStream;
        }

        @Override
        public void onSubscribe() {
            mDownStream.onSubscribe();
        }

        @Override
        public void onNext(T t) {
            mDownStream.onNext(t);
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
