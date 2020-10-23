package com.rxjava.demo.core;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;

/**
 * 描述：线程切换类
 *
 * @author fzJiang
 * @date 2020-10-22 15:53.
 */
public class Schedulers {

    private static final int IO = 0;
    private static final int MAIN = 1;

    /**
     * io线程池
     */
    private static final ExecutorService IO_THREAD = Executors.newCachedThreadPool();

    /**
     * 主线程Handler
     */
    private static final Handler MAIN_THREAD = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            msg.getCallback().run();
        }
    };

    private static Schedulers INSTANCE;

    public static Schedulers getInstance() {
        if (INSTANCE == null) {
            synchronized (Schedulers.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Schedulers();
                }
            }
        }
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public void submitSubscribeWork(ObservableOnSubscribe source, Observer downStream, int thread) {
        switch (thread) {
            case IO:
                IO_THREAD.execute(new Runnable() {

                    @Override
                    public void run() {
                        source.subscribe(downStream);
                    }
                });
                break;

            case MAIN:
                Message message = Message.obtain(MAIN_THREAD,
                        new Runnable() {

                            @Override
                            public void run() {
                                source.subscribe(downStream);
                            }
                        });
                MAIN_THREAD.sendMessage(message);
                break;

            default:
                break;
        }
    }

    public void submitObserverWork(Func func, int thread) {
        switch (thread) {
            case IO:
                IO_THREAD.execute(new Runnable() {

                    @Override
                    public void run() {
                        func.invoke();
                    }
                });
                break;

            case MAIN:
                Message message = Message.obtain(MAIN_THREAD,
                        new Runnable() {

                            @Override
                            public void run() {
                                func.invoke();
                            }
                        });
                MAIN_THREAD.sendMessage(message);
                break;

            default:
                break;
        }
    }

    public static int io() {
        return IO;
    }

    public static int mainThread() {
        return MAIN;
    }
}
