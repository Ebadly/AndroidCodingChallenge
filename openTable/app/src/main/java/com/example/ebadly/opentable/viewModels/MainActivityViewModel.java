package com.example.ebadly.opentable.viewModels;

import android.content.Context;
import android.support.annotation.NonNull;
import com.example.ebadly.opentable.MovieManager;
import com.example.ebadly.opentable.dataModels.Movie;
import com.example.ebadly.opentable.network.NYTimesService;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

/**
 * MVVM
 * This view model handles the progress bar logic
 * and retrieval of list
 */
public class MainActivityViewModel {

    private MovieManager movieManager;
    private BehaviorSubject<Boolean> loadingIndicator;

    public MainActivityViewModel(Context context){
        movieManager = new MovieManager(context);
        loadingIndicator = BehaviorSubject.create();
        loadingIndicator.onNext(false);
    }

    @NonNull
    public Observable<Boolean> getLoadingIndicatorVisibility() {
        return Observable.just(loadingIndicator.getValue());
    }

    @NonNull
    public Observable<List<Movie>> getList(){
        return movieManager.getMovieList(NYTimesService.OrderBy.BY_DATE)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        loadingIndicator.onNext(true);
                    }
                })
                .doOnNext(new Consumer<List<Movie>>() {
                    @Override
                    public void accept(List<Movie> movies) throws Exception {
                        loadingIndicator.onNext(false);
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        loadingIndicator.onNext(false);
                    }
                });
    }
}
