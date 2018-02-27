package com.example.two;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.two.entity.DataEntity;
import com.example.two.entity.RequestParam;
import com.example.two.utils.HttpUtil;
import com.example.yintao.utils.LogUtil;

import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    @Bind(R.id.request)
    Button request;
    @Bind(R.id.cancel)
    Button cancel;
    @Bind(R.id.result)
    TextView result;
    @Bind(R.id.test1)
    Button test1;
    @Bind(R.id.test2)
    Button test2;

    private Subscription sp;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


    }

    public void TestOne()
    {
        // FIXME: 2018/1/9 创建被观察者(基本创建方式)
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>()
        {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception
            {
                emitter.onNext("我发送数据");
            }
        });

        // FIXME: 2018/1/9 创建观察者
        Observer<String> observer = new Observer<String>()
        {
            @Override
            public void onSubscribe(@NonNull Disposable d)
            {

            }

            @Override
            public void onNext(@NonNull String s)
            {

            }

            @Override
            public void onError(@NonNull Throwable e)
            {

            }

            @Override
            public void onComplete()
            {

            }
        };
        //被观察者签署观察者（订阅）
        observable.subscribe(observer);
    }

    @OnClick({R.id.request, R.id.cancel, R.id.test1, R.id.test2})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.request:
                SendRequest();
                break;
            case R.id.cancel:

                break;
            case R.id.test1:
                //  test1();
                //   test2();
                // FlowableTest();
                //                FlowableTest2();
                //                test3();
                //                test4();
