package com.yamlin.search.image.googleimagesearch;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;

import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;


public class MainActivity extends Activity {

    public static final String TAG = "MainActivity";
    public static final String PREFERENCE_NAME = "pref";
    public static final String URL = "https://ajax.googleapis.com/ajax/services/search/images";
    public static final int REQUEST_OPTIONS = 1;

    private String mQueryString;
    private StaggeredGridView mGridView;
    private ImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQueryString = "";
        setupGridView();
        //requestGoogleImages(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                Log.e(TAG, "query: " + query);
                mQueryString = query;
                mAdapter.clear();
                // send request
                requestGoogleImages(0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            Intent intent = new Intent(this, SearchOptionActivity.class);
//            startActivityForResult(intent, REQUEST_OPTIONS);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            EditOptionDialog editOptionDialog = EditOptionDialog.newInstance();
            editOptionDialog.show(ft, "dialog");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_OPTIONS:

                if (RESULT_OK == resultCode) {
                    mAdapter.clear();
                    requestGoogleImages(0);
                }
                break;
        }
    }

    protected void setupGridView() {
        mGridView = (StaggeredGridView) findViewById(R.id.gridView);
        //mGridView.setNumColumns(2);
        mAdapter = new ImageAdapter(this, new ArrayList<ImageItem>());
        mGridView.setAdapter(mAdapter);
        mGridView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                requestGoogleImages(page + 1);
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageItem item = mAdapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, ShowImageActivity.class);
                intent.putExtra("url", item.url);
                startActivity(intent);
            }
        });
    }

    protected Map<String, ?> getQueryPreference() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        return preferences.getAll();
    }

    protected Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public void requestGoogleImages(long page) {

        if (!isNetworkAvailable()) {
            Log.e(TAG, "Network is not available");
            return;
        }

        // prepare the query data
        RequestParams params = new RequestParams();
        params.put("q", mQueryString);
        params.put("v", "1.0");
        params.put("rsz", "8");
        params.put("start", page * 8);
        Map<String, ?> preference = getQueryPreference();
        for (String key: preference.keySet()) {
            params.put(key, preference.get(key));
        }


        AsyncHttpClient client = new AsyncHttpClient();
        //timeout 5 seconds
        client.setConnectTimeout(5000);
        client.get(URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(TAG, "resp: " + response);
                if (response.optInt("responseStatus") == 200) {
                    List<ImageItem> items = ImageItem.getImages(
                            response.optJSONObject("responseData"));

                    //Log.e(TAG, "images: " + items.size());
                    mAdapter.addAll(items);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                Log.e(TAG, "status code: " + statusCode);
            }
        });
    }
}
