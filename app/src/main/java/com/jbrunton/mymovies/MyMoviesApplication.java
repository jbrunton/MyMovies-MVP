package com.jbrunton.mymovies;

import android.app.Application;

import com.jbrunton.mymovies.app.ApplicationDependencies;
import com.jbrunton.mymovies.app.HttpDependencies;

public class MyMoviesApplication extends Application {
    private ApplicationDependencies dependencies;

    @Override public void onCreate() {
        super.onCreate();
        dependencies = createDependencyGraph();
    }

    public ApplicationDependencies dependencies() {
        return dependencies;
    }

    protected ApplicationDependencies createDependencyGraph() {
        return new HttpDependencies();
    }
}
