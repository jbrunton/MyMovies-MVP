package com.jbrunton.mymovies;

import android.app.Application;

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
