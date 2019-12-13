package com.example.lwxg.changweistory.module.down;

import com.example.lwxg.changweistory.model.DownloadInfo;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class DownLoadObserver implements Observer<DownloadInfo> {

    protected Disposable d;//可以用于取消注册的监听者
    protected DownloadInfo downloadInfo;

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
    }


    @Override
    public void onNext(DownloadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
    }


    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

}