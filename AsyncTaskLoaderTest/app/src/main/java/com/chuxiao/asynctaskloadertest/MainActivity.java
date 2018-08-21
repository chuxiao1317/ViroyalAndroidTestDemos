/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chuxiao.asynctaskloadertest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.chuxiao.asynctaskloadertest.utilties.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    /* A constant to save and restore the URL that is being displayed */
    private static final String SEARCH_QUERY_URL_EXTRA = "query";

    // TODO (28) Remove the key for storing the search results JSON
    /* A constant to save and restore the JSON that is being displayed */
    private static final String SEARCH_RESULTS_RAW_JSON = "results";

    // TODO (2) Create a constant int to uniquely identify your loader. Call it GITHUB_SEARCH_LOADER
    /**
     * 加载器唯一标识
     */
    private static final int GITHUB_SEARCH_LOADER = 22;

    private EditText mSearchBoxEditText;

    private TextView mUrlDisplayTextView;
    private TextView mSearchResultsTextView;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = findViewById(R.id.et_search_box);

        mUrlDisplayTextView = findViewById(R.id.tv_url_display);
        mSearchResultsTextView = findViewById(R.id.tv_github_search_results_json);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        // 如有缓存则显示在UI上
        if (savedInstanceState != null) {
            String queryUrl = savedInstanceState.getString(SEARCH_QUERY_URL_EXTRA);
//
//            // TODO (26) Remove the code that retrieves the JSON
//            String rawJsonSearchResults = savedInstanceState.getString(SEARCH_RESULTS_RAW_JSON);
//
            mUrlDisplayTextView.setText(queryUrl);
//            // TODO (25) Remove the code that displays the JSON
//            mSearchResultsTextView.setText(rawJsonSearchResults);
        }

        // TODO (24) Initialize the loader with GITHUB_SEARCH_LOADER as the ID, null for the bundle, and this for the context
        // 初始化加载器
        getSupportLoaderManager().initLoader(GITHUB_SEARCH_LOADER, null, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String queryUrl = mUrlDisplayTextView.getText().toString();
        outState.putString(SEARCH_QUERY_URL_EXTRA, queryUrl);

        // TODO (27) Remove the code that persists the JSON
//        String rawJsonSearchResults = mSearchResultsTextView.getText().toString();
//        outState.putString(SEARCH_RESULTS_RAW_JSON, rawJsonSearchResults);
    }

    /**
     * 搜索
     */
    private void makeGithubSearchQuery() {
        String githubQuery = mSearchBoxEditText.getText().toString();

        // TODO (17) If no search was entered, indicate that there isn't anything to search for and return

        if (TextUtils.isEmpty(githubQuery)) {
            mUrlDisplayTextView.setText("输入内容为空，无法搜索到结果");
            return;
        }

        // 显示搜索结果的URL
        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
        mUrlDisplayTextView.setText(githubSearchUrl.toString());

        // TODO (18) Remove the call to execute the AsyncTask
//        new GithubQueryTask().execute(githubSearchUrl);

        // TODO (19) Create a bundle called queryBundle
        // TODO (20) Use putString with SEARCH_QUERY_URL_EXTRA as the key and the String value of the URL as the value
        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_URL_EXTRA, githubSearchUrl.toString());

        // TODO (21) Call getSupportLoaderManager and store it in a LoaderManager variable
        // TODO (22) Get our Loader by calling getLoader and passing the ID we specified
        // TODO (23) If the Loader was null, initialize it. Else, restart it.
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> githubSearchLoader = loaderManager.getLoader(GITHUB_SEARCH_LOADER);
        // 保证加载器已经初始化
        if (githubSearchLoader == null) {
            loaderManager.initLoader(GITHUB_SEARCH_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(GITHUB_SEARCH_LOADER, queryBundle, this);
        }
    }

    /**
     * This method will make the View for the JSON data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showJsonDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the JSON data is visible */
        mSearchResultsTextView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the JSON
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    /**
     * 加载器重写方法1
     */
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            private String mGithubJson;

            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }
                mLoadingIndicator.setVisibility(View.VISIBLE);
                if (mGithubJson != null) {
                    // 将结果发送给监听器
                    // 加载器与Activity的生命周期绑定的，因此，当用户导航离开Activity然后返回
                    //  此时就会重新加载，但是我们可以通过缓存和重新交付，从而避免额外的加载，优化性能
                    deliverResult(mGithubJson);
                } else {
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public String loadInBackground() {
                String searchQueryUtlString = args.getString(SEARCH_QUERY_URL_EXTRA);
                if (searchQueryUtlString == null || TextUtils.isEmpty(searchQueryUtlString)) {
                    return null;
                }

                try {
                    URL githubUrl = new URL(searchQueryUtlString);
                    String githubSearchResults = NetworkUtils.getResponseFromHttpUrl(githubUrl);
                    return githubSearchResults;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(@Nullable String data) {
                // 保存数据以备后用
                mGithubJson = data;
                super.deliverResult(data);
            }
        };
    }

    /**
     * 加载器重写方法2
     */
    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data == null) {
            showErrorMessage();
        } else {
            Toast.makeText(this, "旋转屏幕看看搜索结果是否还在", Toast.LENGTH_SHORT).show();
            mSearchResultsTextView.setText(data);
            showJsonDataView();
        }
    }

    /**
     * 加载器重写方法3
     */
    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    // TODO (3) Override onCreateLoader
    // Within onCreateLoader
    // TODO (4) Return a new AsyncTaskLoader<String> as an anonymous inner class with this as the constructor's parameter
    // TODO (5) Override onStartLoading
    // Within onStartLoading

    // TODO (6) If args is null, return.

    // TODO (7) Show the loading indicator

    // TODO (8) Force a load
    // END - onStartLoading

    // TODO (9) Override loadInBackground

    // Within loadInBackground
    // TODO (10) Get the String for our URL from the bundle passed to onCreateLoader

    // TODO (11) If the URL is null or empty, return null

    // TODO (12) Copy the try / catch block from the AsyncTask's doInBackground method
    // END - loadInBackground

    // TODO (13) Override onLoadFinished

    // Within onLoadFinished
    // TODO (14) Hide the loading indicator

    // TODO (15) Use the same logic used in onPostExecute to show the data or the error message
    // END - onLoadFinished

    // TODO (16) Override onLoaderReset as it is part of the interface we implement, but don't do anything in this method

    // TODO (29) Delete the AsyncTask class
//    public class GithubQueryTask extends AsyncTask<URL, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mLoadingIndicator.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected String doInBackground(URL... params) {
//            URL searchUrl = params[0];
//            String githubSearchResults = null;
//            try {
//                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return githubSearchResults;
//        }
//
//        @Override
//        protected void onPostExecute(String githubSearchResults) {
//            mLoadingIndicator.setVisibility(View.INVISIBLE);
//            if (githubSearchResults != null && !githubSearchResults.equals("")) {
//                showJsonDataView();
//                mSearchResultsTextView.setText(githubSearchResults);
//            } else {
//                showErrorMessage();
//            }
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            // 搜索输入内容
            makeGithubSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}