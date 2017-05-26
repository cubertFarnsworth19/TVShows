package com.example.android.tvshows.ui;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.adapter.rxjava2.HttpException;

// class used in RxJava2 retryWhen
// http error 429 is when too many requests are made in a given time period,
// tmdb only allows 40 requests every 10 seconds
// the class attempts to download the jsons until completed.
public class RetryUntilDownloaded implements Function<Observable<? extends Throwable>, Observable<?>> {

    private final int retryDelayMillis;

    public RetryUntilDownloaded(final int retryDelayMillis) {

        this.retryDelayMillis = retryDelayMillis;
    }

    @Override
    public Observable<?> apply(final Observable<? extends Throwable> attempts) {
        return attempts
                .flatMap(new Function<Throwable, Observable<?>>() {
                    @Override
                    public Observable<?> apply(final Throwable throwable) {
                        if (throwable instanceof HttpException) {
                            HttpException httpException = (HttpException) throwable;
                            if (httpException.code()==429)
                            // When this Observable calls onNext, the original
                            // Observable will be retried (i.e. re-subscribed).
                            return Observable.timer(retryDelayMillis,
                                    TimeUnit.MILLISECONDS);
                        }

                        // Max retries hit. Just pass the error along.
                        return Observable.error(throwable);
                    }
                });
    }
}