//                registerAndLogin();
                heartBeat();
                break;
            case R.id.test2:
                //                sp.request(10);
                break;
        }
    }

    private void SendRequest()
    {
        RequestParam param = new RequestParam();
        param.put("key", "fcc736b44a9f3ed587971eb62276ff0b");
        param.put("page", 10);
        param.put("pagesize", 10);
        param.put("type", 1);
        HttpUtil.getJokeDataApi().getJokeData(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataEntity>()
                {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) //用来切断观察者与被观察者的联系
                    {
                        Log.e(TAG, "onSubscribe: d");
                        d.dispose();
                    }

                    @Override
                    public void onNext(@NonNull DataEntity dataEntity)
                    {
                        Log.e(TAG, "onNext: " + dataEntity.toString());
                        result.setText("结果" + dataEntity.toString());
                    }

                    @Override
                    public void onError(@NonNull Throwable e)
                    {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete()
                    {
                        Log.e(TAG, "onComplete:");
                    }
                });
    }

    /**
     * flatMap发送不一定有序。使用有序的contactMap
     */
    private void test1()
    {
        Observable.just(1, 2, 3).flatMap(new Function<Integer, ObservableSource<String>>()
        {
            @Override
            public ObservableSource<String> apply(@NonNull Integer integer) throws Exception
            {
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < 3; ++i)
                {
                    list.add("发送到来" + integer);
                }
                return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Observer<String>()
        {
            @Override
            public void onSubscribe(@NonNull Disposable d)
            {

            }

            @Override
            public void onNext(@NonNull String s)
            {
                Log.e(TAG, "onNext: " + s);
            }

            @Override
            public void onError(@NonNull Throwable e)
            {

            }

            @Override
            public void onComplete()
            {

            }
        });
    }

    /**
     * zip组合操作，以最短的事件为基准
     * 速度最好差不多，否则使用水缸（对列）
     */
    private void test2()
    {
        Observable<Integer> observable1 = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Observable<String> observable2 = Observable.just("A", "B", "C", "D", "E", "F", "G", "H", "I");

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>()
        {
            @Override
            public String apply(@NonNull Integer integer, @NonNull String s) throws Exception
            {
                return integer + s;
            }
        }).subscribe(new Observer<String>()
        {
            @Override
            public void onSubscribe(@NonNull Disposable d)
            {

            }

            @Override
            public void onNext(@NonNull String s)
            {
                Log.e(TAG, "onNext: " + s);
            }

            @Override
            public void onError(@NonNull Throwable e)
            {

            }

            @Override
            public void onComplete()
            {
                Log.e(TAG, "onComplete: ");
            }
        });
    }

    private void FlowableTest()
    {
        Flowable.create(new FlowableOnSubscribe<Integer>()
        {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Integer> emitter) throws Exception
            {
                for (int i = 0; i < 128; ++i) //缓存只有128个事件
                {
                    emitter.onNext(i);
                }
                Log.e(TAG, "subscribe: " + emitter.requested());
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io())//使用策略
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>()
                {
                    @Override
                    public void onSubscribe(Subscription s)
                    {
                        sp = s;
                        s.request(10);
                    }

                    @Override
                    public void onNext(Integer integer)
                    {
                        Log.e(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t)
                    {
                        Log.e(TAG, "onError: " + t);
                    }

                    @Override
                    public void onComplete()
                    {
                        Log.e(TAG, "onComplete: ");
                    }
                });

    }

    /** Interval发送long事件，从0开始，每隔指定时间，加1发送 */
    private void FlowableTest2()
    {
        Flowable.interval(1, TimeUnit.MICROSECONDS)
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>()
                {
                    @Override
                    public void onSubscribe(Subscription s)
                    {
                        sp = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong)
                    {
                        Log.e(TAG, "onNext: " + aLong);

                    }

                    @Override
                    public void onError(Throwable t)
                    {
                        Log.e(TAG, "onError: " + t);
                    }

                    @Override
                    public void onComplete()
                    {
                        Log.e(TAG, "onComplete: ");
                    }
                });
    }

    /** 将两个事件简单连接 */
    private void test3()
    {
        Observable.concat(Observable.just(1, 2, 3), Observable.just("A", "B", "C")).subscribe(new Consumer<Serializable>()
        {
            @Override
            public void accept(Serializable serializable) throws Exception
            {
                Log.e(TAG, "accept: " + serializable);
            }
        });

        Observable.just(1, 1, 2, 2, 3, 3).distinct().subscribe(new Consumer<Integer>()
        {
            @Override
            public void accept(Integer integer) throws Exception
            {
                Log.e(TAG, "accept: " + integer);//去除重复 1 2 3
            }
        });
        //buffer按长度组合结果输出，并且重起始跳动长度，也许会重复
        Observable.just(1, 2, 3, 4, 5, 6, 7).buffer(3, 2)
                .subscribe(new Consumer<List<Integer>>()
                {
                    @Override
                    public void accept(List<Integer> integers) throws Exception
                    {
                        Log.e(TAG, "accept:integers.size =" + integers.size());
                        for (Integer i : integers)
                        {
                            Log.e(TAG, "accept: i=" + i);
                        }
                    }
                });

        //timer和interval都在新线程
        Observable.timer(2, TimeUnit.SECONDS).subscribe(new Consumer<Long>()
        {
            @Override
            public void accept(Long aLong) throws Exception
            {
                //定时
            }
        });
        Observable.interval(2, TimeUnit.SECONDS)
                .skip(2)//跳过2个数开始计数
                .take(2)//只接受2个数
                .doOnNext(new Consumer<Long>()
                {
                    @Override
                    public void accept(Long aLong) throws Exception
                    {
                        //做一些无关数据的其他操作
                    }
                })
                .subscribe(new Consumer<Long>()
                {
                    @Override
                    public void accept(Long aLong) throws Exception
                    {
                        //重0 开始每隔相同时间输出(i=0)++
                    }
                });
    }


    private void test4()
    {
        /*
         * Single只接受一个参数
         * SingeObserver只会调用onError或者OnSuccess
         */
        Single.just(new Random().nextInt()).subscribe(new SingleObserver<Integer>()
        {
            @Override
            public void onSubscribe(@NonNull Disposable d)
            {
                Log.e(TAG, "onSubscribe: ");
            }

            @Override
            public void onSuccess(@NonNull Integer integer)
            {
                Log.e(TAG, "onSuccess: " + integer);
            }

            @Override
            public void onError(@NonNull Throwable e)
            {
                Log.e(TAG, "onError: ");
            }
        });

        Observable.create(new ObservableOnSubscribe<Integer>()
        {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception
            {
                emitter.onNext(1); // skip
                Thread.sleep(400);
                emitter.onNext(2); // deliver
                Thread.sleep(505);
                emitter.onNext(3); // skip
                Thread.sleep(100);
                emitter.onNext(4); // deliver
                Thread.sleep(605);
                emitter.onNext(5); // deliver
                Thread.sleep(510);
                emitter.onComplete();
            }
        }).debounce(500, TimeUnit.MILLISECONDS) //拥挤丢弃
                .subscribe(new Consumer<Integer>()
                {
                    @Override
                    public void accept(Integer integer) throws Exception
                    {
                        Log.e(TAG, "accept: " + integer);
                    }
                });

        Observable<Integer> observable = Observable.defer(new Callable<ObservableSource<Integer>>() //如果不订阅不会产生新的Observale
        {
            @Override
            public ObservableSource<Integer> call() throws Exception
            {
                return Observable.just(1, 2, 3);
            }
        });

        Disposable subscribe = Observable.just(1, 2, 3, 4).last(5).subscribe(new Consumer<Integer>()
        {
            @Override
            public void accept(Integer integer) throws Exception
            {

            }
        });
        //混合发送
        Observable.merge(observable, observable).subscribe(new Consumer<Integer>()
        {
            @Override
            public void accept(Integer integer) throws Exception
            {

            }
        });
        //迭代操作，结果
        Observable.just(1, 2, 3, 4, 5).reduce(new BiFunction<Integer, Integer, Integer>()
        {
            @Override
            public Integer apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception
            {
                return integer + integer2;
            }
        }).subscribe(new Consumer<Integer>()
        {
            @Override
            public void accept(Integer integer) throws Exception
            {

            }
        });
        //迭代操作，过程
        Observable.just(1, 2, 3, 4, 5).scan(new BiFunction<Integer, Integer, Integer>()
        {
            @Override
            public Integer apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception
            {
                return integer + integer2;
            }
        }).subscribe(new Consumer<Integer>()
        {
            @Override
            public void accept(Integer integer) throws Exception
            {

            }
        });
        //window每隔多长时间跳出内部
        Observable.interval(1, TimeUnit.SECONDS)
                .take(15)
                .window(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Observable<Long>>()
                {
                    @Override
                    public void accept(Observable<Long> longObservable) throws Exception
                    {
                        longObservable.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Long>()
                                {
                                    @Override
                                    public void accept(Long aLong) throws Exception
                                    {

                                    }
                                });
                    }
                });


    }

    private void registerAndLogin()
    {
        Observable.create(new ObservableOnSubscribe<JSONObject>()
        {
            @Override
            public void subscribe(@NonNull ObservableEmitter<JSONObject> emitter) throws Exception
            {
                Log.e(TAG, "Observable: " + Thread.currentThread().getName());
                String response = "{\"result\":1,\"reason\":\"应用未审核超时，请提交认证\"}";
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.get("result").equals(1)) //成功
                {
                    emitter.onNext(jsonObject);
                }
                else //失败
                {
                    emitter.onError(new TimeoutException("22"));
                }
            }
        }).map(new Function<JSONObject, DataEntity>()
        {
            @Override
            public DataEntity apply(@NonNull JSONObject obj) throws Exception
            {
                Log.e(TAG, "map: " + Thread.currentThread().getName());
                if (obj.getString("result").equals("1")) //成功
                {
                    DataEntity dataEntity = new DataEntity();
                    dataEntity.setReason("A");
                    return dataEntity;
                }
                return null;
            }
        }).doOnNext(new Consumer<DataEntity>()
        {
            @Override
            public void accept(DataEntity dataEntity) throws Exception
            {
                Log.e(TAG, "doOnNext: " + Thread.currentThread().getName());

            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DataEntity>()
                {
                    @Override
                    public void accept(DataEntity dataEntity) throws Exception
                    {
                        Log.e(TAG, "subscribe_success: " + Thread.currentThread().getName());
                        Log.e(TAG, "accept: " + dataEntity.toString());
                        //成功
                    }
                }, new Consumer<Throwable>()
                {
                    @Override
                    public void accept(Throwable throwable) throws Exception
                    {
                        Log.e(TAG, "subscribe_failed: " + Thread.currentThread().getName());
                        //失败
                        Log.e(TAG, "Throwable: " + throwable.getMessage());
                    }
                });
    }

    private void heartBeat()
    {
         disposable = Flowable.interval(1, TimeUnit.SECONDS)
                .doOnNext(new Consumer<Long>()
                {
                    @Override
                    public void accept(Long aLong) throws Exception
                    {
                        Log.e(TAG, "accept: " + aLong);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>()
                {
                    @Override
                    public void accept(Long aLong) throws Exception
                    {
                        result.setText("接受：" + aLong);
                    }
                });

    }

    @Override
    protected void onDestroy()
    {
        if (disposable != null)
        {
            LogUtil.e("");
            disposable.dispose();
        }
        super.onDestroy();
    }
}
