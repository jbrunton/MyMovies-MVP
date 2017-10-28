package com.jbrunton.mymovies.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.crashlytics.android.Crashlytics;
import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.app.discover.DiscoverFragment;
import com.jbrunton.mymovies.app.search.SearchFragment;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private FrameLayout content;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content, new SearchFragment())
                            .commit();
                    return true;
                case R.id.navigation_discover:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content, new DiscoverFragment())
                            .commit();
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        content = (FrameLayout) findViewById(R.id.content);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, new SearchFragment())
                .commit();
    }

}
