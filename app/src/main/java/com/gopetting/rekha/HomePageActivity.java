package com.gopetting.rekha;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    final List<ObjectsBean> info = new ArrayList<>();
    ArrayList<ObjectsBean> cartlist = new ArrayList<>();
    String TAG = getClass().getName();
    int Cartnumber = 0;
    int mCount = 0;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private HomeAdapter Adapter;
    private boolean loading = true;
    private SwipeRefreshLayout achiver_swipe;
    public Context context;
    public TextView cart_count;
    private int result_ok = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        setToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.achiverRecyclerView);
        cart_count = (TextView) findViewById(R.id.cart_count);
        sharedPreferences = getSharedPreferences("MyProfile", Context.MODE_PRIVATE);
        achiver_swipe = (SwipeRefreshLayout) findViewById(R.id.achiver_swipe_refresh_layout);
        linearLayoutManager = new LinearLayoutManager(HomePageActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        Adapter = new HomeAdapter(HomePageActivity.this, info);
        achiver_swipe.setOnRefreshListener(this);
        progressDialog = new ProgressDialog(HomePageActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        apiCall();
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        ObjectsBean object = info.get(position);

                        if (cartlist.indexOf(object) == -1) {
                            Cartnumber = Cartnumber + 1;
                            cartlist.add(object);
                            cart_count.setText(Integer.toString(Cartnumber));
                        }
                    }
                })
        );
        Log.e(TAG, "cartlist size" + cartlist.size() + "cartnumber" + Cartnumber);

        // Login testing

        startActivity(new Intent(this, LoginActivity.class));
    }

    public void openCartactivity(View view) {
        Bundle cartBundle =  new Bundle();
        cartBundle.putInt("cartcount", Cartnumber);
        cartBundle.putSerializable("cartbeanlist", cartlist);
        CartFragment cartFragment =  new CartFragment();
        cartFragment.setArguments(cartBundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.parentLL,cartFragment,"cartfragment").addToBackStack("cartfragment").commit();
    }


    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarAchievers);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("Our Gides");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return true;
    }

    private void apiCall() {
        Log.d(TAG, "in api call func ");
        achiver_swipe.setRefreshing(true);
        Ion.with(HomePageActivity.this)
                .load(Urls.getObjects)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            Log.d(TAG, "value of result " + result.toString());
                            final String totals = String.valueOf(result.get("total")).replaceAll("\"", "");
                            achiver_swipe.setRefreshing(false);
                            loading = true;
                            JsonArray jsonArray = result.getAsJsonArray("data");
                            String posts = jsonArray.toString();
                            Log.d(TAG, "value of posts " + posts);
                            try {
                                JSONArray postsArray = new JSONArray(posts);
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JSONObject jsonObject = postsArray.getJSONObject(i);
                                    String name = jsonObject.getString("name");
                                    String endDate = jsonObject.getString("endDate");
                                    String icon = jsonObject.getString("icon");
                                    ObjectsBean Bean = new ObjectsBean();
                                    Bean.setName(name);
                                    Bean.setIcon(icon);
                                    Bean.setEndDate(endDate);
                                    info.add(Bean);
                                    mCount++;
                                }
                                recyclerView.setAdapter(Adapter);
                                Adapter.notifyDataSetChanged();
                                progressDialog.dismiss();

                            } catch (JSONException e1) {
                                progressDialog.dismiss();
                                e1.printStackTrace();
                            }
                        } else {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(HomePageActivity.this, getResources().getString(R.string.oops), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



    @Override
    public void onRefresh() {
        Log.e(TAG, "at swipe to refresh");
        info.clear();
        Adapter.notifyDataSetChanged();
        apiCall();
    }


    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        public interface OnItemClickListener {
            public void onItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            this.finish();
        }
    }
}
