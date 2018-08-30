package com.example.ebadly.opentable.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.ebadly.opentable.MoviesAdapter;
import com.example.ebadly.opentable.R;
import com.example.ebadly.opentable.dataModels.Movie;
import com.example.ebadly.opentable.viewModels.MainActivityViewModel;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private CompositeDisposable mSubscription;
    private MainActivityViewModel mainActivityViewModel;
    private ProgressBar progressBar;
    private TextView errorMessage;
    private RecyclerView recyclerView;
    private MoviesAdapter moviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSubscription = new CompositeDisposable();
        mainActivityViewModel = new MainActivityViewModel(this);
        errorMessage = findViewById(R.id.error_text);
        recyclerView = findViewById(R.id.content);
        progressBar = findViewById(R.id.progressbar);

        moviesAdapter = new MoviesAdapter(new ArrayList<Movie>(0));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(moviesAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindViewModel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindViewModel();
    }

    /**
     * Used a composite subscriber to hold onto a list of of observables
     * we can then unsubscribe/subcribe all to avoid memory leaks
     */
    private void bindViewModel(){
        mSubscription = new CompositeDisposable();

        mSubscription.add(mainActivityViewModel.getLoadingIndicatorVisibility()
                .subscribe(
                        new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean isloading) throws Exception {
                                setLoadingBar(isloading);
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                showError(throwable.getMessage());
                            }
                        }
                )
        );

        mSubscription.add(mainActivityViewModel.getList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Movie>>() {
                               @Override
                               public void accept(List<Movie> movies) throws Exception {
                                   showMovies(movies);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                            }
                        }
                )
        );
    }

    private void showMovies(List<Movie> movies){
        recyclerView.setVisibility(View.VISIBLE);
        moviesAdapter.replaceData(movies);
    }

    private void showError(String message) {
        recyclerView.setVisibility(View.GONE);
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setText(message);
    }

    private void setLoadingBar(boolean isLoading){
        int showVal = isLoading ? View.VISIBLE : View.GONE;
        progressBar.setVisibility(showVal);
        if(isLoading) {
            recyclerView.setVisibility(View.GONE);
            errorMessage.setVisibility(View.GONE);
        }
    }

    private void unbindViewModel() {
        // unsubscribing from all the subscriptions to ensure we don't have any memory leaks
        mSubscription.clear();
    }
}