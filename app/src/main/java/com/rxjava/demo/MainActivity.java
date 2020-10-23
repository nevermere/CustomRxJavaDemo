package com.rxjava.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.rxjava.demo.core.Func1;
import com.rxjava.demo.core.Func2;
import com.rxjava.demo.core.Observable;
import com.rxjava.demo.core.ObservableOnSubscribe;
import com.rxjava.demo.core.Observer;
import com.rxjava.demo.core.Schedulers;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.btn_action);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                doAction();
            }
        });
    }

    private void doAction() {
        Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(Observer<String> emitter) {
                Log.i("RxJava", "------上游发送事件------" + Thread.currentThread().getName());

                emitter.onNext("上游开始发送数据" + Thread.currentThread().getName());
                emitter.onComplete();
            }
        }).map(new Func2<String, Double>() {

            @Override
            public Double invoke(String s) {
                Log.i("RxJava", "------map操作符数据转换------" + Thread.currentThread().getName());
                return 20.0;
            }
        }).subscribeOn(Schedulers.io())
                .doOnNext(new Func1<Double>() {

                    @Override
                    public Double invoke(Double aDouble) {
                        Log.i("RxJava", "------doOnNext操作符------" + Thread.currentThread().getName());
                        return 15.0;
                    }
                }).observerOn(Schedulers.mainThread())
                .subscribe(new Observer<Double>() {

                    @Override
                    public void onSubscribe() {
                        Log.i("RxJava", "------下游---onSubscribe---" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(Double d) {
                        Log.i("RxJava", "------下游---onNext---" + d + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("RxJava", "------下游---onError---" + e.getMessage() + Thread.currentThread().getName());
                    }

                    @Override
                    public void onComplete() {
                        Log.i("RxJava", "------下游---onComplete---" + Thread.currentThread().getName());
                    }
                });
    }
}