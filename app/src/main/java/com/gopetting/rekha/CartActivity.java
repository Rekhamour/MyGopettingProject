package com.gopetting.rekha;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
   public  ArrayList<ObjectsBean> cartlist = new ArrayList<>();
    public int cartcount;
    public Toolbar toolbar;
    public RecyclerView recyclerView;
    public LinearLayoutManager linearLayoutManager;
    public CartAdapter Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        cartcount = getIntent().getIntExtra("cartcount",0);
        cartlist= (ArrayList<ObjectsBean>) getIntent().getSerializableExtra("cartbeanlist");
        setToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.cartRecyclerView);
        linearLayoutManager = new LinearLayoutManager(CartActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        Adapter = new CartAdapter(CartActivity.this, cartlist,cartcount);
        recyclerView.setAdapter(Adapter);

    }
    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_cart);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("Our Cart");
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
}
