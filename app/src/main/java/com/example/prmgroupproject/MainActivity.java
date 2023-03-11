package com.example.prmgroupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.prmgroupproject.activities.CartActivity;
import com.example.prmgroupproject.adapter.ProductAdapter;
import com.example.prmgroupproject.api.ProductRepository;
import com.example.prmgroupproject.model.Product;
import com.example.prmgroupproject.service.ProductService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton mMapFab, mPersonFab, mMenuFab, mChatFab;
    Boolean isAllFabsVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapFab = findViewById(R.id.fabMap);
        mPersonFab = findViewById(R.id.fabPerson);
        mMenuFab = findViewById(R.id.fabMenu);
        mChatFab = findViewById(R.id.fabChat);

        mChatFab.setVisibility(View.GONE);
        mPersonFab.setVisibility(View.GONE);
        mMapFab.setVisibility(View.GONE);

        isAllFabsVisible = false;

        mMenuFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllFabsVisible) {

                    mMapFab.show();
                    mPersonFab.show();
                    mChatFab.show();
                    mMenuFab.setImageResource(R.drawable.ic_cancel);

                    isAllFabsVisible = true;
                } else {

                    mPersonFab.hide();
                    mChatFab.hide();
                    mMenuFab.setImageResource(R.drawable.ic_menu);

                    isAllFabsVisible = false;
                }
            }
        });

        mMapFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Map Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        mPersonFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Person Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        mChatFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Chat Clicked", Toast.LENGTH_SHORT).show();
            }
        });


        ProductService productService = ProductRepository.getProductService();

        Call<List<Product>> call = productService.getProducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> productList = response.body();
                RecyclerView recyclerView = findViewById(R.id.recycleView);
                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                recyclerView.setAdapter(new ProductAdapter(MainActivity.this, productList));
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                System.out.println(t.toString());
                Toast.makeText(getApplicationContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuCart:
                Intent i = new Intent(this, CartActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}